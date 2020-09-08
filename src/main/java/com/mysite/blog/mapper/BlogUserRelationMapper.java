package com.mysite.blog.mapper;

import com.mysite.blog.pojo.BlogUserRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/16 19:13
 */
@Mapper
@Repository
public interface BlogUserRelationMapper {

    /**
     * 动态查询
     * @param map
     * @return
     */
    BlogUserRelation queryDynamic(Map map);

    /**
     * 添加
     * @param blogUserRelation
     * @return
     */
    int insert(BlogUserRelation blogUserRelation);
    /**
     * 根据博客id删除关系数据
     * @param blogId
     * @return
     */
    int deleteByBlogPrimaryId(@Param("blogId") String blogId);

    /**
     * 根据用户id查询博客id
     * @param userId 用户id
     * @return 博客id 集合
     */
    List<BlogUserRelation> queryBlogId(@Param("userId") String userId);
}
