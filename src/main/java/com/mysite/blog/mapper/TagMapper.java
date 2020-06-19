package com.mysite.blog.mapper;

import com.mysite.blog.pojo.BlogTagCount;
import com.mysite.blog.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/6 16:01
 */
@Mapper
@Repository
public interface TagMapper {

    /**
     * 得到标签个数
     * @return
     */
    int getTagTotal();
    /**
     * 分页查询用户
     * @return
     */
    List<Tag> selectPage();

    /**
     * 以分类名查询
     * @param tagName
     * @return
     */
    Tag queryByName(@Param("tagName")String tagName);
    /**
     * 以分类名查询
     * @param tagId
     * @return
     */
    Tag queryById(@Param("tagId")Integer tagId);
    /**
     * 添加
     * @param tagName
     * @return
     */
    int insertTag(@Param("tagName")String tagName);

    /**
     * 删除
     * @param ids
     * @return
     */
    int deleteById(Integer[] ids);

    /**
     * 更新删除状态
     * @param tagId
     * @return
     */
    int updateIsDelete(@Param("tagId")Integer tagId);

    /**
     * 批量插入博客标签
     * @param tagList
     * @return
     */
    int batchInsertBlogTag(List<Tag> tagList);

    /**
     * 热门标签
     * @return
     */
    List<BlogTagCount> getTagCount();
}
