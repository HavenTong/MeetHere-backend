package com.rhenium.meethere.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HavenTong
 * @date 2019/11/22 5:16 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer implements Serializable {
    private static final long serialVersionUID = -732486021572261637L;

    private Integer customerId;

    @Email(message = "必须满足邮箱格式")
    private String email;
    private String phoneNumber;

    @Size(max = 8, message = "用户名不超过8个字符")
    private String userName;
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime registeredTime;
}
