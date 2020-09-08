package com.mysite.blog.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.mysite.blog.config.PathUtil;
import com.mysite.blog.pojo.Blog;
import com.mysite.blog.pojo.UserInfo;
import com.mysite.blog.service.BlogConfigService;
import com.mysite.blog.service.BlogService;
import com.mysite.blog.service.CategoryService;
import com.mysite.blog.service.impl.UserInfoServiceImpl;
import com.mysite.blog.util.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/7 15:27
 * BlogController
 */
@Controller
@RequestMapping("/admin")
public class BlogController {
    @Resource
    private CategoryService categoryService;

    @Resource
    private BlogService blogService;

    @Resource
    private UserInfoServiceImpl userInfoService;

    @Resource
    private BlogConfigService blogConfigService;
    /**
     * 跳转
     * @param request request
     * @return 返回页面
     */
    @GetMapping("/blog")
    public String list(HttpServletRequest request) {
        request.setAttribute("path", "blog");
        request.setAttribute("configurations",blogConfigService.getAllConfigs());
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        UserInfo userInfo = userInfoService.queryById(principal);
        request.setAttribute("user",new UserInfo(userInfo.getUserId(), userInfo.getLoginUserName(), userInfo.getNickName(), userInfo.getSex(), userInfo.getUserPhone(), userInfo.getUserEmail(), userInfo.getUserAddress(), userInfo.getProfilePictureUrl()));
        return "admin/blog";
    }

    /**
     * 跳转博客添加
     * @param request request
     * @return 返回页
     */
    @GetMapping("/blog/edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        request.setAttribute("configurations",blogConfigService.getAllConfigs());
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        UserInfo userInfo = userInfoService.queryById(principal);
        request.setAttribute("user",new UserInfo(userInfo.getUserId(), userInfo.getLoginUserName(), userInfo.getNickName(), userInfo.getSex(), userInfo.getUserPhone(), userInfo.getUserEmail(), userInfo.getUserAddress(), userInfo.getProfilePictureUrl()));
        request.setAttribute("categories", categoryService.getAllCategories());
        return "admin/edit";
    }

    /**
     * 博客列表
     * @param params params
     * @return Result
     */
    @GetMapping("/blog/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String pageNum = (String) params.get("page");
        String pageSize = (String) params.get("limit");
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        return ResultGenerator.genSuccessResult(blogService.queryUserBlogList(new PageRequest(Integer.parseInt(pageNum),Integer.parseInt(pageSize)),principal));
    }

    /**
     * 根据id修改博客
     * @param request request
     * @param blogPrimaryId blogPrimaryId
     * @param model model
     * @return 返回页
     */
    @GetMapping("/blog/edit/{blogPrimaryId}")
    public String edit(HttpServletRequest request, @PathVariable("blogPrimaryId") Long blogPrimaryId, Model model) {
        request.setAttribute("path", "edit");
        request.setAttribute("configurations",blogConfigService.getAllConfigs());
        Blog blog = blogService.getBlogById(blogPrimaryId);
        if (blog == null) {
            return "error/error_400";
        }
        model.addAttribute("blog",blog);
        model.addAttribute("categories",categoryService.getAllCategories());
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        UserInfo userInfo = userInfoService.queryById(principal);
        request.setAttribute("user",new UserInfo(userInfo.getUserId(), userInfo.getLoginUserName(), userInfo.getNickName(), userInfo.getSex(), userInfo.getUserPhone(), userInfo.getUserEmail(), userInfo.getUserAddress(), userInfo.getProfilePictureUrl()));
        return "admin/edit";
    }

    /**
     * 模糊搜索
     * @param params params
     * @return Result
     */
    @GetMapping("/blog/fuzzySearch")
    @ResponseBody
    public Result fuzzySearch(@RequestParam Map<String, Object> params){
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String pageNum = (String) params.get("page");
        String pageSize = (String) params.get("limit");
        String keyword = (String) params.get("keyword");
        PageResult pageResult = blogService.fuzzySearch(new PageRequest(Integer.parseInt(pageNum), Integer.parseInt(pageSize)), keyword);
        if (CollectionUtils.isEmpty(pageResult.getContent())){
            return ResultGenerator.genSuccessResult("null");
        }
        return ResultGenerator.genSuccessResult(pageResult);
    }

    /**
     * 删除
     * @param ids id
     * @return Result
     */
    @PostMapping("/blog/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        if (ids.length < 1){
            return ResultGenerator.genFailResult("参数错误");
        }

        if (blogService.batchDeleteById(ids)){
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("修改失败");
    }

