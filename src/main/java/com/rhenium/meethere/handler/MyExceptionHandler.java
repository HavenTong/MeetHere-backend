package com.rhenium.meethere.handler;

import com.rhenium.meethere.vo.ResultEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author HavenTong
 * @date 2019/12/7 8:50 下午
 * 全局异常处理，处理所有的异常，返回ResultEntity类型，其中包含异常信息
 */
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResultEntity handleException(Exception ex){
        ex.printStackTrace();
        return ResultEntity.fail(ex.getMessage());
    }

}
