package com.mysite.blog.mapper;

import com.mysite.blog.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/5 15:54
 */
@Mapper
@Repository
public interface CategoryMapper {

    /**
     * 得到分类个数
     * @return
     */
    int getCategoryTotal();

    /**
     * 分页查询用户
     * @return
     */
    List<Category> selectPage();

    /**
     * 添加分类
     * @param categoryName
     * @return
     */
    int insertCategory(@Param("categoryName")String categoryName);

    /**
     * 删除操作（修改is_delete为1）
     * @param ids
     * @return
     */
    int deleteById(Integer[] ids);
    /**
     * 修改删除状态并且更改添加事件
     * @param categoryId
     * @param createTime
     * @return
     */
    int updateIsDelete(@Param("categoryId")Integer categoryId, @Param("createTime")Date createTime);

    /**
     * 动态修改
     * @param category
     * @return
     */
    int updateDynamicById(Category category);

    /**
     * 添加重复分类时修改删除状态并且更新createTime
     * @param categoryId
     * @return
     */
    int updateIsDeleteTime(@Param("categoryId")Integer categoryId);
    /**
     * 是否重复
     * @param categoryName
     * @return
     */
    Category queryByName(@Param("categoryName")String categoryName);

    /**
     * 是否重复
     * @param categoryId
     * @return
     */
    Category queryById(@Param("categoryId")Integer categoryId);

}
