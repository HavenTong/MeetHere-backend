package com.rhenium.meethere.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author HavenTong
 * @date 2019/12/7 8:27 下午
 * 表示各种异常的枚举类, 错误代码从1001开始依次增加
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
    /**
     * 在这里可以一直添加异常类型的枚举
     */
    EMAIL_EXISTS(1001, "邮箱已被使用"),
    ;

    private Integer code;
    private String message;

}
