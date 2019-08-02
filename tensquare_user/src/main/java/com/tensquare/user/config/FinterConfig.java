package com.tensquare.user.config;

import com.tensquare.user.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class FinterConfig extends WebMvcConfigurationSupport {

    @Autowired
    private JwtFilter jwtFilter;

    protected void addInterceptors(InterceptorRegistry registry) {

        //拦截对象的声明和请求
        registry.addInterceptor(jwtFilter)
                .addPathPatterns("/**")
                .excludePathPatterns("/**/login/**");
    }
}
