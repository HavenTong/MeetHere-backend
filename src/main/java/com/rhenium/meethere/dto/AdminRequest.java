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
}
