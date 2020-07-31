package com.mysite.blog.service;

import com.mysite.blog.pojo.Category;
import com.mysite.blog.util.PageRequest;
import com.mysite.blog.util.PageResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/5 15:58
 */
public interface CategoryService {

    /**
     * 得到分类个数
     * @return
     */
    int getCategoryTotal();

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
     * 修改
     * @param categoryId
     * @param categoryName
     * @return
     */
    String updateCategory(@Param("categoryId")Integer categoryId,@Param("categoryName")String categoryName);

    /**
     * 是否重复
     * @param categoryName
     * @return
     */
    String queryByName(@Param("categoryName")String categoryName);

    /**
     * 是否重复
     * @param ids
     * @return
     */
    String queryById(Integer[] ids);

    /**
     * 查询全部分类
     * @return
     */
    List<Category> getAllCategories();

}
