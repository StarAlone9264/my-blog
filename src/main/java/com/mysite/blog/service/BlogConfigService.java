package com.mysite.blog.service;

import com.mysite.blog.pojo.BlogConfig;

import java.util.Map;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/14 22:30
 */
public interface BlogConfigService {
    /**
     * 获取所有的配置项
     *
     * @return
     */
    Map<String,String> getAllConfigs();

    /**
     * 修改
     * @param configName
     * @param configValue
     * @return
     */
    int updateByConfigName(String configName, String configValue);
}
