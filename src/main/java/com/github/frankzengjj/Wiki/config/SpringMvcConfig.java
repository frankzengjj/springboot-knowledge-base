package com.github.frankzengjj.Wiki.config;

import com.github.frankzengjj.Wiki.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Resource
    LoginInterceptor loginInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/test/**",
                        "/user/login",
                        "/redis/**",
                        "/category/all",
                        "/ebook/list",
                        "/doc/all/**",
                        "/doc/content/**",
                        "/doc/vote/**",
                        "/ebook-snapshot/**"
                );
    }
}
