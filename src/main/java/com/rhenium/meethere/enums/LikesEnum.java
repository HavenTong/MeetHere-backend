package com.rhenium.meethere.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @author HavenTong
 * @date 2019/12/22 3:53 下午
 */
@Getter
public enum LikesEnum {
    LIKED(1, "已点赞"),
    NOT_LIKED(0,"未点赞" ),
    ;


    private Integer code;
    private String message;

    LikesEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
