package com.ruimeng.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruimeng.entity.User;
import com.ruimeng.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private UserService userService;
    /**
     * 拦截器注册类
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
    }
    /**
     * 定义拦截器
     */
    public class LogInterceptor implements HandlerInterceptor {
        /**
         * 请求执行前执行
         */
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
            log.info("执行到了preHandle方法");
            User user = getByToken(request);
            if (user==null){
                response.sendRedirect(request.getContextPath()+"/user/toIndex");//拦截后跳转的方法
                log.info("已成功拦截并转发跳转");
                return false;
            }
            log.info("不需要拦截，放行");
            return true;
        }

        private User getByToken(HttpServletRequest request) {
            String token = request.getHeader("token");
            if (StringUtils.isNotBlank(token)) {
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("token", token);
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
            log.info("执行了postHandle方法");
        }
        /**
         * 视图渲染完成后执行
         */
        @Override
        public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
            log.info("执行到了afterCompletion方法");
        }
    }
}