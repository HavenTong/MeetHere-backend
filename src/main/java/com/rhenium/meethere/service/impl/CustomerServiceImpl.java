package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.CustomerDao;
import com.rhenium.meethere.domain.Customer;
import com.rhenium.meethere.dto.CustomerRequest;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.CustomerService;
import com.rhenium.meethere.service.MailService;
import com.rhenium.meethere.util.CheckCodeUtil;
import com.rhenium.meethere.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.transform.Result;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author HavenTong
 * @date 2019/12/7 6:05 下午
 */
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MailService mailService;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public void sendCheckCode(String email) {
        // 判断邮箱是否存在，若存在则不发送
        Customer customer = customerDao.findCustomerByEmail(email);
        if (customer != null){
            throw new MyException(ResultEnum.EMAIL_EXISTS);
        }
        // 邮箱不存在，发送验证码
        String checkCode = CheckCodeUtil.generateCheckCode();
        redisTemplate.opsForValue().set("code:" + email, checkCode, 300, TimeUnit.SECONDS);
        String subject= "Registration from MeetHere";
        String content = "<h1>Welcome to MeetHere!</h1><p>Your check code is <u>" +
                checkCode + "</u></p>";
        mailService.sendHtmlMail(email, subject, content);
    }

    @Override
    public void register(CustomerRequest customerRequest) {
        String expectedCode = redisTemplate.opsForValue().get("code:" + customerRequest.getEmail());
        if (expectedCode == null){
            throw new MyException(ResultEnum.EMAIL_NO_CHECK_CODE);
        }
        if (!expectedCode.equals(customerRequest.getCheckCode())){
            throw new MyException(ResultEnum.CHECK_CODE_ERROR);
        }
        Customer customer = new Customer();
        customer.setEmail(customerRequest.getEmail());
        customer.setPassword(encoder.encode(customerRequest.getPassword()));
        customer.setUserName(customerRequest.getUserName());
        customer.setRegisteredTime(LocalDateTime.now());
        customerDao.saveCustomer(customer);
    }

    @Override
    public Map<String, String> login(CustomerRequest customerRequest) {
        // 根据邮箱和密码进行登录，首先通过邮箱查找用户判断用户是否存在，
        // 若存在则判断密码，若密码一致则生成JWT，将信息放入Map中返回，
        // 密码不一致抛出异常
        // 用户不存在抛出异常
        Customer customer = customerDao.findCustomerByEmail(customerRequest.getEmail());
        if (customer == null){
            throw new MyException(ResultEnum.USER_NOT_EXIST);
        }
        if (!encoder.matches(customerRequest.getPassword(), customer.getPassword())){
            throw new MyException(ResultEnum.PASSWORD_ERROR);
        }
        Map<String, String> loginInfo = new HashMap<>();
        String token = JwtUtil.createJwt(customer);
        String customerId = customer.getCustomerId().toString();
        String email = customer.getEmail();
        String userName = customer.getUserName();
        loginInfo.put("token", token);
        loginInfo.put("customerId", customerId);
        loginInfo.put("email", email);
        loginInfo.put("userName", userName);
        return loginInfo;
    }
}
