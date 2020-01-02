package com.rhenium.meethere.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/16 23:18
 */
@Getter
@AllArgsConstructor
public enum StadiumTypeEnum {
    // 表示场馆的类型，code中内容为存到数据库中内容，type表达实际含义
    NONE_STADIUM(-1, "待定分类"),
    TENNIS_BALL_STADIUM(0, "网球场"),
    VOLLEYBALL_STADIUM(1, "排球场"),
    BASKETBALL_STADIUM(2, "篮球场"),
    BADMINTON_STADIUM(3, "羽毛球场"),
    TABLE_TENNIS_STADIUM(4, "乒乓球场");


    private Integer code;
    private String type;

    public static StadiumTypeEnum getByCode(Integer code) {
        for (StadiumTypeEnum e : StadiumTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return StadiumTypeEnum.NONE_STADIUM;

    }

    public static StadiumTypeEnum getByType(String name) {
        for (StadiumTypeEnum e : StadiumTypeEnum.values()) {
            if (e.getType().equals(name)) {
                return e;
            }
        }
        return StadiumTypeEnum.NONE_STADIUM;
    }

    public static List<Map<String, Object>> getTypes() {
        List<Map<String, Object>> types = new ArrayList<>();
        for (StadiumTypeEnum e : StadiumTypeEnum.values()) {
            Map<String, Object> type = new HashMap<>();
            type.put("stadiumName", e.getType());
            type.put("type", e.getCode());
            types.add(type);
        }
        return types;
    }
}
