package com.md.basePlatform.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.md.basePlatform.shiro.DemoRealm;

/**
 * Apache Shiro 安全配置（演示账号：admin / admin123）。
 */
@Configuration
public class ShiroConfiguration {

    /**
     * 自定义 Realm。
     *
     * @return DemoRealm
     */
    @Bean
    public DemoRealm demoRealm(CredentialsMatcher credentialsMatcher) {
        DemoRealm realm = new DemoRealm();
        realm.setCredentialsMatcher(credentialsMatcher);
        return realm;
    }

    /**
     * 明文比对演示账号密码（仅用于演示环境）。
     *
     * @return CredentialsMatcher
     */
    @Bean
    public CredentialsMatcher credentialsMatcher() {
        return (token, info) -> {
            org.apache.shiro.authc.UsernamePasswordToken up =
                    (org.apache.shiro.authc.UsernamePasswordToken) token;
            String input = new String(up.getPassword());
            Object stored = info.getCredentials();
            return stored != null && stored.equals(input);
        };
    }

    /**
     * Web SecurityManager。
     *
     * @param demoRealm realm
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager(DemoRealm demoRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(demoRealm);
        return manager;
    }

    /**
     * Shiro 过滤器链。
     *
     * @param securityManager 安全管理器
     * @return ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setLoginUrl("/login");
        factoryBean.setSuccessUrl("/uavs");
        factoryBean.setUnauthorizedUrl("/login");
        
        // 设置字符编码过滤器
        Map<String, javax.servlet.Filter> filters = new LinkedHashMap<>();
        filters.put("encodingFilter", new org.apache.shiro.web.servlet.OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(javax.servlet.ServletRequest servletRequest, 
                                           javax.servlet.ServletResponse servletResponse, 
                                           javax.servlet.FilterChain filterChain) 
                    throws javax.servlet.ServletException, java.io.IOException {
                servletRequest.setCharacterEncoding("UTF-8");
                servletResponse.setCharacterEncoding("UTF-8");
                filterChain.doFilter(servletRequest, servletResponse);
            }
        });
        factoryBean.setFilters(filters);
        
        Map<String, String> chain = new LinkedHashMap<>();
        chain.put("/login", "anon");
        chain.put("/logout", "logout");
        chain.put("/css/**", "anon");
        chain.put("/webjars/**", "anon");
        chain.put("/error", "anon");
        chain.put("/**", "authc");
        factoryBean.setFilterChainDefinitionMap(chain);
        return factoryBean;
    }

    /**
     * 支持 Shiro 注解（如未来启用 {@code @RequiresPermissions}）。
     *
     * @param securityManager 安全管理器
     * @return Advisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
