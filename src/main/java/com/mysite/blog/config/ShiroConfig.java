package com.mysite.blog.config;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/20 19:05
 */
@Configuration
public class ShiroConfig {

    /**
     * ShiroFilterFactoryBean
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String,String> map = new LinkedHashMap<>();
        map.put("/admin/common/**","anon");
        map.put("/admin/dist/**","anon");
        map.put("/admin/plugins/**","anon");
        map.put("/admin/upload/**","anon");
        map.put("/admin/**","user");
        map.put("/admin/configurations/**","authc");
        map.put("/admin/profile/**","authc");
        map.put("/admin/login","anon");
        map.put("/admin/loginOut","anon");
        shiroFilterFactoryBean.setLoginUrl("/admin/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }


    /**
     * DefaultWebSecurityManager
     * @return
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm")UserRealm userRealm, @Qualifier("rememberMeManager")RememberMeManager rememberMeManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 关联userRealm
        securityManager.setRealm(userRealm);
        securityManager.setRememberMeManager(rememberMeManager);
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public DefaultWebSessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionIdCookie(cookieDao());
        return sessionManager;
    }

    public Cookie cookieDao(){
        Cookie cookie = new SimpleCookie();
        cookie.setName("WEBCOOKIEID");
        return cookie;
    }

    /**
     * realm
     * @return
     */
    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }

    @Bean
    public SimpleCookie simpleCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // 设置cookie过期时间 172800-->2 天
        simpleCookie.setMaxAge(172800);
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager(@Qualifier("simpleCookie")SimpleCookie simpleCookie){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(simpleCookie);
        cookieRememberMeManager.setCipherKey(Base64.decode("6ZmI6I2j5Y+R5aSn5ZOlAA=="));
        return cookieRememberMeManager;
    }

}
