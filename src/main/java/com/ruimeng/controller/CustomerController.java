package com.ruimeng.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruimeng.entity.Customer;
import com.ruimeng.service.CustomerService;
import com.ruimeng.vo.PageParam;
import com.ruimeng.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("customer")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("save")
    public Result save(@RequestBody Customer customer) {
        customer.setCreateTime(new Date());
        customer.setUpdateTime(new Date());
       if (customerService.save(customer))  {
           return Result.ok();
        }
        return Result.error();
    }

    @GetMapping("deleteById")
    public Result deleteById(int id) {
        if(customerService.removeById(id)){
            return Result.ok();
        }
        return Result.error();
    }
    @PostMapping("update")
    public Result update(@RequestBody Customer customer) {
        customer.setUpdateTime(new Date());
        if(customerService.updateById(customer)){
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("findByPage")
    public Result findByPage(@RequestBody PageParam pageParam ) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getSearch())){
            queryWrapper.like("search",pageParam.getSearch());
        }
        if(StringUtils.isNotBlank(pageParam.getOrderBy())){
            queryWrapper.orderBy(true,pageParam.isAscOrDesc(),pageParam.getOrderBy());
        }
        Page<Customer> page = new Page<>(pageParam.getIndex(),pageParam.getSize());
        IPage<Customer> customerIPage = customerService.page(page, queryWrapper);
        return Result.ok().data("page",customerIPage);
    }
    @GetMapping("getById")
    public Result getById(int id) {
        Customer customer = customerService.getById(id);
        return Result.ok().data("customer",customer);
    }



}

