package com.mysite.blog.controller.admin;

import com.mysite.blog.pojo.UserInfo;
import com.mysite.blog.service.BlogConfigService;
import com.mysite.blog.service.impl.CategoryServiceImpl;
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
 * @date 2020/6/5 16:11
 */
@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Resource
    private CategoryServiceImpl categoryService;

    @Resource
    private UserInfoServiceImpl userInfoService;

    @Resource
    private BlogConfigService blogConfigService;

    @GetMapping("/category")
    public String toCategory(HttpServletRequest request){
        request.setAttribute("path","category");
        request.setAttribute("configurations",blogConfigService.getAllConfigs());
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        UserInfo userInfo = userInfoService.queryById(principal);
        request.setAttribute("user",new UserInfo(userInfo.getUserId(), userInfo.getLoginUserName(), userInfo.getNickName(), userInfo.getSex(), userInfo.getUserPhone(), userInfo.getUserEmail(), userInfo.getUserAddress(), userInfo.getProfilePictureUrl()));
        return "admin/category";
    }
    /**
     * 分页查询
     * @param params params
     * @return Result
     */
    @GetMapping("/category/categoryList")
    @ResponseBody
    public Result categoryList(@RequestParam Map<String, Object> params) {
        String pageNum = (String) params.get("page");
        String pageSize = (String) params.get("limit");
        return ResultGenerator.genSuccessResult(categoryService.findPage(new PageRequest(Integer.parseInt(pageNum),Integer.parseInt(pageSize))));
    }

    /**
     * 添加
     * @param categoryName 分类名
     * @return String
     */
    @PostMapping("/category/insertCategory")
    @ResponseBody
    public String insertCategory(@RequestParam("categoryName") String categoryName){
        return categoryService.queryByName(categoryName);
    }
    @PostMapping("/category/updateCategory")
    @ResponseBody
    public String updateCategory(@RequestParam("categoryId") Integer categoryId,
                                 @RequestParam("categoryName") String categoryName){
        if (StringUtils.isEmpty(categoryName)) {
            return "null";
        }
        return categoryService.updateCategory(categoryId,categoryName);
    }

    /**
     * 删除
     * @param ids id数组
     * @return String
     */
    @PostMapping("/category/delete")
    @ResponseBody
    public String delete(@RequestBody Integer[] ids){
        if(ids.length<1){
            return "risk";
        }
        return categoryService.queryById(ids);

    }

}
