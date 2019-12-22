package com.ruimeng.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruimeng.entity.CustomerGasCylinder;
import com.ruimeng.service.CustomerGasCylinderService;
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
@RequestMapping("customerGasCylinder")
public class CustomerGasCylinderController {

    @Autowired
    private CustomerGasCylinderService customerGasCylinderService;

    @PostMapping("save")
    public Result save(CustomerGasCylinder customerGasCylinder) {
        customerGasCylinder.setCreateTime(new Date());
        customerGasCylinder.setUpdateTime(new Date());
        if (customerGasCylinderService.save(customerGasCylinder)) {
            return Result.ok();
        }
        return Result.error();
    }

    @GetMapping("deleteById")
    public Result deleteById(int id) {
        if (customerGasCylinderService.removeById(id)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("update")
    public Result update(CustomerGasCylinder customerGasCylinder) {
        customerGasCylinder.setUpdateTime(new Date());
        if (customerGasCylinderService.updateById(customerGasCylinder)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("findByPage")
    public Result findByPage(PageParam pageParam) {
        QueryWrapper<CustomerGasCylinder> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getSearch())) {
            queryWrapper.like("search", pageParam.getSearch());
        }
        if (StringUtils.isNotBlank(pageParam.getOrderBy())) {
            queryWrapper.orderBy(true, pageParam.isAscOrDesc(), pageParam.getOrderBy());
        }
        Page<CustomerGasCylinder> page = new Page<>(pageParam.getIndex(), pageParam.getSize());
        IPage<CustomerGasCylinder> customerGasCylinderIPage = customerGasCylinderService.page(page, queryWrapper);
        return Result.ok().data("page", customerGasCylinderIPage);
    }

    @GetMapping("getById")
    public Result getById(int id) {
        CustomerGasCylinder customerGasCylinder = customerGasCylinderService.getById(id);
        return Result.ok().data("customerGasCylinder", customerGasCylinder);
    }

}

