package com.mysite.blog.controller.vo;

import java.io.Serializable;

/**
 * @author Star
 */
public class SimpleBlogListVo implements Serializable {

    private Long blogPrimaryId;

    private String blogTitle;

    public Long getBlogPrimaryId() {
        return blogPrimaryId;
    }

    public void setBlogPrimaryId(Long blogPrimaryId) {
        this.blogPrimaryId = blogPrimaryId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }
}
