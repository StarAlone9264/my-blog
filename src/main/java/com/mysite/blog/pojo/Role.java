package com.mysite.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * @author Star
 * @version 1.0
 * @date 2020/9/8 10:07
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role {
    private Integer roleId;
    private String roleName;
}
