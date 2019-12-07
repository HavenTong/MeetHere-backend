package com.rhenium.meethere.controller;

import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.service.CustomerService;
import com.rhenium.meethere.service.MailService;
import com.rhenium.meethere.util.CheckCodeUtil;
import com.rhenium.meethere.vo.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/email")
    public ResultEntity checkEmail(){
        return ResultEntity.fail("邮箱检查");
    }

}
