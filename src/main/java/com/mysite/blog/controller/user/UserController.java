package com.mysite.blog.controller.user;

import com.mysite.blog.mapper.UserEmailVerificationMapper;
import com.mysite.blog.pojo.UserEmailVerification;
import com.mysite.blog.service.BlogConfigService;
import com.mysite.blog.service.UserInfoService;
import com.mysite.blog.util.Result;
import com.mysite.blog.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Star
 * @version 1.0
 * @date 2020/9/8 23:50
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private BlogConfigService blogConfigService;

    @Resource
    private UserEmailVerificationMapper userEmailVerificationMapper;

    /**
     * 点击忘记密码执行
     * @param request request
     * @return 网页
     */
    @GetMapping("/forgetPassword")
    public String toAfterForgetPassword(HttpServletRequest request){
        request.setAttribute("configurations", blogConfigService.getAllConfigs());
        request.setAttribute("forgetPassword","可怜的用户忘记密码了");
        return "admin/register";
    }

    /**
     * 为用户发送重置密码邮件
     * @param request request
     * @param loginUserName 登陆名
     * @param userEmail 用户邮箱
     * @return Result
     */
    @PostMapping("/forgetPassword")
    @ResponseBody
    public Result forgetPassword(HttpServletRequest request,  String loginUserName, String userEmail){
        // 判断用户名密码是否为空
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(userEmail)) {
            return ResultGenerator.genSuccessResult("null");
        }
        return ResultGenerator.genSuccessResult(userInfoService.forgetPassword(loginUserName, userEmail));
    }

    /**
     * 为用户发送重置密码邮件
     * @param loginUserName 登陆名
     * @param userEmail 用户邮箱
     * @return Result
     */
    @PostMapping("/findUser")
    @ResponseBody
    public Result findUser(String loginUserName, String userEmail){
        // 判断用户名密码是否为空
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(userEmail)) {
            return ResultGenerator.genSuccessResult("null");
        }
        return ResultGenerator.genSuccessResult(userInfoService.findUser(loginUserName, userEmail));
    }

    /**
     * 验证邮箱链接是否失效
     * @param request request
     * @param userId 用户id
     * @param verifyId 验证id
     * @return 错误页/重置密码页
     */
    @GetMapping("/toResetPassword/{userId}/{verifyId}")
    public String toResetPassword(HttpServletRequest request, @PathVariable("userId") String userId, @PathVariable("verifyId") String verifyId){
        UserEmailVerification queryById = userEmailVerificationMapper.queryById(userId, verifyId);
        if (queryById.getExpired() == 1){
            request.setAttribute("msg","此链接不存在或已失效！");
            return "error/error_404";
        }
        request.setAttribute("configurations", blogConfigService.getAllConfigs());
        request.setAttribute("userId",userId);
        request.setAttribute("verifyId",verifyId);
        return "user/resetPassword";
    }

    /**
     * 重置密码
     * @param verifyId 验证id
     * @param userId 用户id
     * @param password 密码
     * @return Result
     */
    @PostMapping("/resetPassword")
    @ResponseBody
    public Result resetPassword(String verifyId, String userId, String password){
        if (StringUtils.isEmpty(verifyId)){
            return ResultGenerator.genSuccessResult("verifyId is null!");
        }
        if (StringUtils.isEmpty(userId)){
            return ResultGenerator.genSuccessResult("userId is null!");
        }
        if (StringUtils.isEmpty(password)){
            return ResultGenerator.genSuccessResult("Password cannot be empty!");
        }
        return ResultGenerator.genSuccessResult(userInfoService.resetPassword(verifyId,userId,password));
    }
}
