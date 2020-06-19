package com.mysite.blog.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/7 13:34
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    private Long blogPrimaryId;
    private String blogId;
    private String blogTitle;
    private String blogAccessUrl;
    private String blogCoverImage; 
    private String blogContent;
    private int blogCategoryId;
    private String blogCategoryName;
    private String blogTags;
    private Integer blogStatus;
    private Long blogViews;
    private Integer allowComment;
    private Integer isDelete;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private Date updateTime;
    // 存放用户信息----头像+名称
    private Map<String,String> map;
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", blogPrimaryId=").append(blogPrimaryId);
        sb.append(", blogId=").append(blogId);
        sb.append(", blogTitle=").append(blogTitle);
        sb.append(", blogAccessUrl=").append(blogAccessUrl);
        sb.append(", blogCoverImage=").append(blogCoverImage);
        sb.append(", blogCategoryId=").append(blogCategoryId);
        sb.append(", blogCategoryName=").append(blogCategoryName);
        sb.append(", blogTags=").append(blogTags);
        sb.append(", blogStatus=").append(blogStatus);
        sb.append(", blogViews=").append(blogViews);
        sb.append(", allowComment=").append(allowComment);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", blogContent=").append(blogContent);
        sb.append(", map=").append(map);
        sb.append("]");
        return sb.toString();
    }
}
