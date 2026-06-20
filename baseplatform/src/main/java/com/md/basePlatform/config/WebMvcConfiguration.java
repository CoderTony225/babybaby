package com.md.basePlatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.md.basePlatform.interceptor.RequestLoggingInterceptor;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

/**
 * MVC：拦截器与 Thymeleaf-Shiro 方言。
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final RequestLoggingInterceptor requestLoggingInterceptor;

    public WebMvcConfiguration(RequestLoggingInterceptor requestLoggingInterceptor) {
        this.requestLoggingInterceptor = requestLoggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor).addPathPatterns("/**");
    }

    /**
     * Thymeleaf Shiro 标签方言。
     *
     * @return ShiroDialect
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
