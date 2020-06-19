package com.mysite.blog.controller.admin;

import com.mysite.blog.mapper.BlogMapper;
import com.mysite.blog.pojo.Blog;
import com.mysite.blog.pojo.UserInfo;
import com.mysite.blog.service.impl.BlogServiceImpl;
import com.mysite.blog.service.impl.CategoryServiceImpl;
import com.mysite.blog.service.impl.TagServiceImpl;
import com.mysite.blog.service.impl.UserInfoServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * @author Star
 * @version 1.0
 * @date 2020/5/29 21:12
 */
@Controller
@RequestMapping("/admin")
public class UserInfoController {
    private final String RANDOM_CODE_KEY = "RANDOM_CODE_KEY";
    private final String SESSION_USER_NAME = "loginUser";

    @Resource
    private UserInfoServiceImpl userInfoService;

    @Resource
    private CategoryServiceImpl categoryService;

    @Resource
    private TagServiceImpl tagService;

    @Resource
    private BlogServiceImpl blogService;
    /**
     * 跳转登陆页
     *
     * @return
     */
    @GetMapping({"/login"})
    public String login(HttpServletRequest request) {
        return "admin/login";
    }

    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        request.setAttribute("blogTotal",blogService.getBlogTotal());
        request.setAttribute("categoryTotal",categoryService.getCategoryTotal());
        request.setAttribute("tagCount", tagService.getTagTotal());
        return "admin/index";
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request){
        request.setAttribute("path","profile");
        return "admin/profile";
    }
    /**
     * 登陆
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 返回页
     */
    @PostMapping({"/login"})
    public String login(HttpSession session, @RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("verifyCode") String verifyCode) {
        // 判断用户名密码是否为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空！");
            return "admin/login";
        }
        // 判断验证码是否为空
        if (StringUtils.isEmpty(verifyCode)) {
            session.setAttribute("errorMsg", "验证码不能为空！");
            return "admin/login";
        }
        // 获取session中的验证码
        String sessionVerifyCode = (String) session.getAttribute(RANDOM_CODE_KEY);
        // 判断用户填写的验证码是否与session中的一致
        if (!verifyCode.equalsIgnoreCase(sessionVerifyCode)) {
            session.setAttribute("errorMsg", "验证码不正确！");
            return "admin/login";
        }
        // 登陆
        UserInfo login = userInfoService.login(username, password);
        if (login == null) {
            session.setAttribute("errorMsg", "用户名或密码不正确！");
            return "admin/login";
        }
        // 判断该用户是否有权限登陆后台
        if (login.getIsLock() == 0) {
            session.setAttribute("errorMsg", "您没有权限登陆后台，请联系管理员。");
            return "admin/login";
        }
        // 封装对象
        UserInfo loginUser = new UserInfo(login.getUserId(), login.getLoginUserName(),login.getNickName(), login.getUserPhone(),login.getUserEmail(), login.getUserAddress(),login.getProfilePictureUrl());

        session.setAttribute(SESSION_USER_NAME, loginUser);
        // 设置session超时时间
        session.setMaxInactiveInterval(60 * 60 * 3);
        return "redirect:/admin/index";
    }

    /**
     * 修改信息
     * @param request request
     * @param loginUserName 登录名
     * @param nickName 用户名
     * @param userPhone 电话
     * @param userEmail 邮箱
     * @param userAddress 地址
     * @param profilePictureUrl 头像
     * @return String
     */
    @PostMapping("/profile/modifyInformation")
    @ResponseBody
    public String modifyInformation(HttpServletRequest request,
            @RequestParam("loginUserName")String loginUserName,
            @RequestParam("nickName")String nickName,
            @RequestParam("userPhone")String userPhone,
            @RequestParam("userEmail")String userEmail,
            @RequestParam("userAddress")String userAddress,
            @RequestParam("profilePictureUrl")String profilePictureUrl){
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName) || StringUtils.isEmpty(userPhone) ||
                StringUtils.isEmpty(userEmail) || StringUtils.isEmpty(userAddress)) {
            return "参数不能为空！";

        }
        if (request.getSession().getAttribute(SESSION_USER_NAME)==null){
            System.out.println("获取信息失败");
            return "修改失败";
        }
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(SESSION_USER_NAME);
        String userId = userInfo.getUserId();
        if(userInfoService.modifyInformationByUserId(new UserInfo(userId, loginUserName, nickName, userPhone, userEmail, userAddress, profilePictureUrl))>0){
            request.getSession().removeAttribute(SESSION_USER_NAME);
            return "success";
        }else {
            return "修改失败";
        }
    }

    /**
     * 修改密码
     * @param request request
     * @param originalPassword 原密码
     * @param newPassword 新密码
     * @return String
     */
    @PostMapping("/profile/changePassword")
    @ResponseBody
    public String alterPassword(HttpServletRequest request,
            @RequestParam("originalPassword")String originalPassword,
            @RequestParam("newPassword")String newPassword){

        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            return "参数不能为空!";
        }
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(SESSION_USER_NAME);
        if (userInfoService.changePassword(userInfo.getUserId(),originalPassword,newPassword)){
            request.getSession().removeAttribute(SESSION_USER_NAME);
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
    public String loginOut(HttpSession session) {
        session.removeAttribute(SESSION_USER_NAME);
        session.removeAttribute("errorMsg");
        return "redirect:/";
    }
}
