package com.rhenium.meethere.controller;

import com.rhenium.meethere.annotation.UserLoginRequired;
import com.rhenium.meethere.domain.Customer;
import com.rhenium.meethere.dto.CustomerRequest;
import com.rhenium.meethere.service.CustomerService;
import com.rhenium.meethere.util.JwtUtil;
import com.rhenium.meethere.vo.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author HavenTong
 * @date 2019/12/7 5:51 下午
 */
@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/check-code")
    public ResultEntity sendCheckCode(@RequestParam String email) {
        customerService.sendCheckCode(email);
        return ResultEntity.succeed();
    }

    @PostMapping("/register")
    public ResultEntity register(@RequestBody CustomerRequest customerRequest){
        customerService.register(customerRequest);
        return ResultEntity.succeed();
    }

    @PostMapping("/login")
    public ResultEntity login(@RequestBody CustomerRequest customerRequest){
        Map<String, String> loginInfo = customerService.login(customerRequest);
        return ResultEntity.succeed(loginInfo);
    }

    @PostMapping("/save-user-info")
    @UserLoginRequired
    public ResultEntity saveUserInfo(@RequestBody CustomerRequest customerRequest) {
        customerService.saveUserInfo(customerRequest);
        return ResultEntity.succeed();
    }

    @PostMapping("/change-password")
    @UserLoginRequired
    public ResultEntity changePassword(@RequestBody CustomerRequest customerRequest){
        customerService.changePassword(customerRequest);
        return ResultEntity.succeed();
    }


    // Testing
    // 可以接收并成功验证
//    @GetMapping("/get-one")
//    @UserLoginRequired
//    public ResultEntity getOne(@RequestParam String email, @RequestBody CustomerRequest customerRequest){
//        Customer customer = customerService.getOne(email);
//        return ResultEntity.succeed(customer);
//    }


    /**
     * 非业务接口，仅仅便于获得某个用户的JWT进行测试
     * @param customerRequest 想要获取JWT的customerRequest
     * @return JWT
     */
    @GetMapping("/jwt")
    public ResultEntity getCustomerJwt(@RequestBody CustomerRequest customerRequest){
        Customer customer = Customer.builder()
                .customerId(customerRequest.getCustomerId())
                .email(customerRequest.getEmail())
                .build();
        String token = JwtUtil.createJwt(customer);
        return ResultEntity.succeed(token);
    }

}
