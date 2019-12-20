package com.rhenium.meethere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

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

    @Email
    private String email;
    private String phoneNumber;
    private String adminName;
    private String password;

    /**
     * 当管理员需要对用户进行一些操作时需要匹配 customerId.
     */
    private Integer customerId;

    // 管理员需要对留言进行审核
    private Integer commentId;

    private Integer bookingId;
}
