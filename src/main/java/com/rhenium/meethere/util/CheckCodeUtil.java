package com.rhenium.meethere.util;

import java.util.Random;

/**
 * @author HavenTong
 * @date 2019/12/7 5:54 下午
 */
public class CheckCodeUtil {
    private static final String source = "0123456789";

    public static String generateCheckCode(){
        Random random = new Random();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < 6; i++){
            buffer.append(source.charAt(random.nextInt(10)));
        }
        return buffer.toString();
    }
}
