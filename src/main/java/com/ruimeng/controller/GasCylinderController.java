package com.ruimeng.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruimeng.entity.GasCylinder;
import com.ruimeng.service.GasCylinderService;
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
@RequestMapping("gasCylinder")
public class GasCylinderController {

    @Autowired
    private GasCylinderService gasCylinderService;

    @PostMapping("save")
    public Result save(GasCylinder gasCylinder) {
        gasCylinder.setCreateTime(new Date());
        gasCylinder.setUpdateTime(new Date());
        if (gasCylinderService.save(gasCylinder)) {

            return Result.ok();
        }
        return Result.error();
    }

    @GetMapping("deleteById")
    public Result deleteById(int id) {
        if (gasCylinderService.removeById(id)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("update")
    public Result update(GasCylinder gasCylinder) {
        gasCylinder.setUpdateTime(new Date());
        if (gasCylinderService.updateById(gasCylinder)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("findByPage")
    public Result findByPage(PageParam pageParam) {
        QueryWrapper<GasCylinder> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getSearch())) {
            queryWrapper.like("search", pageParam.getSearch());
        }
        if (StringUtils.isNotBlank(pageParam.getOrderBy())) {
            queryWrapper.orderBy(true, pageParam.isAscOrDesc(), pageParam.getOrderBy());
        }
        Page<GasCylinder> page = new Page<>(pageParam.getIndex(), pageParam.getSize());
        IPage<GasCylinder> gasCylinderIPage = gasCylinderService.page(page, queryWrapper);
        return Result.ok().data("page", gasCylinderIPage);
    }

    @GetMapping("getById")
    public Result getById(int id) {
        GasCylinder gasCylinder = gasCylinderService.getById(id);
        return Result.ok().data("gasCylinder", gasCylinder);
    }
}

