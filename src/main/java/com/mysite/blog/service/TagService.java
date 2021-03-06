package com.mysite.blog.service;

import com.mysite.blog.pojo.BlogTagCount;
import com.mysite.blog.util.PageRequest;
import com.mysite.blog.util.PageResult;
import com.mysite.blog.util.Result;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/6 16:03
 */
public interface TagService {

    /**
     * 得到标签个数
     * @return
     */
    int getTagTotal();
    /**
     * 分页查询接口
     * 这里统一封装了分页请求和结果，避免直接引入具体框架的分页对象, 如MyBatis或JPA的分页对象
     * 从而避免因为替换ORM框架而导致服务层、控制层的分页接口也需要变动的情况，替换ORM框架也不会
     * 影响服务层以上的分页接口，起到了解耦的作用
     * @param pageRequest 自定义，统一分页查询请求
     * @return PageResult 自定义，统一分页查询结果
     */
    PageResult findPage(PageRequest pageRequest);

    /**
     * 以分类名查询
     * @param tagName
     * @return
     */
    Result queryByName(@Param("tagName")String tagName);

    /**
     * 以分类名查询
     * @param ids
     * @return
     */
    Result queryById(Integer[] ids);

    /**
     * 获取热门标签
     * @return
     */
    List<BlogTagCount> getBlogTagCountForIndex();
}
