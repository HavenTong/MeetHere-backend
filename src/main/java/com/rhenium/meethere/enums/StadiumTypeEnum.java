package com.rhenium.meethere.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/16 23:18
 */
@Getter
@AllArgsConstructor
public enum StadiumTypeEnum {
    NONE_STADIUM(-1, "错误"),
    TENNIS_BALL_STADIUM(0, "网球场"),
    VOLLEYBALL_STADIUM(1, "排球场");


    private Integer code;
    private String type;

    public static StadiumTypeEnum getByCode(Integer code) {
        switch (code) {
            case 0: return StadiumTypeEnum.TENNIS_BALL_STADIUM;
            case 1: return StadiumTypeEnum.VOLLEYBALL_STADIUM;
            default: return StadiumTypeEnum.NONE_STADIUM;
        }
    }

    public static StadiumTypeEnum getByType(String name) {
        switch (name) {
            case "网球场": return StadiumTypeEnum.TENNIS_BALL_STADIUM;
            case "排球场": return StadiumTypeEnum.VOLLEYBALL_STADIUM;
            default: return StadiumTypeEnum.NONE_STADIUM;
        }
    }
}
