package com.rhenium.meethere.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author HavenTong
 * @date 2019/11/22 5:18 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin implements Serializable {
    private static final long serialVersionUID = 8963513786794065582L;

    private Integer adminId;

    @Email(message = "必须满足邮箱格式")
    private String email;

    private String phoneNumber;
    private String adminName;
    private String password;
}
