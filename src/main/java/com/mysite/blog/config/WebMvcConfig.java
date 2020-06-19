package com.mysite.blog.config;

import com.mysite.blog.interceptor.AdminLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author Star
 * @version 1.0
 * @date 2020/5/28 14:26
 * WebMvcConfig
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加一个拦截器，拦截以/admin为前缀的url路径
        registry.addInterceptor(adminLoginInterceptor)
                .addPathPatterns("/admin/**")
                // 排除登陆页
                .excludePathPatterns("/admin/login")
                // 排除静态资源
                .excludePathPatterns("/admin/dist/**")
                .excludePathPatterns("/admin/plugins/**")
                .excludePathPatterns("/admin/upload/**")
                .excludePathPatterns("/admin/common/**");
    }

    /**
     * 上传服务器需要删除以下代码
     * 本地不需要注释
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //访问的时候就直接访问http://localhost:2333/upload/文件名
        String path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\upload\\";
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+path);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
