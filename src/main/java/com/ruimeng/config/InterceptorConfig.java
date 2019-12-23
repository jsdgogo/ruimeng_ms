package com.ruimeng.config;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruimeng.entity.User;
import com.ruimeng.service.UserService;
import com.ruimeng.vo.Result;
import com.ruimeng.vo.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private UserService userService;

    /**
     * 拦截器注册类
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**").excludePathPatterns("/user/login");
    }

    /**
     * 定义拦截器
     */
    public class LogInterceptor implements HandlerInterceptor {
        /**
         * 请求执行前执行
         */
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o){
            crossDomain(request,response);
            if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
                response.setContentType("text/plain, charset=utf-8");
                response.setContentLength(0);
                response.setStatus(204);
                return true;
            }
            User user = getByToken(request);
            if (user == null) {
                Result result = Result.setResult(ResultCodeEnum.NO_USER_ERROR);
                String resultJson = JSONObject.toJSONString(result);
                returnJson(response, resultJson);
                log.info("已成功拦截并转发跳转");
                return false;
            }
            return true;
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
         * 请求结束执行
         */
        @Override
        public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        }

        /**
         * 视图渲染完成后执行
         */
        @Override
        public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        }

                /**
         * 跨域处理
         */
        public void crossDomain(HttpServletRequest request, HttpServletResponse response) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "*");

        }
        private void returnJson(HttpServletResponse response, String json){
            PrintWriter writer = null;
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=utf-8");
            try {
                writer = response.getWriter();
                writer.print(json);
            } catch (IOException e) {
                log.error("response error", e);
            } finally {
                if (writer != null)
                    writer.close();
            }
        }
    }

}