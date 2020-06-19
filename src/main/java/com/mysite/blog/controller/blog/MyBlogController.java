package com.mysite.blog.controller.blog;

import com.mysite.blog.controller.vo.BlogDetailVo;
import com.mysite.blog.pojo.BlogTagCount;
import com.mysite.blog.service.BlogConfigService;
import com.mysite.blog.service.BlogService;
import com.mysite.blog.service.CategoryService;
import com.mysite.blog.service.TagService;
import com.mysite.blog.uitl.PageResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Star
 * @version 1.0
 * @date 2020/5/31 16:11
 */
@Controller
public class MyBlogController {

    public static String theme = "amaze";

    @Resource
    private BlogService blogService;

    @Resource
    private BlogConfigService blogConfigService;

    @Resource
    private TagService tagService;

    @Resource
    private CategoryService categoryService;

    /**
     * 首页
     * @param request request
     * @return String
     */
    @GetMapping({"","/","index","index.html"})
    public String toIndex(HttpServletRequest request){
        return this.page(request,1);
    }

    /**
     * 博客分页
     * @param request request
     * @param pageNum 页数
     * @return String
     */
    @GetMapping("/page/{pageNum}")
    public String page(HttpServletRequest request, @PathVariable("pageNum")int pageNum){
        PageResult blogPageResult = blogService.getBlogForListPage(pageNum);
        if (blogPageResult == null){
            return "error/error_404";
        }
        request.setAttribute("blogPageResult",blogPageResult);
        request.setAttribute("pageName", "首页");
        request.setAttribute("configurations",blogConfigService.getAllConfigs());
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("newBlog",blogService.getBlogForListForIndex(1));
        request.setAttribute("hotBlog",blogService.getBlogForListForIndex(0));
        return "blog/amaze/index";
    }

    /**
     * 博客详情页
     * @param request request
     * @param blogPrimaryId 主键id
     * @return String
     */
    @GetMapping("/blog/{blogPrimaryId}")
    public String detail(HttpServletRequest request, @PathVariable("blogPrimaryId") Long blogPrimaryId) {
        BlogDetailVo blogDetailVo = blogService.getBlogDetail(blogPrimaryId);
        request.setAttribute("blogDetailVo", blogDetailVo);
        request.setAttribute("pageName", "详情");
        request.setAttribute("configurations", blogConfigService.getAllConfigs());
        return "blog/amaze/detail";
    }

    /**
     * 根据分类查询博客
     * @param request request
     * @param categoryName 分类名
     * @return String
     */
    @GetMapping({"/category/{categoryName}"})
    public String category(HttpServletRequest request ,@PathVariable String categoryName){
        return this.category(request,categoryName,1);
    }
    /**
     * 根据分类查询博客
     * @param request request
     * @param categoryName 分类名
     * @return String
     */
    @GetMapping({"/category/{categoryName}/{page}"})
    public String category(HttpServletRequest request, @PathVariable("categoryName") String categoryName, @PathVariable("page") Integer page) {
        PageResult pageResult = blogService.getBlogPageByCategory(categoryName,page);
        request.setAttribute("blogPageResult", pageResult);
        request.setAttribute("pageName", "分类");
        request.setAttribute("pageUrl", "category");
        request.setAttribute("keyword", categoryName);
        request.setAttribute("newBlog", blogService.getBlogForListForIndex(1));
        request.setAttribute("hotBlog", blogService.getBlogForListForIndex(0));
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("configurations", blogConfigService.getAllConfigs());
        return "blog/amaze/list";
    }
    /**
     * 根据分类查询博客
     * @param request request
     * @param tagName 分类名
     * @return String
     */
    @GetMapping({"/tag/{tagName}"})
    public String blogTag(HttpServletRequest request ,@PathVariable String tagName){
        return this.blogTag(request,tagName,1);
    }
    /**
     * 根据分类查询博客
     * @param request request
     * @param tagName 分类名
     * @return String
     */
    @GetMapping({"/tag/{tagName}/{page}"})
    public String blogTag(HttpServletRequest request, @PathVariable("tagName") String tagName, @PathVariable("page") Integer page) {
        PageResult pageResult = blogService.getBlogPageByTag(tagName,page);
        request.setAttribute("blogPageResult", pageResult);
        request.setAttribute("pageName", "标签");
        request.setAttribute("pageUrl", "tag");
        request.setAttribute("keyword", tagName);
        request.setAttribute("newBlog", blogService.getBlogForListForIndex(1));
        request.setAttribute("hotBlog", blogService.getBlogForListForIndex(0));
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("configurations", blogConfigService.getAllConfigs());
        return "blog/amaze/list";
    }

    /**
     * 搜索
     * @param request request
     * @param keyword 关键字
     * @return String
     */
    @GetMapping({"/search/{keyword}"})
    public String search(HttpServletRequest request ,@PathVariable String keyword){
        return this.search(request,keyword,1);
    }
    /**
     * 搜索
     * @param request request
     * @param keyword 关键字
     * @return String
     */
    @GetMapping({"/search/{keyword}/{page}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword, @PathVariable("page") Integer page) {
        PageResult pageResult = blogService.getBlogPageBySearch(keyword,page);
        request.setAttribute("blogPageResult", pageResult);
        request.setAttribute("pageName", "搜索");
        request.setAttribute("pageUrl", "tag");
        request.setAttribute("keyword", keyword);
        request.setAttribute("newBlog", blogService.getBlogForListForIndex(1));
        request.setAttribute("hotBlog", blogService.getBlogForListForIndex(0));
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("configurations", blogConfigService.getAllConfigs());
        return "blog/amaze/list";
    }
    @GetMapping({"/{subUrl}"})
    public String detail(HttpServletRequest request, @PathVariable("subUrl") String subUrl) {
        BlogDetailVo blogDetailVo = blogService.getBlogPageBySubUrl(subUrl);
        if (blogDetailVo != null) {
            request.setAttribute("blogDetailVo", blogDetailVo);
            request.setAttribute("pageName", subUrl);
            request.setAttribute("configurations", blogConfigService.getAllConfigs());
            return "blog/" + theme + "/detail";
        } else {
            return "error/error_400";
        }
    }
}
