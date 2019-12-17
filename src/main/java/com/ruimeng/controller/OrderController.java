package com.ruimeng.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruimeng.entity.Order;
import com.ruimeng.service.OrderService;
import com.ruimeng.vo.PageParam;
import com.ruimeng.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author JiangShiDing
 * @since 2019-11-27
 */
@RestController
@RequestMapping("order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("save")
    public Result save(@RequestBody Order order) {
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        if (orderService.save(order))  {
            return Result.ok();
        }
        return Result.error();
    }

    @GetMapping("deleteById")
    public Result deleteById(int id) {
        if(orderService.removeById(id)){
            return Result.ok();
        }
        return Result.error();
    }
    @PostMapping("update")
    public Result update(@RequestBody Order order) {
        order.setUpdateTime(new Date());
        if(orderService.updateById(order)){
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("findByPage")
    public Result findByPage(@RequestBody PageParam pageParam,String startTime,String endTime) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getSearch())){
            queryWrapper.like("search",pageParam.getSearch());
        }
        if(StringUtils.isNotBlank(pageParam.getOrderBy())){
            queryWrapper.orderBy(true,pageParam.isAscOrDesc(),pageParam.getOrderBy());
        }
        Page<Order> page = new Page<>(pageParam.getIndex(),pageParam.getSize());
        IPage<Order> orderIPage = orderService.page(page, queryWrapper);
        return Result.ok().data("page",orderIPage);
    }
    @GetMapping("getById")
    public Result getById(int id) {
        Order order = orderService.getById(id);
        return Result.ok().data("order",order);
    }
}

