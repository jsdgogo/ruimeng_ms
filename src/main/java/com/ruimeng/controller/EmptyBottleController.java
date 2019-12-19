package com.ruimeng.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruimeng.entity.EmptyBottle;
import com.ruimeng.service.EmptyBottleService;
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
@RequestMapping("emptyBottle")
public class EmptyBottleController {
    @Autowired
    private EmptyBottleService emptyBottleService;

    @PostMapping("save")
    public Result save(EmptyBottle emptyBottle) {
        emptyBottle.setCreateTime(new Date());
        emptyBottle.setUpdateTime(new Date());
        if (emptyBottleService.save(emptyBottle))  {
            return Result.ok();
        }
        return Result.error();
    }

    @GetMapping("deleteById")
    public Result deleteById(int id) {
        if(emptyBottleService.removeById(id)){
            return Result.ok();
        }
        return Result.error();
    }
    @PostMapping("update")
    public Result update(EmptyBottle emptyBottle) {
        emptyBottle.setUpdateTime(new Date());
        if(emptyBottleService.updateById(emptyBottle)){
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("findByPage")
    public Result findByPage(PageParam pageParam ) {
        QueryWrapper<EmptyBottle> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getSearch())){
            queryWrapper.like("search",pageParam.getSearch());
        }
        if(StringUtils.isNotBlank(pageParam.getOrderBy())){
            queryWrapper.orderBy(true,pageParam.isAscOrDesc(),pageParam.getOrderBy());
        }
        Page<EmptyBottle> page = new Page<>(pageParam.getIndex(),pageParam.getSize());
        IPage<EmptyBottle> emptyBottleIPage = emptyBottleService.page(page, queryWrapper);
        return Result.ok().data("page",emptyBottleIPage);
    }
    @GetMapping("getById")
    public Result getById(int id) {
        EmptyBottle emptyBottle = emptyBottleService.getById(id);
        return Result.ok().data("emptyBottle",emptyBottle);
    }


}

