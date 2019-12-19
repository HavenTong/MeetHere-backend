package com.rhenium.meethere.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HavenTong
 * @date 2019/12/19 12:58 上午
 * 工具类，可以将对象的所有属性名及其值放到一个map中
 */
@Slf4j
public class MapUtil {
    public static Map<String, Object> objToMap(Object object){
        if (object == null) return null;
        Map<String, Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields){
            field.setAccessible(true);
            log.info("accessible");
            try {
                log.info("fieldName: {}", field.getName());
                Object attributeValue = field.get(object);
                if (attributeValue != null && !StringUtils.isEmpty(attributeValue)){
                    map.put(field.getName(), attributeValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
