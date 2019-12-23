package com.rhenium.meethere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author HavenTong
 * @date 2019/12/17 5:15 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminRequest {
    private Integer adminId;

    @Email(message = "必须满足邮箱格式")
    private String email;

    @Pattern(regexp = "^1[34578]\\d{9}$", message = "必须满足手机号码格式")
    private String phoneNumber;

    @Size(min = 1, max = 8, message = "管理员用户名必须在8位以内")
    private String adminName;

    @Size(min = 6,  max = 20, message = "密码长度必须在6-20位")
    private String password;

    /**
     * 当管理员需要对用户进行一些操作时需要匹配 customerId.
     */
    private Integer customerId;

    // 管理员需要对留言进行审核
    private Integer commentId;

    private Integer bookingId;
}
