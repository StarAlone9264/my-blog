package com.mysite.blog.util;

import java.util.UUID;

/**
 * @author Star
 * @version 1.0
 * @date 2020/6/8 16:26
 */
public class UuidUtil {
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

}