    /**
     * 修改博客
     * @param blogPrimaryId id
     * @param blogTitle 标题
     * @param blogAccessUrl 自定义url
     * @param blogCategoryId 分类id
     * @param blogTags 标签
     * @param blogContent 博客
     * @param blogCoverImage 封面
     * @param blogStatus 发布状态
     * @param allowComment 允许评论
     * @param httpServletRequest httpServletRequest
     * @return Result
     */
    @PostMapping("/blog/update")
    @ResponseBody
    public Result update(@RequestParam("blogPrimaryId") Long blogPrimaryId,
                       @RequestParam("blogTitle") String blogTitle,
                       @RequestParam(name = "blogAccessUrl", required = false) String blogAccessUrl,
                       @RequestParam("blogCategoryId") Integer blogCategoryId,
                       @RequestParam("blogTags") String blogTags,
                       @RequestParam("blogContent") String blogContent,
                       @RequestParam("blogCoverImage") String blogCoverImage,
                       @RequestParam("blogStatus") Integer blogStatus,
                       @RequestParam("allowComment") Integer allowComment,
                       HttpServletRequest httpServletRequest) {
        if (StringUtils.isEmpty(blogTitle)) {
            return ResultGenerator.genFailResult("请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (StringUtils.isEmpty(blogTags)) {
            return ResultGenerator.genFailResult("请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            return ResultGenerator.genFailResult("标签过长");
        }
        if (blogAccessUrl.trim().length() > 150) {
            return ResultGenerator.genFailResult("路径过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            return ResultGenerator.genFailResult("请输入文章内容");
        }
        if (blogTags.trim().length() > 100000) {
            return ResultGenerator.genFailResult("文章内容过长");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }
        Blog blog = new Blog();
        blog.setBlogPrimaryId(blogPrimaryId);
        blog.setBlogTitle(blogTitle);
        blog.setBlogAccessUrl(blogAccessUrl);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogContent(blogContent);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogStatus(blogStatus);
        blog.setAllowComment(allowComment);
        String s = blogService.updateBlog(blog);
        if ("success".equals(s)){
            return ResultGenerator.genSuccessResult("修改成功");
        }else {
            return ResultGenerator.genFailResult(s);
        }
    }

    /**
     * 添加
     * @param blogTitle 标题
     * @param blogAccessUrl 自定义url
     * @param blogCategoryId 分类id
     * @param blogTags 标签
     * @param blogContent 博客
     * @param blogCoverImage 封面
     * @param blogStatus 发布状态
     * @param allowComment 允许评论
     * @param httpServletRequest httpServletRequest
     * @return Result
     */
    @PostMapping("/blog/save")
    @ResponseBody
    public Result save(@RequestParam("blogTitle") String blogTitle,
                       @RequestParam(name = "blogAccessUrl", required = false) String blogAccessUrl,
                       @RequestParam("blogCategoryId") Integer blogCategoryId,
                       @RequestParam("blogTags") String blogTags,
                       @RequestParam("blogContent") String blogContent,
                       @RequestParam("blogCoverImage") String blogCoverImage,
                       @RequestParam("blogStatus") Integer blogStatus,
                       @RequestParam("allowComment") Integer allowComment,
                        HttpServletRequest httpServletRequest) {
        if (StringUtils.isEmpty(blogTitle)) {
            return ResultGenerator.genFailResult("请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (StringUtils.isEmpty(blogTags)) {
            return ResultGenerator.genFailResult("请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            return ResultGenerator.genFailResult("标签过长");
        }
        if (blogAccessUrl.trim().length() > 150) {
            return ResultGenerator.genFailResult("路径过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            return ResultGenerator.genFailResult("请输入文章内容");
        }
        if (blogTags.trim().length() > 100000) {
            return ResultGenerator.genFailResult("文章内容过长");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }
        Blog blog = new Blog();
        blog.setBlogId(UuidUtil.getUUID());
        blog.setBlogTitle(blogTitle);
        blog.setBlogAccessUrl(blogAccessUrl);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogContent(blogContent);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogStatus(blogStatus);
        blog.setAllowComment(allowComment);
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        UserInfo userInfo = userInfoService.queryById(principal);
        if (userInfo == null){
            return ResultGenerator.genFailResult("请求异常");
        }
        String s = blogService.insertBlog(blog,userInfo.getUserId());
        if ("success".equals(s)){
            return ResultGenerator.genSuccessResult("添加成功");
        }else {
            return ResultGenerator.genFailResult(s);
        }
    }

    /**
     * 博客图片上传
     * @param file file
     * @param request request
     * @return JSONObject
     * @throws IOException IOException
     */
    @RequestMapping("/blog/upload/file")
    @ResponseBody
    public JSONObject fileUpload(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file, HttpServletRequest request) throws IOException {
        // 返回图片地址
        String returnUrl = "/upload/";
        // 上传目录到 static/admin/upload
        String path  = PathUtil.UPLOAD_PATH;
        //按照月份进行分类：
        Calendar instance = Calendar.getInstance();
        String month = (instance.get(Calendar.MONTH) + 1)+"month";
        path = path+month;
        File realPath = new File(path);
        if (!realPath.exists()){
            realPath.mkdirs();
        }
        //解决文件名字问题：我们使用uuid;
        String filename = "pg-"+ UUID.randomUUID().toString().replaceAll("-", "")+".jpg";
        File newFile = new File(realPath, filename);
        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(newFile);
        //给editormd进行回调
        JSONObject res = new JSONObject();
        res.put("url", returnUrl + month+"/"+ filename);
        res.put("success", 1);
        res.put("message", "upload success!");
        return res;
    }
}
