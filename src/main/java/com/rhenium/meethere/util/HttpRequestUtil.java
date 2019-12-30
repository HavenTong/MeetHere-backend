package com.rhenium.meethere.util;

import java.util.Map;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/30 19:44
 */
public class HttpRequestUtil {
    public static String getGetRequestUrl(String BaseUrl, Map<String, String> request) {
        BaseUrl += "?";
        for(Map.Entry<String, String> item : request.entrySet()) {
            BaseUrl += item.getKey() + "=" + item.getValue() + "&";
        }
        return BaseUrl;
    }
}
