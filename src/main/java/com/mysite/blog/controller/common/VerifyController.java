package com.mysite.blog.controller.common;

import com.mysite.blog.uitl.VerifyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

/**
 * @author Star
 * @version 1.0
 * @date 2020/5/30 11:40
 */
@Controller
@RequestMapping("/verify")
public class VerifyController {

    /**
     * @author XXXXXX
     * @date 2018年7月11日
     * @desc 图形验证码生成
     */
    @RequestMapping("/createImg")
    public void createImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //设置相应类型,告诉浏览器输出的内容为图片
            response.setContentType("image/jpeg");
            //设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            VerifyUtil randomValidateCode = new VerifyUtil();
            //输出验证码图片
            randomValidateCode.getRandcode(request, response);
        }catch (Exception e) {
            // 没引入日志 logger. error("获取验证码异常：", e);
            System.out.println("获取验证码异常：");
            e.printStackTrace();
        }
    }
}