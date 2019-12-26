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
    NONE_STADIUM(-1, "待定分类"),
    TENNIS_BALL_STADIUM(0, "网球场"),
    VOLLEYBALL_STADIUM(1, "排球场"),
    BASKETBALL_STADIUM(2, "篮球场"),
    BADMINTON_STADIUM(3, "羽毛球场"),
    TABLE_TENNIS_STADIUM(4, "乒乓球场");


    private Integer code;
    private String type;

    public static StadiumTypeEnum getByCode(Integer code) {
        switch (code) {
            case 0:
                return StadiumTypeEnum.TENNIS_BALL_STADIUM;
            case 1:
                return StadiumTypeEnum.VOLLEYBALL_STADIUM;
            default:
                return StadiumTypeEnum.NONE_STADIUM;
        }
    }

    public static StadiumTypeEnum getByType(String name) {
        switch (name) {
            case "网球场":
                return StadiumTypeEnum.TENNIS_BALL_STADIUM;
            case "排球场":
                return StadiumTypeEnum.VOLLEYBALL_STADIUM;
            default:
                return StadiumTypeEnum.NONE_STADIUM;
        }
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
