package com.mysite.blog.config;

import com.mysite.blog.mapper.RoleMapper;
import com.mysite.blog.mapper.RoleUserRelationMapper;
import com.mysite.blog.pojo.Role;
import com.mysite.blog.pojo.RoleUserRelation;
import com.mysite.blog.pojo.UserInfo;
import com.mysite.blog.service.UserInfoService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/20 19:03
 */
public class UserRealm extends AuthorizingRealm {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RoleUserRelationMapper roleUserRelationMapper;

    @Resource
    private RoleMapper roleMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info =new SimpleAuthorizationInfo();
        Map map = new HashMap();
        String userId = (String) principals.getPrimaryPrincipal();
        map.put("userId",userId);
        RoleUserRelation roleUserRelation = roleUserRelationMapper.dynamicQuery(map);
        Role role = roleMapper.queryById(roleUserRelation.getRoleId());
        info.addRole(role.getRoleName());
        return info;
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
