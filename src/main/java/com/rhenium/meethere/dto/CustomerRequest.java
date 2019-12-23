package com.rhenium.meethere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import reactor.util.annotation.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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

    @Email(message = "必须满足邮箱格式")
    private String email;

    @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
    private String phoneNumber;

    @Size(min = 1, max = 8, message = "用户名必须在8位以内")
    private String userName;

    @Size(min = 6,  max = 20, message = "密码长度必须在6-20位")
    private String password;

    @Size(min = 6, max = 20, message = "新密码长度必须在6-20位")
    private String newPassword;

    private String checkCode;
}
