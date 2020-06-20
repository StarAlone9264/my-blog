package com.mysite.blog.config;

import java.io.File;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/7 18:54
 * 自定义图片上传路径
 */
public class PathUtil {
    // 以下代码为本地使用

    public final static String UPLOAD_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static"  + File.separator + "upload" + File.separator;
    public final static String UPLOAD_PATH_COVER = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static"  + File.separator + "upload" + File.separator + "cover" + File.separator;
    public final static String UPLOAD_PATH_USER_AVATAR = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static"  + File.separator + "upload" + File.separator + "userAvatar" + File.separator;

    // 以下路径为服务器使用

//    public final static String UPLOAD_PATH = "/opt/static/upload/";
//    public final static String UPLOAD_PATH_COVER = "/opt/static/upload/cover/";
//    public final static String UPLOAD_PATH_USER_AVATAR = "/opt/static/upload/userAvatar/";
}
