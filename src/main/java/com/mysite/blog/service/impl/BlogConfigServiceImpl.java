package com.mysite.blog.service.impl;

import com.mysite.blog.mapper.BlogConfigMapper;
import com.mysite.blog.pojo.BlogConfig;
import com.mysite.blog.service.BlogConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/14 22:31
 */
@Service
public class BlogConfigServiceImpl implements BlogConfigService {
    // 设置默认值

    private static final String WEBSITE_NAME = "personal blog";
    private static final String WEBSITE_DESCRIPTION = "My blog是我的练手网站，由springboot+mysql+thymeleaf开发";
    private static final String WEBSITE_LOGO_URL = "/admin/dist/img/logo2.png";
    private static final String WEBSITE_ICON_URL = "/admin/dist/img/favicon.png";
    private static final String WEBSITE_HEADER_TITLE = "凡事不要想得太复杂，手握的太紧，东西会碎，手会疼。";
    private static final String WEBSITE_COVER_URL = "/blog/amaze/images/header.jpg";
    private static final String FOOTER_ABOUT = "My blog";
    private static final String FOOTER_CASE_NUMBER = "备案号现在没有";
    private static final String FOOTER_COPY_RIGHT = "阿航";
    private static final String FOOTER_POWERED_BY = "这写GitHub";
    private static final String FOOTER_POWERED_BY_URL = "这写GitHub网址";

    @Autowired
    private BlogConfigMapper blogConfigMapper;

    /**
     * 查询全部
     * @return
     */
    @Override
    public Map<String, String> getAllConfigs() {
        List<BlogConfig> blogConfigs = blogConfigMapper.queryAll();
        Map<String, String> configMap = blogConfigs.stream().collect(Collectors.toMap(BlogConfig::getConfigName, BlogConfig::getConfigValue));
        for (Map.Entry<String, String> config : configMap.entrySet()) {
            if ("websiteName".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())){
                config.setValue(WEBSITE_NAME);
            }
            if ("websiteDescription".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())){
                config.setValue(WEBSITE_DESCRIPTION);
            }
            if ("websiteLogoUrl".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())){
                config.setValue(WEBSITE_LOGO_URL);
            }
            if ("websiteIconUrl".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())){
                config.setValue(WEBSITE_ICON_URL);
            }
            if ("websiteHeaderTitle".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())){
                config.setValue(WEBSITE_HEADER_TITLE);
            }
            if ("websiteCoverUrl".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())){
                config.setValue(WEBSITE_COVER_URL);
            }
            if ("footerAbout".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())){
                config.setValue(FOOTER_ABOUT);
            }
            if ("footerCaseNumber".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())){
                config.setValue(FOOTER_CASE_NUMBER);
            }
            if ("footerCopyRight".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())){
                config.setValue(FOOTER_COPY_RIGHT);
            }
            if ("footerPoweredBy".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())){
                config.setValue(FOOTER_POWERED_BY);
            }
            if ("footerPoweredByUrl".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())){
                config.setValue(FOOTER_POWERED_BY_URL);
            }
        }
        return configMap;
    }

    /**
     * 修改
     * @param configName
     * @param configValue
     * @return
     */
    @Override
    public int updateByConfigName(String configName, String configValue) {
        BlogConfig blogConfig = blogConfigMapper.queryByConfigName(configName);
        if(blogConfig != null){
            blogConfig.setConfigValue(configValue);
            blogConfig.setUpdateTime(new Date());
            return blogConfigMapper.updateByConfigName(blogConfig);
        }
        return 0;
    }
}
