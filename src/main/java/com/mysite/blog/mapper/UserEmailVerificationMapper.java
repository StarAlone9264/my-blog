package com.mysite.blog.mapper;

import com.mysite.blog.pojo.UserEmailVerification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Star
 * @version 1.0
 * @date 2020/9/9 14:51
 */
@Mapper
@Repository
public interface UserEmailVerificationMapper {
    /**
     * 创建用户使用邮箱修改密码信息
     *
     * @param userEmailVerification UserEmailVerification对象
     * @return int
     */
    public int insert(UserEmailVerification userEmailVerification);

    /**
     * 删除信息
     *
     * @param userEmailVerification userEmailVerification
     * @return int
     */
    public int deleteById(UserEmailVerification userEmailVerification);

    /**
     * 使用用户id 与 验证id 查询添加时间
     *
     * @param userId   用户id
     * @param verifyId 验证id
     * @return UserEmailVerification对象
     */
    public UserEmailVerification queryById(@Param("userId") String userId, @Param("verifyId") String verifyId);

}
