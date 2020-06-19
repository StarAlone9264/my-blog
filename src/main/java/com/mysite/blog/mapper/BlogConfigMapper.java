package com.mysite.blog.mapper;

import com.mysite.blog.pojo.BlogConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/14 22:24
 */
@Mapper
@Repository
public interface BlogConfigMapper {

    /**
     * 查询全部
     * @return
     */
    List<BlogConfig> queryAll();

    /**
     * 按照配置名称查询
     * @param configName
     * @return
     */
    BlogConfig queryByConfigName(String configName);

    /**
     * 修改
     * @param blogConfig
     * @return
     */
    int updateByConfigName(BlogConfig blogConfig);
}
