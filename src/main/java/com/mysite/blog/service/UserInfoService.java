package com.mysite.blog.service;

import com.mysite.blog.pojo.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Star
 * @version 1.0
 * @date 2020/5/29 15:30
 */
public interface UserInfoService {
    /**
     * 用户登陆
     * @param loginUserName
     * @param loginUserPassword
     * @return
     */
    UserInfo login(@Param("loginUserName")String loginUserName, @Param("loginUserPassword")String loginUserPassword);

    /**
     * 修改用户信息
     * @param userInfo
     * @return
     */
    int modifyInformationByUserId(UserInfo userInfo);

    /**
     * 修改密码
     * @param userId
     * @param oldLoginUserPassword
     * @param newLoginUserPassword
     * @return
     */
    Boolean changePassword(@Param("userId")String userId,@Param("oldLoginUserPassword")String oldLoginUserPassword,@Param("newLoginUserPassword")String newLoginUserPassword);
    /**
     * 查全部
     * @return
     */
    List<UserInfo> queryAll();
}
