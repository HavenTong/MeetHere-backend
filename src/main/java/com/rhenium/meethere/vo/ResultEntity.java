package com.rhenium.meethere.vo;

import com.rhenium.meethere.enums.ResultEnum;
import lombok.Data;

/**
 * @author HavenTong
 * @date 2019/12/7 8:19 下午
 * ResultEntity用于请求的响应，作为controller所有方法的返回值类型
 *
 */
@Data
public class ResultEntity {
    /**
     * data: 返回的数据
     */
    private Object data;

    /**
     * code: 0-表示成功，-1-表示失败
     */
    private Integer code;

    /**
     * message: 返回的信息
     */
    private String message;

    private ResultEntity(Object data, Integer code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    /**
     * 成功，但不返回数据
     * @return
     */
    public static ResultEntity succeed(){
        return new ResultEntity(null, 0, "success");
    }

    /**
     *  成功且需要返回数据
     * @param data 返回的数据
     * @return
     */
    public static ResultEntity succeed(Object data){
        return new ResultEntity(data, 0, "success");
    }

    /**
     * 失败且返回失败信息
     * @param message 失败信息
     * @return
     */
    public static ResultEntity fail(String message){
        return new ResultEntity(null, -1, message);
    }

    /**
     * 失败且返回失败的枚举类型
     * @param resultEnum 失败的枚举类型
     * @return
     */
    public static ResultEntity fail(ResultEnum resultEnum){
        return new ResultEntity(null, resultEnum.getCode(), resultEnum.getMessage());
    }


}
