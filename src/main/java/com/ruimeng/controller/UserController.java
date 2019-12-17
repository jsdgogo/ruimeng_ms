package com.ruimeng.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @author JiangShiDing
 * @description 用户控制器
 * @Date 2019/12/9 0009
 */
@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {
    /**
     * @Title: login
     * @Description: 这里用一句话描述这个方法的作用
     * @param username 2
     * @param password 1
     * @return String 返回类型
     */
    @PostMapping("login")
    public String login(String username, String password) {
        return "{\"code\":0,\"flag\":true,\"data\":{\"token\": \"A0193-103AC-VV224-12334-45134\"}}";

    }

    /**
     * @Title: login
     * @Description: 这里用一句话描述这个方法的作用
     * @return String 返回类型
     */
    @GetMapping("info")
    public String info() {
        return "{\"code\": 0,\"flag\": true,\"data\": {\"token\": "
                + "\"A0193-103AC-VV224-12334-45134\",\"roles\": \"admin\",\"name\":"
                + " \"小白\",\"avatar\": \"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif\"}}";
    }

    /**
     * @Title: login
     * @Description: 这里用一句话描述这个方法的作用
     * @return String 返回类型
     */
    @PostMapping("logout")
    public String logout() {
        return "{\"code\": 0,\"flag\": true}";

    }
}
