package com.rhenium.meethere.util;

import java.util.Map;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/30 19:44
 */
public class HttpRequestUtil {
    public static String getGetRequestUrl(String baseUrl, Map<String, String> request) {
        baseUrl += "?";
        for(Map.Entry<String, String> item : request.entrySet()) {
            baseUrl += item.getKey() + "=" + item.getValue() + "&";
        }
        return baseUrl;
    }
}
