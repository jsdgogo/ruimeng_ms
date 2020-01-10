package com.ruimeng.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruimeng.entity.Bill;
import com.ruimeng.entity.CustomerEmptyBottle;
import com.ruimeng.entity.Orders;
import com.ruimeng.service.BillService;
import com.ruimeng.service.CustomerEmptyBottleService;
import com.ruimeng.service.EmptyBottleService;
import com.ruimeng.util.DateUtil;
import com.ruimeng.vo.PageParam;
import com.ruimeng.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author JiangShiDing
 * @since 2019-11-27
 */
@RestController
@RequestMapping("customerEmptyBottle")
@Transactional
public class CustomerEmptyBottleController {
    @Autowired
    private CustomerEmptyBottleService customerEmptyBottleService;
    @Autowired
    private BillService billService;

    @PostMapping("save")
    public Result save(CustomerEmptyBottle customerEmptyBottle, String createTimeStr) throws ParseException {
        customerEmptyBottle.setCreateTime(new Date());
        customerEmptyBottle.setUpdateTime(new Date());
        if (StringUtils.isNotBlank(createTimeStr)) {
            customerEmptyBottle.setCreateTime(DateUtil.stringToDate(createTimeStr));
        }
        int sendBackNumber = customerEmptyBottle.getSendBackNumber();
        int total = customerEmptyBottle.getTotal();
        customerEmptyBottle.setNowNumber(total - sendBackNumber);
        if (customerEmptyBottleService.save(customerEmptyBottle)) {
            QueryWrapper<Bill> billQueryWrapper = new QueryWrapper<>();
            QueryWrapper<CustomerEmptyBottle> customerEmptyBottleQueryWrapper = new QueryWrapper<>();
            List<CustomerEmptyBottle> customerEmptyBottles = customerEmptyBottleService.list(customerEmptyBottleQueryWrapper);
            Bill oldBill = billService.getOne(billQueryWrapper);
            Bill newBill = new Bill();
            int nowNumber = 0;
            double price = 0;
            for (CustomerEmptyBottle customerEB : customerEmptyBottles) {
                nowNumber+= customerEB.getNowNumber();
                price+= customerEB.getPrice();
            }
            newBill.setCreateTime(new Date());
            newBill.setUpdateTime(new Date());
            newBill.setCustomerId(customerEmptyBottle.getCustomerId());
            newBill.setCustomerName(customerEmptyBottle.getCustomerName());
            newBill.setEmptyBottleTotal(nowNumber*price);
            if (oldBill != null) {
                newBill.setOrderDebt(oldBill.getOrderDebt());
                newBill.setPaid(oldBill.getPaid());
                newBill.setOrderTotal(oldBill.getOrderTotal());
                newBill.setCreateTime(oldBill.getCreateTime());
            }
            newBill.setTotalDebt(newBill.getOrderDebt()+newBill.getEmptyBottleTotal());
            billService.remove(billQueryWrapper);
            billService.save(newBill);
            return Result.ok();
        }
        return Result.error();
    }

    @GetMapping("deleteById")
    public Result deleteById(int id) {
        if (customerEmptyBottleService.removeById(id)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("update")
    public Result update(CustomerEmptyBottle customerEmptyBottle, String createTimeStr) throws ParseException {
        customerEmptyBottle.setUpdateTime(new Date());
        if (StringUtils.isNotBlank(createTimeStr)) {
            customerEmptyBottle.setCreateTime(DateUtil.stringToDate(createTimeStr));
        }
        int sendBackNumber = customerEmptyBottle.getSendBackNumber();
        int total = customerEmptyBottle.getTotal();
        customerEmptyBottle.setNowNumber(total - sendBackNumber);
        if (customerEmptyBottleService.updateById(customerEmptyBottle)) {
            QueryWrapper<Bill> billQueryWrapper = new QueryWrapper<>();
            QueryWrapper<CustomerEmptyBottle> customerEmptyBottleQueryWrapper = new QueryWrapper<>();
            List<CustomerEmptyBottle> customerEmptyBottles = customerEmptyBottleService.list(customerEmptyBottleQueryWrapper);
            Bill oldBill = billService.getOne(billQueryWrapper);
            Bill newBill = new Bill();
            double emptyBottleTotal = 0;
            for (CustomerEmptyBottle customerEB : customerEmptyBottles) {
                emptyBottleTotal+=customerEB.getNowNumber()*customerEB.getPrice();
            }
            newBill.setCreateTime(new Date());
            newBill.setUpdateTime(new Date());
            newBill.setCustomerId(customerEmptyBottle.getCustomerId());
            newBill.setCustomerName(customerEmptyBottle.getCustomerName());
            newBill.setEmptyBottleTotal(emptyBottleTotal);
            if (oldBill != null) {
                newBill.setOrderDebt(oldBill.getOrderDebt());
                newBill.setPaid(oldBill.getPaid());
                newBill.setOrderTotal(oldBill.getOrderTotal());
                newBill.setCreateTime(oldBill.getCreateTime());
            }
            newBill.setTotalDebt(newBill.getOrderDebt()+newBill.getEmptyBottleTotal());
            billService.remove(billQueryWrapper);
            billService.save(newBill);
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("findByPage")
    public Result findByPage(PageParam pageParam, String beginTime, String endTime) throws ParseException {
        QueryWrapper<CustomerEmptyBottle> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getSearch())) {
            queryWrapper.like("search", pageParam.getSearch());
        }
        if (StringUtils.isNotBlank(pageParam.getOrderBy())) {
            queryWrapper.orderBy(true, pageParam.isAscOrDesc(), pageParam.getOrderBy());
        }
        //创建时间大于等于开始时间
        if (StringUtils.isNotBlank(beginTime)) {
            Date startDate = DateUtil.stringToDate(beginTime);
            queryWrapper.ge("createTime", startDate);
        }
        //创建时间小于等于结束时间
        if (StringUtils.isNotBlank(endTime)) {
            Date endDate = DateUtil.stringToDate(endTime);
            queryWrapper.le("createTime", endDate);
        }
        Page<CustomerEmptyBottle> page = new Page<>(pageParam.getIndex(), pageParam.getSize());
        IPage<CustomerEmptyBottle> customerEmptyBottle = customerEmptyBottleService.page(page, queryWrapper);
        return Result.ok().data("page", customerEmptyBottle);
    }

    @GetMapping("getById")
    public Result getById(int id) {
        CustomerEmptyBottle customerEmptyBottle = customerEmptyBottleService.getById(id);
        return Result.ok().data("customerEmptyBottle", customerEmptyBottle);
    }


}

