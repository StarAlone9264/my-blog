package com.mysite.blog.controller.admin;

import com.mysite.blog.pojo.UserInfo;
import com.mysite.blog.service.BlogConfigService;
import com.mysite.blog.service.impl.BlogServiceImpl;
import com.mysite.blog.service.impl.CategoryServiceImpl;
import com.mysite.blog.service.impl.TagServiceImpl;
import com.mysite.blog.service.impl.UserInfoServiceImpl;
import com.mysite.blog.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Star
 * @version 1.0
 * @date 2020/5/29 21:12
 */
@Controller
@RequestMapping("/admin")
public class UserInfoController {
    @Resource
    private UserInfoServiceImpl userInfoService;

    @Resource
    private CategoryServiceImpl categoryService;

    @Resource
    private TagServiceImpl tagService;

    @Resource
    private BlogServiceImpl blogService;

    @Resource
    private BlogConfigService blogConfigService;

    /**
     * 跳转登陆页
     *
     * @return
     */
    @GetMapping({"/login"})
    public String login(HttpServletRequest request) {
        // 强制登陆，退出当前用户
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if (principal != null) {
            request.setAttribute("errorMsg", "检测到强制登陆，请重新登陆！");
            subject.logout();
        }
        request.setAttribute("configurations", blogConfigService.getAllConfigs());
        request.setAttribute("key", RandomUtils.getRandomStr(16));
        return "admin/login";
    }

