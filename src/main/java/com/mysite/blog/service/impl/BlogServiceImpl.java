package com.mysite.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysite.blog.controller.vo.BlogDetailVo;
import com.mysite.blog.controller.vo.SimpleBlogListVo;
import com.mysite.blog.mapper.*;
import com.mysite.blog.pojo.*;
import com.mysite.blog.service.BlogService;
import com.mysite.blog.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/8 15:28
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Resource
    private BlogMapper blogMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private BlogTagRelationMapper blogTagRelationMapper;

    @Resource
    private BlogUserRelationMapper blogUserRelationMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public int getBlogTotal(String userId) {
        return blogMapper.getBlogTotal(userId);
    }

    @Override
    public String insertBlog(Blog blog,String userId) {
        Category category = categoryMapper.queryById(blog.getBlogCategoryId());
        // 如果分类不存在 分类默认为 id 为1
        if (category == null) {
            // 设置 blog 的分类id
            blog.setBlogCategoryId(1);
            // 设置 blog 的分类名
            Category category1 = categoryMapper.queryById(blog.getBlogCategoryId());
            blog.setBlogCategoryName(category1.getCategoryName());
            // 该分类hotRank +1
            category1.setHotRank(category1.getHotRank() + 1);
            // 将 category1值 赋给 category 最后统一处理
            category = category1;
        } else {
            // 如果存在  设置 blog 的分类id
            blog.setBlogCategoryName(category.getCategoryName());
            // 该分类hotRank +1
            category.setHotRank(category.getHotRank() + 1);
        }

        String[] tags = blog.getBlogTags().split(",");
        if (tags.length > 6) {
            return "标签数量最多为6个";
        }
        // 添加博客
        if (blogMapper.insertBlog(blog) > 0) {
            // 建立博客与用户关系
            BlogUserRelation blogUserRelation = new BlogUserRelation();
            blogUserRelation.setBlogId(blog.getBlogId());
            blogUserRelation.setUserId(userId);
            if (blogUserRelationMapper.insert(blogUserRelation) < 0){
                return "异常";
            }
            // 修改分类相关  修改分类排序值(HotRank)
            categoryMapper.updateDynamicById(category);
            // 要添加标签数组
            List<Tag> tagListForInsert = new ArrayList<>();
            // 全部标签数组
            List<Tag> allTagList = new ArrayList<>();
            for (int i = 0; i < tags.length; i++) {
                Tag tag = tagMapper.queryByName(tags[i]);
                if (tag == null) {
                    Tag tagTemp = new Tag();
                    tagTemp.setTagName(tags[i]);
                    tagListForInsert.add(tagTemp);
                } else {
                    allTagList.add(tag);
                }
            }
            //新增标签数据
            if (!CollectionUtils.isEmpty(tagListForInsert)) {
                tagMapper.batchInsertBlogTag(tagListForInsert);
            }
            allTagList.addAll(tagListForInsert);
            //  新增标签与博客关系数据
            List<BlogTagRelation> blogTagRelations = new ArrayList<>();
            for (Tag tag : allTagList) {
                BlogTagRelation blogTagRelation = new BlogTagRelation();
                blogTagRelation.setBlogId(blog.getBlogPrimaryId());
                blogTagRelation.setTagId(tag.getTagId());
                blogTagRelations.add(blogTagRelation);
            }
            if (blogTagRelationMapper.batchInsert(blogTagRelations) > 0) {
                return "success";
            }
        }
        return "error";
    }

    @Override
    public String updateBlog(Blog blog) {
        Blog blogById = blogMapper.getBlogById(blog.getBlogPrimaryId());
        if (blogById == null){
            return "异常操作";
        }
        // 将修改的值封装给blog对象
        blogById.setBlogTitle(blog.getBlogTitle());
        blogById.setBlogAccessUrl(blog.getBlogAccessUrl());
        blogById.setBlogContent(blog.getBlogContent());
        blogById.setBlogCoverImage(blog.getBlogCoverImage());
        blogById.setBlogStatus(blog.getBlogStatus());
        blogById.setAllowComment(blog.getAllowComment());
        // 判断分类是否为空
        // 如果为空就默认id为1的分类
        Category category = categoryMapper.queryById(blog.getBlogCategoryId());
        if (category == null){
            blogById.setBlogCategoryId(1);
            blogById.setBlogCategoryName(categoryMapper.queryById(blogById.getBlogCategoryId()).getCategoryName());
        }else {
            // 设置分类id与分类名称
            blogById.setBlogCategoryId(category.getCategoryId());
            blogById.setBlogCategoryName(category.getCategoryName());
            // 该分类rank值+1
            category.setHotRank(category.getHotRank()+1);
        }
        //处理标签数据
        String[] tags = blog.getBlogTags().split(",");
        if (tags.length > 6) {
            return "标签数量限制为6";
        }
        // 设置博客标签
        blogById.setBlogTags(blog.getBlogTags());
        // tag数据处理
        // 要添加标签数组
        List<Tag> tagListForInsert = new ArrayList<>();
        // 全部标签数组
        List<Tag> allTagList = new ArrayList<>();
        for (int i = 0; i < tags.length; i++) {
            Tag tag = tagMapper.queryByName(tags[i]);
            if (tag == null) {
                Tag tagTemp = new Tag();
                tagTemp.setTagName(tags[i]);
                tagListForInsert.add(tagTemp);
            } else {
                allTagList.add(tag);
            }
        }
        // 新增标签数据
        if (!CollectionUtils.isEmpty(tagListForInsert)){
            tagMapper.batchInsertBlogTag(tagListForInsert);
        }
        allTagList.addAll(tagListForInsert);
        // 新增关系数据
        List<BlogTagRelation> blogTagRelations = new ArrayList<>();
        for (Tag tag : allTagList) {
            BlogTagRelation blogTagRelation = new BlogTagRelation();
            blogTagRelation.setBlogId(blog.getBlogPrimaryId());
            blogTagRelation.setTagId(tag.getTagId());
            blogTagRelations.add(blogTagRelation);
        }
        //修改blog信息->修改分类排序值->删除原关系数据->保存新的关系数据
        categoryMapper.updateDynamicById(category);
        blogTagRelationMapper.deleteByBlogPrimaryId(blogById.getBlogPrimaryId());
        blogTagRelationMapper.batchInsert(blogTagRelations);
        if (blogMapper.updateDynamicByPrimaryId(blogById) > 0){
            return "success";
        }
        return "修改失败";
    }

    @Override
    public Blog getBlogById(Long blogPrimaryId) {
        return blogMapper.getBlogById(blogPrimaryId);
    }

    @Override
    public Boolean batchDeleteById(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            Blog blogById = blogMapper.getBlogById((ids[i].longValue()));
            if (blogById != null){
                blogUserRelationMapper.deleteByBlogPrimaryId(blogById.getBlogId());
            }
        }
        if (blogMapper.batchDelete(ids) > 0){
            return true;
        }
        return false;
    }

    /**
     * 调用分页插件完成分页
     * @param pageRequest
     * @return
     */
    private PageInfo<Blog> getPageInfo(PageRequest pageRequest, Map map) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> list = blogMapper.queryBlogList(map);
        PageInfo<Blog> blogPageInfo = new PageInfo<>(list);
        List<Blog> blogUserList = new ArrayList<>();
        // 通过关系数据 给博客添加作者信息
        for (Blog blog : list) {
            Map map1 = new HashMap();
            map1.put("blogId",blog.getBlogId());
            BlogUserRelation blogUserRelation = blogUserRelationMapper.queryDynamic(map1);
            UserInfo userInfo = userInfoMapper.selectByUserId(blogUserRelation.getUserId());
            Map map2 = new HashMap();
            // profilePictureUrl nickName
            map2.put("nickName",userInfo.getNickName());
            map2.put("profilePictureUrl",userInfo.getProfilePictureUrl());
            blog.setMap(map2);
            blogUserList.add(blog);
        }
        blogPageInfo.setList(blogUserList);
        return blogPageInfo;
    }


    @Override
    public PageResult findPage(PageRequest pageRequest) {
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest,null));
    }

    @Override
    public PageResult queryUserBlogList(PageRequest pageRequest, String userId) {
        List<BlogUserRelation> blogIdList = blogUserRelationMapper.queryBlogId(userId);
        if (blogIdList.isEmpty()){
            return null;
        }
        return PageUtils.getPageResult(pageRequest, getPageInfoByUserId(pageRequest,blogIdList));
    }

    private PageInfo<Blog> getPageInfoByUserId(PageRequest pageRequest, List list) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blogList = blogMapper.queryUserBlogList(list);
        PageInfo<Blog> blogPageInfo = new PageInfo<>(list);
        List<Blog> blogUserList = new ArrayList<>();
        // 通过关系数据 给博客添加作者信息
        for (Blog blog : blogList) {
            Map map1 = new HashMap();
            map1.put("blogId",blog.getBlogId());
            BlogUserRelation blogUserRelation = blogUserRelationMapper.queryDynamic(map1);
            UserInfo userInfo = userInfoMapper.selectByUserId(blogUserRelation.getUserId());
            Map map2 = new HashMap();
            // profilePictureUrl nickName
            map2.put("nickName",userInfo.getNickName());
            map2.put("profilePictureUrl",userInfo.getProfilePictureUrl());
            blog.setMap(map2);
            blogUserList.add(blog);
        }
        blogPageInfo.setList(blogUserList);
        return blogPageInfo;
    }

    @Override
    public PageResult fuzzySearch(PageRequest pageRequest, String keyword) {
        Map map = new HashMap();
        map.put("keyword",keyword);
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest,map));
    }

    @Override
    public PageResult getBlogForListPage(int pageNum) {
        Map map = new HashMap();
        // 博客状态
        map.put("blogStatus","1");
        // 设置一页条数
        int limit = 8;
        PageRequest pageRequest = new PageRequest(pageNum, limit);
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest, map));
    }
    @Override
    public List<SimpleBlogListVo> getBlogForListForIndex(int type) {
        List<SimpleBlogListVo> blogForListForIndex = new ArrayList<>();
        List<Blog> blogList = blogMapper.queryBlogForIndex(type, 8);
        for (Blog blog : blogList) {
            SimpleBlogListVo simpleBlogListVo = new SimpleBlogListVo();
            BeanUtils.copyProperties(blog, simpleBlogListVo);
            blogForListForIndex.add(simpleBlogListVo);
        }
        return blogForListForIndex;
    }

    @Override
    public BlogDetailVo getBlogDetail(Long blogPrimaryId) {
        Blog blogById = blogMapper.getBlogById(blogPrimaryId);

        BlogDetailVo blogDetailVo = getBlogDetailVo(blogById);
        if (blogDetailVo != null){
            return blogDetailVo;
        }
        return null;
    }
    @Override
    public PageResult getBlogPageByCategory(String categoryName, Integer page) {
        if (PatternUtil.validKeyword(categoryName)){
            Category category = categoryMapper.queryByName(categoryName);
            // 判断该分类是否存在 如果不存在 默认id 为1
            if (category == null){
                Category categoryTemp = categoryMapper.queryById(1);
                category = categoryTemp;
            }
            Map map = new HashMap();
            // 博客状态
            map.put("blogStatus","1");
            map.put("blogCategoryId",category.getCategoryId());
            // 设置一页条数
            int limit = 8;
            PageRequest pageRequest = new PageRequest(page, limit);
            return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest, map));
        }
        return null;
    }

    @Override
    public PageResult getBlogPageBySearch(String keyword, Integer page) {
        Map map = new HashMap();
        // 关键字
        map.put("keyword",keyword);
        // 博客状态
        map.put("blogStatus","1");
        int limit = 8;
        PageRequest pageRequest = new PageRequest(page, limit);
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest, map));
    }

    @Override
    public PageResult getBlogPageByTag(String tagName, Integer page) {
        Tag tag = tagMapper.queryByName(tagName);
        if (tag == null){
            return null;
        }
        int limit = 8;
        PageHelper.startPage(page,limit);
        PageRequest pageRequest = new PageRequest(page,limit);
        List<Blog> blogList = blogMapper.queryPageByTagId(tag.getTagId());
        PageInfo<Blog> blogPageInfo = new PageInfo<>(blogList);
        List<Blog> blogUserList = new ArrayList<>();
        // 通过关系数据 给博客添加作者信息
        for (Blog blog : blogList) {
            Map map1 = new HashMap();
            map1.put("blogId",blog.getBlogId());
            BlogUserRelation blogUserRelation = blogUserRelationMapper.queryDynamic(map1);
            UserInfo userInfo = userInfoMapper.selectByUserId(blogUserRelation.getUserId());
            Map map2 = new HashMap();
            // profilePictureUrl nickName
            map2.put("nickName",userInfo.getNickName());
            map2.put("profilePictureUrl",userInfo.getProfilePictureUrl());
            blog.setMap(map2);
            blogUserList.add(blog);
        }
        blogPageInfo.setList(blogUserList);
        return PageUtils.getPageResult(pageRequest,blogPageInfo);
    }
    @Override
    public BlogDetailVo getBlogPageBySubUrl(String subUrl) {
        Blog blog = blogMapper.queryBlogBySubUrl(subUrl);
        BlogDetailVo blogDetailVo = getBlogDetailVo(blog);
        if (blogDetailVo != null){
            return blogDetailVo;
        }
        return null;
    }
    private BlogDetailVo getBlogDetailVo(Blog blog) {
        if (blog != null && blog.getBlogStatus() == 1){
            // 增加浏览量
            blog.setBlogViews(blog.getBlogViews() + 1);
            // 防止修改更新日期
            blog.setUpdateTime(null);
            blogMapper.updateDynamicByPrimaryId(blog);
            // 处理博文
            BlogDetailVo blogDetailVo = new BlogDetailVo();
            BeanUtils.copyProperties(blog,blogDetailVo);
            blogDetailVo.setBlogContent(MarkDownUtil.mdToHtml(blogDetailVo.getBlogContent()));
            // 处理标签
            if(!StringUtils.isEmpty(blog.getBlogTags())){
                List<String> tags = Arrays.asList(blog.getBlogTags().split(","));
                blogDetailVo.setBlogTags(tags);
            }
            return blogDetailVo;
        }
        return null;
    }
}
