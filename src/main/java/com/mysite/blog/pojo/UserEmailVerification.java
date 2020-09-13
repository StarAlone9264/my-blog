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
 * @date 2020/9/9 14:49
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserEmailVerification {
    private Integer primaryId;
    private String userId;
    private String verifyId;
    private Date createTime;
    private Integer expired;

    public UserEmailVerification(String userId, String verifyId) {
        this.userId = userId;
        this.verifyId = verifyId;
    }
}
