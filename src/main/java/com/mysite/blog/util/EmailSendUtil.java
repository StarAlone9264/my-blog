package com.mysite.blog.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

/**
 * @author Star
 * @version 1.0
 * @date 2020/9/9 0:47
 * 邮件发送
 */
@Component
public class EmailSendUtil {

    @Resource
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    /**
     * 邮件发送
     * @param userEmail 用户邮箱
     * @param tittle 邮件标题
     * @param news 邮件内容
     * @return 返回结果
     */
    public String sendEmail(String userEmail, String tittle, String news){
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(username);
            helper.setTo(userEmail);
            helper.setSubject(tittle);
            helper.setText(news,true);
            javaMailSender.send(message);
            return "Mail sent successfully";
        }catch (Exception exception){
            exception.printStackTrace();
            System.out.println("发送失败："+exception.getMessage());
            return "Failed to send mail";
        }
    }

}
