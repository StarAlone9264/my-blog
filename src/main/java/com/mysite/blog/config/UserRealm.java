package com.mysite.blog.config;

import com.mysite.blog.pojo.UserInfo;
import com.mysite.blog.service.UserInfoService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/20 19:03
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserInfoService userInfoService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        UserInfo login = userInfoService.login(usernamePasswordToken.getUsername());
        if (login == null){
            return null;
        }
        return new SimpleAuthenticationInfo(login.getUserId(), login.getLoginUserPassword(), getName());
    }
}
