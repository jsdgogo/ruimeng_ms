package com.ruimeng.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruimeng.entity.Staff;
import com.ruimeng.service.StaffService;
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
@RequestMapping("staff")
@CrossOrigin
public class StaffController {
    @Autowired
    private StaffService staffService;

    @PostMapping("save")
    public Result save(Staff staff) {
        staff.setCreateTime(new Date());
        staff.setUpdateTime(new Date());
        if (staffService.save(staff)) {
            return Result.ok();
        }
        return Result.error();
    }

    @GetMapping("deleteById")
    public Result deleteById(int id) {
        if (staffService.removeById(id)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("update")
    public Result update(Staff staff) {
        staff.setUpdateTime(new Date());
        if (staffService.updateById(staff)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("findByPage")
    public Result findByPage(PageParam pageParam) {
        QueryWrapper<Staff> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getSearch())) {
            queryWrapper.like("search", pageParam.getSearch());
        }
        if (StringUtils.isNotBlank(pageParam.getOrderBy())) {
            queryWrapper.orderBy(true, pageParam.isAscOrDesc(), pageParam.getOrderBy());
        }
        Page<Staff> page = new Page<>(pageParam.getIndex(), pageParam.getSize());
        IPage<Staff> staffIPage = staffService.page(page, queryWrapper);
        return Result.ok().data("page", staffIPage);
    }

    @GetMapping("getById")
    public Result getById(int id) {
        Staff staff = staffService.getById(id);
        return Result.ok().data("staff", staff);
    }
}

