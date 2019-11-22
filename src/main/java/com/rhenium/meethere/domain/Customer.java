package com.rhenium.meethere.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HavenTong
 * @date 2019/11/22 5:16 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {
    private static final long serialVersionUID = -732486021572261637L;

    private Integer customerId;

    @Email
    private String email;
    private String phoneNumber;
    private String userName;
    private String password;

    private LocalDateTime registeredTime;
}
