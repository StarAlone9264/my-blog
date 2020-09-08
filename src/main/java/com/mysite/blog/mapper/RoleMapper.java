package com.mysite.blog.mapper;

import com.mysite.blog.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Star
 * @version 1.0
 * @date 2020/9/8 10:24
 */
@Mapper
@Repository
public interface RoleMapper {

    public Role queryById(Integer roleId);

}
