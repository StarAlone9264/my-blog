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
 * @date 2020/9/8 10:09
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleUserRelation {
    private Integer relationId;
    private Integer roleId;
    private String userId;
    private Date createTime;

    public RoleUserRelation(Integer roleId, String userId) {
        this.roleId = roleId;
        this.userId = userId;
    }
}
