package com.tensquare.user.config;


import com.tensquare.user.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
@Component
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //拦截器 要声明 我要拦截的对象  和拦截的请求
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")//拦截所有请求
                .excludePathPatterns("/**/login/**");//不拦截 login 开头的请求
    }
}
