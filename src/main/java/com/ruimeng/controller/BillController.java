package com.ruimeng.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruimeng.entity.Bill;
import com.ruimeng.service.BillService;
import com.ruimeng.util.DateUtil;
import com.ruimeng.vo.PageParam;
import com.ruimeng.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author JiangShiDing
 * @since 2019-11-27
 */
@RestController
@RequestMapping("bill")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping("save")
    public Result save(Bill bill) {
        bill.setCreateTime(new Date());
        bill.setUpdateTime(new Date());
        if (billService.save(bill)) {
            return Result.ok();
        }
        return Result.error();
    }

    @GetMapping("deleteById")
    public Result deleteById(int id) {
        if (billService.removeById(id)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("update")
    public Result update(Bill bill) {
        bill.setUpdateTime(new Date());
        if (billService.updateById(bill)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("findByPage")
    public Result findByPage(PageParam pageParam, String beginTime, String endTime) throws ParseException {
        QueryWrapper<Bill> queryWrapper = new QueryWrapper<>();
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
        Page<Bill> page = new Page<>(pageParam.getIndex(), pageParam.getSize());
        IPage<Bill> orderIPage = billService.page(page, queryWrapper);
        return Result.ok().data("page", orderIPage);
    }

    @GetMapping("getById")
    public Result getById(int id) {
        Bill bill = billService.getById(id);
        return Result.ok().data("bill", bill);
    }

}

