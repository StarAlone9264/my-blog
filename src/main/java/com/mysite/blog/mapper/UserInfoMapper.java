package com.mysite.blog.mapper;

import com.mysite.blog.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Star
 * @version 1.0
 * @date 2020/5/29 15:24
 */
@Mapper
@Repository
public interface UserInfoMapper {

    /**
     * 登陆
     * @param loginUserName 登陆名
     * @return 用户对象
     */
    UserInfo login(@Param("loginUserName")String loginUserName);
    /**
     * 修改用户信息
     * @param userInfo 用户对象
     * @return int
     */
    int modifyInformationByUserId(UserInfo userInfo);

    /**
     * 根据id查询
     * @param userId userId
     * @return 用户对象
     */
    UserInfo selectByUserId(@Param("userId")String userId);

    /**
     * 修改密码
     * @param userId userId
     * @param loginUserPassword 登陆密码
     * @return int
     */
    int changePassword(@Param("userId")String userId,@Param("loginUserPassword")String loginUserPassword);

    /**
     * 查全部
     * @return List<UserInfo>
     */
    List<UserInfo> queryAll();

    /**
     * 根据id查询
     * @param userId userId
     * @return UserInfo
     */
    UserInfo queryById(@Param("userId")String userId);

    /**
     * 添加用户
     * @param userInfo userInfo
     * @return int
     */
    int addUser(UserInfo userInfo);

}
