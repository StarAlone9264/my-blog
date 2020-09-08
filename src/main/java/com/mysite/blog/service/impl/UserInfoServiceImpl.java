package com.mysite.blog.service.impl;

import com.mysite.blog.mapper.RoleUserRelationMapper;
import com.mysite.blog.mapper.UserInfoMapper;
import com.mysite.blog.pojo.RoleUserRelation;
import com.mysite.blog.pojo.UserInfo;
import com.mysite.blog.service.UserInfoService;
import com.mysite.blog.util.Md5Util;
import com.mysite.blog.util.RandomName;
import com.mysite.blog.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * @author Star
 * @version 1.0
 * @date 2020/5/29 15:32
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RoleUserRelationMapper roleUserRelationMapper;

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

    @Override
    public String addUser(String loginUserName, String loginUserPassword) {
        UserInfo userInfo = userInfoMapper.login(loginUserName);
        if (userInfo != null){
            return "exist";
        }
        String pwd = Md5Util.Md5Encode(loginUserPassword,"UTF-8");
        String userId = UuidUtil.getUUID();
        String nickName = RandomName.getStringRandom(6);
        // 8张图片随机
        int randomInt =  new Random().nextInt(8)+1;
        String url = "/admin/dist/img/user"+randomInt+"-128x128.jpg";
        UserInfo user = new UserInfo(userId, loginUserName, pwd, nickName, 1, null, null, null, url, 1);
        if (userInfoMapper.addUser(user) > 0){
            // 默认新添加的用户均为普通用户
            // 管理员 1
            // 普通用户 2
            Integer role = 2;
            if (roleUserRelationMapper.insert(new RoleUserRelation(role,user.getUserId())) > 0){
                return "success";
            }else {
                return "failed";
            }
        }
        return "failed";
    }
}
