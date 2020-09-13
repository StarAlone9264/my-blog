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
     * @param loginUserName 用户名
     * @return 用户对象
     */
    UserInfo login(@Param("loginUserName")String loginUserName);
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

    /**
     * 登陆
     * @param loginUserName 登陆名
     * @param loginUserPassword 登陆密码
     * @return 返回结果
     */
    String addUser(String loginUserName , String loginUserPassword, String userEmail);

    /**
     * 查询用户
     * @param loginUserName 登陆名
     * @param userEmail 用户邮箱
     * @return String
     */
    String findUser(String loginUserName,String userEmail);

    /**
     * 忘记密码，第一步验证信息
     * @param loginUserName 登陆名
     * @param userEmail 用户邮箱
     * @return String
     */
    String forgetPassword(String loginUserName,String userEmail);

    /**
     * 邮箱验证修改密码
     * @param verifyId 验证id
     * @param userId 用户id
     * @param password 密码
     * @return String
     */
    String resetPassword(String verifyId, String userId, String password);

}
