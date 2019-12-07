package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.service.CustomerService;
import com.rhenium.meethere.service.MailService;
import com.rhenium.meethere.util.CheckCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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

    @Override
    public void sendCheckCode(String email) {
        // TODO: 发送邮件，并将验证码存入redis
        String checkCode = CheckCodeUtil.generateCheckCode();
        redisTemplate.opsForValue().set("code:" + email, checkCode, 300, TimeUnit.SECONDS);
        String subject= "Registration from MeetHere";
        String content = "<h1>Welcome to MeetHere!</h1><p>Your check code is <u>" +
                checkCode + "</u></p>";
        mailService.sendHtmlMail(email, subject, content);
    }
}
