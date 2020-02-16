package com.ruimeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruimeng.entity.Staff;
import com.ruimeng.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("test")
public class TestController {
    @Autowired
    private StaffService staffService;

    @RequestMapping("hello")
    public String hello(Model m) {
        m.addAttribute("name", "thymeleaf");
        return "hello";
    }

    @GetMapping("getStaff")
    public String getStaff(Model m) {
        String htmlContent = "<p style='color:red'> 红色文字</p>";
        QueryWrapper<Staff> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 10);
        List<Staff> staffList = staffService.list(queryWrapper);
        m.addAttribute("staffList", staffList);
        m.addAttribute("staff", staffList.get(0));
        m.addAttribute("htmlContent", htmlContent);
        m.addAttribute("boolean",true);
        return "hello";
    }
}
