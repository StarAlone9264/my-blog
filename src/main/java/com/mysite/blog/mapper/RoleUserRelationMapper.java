package com.mysite.blog.mapper;

import com.mysite.blog.pojo.RoleUserRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author Star
 * @version 1.0
 * @date 2020/9/8 10:50
 */
@Mapper
@Repository
public interface RoleUserRelationMapper {
    /**
     * 动态查询
     * @param map 指定
     * @return RoleUserRelation
     */
    public RoleUserRelation dynamicQuery(Map map);

    public int insert(RoleUserRelation roleUserRelation);
}