    /**
     * 博客后台管理首页
     * @param request request
     * @return 跳转网页
     */
    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        UserInfo userInfo = userInfoService.queryById(principal);
        request.setAttribute("path", "index");
        request.setAttribute("user", new UserInfo(userInfo.getUserId(), userInfo.getLoginUserName(), userInfo.getNickName(), userInfo.getSex(), userInfo.getUserPhone(), userInfo.getUserEmail(), userInfo.getUserAddress(), userInfo.getProfilePictureUrl()));
        request.setAttribute("configurations", blogConfigService.getAllConfigs());
        request.setAttribute("blogTotal", blogService.getBlogTotal(userInfo.getUserId()));
        request.setAttribute("categoryTotal", categoryService.getCategoryTotal());
        request.setAttribute("tagCount", tagService.getTagTotal());
        return "admin/index";
    }

    /**
     * 登陆
     * @param username 用户名
     * @param password 用户密码
     * @return 返回页
     */
    @PostMapping({"/login"})
    public String login(HttpSession session, HttpServletRequest request, @RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("verifyCode") String verifyCode, @RequestParam("key") String key, Boolean rememberMe) {
        // 判断用户名密码是否为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            request.setAttribute("errorMsg", "用户名或密码不能为空！");
            request.setAttribute("configurations", blogConfigService.getAllConfigs());
            request.setAttribute("key", RandomUtils.getRandomStr(16));
            return "admin/login";
        }
        // 判断验证码是否为空
        if (StringUtils.isEmpty(verifyCode)) {
            request.setAttribute("errorMsg", "验证码不能为空！");
            request.setAttribute("configurations", blogConfigService.getAllConfigs());
            request.setAttribute("key", RandomUtils.getRandomStr(16));
            return "admin/login";
        }
        // 获取session中的验证码
        String sessionVerifyCode = (String) session.getAttribute("RANDOM_CODE_KEY");
        // 判断用户填写的验证码是否与session中的一致
        if (!verifyCode.equalsIgnoreCase(sessionVerifyCode)) {
            request.setAttribute("errorMsg", "验证码不正确！");
            request.setAttribute("configurations", blogConfigService.getAllConfigs());
            request.setAttribute("key", RandomUtils.getRandomStr(16));
            return "admin/login";
        }

        // 判断用户填写的验证码是否与session中的一致
        if (StringUtils.isEmpty(key)) {
            request.setAttribute("errorMsg", "哎呀，发生了未知的错误，建议您重新进入登陆页试试呢！");
            request.setAttribute("configurations", blogConfigService.getAllConfigs());
            request.setAttribute("key", RandomUtils.getRandomStr(16));
            return "admin/login";
        }

        String decrypt = AesUtil.decrypt(password, key);
        password = Md5Util.Md5Encode(decrypt, "UTF-8");
        Subject subject = SecurityUtils.getSubject();
        // 如果存在用户 则 踢出
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        UsernamePasswordToken token = null;
        if (rememberMe != null && rememberMe) {
            token = new UsernamePasswordToken(username, password);
            token.setRememberMe(true);
        } else {
            token = new UsernamePasswordToken(username, password);
        }
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            request.setAttribute("errorMsg", "用户名不存在！");
            request.setAttribute("configurations", blogConfigService.getAllConfigs());
            request.setAttribute("key", RandomUtils.getRandomStr(16));
            return "admin/login";
        } catch (IncorrectCredentialsException e) {
            request.setAttribute("errorMsg", "密码错误");
            request.setAttribute("configurations", blogConfigService.getAllConfigs());
            request.setAttribute("key", RandomUtils.getRandomStr(16));
            return "admin/login";
        }
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        UserInfo userInfo = userInfoService.queryById(principal);
        if (userInfo.getIsLock() == 0) {
            request.setAttribute("errorMsg", "您的账号已被锁定，请联系网站管理员");
            request.setAttribute("configurations", blogConfigService.getAllConfigs());
            request.setAttribute("key", RandomUtils.getRandomStr(16));
            return "admin/login";
        }
        return "redirect:/admin/index";
    }

    /**
     * 跳转注册页
     * @param request request
     * @return 注册页
     */

    @GetMapping("/register")
    public String toRegister(HttpServletRequest request) {
        request.setAttribute("configurations", blogConfigService.getAllConfigs());
        return "admin/register";
    }

    /**
     * 用户注册
     * @param request request
     * @param loginUserName 登陆名
     * @param loginUserPassword 密码
     * @param userEmail 邮箱
     * @return Result
     */
    @PostMapping("/register")
    @ResponseBody
    public Result register(HttpServletRequest request, String loginUserName, String loginUserPassword, String userEmail) {
        String user = userInfoService.addUser(loginUserName, loginUserPassword, userEmail);
        // 注册成功，自动登陆
        if("success".equals(user)){
            Subject subject = SecurityUtils.getSubject();
            // 如果存在用户 则 踢出
            if (subject.isAuthenticated()) {
                subject.logout();
            }
            String pwd = Md5Util.Md5Encode(loginUserPassword,"UTF-8");
            UsernamePasswordToken token = new UsernamePasswordToken(loginUserName, pwd);;
            try {
                subject.login(token);
            } catch (UnknownAccountException e) {
                request.setAttribute("errorMsg", "用户名不存在！");
                request.setAttribute("configurations", blogConfigService.getAllConfigs());
                return ResultGenerator.genSuccessResult("发生了一些我也不知道的错误");
            } catch (IncorrectCredentialsException e) {
                request.setAttribute("errorMsg", "密码错误");
                request.setAttribute("configurations", blogConfigService.getAllConfigs());
                return ResultGenerator.genSuccessResult("发生了一些我也不知道的错误");
            }
        }
        return ResultGenerator.genSuccessResult(user);
    }

    /**
     * 跳转用户信息页
     * @param request request
     * @return 信息页
     */
    @GetMapping("/profile")
    public String profile(HttpServletRequest request) {
        request.setAttribute("path", "profile");
        request.setAttribute("configurations", blogConfigService.getAllConfigs());
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        UserInfo userInfo = userInfoService.queryById(principal);
        request.setAttribute("user", new UserInfo(userInfo.getUserId(), userInfo.getLoginUserName(), userInfo.getNickName(), userInfo.getSex(), userInfo.getUserPhone(), userInfo.getUserEmail(), userInfo.getUserAddress(), userInfo.getProfilePictureUrl()));
        return "admin/profile";
    }

    /**
     * 修改信息
     *
     * @param request           request
     * @param loginUserName     登录名
     * @param nickName          用户名
     * @param userPhone         电话
     * @param userEmail         邮箱
     * @param userAddress       地址
     * @param profilePictureUrl 头像
     * @return String
     */
    @PostMapping("/profile/modifyInformation")
    @ResponseBody
    public String modifyInformation(HttpServletRequest request,
                                    @RequestParam("loginUserName") String loginUserName,
                                    @RequestParam("nickName") String nickName,
                                    @RequestParam("sex") Integer sex,
                                    @RequestParam("userPhone") String userPhone,
                                    @RequestParam("userEmail") String userEmail,
                                    @RequestParam("userAddress") String userAddress,
                                    @RequestParam("profilePictureUrl") String profilePictureUrl) {
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName) || StringUtils.isEmpty(userPhone) ||
                StringUtils.isEmpty(userEmail) || StringUtils.isEmpty(userAddress)) {
            return "参数不能为空！";
        }
        if (SecurityUtils.getSubject() == null) {
            return "异常操作";
        }
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        UserInfo userInfo = userInfoService.queryById(principal);
        if (StringUtils.isEmpty(profilePictureUrl)) {
            profilePictureUrl = userInfo.getProfilePictureUrl();
        }
        if (userInfoService.modifyInformationByUserId(new UserInfo(userInfo.getUserId(), loginUserName, nickName, sex, userPhone, userEmail, userAddress, profilePictureUrl)) > 0) {
            SecurityUtils.getSubject().logout();
            return "success";
        } else {
            return "修改失败";
        }
    }
    /**
     * 修改密码
     * @param request          request
     * @param originalPassword 原密码
     * @param newPassword      新密码
     * @return String
     */
    @PostMapping("/profile/changePassword")
    @ResponseBody
    public String alterPassword(HttpServletRequest request,
                                @RequestParam("originalPassword") String originalPassword,
                                @RequestParam("newPassword") String newPassword) {

        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            return "参数不能为空!";
        }
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        if (userInfoService.changePassword(principal, originalPassword, newPassword)) {
            SecurityUtils.getSubject().logout();
            return "success";
        }
        return "修改失败！";
    }

    /**
     * 登出
     *
     * @return String
     */
    @GetMapping("/loginOut")
    public String loginOut() {
        SecurityUtils.getSubject().logout();
        return "redirect:/";
    }
}
