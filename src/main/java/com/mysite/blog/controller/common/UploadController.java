package com.mysite.blog.controller.common;

import com.mysite.blog.config.PathUtil;
import com.mysite.blog.uitl.Result;
import com.mysite.blog.uitl.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/7 16:26
 * 文件上传
 */
@Controller
@RequestMapping("/admin")
public class UploadController {
    /**
     * 封面上传
     * @param httpServletRequest httpServletRequest
     * @param file file
     * @return Result
     * @throws URISyntaxException URISyntaxException
     */
    @PostMapping({"/upload/file"})
    @ResponseBody
    public Result upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws URISyntaxException {
        // 返回的图片地址
        String returnUrl = "/upload/cover/";
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        // 查找文件夹
        File realPath = new File(PathUtil.UPLOAD_PATH_COVER);
        // 判断文件夹是否存在 不存在则创建
        if (!realPath.exists()){
            realPath.mkdirs();
        }
        File destFile = new File(realPath,newFileName);
        try {
            // 写入文件
            file.transferTo(destFile);
            Result resultSuccess = ResultGenerator.genSuccessResult();
            resultSuccess.setData(returnUrl + newFileName);
            return resultSuccess;
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("文件上传失败");
        }
    }
    /**
     * 用户头像上传
     * @param httpServletRequest httpServletRequest
     * @param file file
     * @return Result
     * @throws URISyntaxException URISyntaxException
     */
    @PostMapping({"/upload/uploadAvatar"})
    @ResponseBody
    public Result uploadAvatar(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws URISyntaxException {
        // 返回的图片地址
        String returnUrl = "/upload/userAvatar/";
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        // 查找文件夹
        File realPath = new File(PathUtil.UPLOAD_PATH_USER_AVATAR);
        // 判断文件夹是否存在 不存在则创建
        if (!realPath.exists()){
            realPath.mkdirs();
        }
        File destFile = new File(realPath,newFileName);
        try {
            // 写入文件
            file.transferTo(destFile);
            Result resultSuccess = ResultGenerator.genSuccessResult();
            resultSuccess.setData(returnUrl + newFileName);
            return resultSuccess;
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("文件上传失败");
        }
    }
}
