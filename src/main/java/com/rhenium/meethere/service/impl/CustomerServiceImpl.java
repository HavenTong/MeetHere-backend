package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.CustomerDao;
import com.rhenium.meethere.domain.Customer;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.CustomerService;
import com.rhenium.meethere.service.MailService;
import com.rhenium.meethere.util.CheckCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public void register(String userName, String email, String password, String checkCode) {
        String expectedCode = redisTemplate.opsForValue().get("code:" + email);
        if (expectedCode == null){
            throw new MyException(ResultEnum.EMAIL_NO_CHECK_CODE);
        }
        if (!checkCode.equals(expectedCode)){
            throw new MyException(ResultEnum.CHECK_CODE_ERROR);
        }
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(encoder.encode(password));
        customer.setUserName(userName);
        customer.setRegisteredTime(LocalDateTime.now());
        customerDao.saveNewCustomer(customer);
    }

    @Override
    public void saveUserInfo(String customerId, String userName, String phoneNumber) {
        // 因为拦截在控制器之前就已经完成，所以该 customerId 是值得信任的
        customerDao.saveCustomerInfo(customerId, userName, phoneNumber);
    }
}
