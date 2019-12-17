package com.ruimeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruimeng.entity.User;
import com.ruimeng.service.UserService;
import com.ruimeng.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author JiangShiDing
 * @description 用户控制器
 * @Date 2019/12/9 0009
 */
@RestController
@RequestMapping("user")
@CrossOrigin
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
    public Result login(String loginName, String password, HttpServletResponse response) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loginName", loginName).eq("password", password);
        User user = userService.getOne(queryWrapper);
        if (user != null) {
            String token = createCookie(response);
            user.setToken(token);
            if (userService.saveOrUpdate(user)) {
                return Result.ok().data("token", token);
            }
        }
        return Result.error();
    }

    private String createCookie(HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("uidx", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setPath("/");
        response.addCookie(cookie);
        return token;
    }

    /**
     * @return Result 返回类型
     * @Title: info
     * @Description: 获取用户信息
     */
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        User user = getByCookie(request);
        if (user != null) {
            return Result.ok().data("user", user);
        }
        return Result.error();
    }

    private User getByCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("uidx")) {
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("token", cookie.getValue());
                User user = userService.getOne(queryWrapper);
                return user;
            }
        }
        return null;
    }

    /**
     * @return Result 返回类型
     * @Title: logout
     * @Description: 退出登录
     */
    @PostMapping("logout")
    public Result logout(HttpServletRequest request) {
        User user = getByCookie(request);
        user.setToken(null);
        userService.updateById(user);
        return Result.ok();
    }

    /**
     * @Description 修改用户名密码
     * @param password 密码
     * @param loginName 登录名
     * @param request 请求
     * @return Result 结果
     */
    @PostMapping("update")
    public Result update(String password, String loginName,HttpServletRequest request) {
        User user = getByCookie(request);
        if (StringUtils.isNotBlank(loginName)){
            user.setLoginName(loginName);
        }
        if(StringUtils.isNotBlank(password)){
            user.setPassword(password);
        }
        userService.updateById(user);
        return Result.ok();
    }
}
