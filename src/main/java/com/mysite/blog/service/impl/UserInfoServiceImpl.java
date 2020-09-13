package com.mysite.blog.service.impl;

import com.mysite.blog.mapper.RoleUserRelationMapper;
import com.mysite.blog.mapper.UserEmailVerificationMapper;
import com.mysite.blog.mapper.UserInfoMapper;
import com.mysite.blog.pojo.RoleUserRelation;
import com.mysite.blog.pojo.UserEmailVerification;
import com.mysite.blog.pojo.UserInfo;
import com.mysite.blog.service.UserInfoService;
import com.mysite.blog.util.EmailSendUtil;
import com.mysite.blog.util.Md5Util;
import com.mysite.blog.util.RandomName;
import com.mysite.blog.util.UuidUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Star
 * @version 1.0
 * @date 2020/5/29 15:32
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {


    @Value("${customize.url}")
    private String urlPerFix;

    @Value("${customize.expirationTime}")
    private long expirationTime;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RoleUserRelationMapper roleUserRelationMapper;

    @Resource
    private UserEmailVerificationMapper userEmailVerificationMapper;

    @Resource
    private EmailSendUtil emailSendUtil;

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
    public String addUser(String loginUserName, String loginUserPassword, String userEmail) {
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
        if (randomInt == 2){
            url = "/admin/dist/img/user"+randomInt+"-160x160.jpg";
        }
        UserInfo user = new UserInfo(userId, loginUserName, pwd, nickName, 1, null, userEmail, null, url, 1);
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

    @Override
    public String findUser(String loginUserName, String userEmail) {
        UserInfo login = userInfoMapper.login(loginUserName);
        if (login == null){
            return "empty";
        }
        if(!userEmail.equals(login.getUserEmail())){
            return "Incorrect email";
        }
        return "success";
    }

    /**
     * 重置密码，给用户发送验证链接
     * 使用延迟线程，到时间使链接失效
     * @param loginUserName 登陆名
     * @param userEmail 用户邮箱
     * @return 结果
     */
    @Override
    public String forgetPassword(String loginUserName, String userEmail) {
        UserInfo login = userInfoMapper.login(loginUserName);
        if (login == null){
            return "empty";
        }
        if (!userEmail.equals(login.getUserEmail())){
            return "Inconsistent information!";
        }
        // 生成验证链接并将验证id 存入数据库
        String verifyId = UuidUtil.getUUID();
        UserEmailVerification userEmailVerification = new UserEmailVerification();
        userEmailVerification.setUserId(login.getUserId());
        userEmailVerification.setVerifyId(verifyId);
        String url = urlPerFix +"user/toResetPassword/"+login.getUserId()+"/"+verifyId;
        // 定义发送内容
        String tittle = "你好："+ login.getNickName();
        String news = "<html>\n" +
                "<body>\n" +
                "\t<div>小老弟，你有点粗心\uD83D\uDE10</div>\n" +
                "\t<div>请将链接复制到浏览器打开："+url+"</div>\n" +
                "\t<div>❗该链接有效时间为20分钟❗</div>\n" +
                "\t<div>这是一份密码重置邮件，无需回复。</div>\n" +
                "</body>\n" +
                "</html>";
        String s = emailSendUtil.sendEmail(userEmail, tittle, news);
            // 邮件发送成功后 添加数据并执行定时任务
            if("Mail sent successfully".equals(s)){
                if (userEmailVerificationMapper.insert(userEmailVerification) > 0) {
                    ScheduledExecutorService pool = Executors.newScheduledThreadPool(3);
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    System.out.println("========开始计时"+sdf.format(new Date())+"========");
                    System.out.println("用户："+login.getLoginUserName()+"的重置密码链接将在"+expirationTime+"分钟后失效");
                    pool.schedule(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("========延时线程开始执行========");
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            // 判断该链接是否被使用
                            UserEmailVerification queryById = userEmailVerificationMapper.queryById(login.getUserId(), verifyId);
                            if (queryById.getExpired() == 1){
                                System.out.println("当前线程名："+Thread.currentThread().getName());
                                System.out.println("用户id为："+ login.getUserId()+ "--->的邮箱修改密码链接已被使用！");
                                System.out.println("=========执行结束==========");
                                return;
                            }
                            // 使验证链接失效
                            if (userEmailVerificationMapper.deleteById(new UserEmailVerification(login.getUserId(),verifyId)) > 0){
                                System.out.println("当前线程名："+Thread.currentThread().getName());
                                System.out.println("用户id为："+ login.getUserId()+ "--->执行的邮箱修改密码链接已失效！");
                                System.out.println("失效链接：" + url);
                                System.out.println("失效时间："+sdf.format(new Date()));
                                System.out.println("=========执行结束==========");
                            }else {
                                // 修改失败执行操作
                                System.out.println("当前线程名："+Thread.currentThread().getName());
                                System.out.println("删除链接失败！");
                                System.out.println("操作时间："+sdf.format(new Date()));
                                System.out.println("=========执行结束==========");
                            }
                        }
                    },expirationTime, TimeUnit.MINUTES);
                    // 关闭线程池
                    pool.shutdown();
                }else {
                    return "发生了我也不知道的错误！";
                }
                return s;
            }
        return "发生了我也不知道的错误！";
    }

    /**
     * 重置密码
     * @param verifyId 验证id
     * @param userId 用户id
     * @param password 密码
     * @return 结果
     */
    @Override
    public String resetPassword(String verifyId, String userId, String password) {
        UserEmailVerification queryById = userEmailVerificationMapper.queryById(userId, verifyId);
        if (queryById == null){
            return "verification failed!";
        }
        UserInfo userInfo = userInfoMapper.queryById(userId);
        if (userInfo == null){
            return "User does not exist!";
        }
        String loginPassword = Md5Util.Md5Encode(password, "UTF-8");
        // 密码重置成功 链接失效
        if (userInfoMapper.changePassword(userId, loginPassword) > 0){
            if (userEmailVerificationMapper.deleteById(new UserEmailVerification(userId,verifyId)) > 0){
                return "success";
            }else {
                return "failed";
            }
        }
        return "failed";
    }
}
