package com.rhenium.meethere.controller;

import com.rhenium.meethere.service.CustomerService;
import com.rhenium.meethere.vo.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;

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

    @RequestMapping(value = "check-code", method = RequestMethod.POST)
    public ResultEntity sendCheckCode(@RequestParam String email) {
        customerService.sendCheckCode(email);
        return ResultEntity.succeed();
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResultEntity register(@RequestParam String userName,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String checkCode) {
        customerService.register(userName, email, password, checkCode);
        return ResultEntity.succeed();
    }

    @RequestMapping(value = "save-user-info", method = RequestMethod.POST)
    public ResultEntity saveUserInfo(@RequestParam String customerId,
                                     @RequestParam String userName,
                                     @RequestParam String phoneNumber) {
        customerService.saveUserInfo(customerId, userName, phoneNumber);
        return ResultEntity.succeed();
    }
}
