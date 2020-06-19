package com.mysite.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Star
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlogTagRelation {

    private Long relationId;
    private Long blogId;
    private Integer tagId;
    private Date createTime;
}