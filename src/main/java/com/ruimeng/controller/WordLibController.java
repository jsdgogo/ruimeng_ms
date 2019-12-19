package com.ruimeng.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruimeng.entity.WordLib;
import com.ruimeng.service.WordLibService;
import com.ruimeng.vo.PageParam;
import com.ruimeng.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author JiangShiDing
 * @since 2019-11-27
 */
@RestController
@RequestMapping("wordLib")
public class WordLibController {

    @Autowired
    private WordLibService wordLibService;

    @GetMapping("findAll")
    public Result findAll(WordLib wordLib){
        QueryWrapper<WordLib> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("code",wordLib.getCode()).or().like("type",wordLib.getType());
        List<WordLib> wordLibs = wordLibService.list(queryWrapper);
        return Result.ok().data("wordLibs",wordLibs);
    }

    @PostMapping("findByPage")
    public Result findByPage(PageParam pageParam ,WordLib wordLib){
        QueryWrapper<WordLib> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getSearch())){
            queryWrapper.like("code",pageParam.getSearch()).or().like("type",pageParam.getSearch()).or().like("value",pageParam.getSearch());
        }
        if(StringUtils.isNotBlank(pageParam.getOrderBy())){
            queryWrapper.orderBy(true,pageParam.isAscOrDesc(),pageParam.getOrderBy());
        }
        Page<WordLib> page = new Page<>(pageParam.getIndex(),pageParam.getSize());
        IPage<WordLib> wordLibIPage = wordLibService.page(page, queryWrapper);
        return Result.ok().data("wordLibs",wordLibIPage);
    }


}

