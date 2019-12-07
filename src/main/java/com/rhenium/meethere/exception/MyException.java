package com.rhenium.meethere.exception;

import com.rhenium.meethere.enums.ResultEnum;
import lombok.Data;
import lombok.Getter;

/**
 * @author HavenTong
 * @date 2019/12/7 8:47 下午
 * 自定义异常，作为全局的异常类，使用该类型抛出异常，包含 code: 错误代码 / message: 错误信息
 */
@Getter
public class MyException extends RuntimeException {
    /**
     * code: 错误代码
     */
    private Integer code;

    public MyException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public MyException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}
