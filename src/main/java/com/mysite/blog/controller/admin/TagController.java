package com.mysite.blog.controller.admin;

import com.mysite.blog.pojo.UserInfo;
import com.mysite.blog.service.BlogConfigService;
import com.mysite.blog.service.impl.TagServiceImpl;
import com.mysite.blog.service.impl.UserInfoServiceImpl;
import com.mysite.blog.util.PageRequest;
import com.mysite.blog.util.Result;
import com.mysite.blog.util.ResultGenerator;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/6 16:09
 */
@Controller
@RequestMapping("/admin")
public class TagController {

    @Resource
    private TagServiceImpl tagService;

    @Resource
    private UserInfoServiceImpl userInfoService;

    @Resource
    private BlogConfigService blogConfigService;

    @GetMapping("/tags")
    public String toTag(HttpServletRequest request){
        request.setAttribute("path","tags");
        request.setAttribute("configurations",blogConfigService.getAllConfigs());
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        UserInfo userInfo = userInfoService.queryById(principal);
        request.setAttribute("user",new UserInfo(userInfo.getUserId(), userInfo.getLoginUserName(), userInfo.getNickName(), userInfo.getUserPhone(), userInfo.getUserEmail(), userInfo.getUserAddress(), userInfo.getProfilePictureUrl()));
        return "admin/tag";
    }

    /**
     * 分页查询
     * @param params params
     * @return Result
     */
    @GetMapping("/tags/list")
    @ResponseBody
    public Result tagList(@RequestParam Map<String, Object> params){
        String pageNum = (String) params.get("page");
        String pageSize = (String) params.get("limit");
        return ResultGenerator.genSuccessResult(tagService.findPage(new PageRequest(Integer.parseInt(pageNum), Integer.parseInt(pageSize))));
    }

    /**
     * 添加标签
     * @param tagName 标签名
     * @return Result
     */
    @PostMapping("/tags/insertTag")
    @ResponseBody
    public Result insertTag(@RequestParam String tagName){
        if (StringUtils.isEmpty(tagName)) {
            return ResultGenerator.genFailResult("参数异常");
        }
        return tagService.queryByName(tagName);
    }

    /**
     * 删除
     * @param ids ids
     * @return Result
     */
    @PostMapping("/tags/delete")
    @ResponseBody
    public Result deleteTag(@RequestBody Integer[] ids){
        if(ids.length<1){
            return ResultGenerator.genFailResult("参数异常");
        }
        return tagService.queryById(ids);
    }
}
