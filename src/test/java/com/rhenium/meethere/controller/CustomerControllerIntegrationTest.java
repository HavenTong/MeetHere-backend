package com.rhenium.meethere.controller;

import com.alibaba.fastjson.JSON;
import com.rhenium.meethere.domain.Customer;
import com.rhenium.meethere.dto.CustomerRequest;
import com.rhenium.meethere.vo.ResultEntity;
import com.sun.mail.iap.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author HavenTong
 * @date 2019/12/30 11:10 上午
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String BASE_URL = "/customer";

    @Test
    @DisplayName("当用已注册邮箱进行注册时，返回异常信息")
    void shouldGetExceptionMessageWhenRegisteringWithEmailExisted(){
        // 通过ResponseEntity来接受返回结果包括HTTP返回的信息，如status code，header等
        Map<String, Object> map = new HashMap<>();
        // map中的key与url中的参数名对应，value是参数对应的值
        map.put("email", "10175101152@stu.ecnu.edu.cn");
        ResponseEntity<ResultEntity> response = testRestTemplate.
                getForEntity(BASE_URL + "/check-code?email={email}", ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("邮箱已被使用", result.getMessage())
        );
    }

    @Test
    @DisplayName("成功向未注册用户发送邮件")
    void shouldSendHtmlMail(){
        Map<String, Object> map = new HashMap<>();
        map.put("email", "852092786@qq.com");
        ResponseEntity<ResultEntity> response = testRestTemplate.
                getForEntity( BASE_URL +"/check-code?email={email}", ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals("success", result.getMessage()),
                () -> assertEquals(0, result.getCode())
        );
    }

    @Test
    @DisplayName("注册用户的邮箱未发送验证码，返回错误信息")
    void shouldGetExceptionMessageWhenNoCheckCodeSent(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .email("852092786@qq.com").password("123456").checkCode("123456").build();
        // 发送post请求
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/register", customerRequest, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals("邮箱未发送验证码或验证码已失效", result.getMessage()),
                () -> assertEquals(-1, result.getCode())
        );
    }

    @Test
    @DisplayName("注册用户的邮箱验证码失效，返回错误信息")
    void shouldGetExceptionMessageWhenCheckCodeIsExpired(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .email("852092786@qq.com").password("123456").checkCode("123456").build();
        // 发送post请求
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/register", customerRequest, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals("邮箱未发送验证码或验证码已失效", result.getMessage()),
                () -> assertEquals(-1, result.getCode())
        );
    }
}
