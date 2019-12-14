package com.rhenium.meethere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Email;

/**
 * @author HavenTong
 * @date 2019/12/14 11:08 上午
 * Customer请求，可用于和JWT匹配以验证身份，也可以来对Customer做信息修改
 */
@Data
@AllArgsConstructor
@Builder
public class CustomerRequest {
    private Integer customerId;

    @Email
    private String email;

    private String phoneNumber;
    private String userName;
    private String password;
    private String checkCode;
}
