package com.example.graduationproject.web.config;

import com.example.graduationproject.web.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author Zzwen
 * @date 2020/4/4 18:02
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
    }
}
