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
 * @date 2020/6/14 22:13
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlogConfig {
    private Integer configId;
    private String configName;
    private String configValue;
    private Date createTime;
    private Date updateTime;
}
