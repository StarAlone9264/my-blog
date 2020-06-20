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

//    /**
//     * 用户登陆
//     * @param loginUserName
//     * @param loginUserPassword
//     * @return
//     */
//    UserInfo login(@Param("loginUserName")String loginUserName, @Param("loginUserPassword")String loginUserPassword);

    UserInfo login(@Param("loginUserName")String loginUserName);
    /**
     * 修改用户信息
     * @param userInfo
     * @return
     */
    int modifyInformationByUserId(UserInfo userInfo);

    /**
     * 根据id查询
     * @param userId
     * @return
     */
    UserInfo selectByUserId(@Param("userId")String userId);

    /**
     * 修改密码
     * @param userId
     * @param loginUserPassword
     * @return
     */
    int changePassword(@Param("userId")String userId,@Param("loginUserPassword")String loginUserPassword);

    /**
     * 查全部
     * @return
     */
    List<UserInfo> queryAll();

    /**
     * 根据id查询
     * @param userId
     * @return
     */
    UserInfo queryById(@Param("userId")String userId);

}
