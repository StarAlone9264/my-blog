package com.mysite.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
