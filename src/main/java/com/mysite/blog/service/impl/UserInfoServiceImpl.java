package com.mysite.blog.service.impl;

import com.mysite.blog.mapper.UserInfoMapper;
import com.mysite.blog.pojo.UserInfo;
import com.mysite.blog.service.UserInfoService;
import com.mysite.blog.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Star
 * @version 1.0
 * @date 2020/5/29 15:32
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo login(String loginUserName) {
        return userInfoMapper.login(loginUserName);
    }

    @Override
    public int modifyInformationByUserId(UserInfo userInfo) {
        return userInfoMapper.modifyInformationByUserId(userInfo);
    }

    @Override
    public Boolean changePassword(String userId, String oldLoginUserPassword ,String newLoginUserPassword) {
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        if (userInfo!=null){
            // 得到原密码
            String loginUserPassword = userInfo.getLoginUserPassword();
            // 判断用户输入的密码与数据库的密码是否一致
            String md5Encode = Md5Util.Md5Encode(oldLoginUserPassword, "UTF-8");
            if (loginUserPassword.equals(md5Encode)){
                // 修改
                newLoginUserPassword = Md5Util.Md5Encode(newLoginUserPassword,"UTF-8");
                if(userInfoMapper.changePassword(userId,newLoginUserPassword)>0){
                    return true;
                }
            }
        }
        return false;
    }

    public UserInfo queryById(String userId){
        return userInfoMapper.queryById(userId);
    }

    @Override
    public List<UserInfo> queryAll() {
        return userInfoMapper.queryAll();
    }
}
