package com.mysite.blog.service;

import com.mysite.blog.controller.vo.BlogDetailVo;
import com.mysite.blog.controller.vo.SimpleBlogListVo;
import com.mysite.blog.pojo.Blog;
import com.mysite.blog.util.PageRequest;
import com.mysite.blog.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/8 15:28
 */
public interface BlogService {

    /**
     * 博客条数
     * @param userId 用户id
     * @return int
     */
    int getBlogTotal(String userId);
    /**
     * 添加博客
     * @param blog blog
     * @param userId 用户id
     * @return String
     */
    String insertBlog(Blog blog,String userId);

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
     * 分页查询用户博客
     * @param pageRequest pageRequest
     * @param userId 用户id
     * @return PageResult
     */
    PageResult queryUserBlogList(PageRequest pageRequest,String userId);
    /**
     * 根据id查询
     * @param blogPrimaryId id
     * @return  Blog
     */
    Blog getBlogById(Long blogPrimaryId);

    /***
     * 批量删除
     * @param ids id
     * @return Boolean
     */
    Boolean batchDeleteById(Integer[] ids);

    /**
     * 修改
     * @param blog blog
     * @return String
     */
    String updateBlog(Blog blog);

    /**
     * 模糊搜索博客
     * @param pageRequest PageRequest
     * @param keyword 关键字
     * @return PageResult
     */
    PageResult fuzzySearch(PageRequest pageRequest, String keyword);

    /**
     * 获取博客首页
     * @param pageNum 页数
     * @return PageResult
     */
    PageResult getBlogForListPage(int pageNum);

    /**
     * 首页导航栏
     * @param type 类型
     * @return List<SimpleBlogListVo>
     */
    List<SimpleBlogListVo> getBlogForListForIndex(int type);

    /**
     * 博客详情页
     * @param blogPrimaryId 主键id
     * @return 返回博客对象
     */
    BlogDetailVo getBlogDetail(Long blogPrimaryId);

    /**
     * 根据分类查询博客
     * @param categoryName 分类名
     * @param page 页数
     * @return PageResult
     */
    PageResult getBlogPageByCategory(String categoryName, Integer page);

    /**
     * 根据标签查询
     * @param tagName 标签名
     * @param page 页数
     * @return PageResult
     */
    PageResult getBlogPageByTag(String tagName, Integer page);

    /**
     * 搜索
     * @param keyword 关键字
     * @param page 页数
     * @return PageResult
     */
    PageResult getBlogPageBySearch(String keyword, Integer page);

    /**
     * 以自定义url查询博客
     * @param subUrl 用户自定义url
     * @return BlogDetailVo
     */
    BlogDetailVo getBlogPageBySubUrl(String subUrl);
}
