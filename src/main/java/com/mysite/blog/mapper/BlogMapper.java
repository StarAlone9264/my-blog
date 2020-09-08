package com.mysite.blog.mapper;

import com.mysite.blog.pojo.Blog;
import com.mysite.blog.pojo.BlogUserRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/8 15:25
 */
@Mapper
@Repository
public interface BlogMapper {

    /**
     * 博客条数
     * @return
     */
    int getBlogTotal(String userId);

    /**
     * 添加博客
     * @param blog
     * @return
     */
    int insertBlog(Blog blog);

    /**
     * 查询
     * @param blogs
     * @return
     */
    List<Blog> queryBlogList(Map blogs);

    /**
     * 根据博客id查询博客
     * @param list list
     * @return List
     */
    List<Blog> queryUserBlogList(List<BlogUserRelation> list);

    /**
     * 根据主键id查询
     * @param blogPrimaryId blogPrimaryId
     * @return Blog
     */
    Blog getBlogById(@Param("blogPrimaryId")Long blogPrimaryId);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int batchDelete(Integer[] ids);

    /**
     * 根据主键id动态修改博客
     * @param blog
     * @return
     */
    int updateDynamicByPrimaryId(Blog blog);

    /**
     * 首页导航栏
     * @param type
     * @param limit
     * @return
     */
    List<Blog> queryBlogForIndex(@Param("type") int type,@Param("limit") int limit);

    /**
     * 根据标签查询博客
     * @param tagId
     * @return
     */
    List<Blog> queryPageByTagId(Integer tagId);

    /**
     * 以subUrl 查询博客
     * @param subUrl subUrl
     * @return Blog
     */
    Blog queryBlogBySubUrl(String subUrl);
}
