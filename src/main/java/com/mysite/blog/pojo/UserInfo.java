package com.mysite.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author Star
 * @version 1.0
 * @date 2020/5/29 15:15
 * 用户实体类
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInfo implements Serializable {
    private Integer indexId;
    private String userId;
    private String loginUserName;
    private String loginUserPassword;
    private String nickName;
    private Integer sex;
    private String userPhone;
    private String userEmail;
    private String userAddress;
    private String profilePictureUrl;
    private Integer isLock;

    public UserInfo(String userId, String loginUserName, String nickName, Integer sex, String userPhone, String userEmail, String userAddress, String profilePictureUrl) {
        this.userId = userId;
        this.loginUserName = loginUserName;
        this.nickName = nickName;
        this.sex = sex;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
        this.profilePictureUrl = profilePictureUrl;
    }

    public UserInfo(String userId, String loginUserName, String loginUserPassword, String nickName, Integer sex, String userPhone, String userEmail, String userAddress, String profilePictureUrl, Integer isLock) {
        this.userId = userId;
        this.loginUserName = loginUserName;
        this.loginUserPassword = loginUserPassword;
        this.nickName = nickName;
        this.sex = sex;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
        this.profilePictureUrl = profilePictureUrl;
        this.isLock = isLock;
    }

    public UserInfo(String userId, String loginUserName, String nickName, String userPhone, String userEmail, String userAddress) {
        this.userId = userId;
        this.loginUserName = loginUserName;
        this.nickName = nickName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
    }

    public UserInfo(String nickName, String userEmail, String userAddress) {
        this.nickName = nickName;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
    }
}
