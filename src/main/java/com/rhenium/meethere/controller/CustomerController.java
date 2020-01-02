package com.rhenium.meethere.controller;

import com.rhenium.meethere.annotation.UserLoginRequired;
import com.rhenium.meethere.domain.Customer;
import com.rhenium.meethere.dto.CustomerRequest;
import com.rhenium.meethere.service.CustomerService;
import com.rhenium.meethere.util.JwtUtil;
import com.rhenium.meethere.vo.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author HavenTong
 * @date 2019/12/7 5:51 下午
 */
@Slf4j
@RestController
@Validated  // 对@RequestParam的校验需要在controller上加@Validated注解
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/check-code")
    public ResultEntity sendCheckCode(@RequestParam
                                          @Email(message = "必须为合法邮箱地址") String email) {
        customerService.sendCheckCode(email);
        return ResultEntity.succeed();
    }

    @PostMapping("/register")
    public ResultEntity register(@Validated @RequestBody CustomerRequest customerRequest){
        customerService.register(customerRequest);
        return ResultEntity.succeed();
    }

    @PostMapping("/login")
    public ResultEntity login(@Validated @RequestBody CustomerRequest customerRequest){
        Map<String, String> loginInfo = customerService.login(customerRequest);
        return ResultEntity.succeed(loginInfo);
    }

    @PostMapping("/save-user-info")
    @UserLoginRequired
    public ResultEntity saveUserInfo(@Validated @RequestBody CustomerRequest customerRequest) {
        customerService.saveUserInfo(customerRequest);
        return ResultEntity.succeed();
    }

    @PostMapping("/change-password")
    @UserLoginRequired
    public ResultEntity changePassword(@Validated @RequestBody CustomerRequest customerRequest) {
        customerService.changePassword(customerRequest);
        return ResultEntity.succeed();
    }

}
