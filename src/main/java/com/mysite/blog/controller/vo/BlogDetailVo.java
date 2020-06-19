package com.mysite.blog.controller.vo;

import java.util.Date;
import java.util.List;

/**
 * @author Star
 */
public class BlogDetailVo {
    private Long blogPrimaryId;

    private String blogId;

    private String blogTitle;

    private Integer blogCategoryId;

    private String blogCategoryName;

    private String blogCoverImage;

    private Long blogViews;

    private List<String> blogTags;

    @Override
    public String toString() {
        return "BlogDetailVo{" +
                "blogPrimaryId=" + blogPrimaryId +
                ", blogId='" + blogId + '\'' +
                ", blogTitle='" + blogTitle + '\'' +
                ", blogCategoryId=" + blogCategoryId +
                ", blogCategoryName='" + blogCategoryName + '\'' +
                ", blogCoverImage='" + blogCoverImage + '\'' +
                ", blogViews=" + blogViews +
                ", blogTags=" + blogTags +
                ", blogContent='" + blogContent + '\'' +
                ", allowComment=" + allowComment +
                ", createTime=" + createTime +
                '}';
    }

    private String blogContent;

    private Byte allowComment;

    private Date createTime;

    public Long getBlogPrimaryId() {
        return blogPrimaryId;
    }

    public void setBlogPrimaryId(Long blogPrimaryId) {
        this.blogPrimaryId = blogPrimaryId;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public Integer getBlogCategoryId() {
        return blogCategoryId;
    }

    public void setBlogCategoryId(Integer blogCategoryId) {
        this.blogCategoryId = blogCategoryId;
    }

    public String getBlogCategoryName() {
        return blogCategoryName;
    }

    public void setBlogCategoryName(String blogCategoryName) {
        this.blogCategoryName = blogCategoryName;
    }

    public String getBlogCoverImage() {
        return blogCoverImage;
    }

    public void setBlogCoverImage(String blogCoverImage) {
        this.blogCoverImage = blogCoverImage;
    }

    public Long getBlogViews() {
        return blogViews;
    }

    public void setBlogViews(Long blogViews) {
        this.blogViews = blogViews;
    }

    public List<String> getBlogTags() {
        return blogTags;
    }

    public void setBlogTags(List<String> blogTags) {
        this.blogTags = blogTags;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Byte getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Byte allowComment) {
        this.allowComment = allowComment;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
