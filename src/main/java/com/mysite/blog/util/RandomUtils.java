package com.mysite.blog.util;

import java.util.Random;

/**
 * @author Star
 * @version 1.0
 * @date 2020/8/15 18:03
 */
public class RandomUtils {
    /**
     * 获取随机字符串，由数字、大小写字母组成
     * @param bytes：生成的字符串的位数
     * @return
     * @author
     */
    public static String getRandomStr(int bytes){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < bytes; i++) {
            //随机判断判断该字符是数字还是字母
            String choice = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(choice)) {
                //随机判断是大写字母还是小写字母
                int start = random.nextInt(2) % 2 == 0 ? 65 : 97;
                sb.append((char) (start + random.nextInt(26)));
            } else if ("num".equalsIgnoreCase(choice)) {
                sb.append(random.nextInt(10));
            }
        }
        return sb.toString();
    }
}
