package com.mysite.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/17 17:59
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlogTagCount {
    private Integer tagId;
    private String tagName;
    private Integer tagCount;
}
