package com.ruimeng.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruimeng.entity.CustomerEmptyBottle;
import com.ruimeng.service.CustomerEmptyBottleService;
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
@RequestMapping("customerEmptyBottle")
public class CustomerEmptyBottleController {

    @Autowired
    private CustomerEmptyBottleService customerEmptyBottleService;

    @PostMapping("save")
    public Result save(@RequestBody CustomerEmptyBottle customerEmptyBottle) {
        customerEmptyBottle.setCreateTime(new Date());
        customerEmptyBottle.setUpdateTime(new Date());
        if (customerEmptyBottleService.save(customerEmptyBottle)) {
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
    public Result update(@RequestBody CustomerEmptyBottle customerEmptyBottle) {
        customerEmptyBottle.setUpdateTime(new Date());
        if (customerEmptyBottleService.updateById(customerEmptyBottle)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("findByPage")
    public Result findByPage(@RequestBody PageParam pageParam) {
        QueryWrapper<CustomerEmptyBottle> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getSearch())) {
            queryWrapper.like("search", pageParam.getSearch());
        }
        if (StringUtils.isNotBlank(pageParam.getOrderBy())) {
            queryWrapper.orderBy(true, pageParam.isAscOrDesc(), pageParam.getOrderBy());
        }
        Page<CustomerEmptyBottle> page = new Page<>(pageParam.getIndex(), pageParam.getSize());
        IPage<CustomerEmptyBottle> customerEmptyBottleIPage = customerEmptyBottleService.page(page, queryWrapper);
        return Result.ok().data("page", customerEmptyBottleIPage);
    }

    @GetMapping("getById")
    public Result getById(int id) {
        CustomerEmptyBottle customerEmptyBottle = customerEmptyBottleService.getById(id);
        return Result.ok().data("customerEmptyBottle", customerEmptyBottle);
    }

}

