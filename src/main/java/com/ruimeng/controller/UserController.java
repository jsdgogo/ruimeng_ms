package com.ruimeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruimeng.entity.User;
import com.ruimeng.service.UserService;
import com.ruimeng.vo.Result;
import com.ruimeng.vo.ResultCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author JiangShiDing
 * @description 用户控制器
 * @Date 2019/12/9 0009
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * @param loginName 用户名
     * @param password  密码
     * @return String 返回类型
     * @Title: login
     * @Description: 登录
     */
    @PostMapping("login")
    public Result login(String loginName, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loginName", loginName).eq("password", password);
        User user = userService.getOne(queryWrapper);
        if (user != null) {
            String token = user.getLoginName();
            return Result.ok().data("token", token);
        }
        return Result.error().message("账号或密码错误");
    }

    /*private String createCookie(HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("uidx", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setPath("/");
        response.addCookie(cookie);
        return token;
    }*/

    /**
     * @return Result 返回类型
     * @Title: info
     * @Description: 获取用户信息
     */
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        User user = getByToken(request);
        if (user != null) {
            return Result.ok().data("user", user);
        }
        return Result.error();
    }

    private User getByToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isNotBlank(token)) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("loginName", token);
            User user = userService.getOne(queryWrapper);
            return user;
        }
        return null;
    }

    /**
     * @return Result 返回类型
     * @Title: logout
     * @Description: 退出登录
     */
    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }

    /**
     * @param user  用戶
     * @return Result 结果
     * @Description 修改用户名密码
     */
    @PostMapping("update")
    public Result update(User user) {
        userService.updateById(user);
        return Result.ok();
    }

}
