package com.rhenium.meethere.controller;

import com.rhenium.meethere.dto.CustomerRequest;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.service.CustomerService;
import com.rhenium.meethere.util.CheckCodeUtil;
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
    public ResultEntity sendCheckCode(@RequestParam String email){
        customerService.sendCheckCode(email);
        return ResultEntity.succeed();
    }

    @PostMapping("/register")
    public ResultEntity register(@RequestBody CustomerRequest customerRequest){
        customerService.register(customerRequest);
        return ResultEntity.succeed();
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody CustomerRequest customerRequest){
        return customerService.login(customerRequest);
    }

}
