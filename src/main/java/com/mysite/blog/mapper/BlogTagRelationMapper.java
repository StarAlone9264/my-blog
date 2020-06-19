package com.mysite.blog.mapper;

import com.mysite.blog.pojo.BlogTagRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/9 13:37
 */
@Mapper
@Repository
public interface BlogTagRelationMapper {

    /**
     * 批量添加
     * @param blogTagRelationList
     * @return
     */
    int batchInsert(@Param("relationList") List<BlogTagRelation> blogTagRelationList);

    /**
     * 根据博客主键id删除关系数据
     * @param blogPrimaryId
     * @return
     */
    int deleteByBlogPrimaryId(Long blogPrimaryId);
}
