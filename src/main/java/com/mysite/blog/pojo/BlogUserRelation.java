package com.mysite.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/16 19:11
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlogUserRelation {
    private Long relationId;
    private String blogId;
    private String userId;
    private Date createTime;
}
