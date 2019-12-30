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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
                .userName("root").email("852092786@qq.com").password("123456").checkCode("123456").build();
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
    @DisplayName("注册用户的用户名的长度不在规定范围内，返回错误信息")
    void shouldGetExceptionMessageWhenRegisterWithUserNameNotValid(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .userName("TheUserNameIsNotValidForMeetHereSystem").email("852092786@qq.com").password("123456").checkCode("123456").build();
        // 发送post请求
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/register", customerRequest, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode())
        );
    }

    @Test
    @DisplayName("注册用户的密码的长度不在规定范围内，返回错误信息")
    void shouldGetExceptionMessageWhenRegisterWithPasswordNotValid(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .userName("root").email("852092786@qq.com").password("ThePasswordIsNotValidForMeetHere").checkCode("123456").build();
        // 发送post请求
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/register", customerRequest, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode())
        );
    }

    @Test
    @DisplayName("注册用户的邮箱验证码失效，返回错误信息")
    void shouldGetExceptionMessageWhenCheckCodeIsExpired(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .userName("root").email("852092786@qq.com").password("123456").checkCode("260813").build();
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
    @DisplayName("注册用户的验证码正确且信息符合要求，成功注册(check-code必须在运行测试脚本前获取最新的check-code，否则当前测试用例无法通过)")
    void shouldRegisterWhenCheckCodeIsCorrect(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .email("852092786@qq.com").password("123456").checkCode("260813")
                .userName("root").build();
        // 发送post请求
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/register", customerRequest, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals("success", result.getMessage()),
                () -> assertEquals(0, result.getCode())
        );
    }

    @Test
    @DisplayName("登录用户密码不正确，返回错误信息")
    void shouldGetExceptionMessageWhenLoginWithWrongPassword(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .email("10175101152@stu.ecnu.edu.cn")
                .password("wrong-password")
                .build();
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/login", customerRequest, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("密码错误", result.getMessage())
        );
    }

    @Test
    @DisplayName("登录用户密码正确，返回正确登录信息")
    void shouldGetCorrectLoginInfoWhenPasswordIsCorrect(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .email("10175101152@stu.ecnu.edu.cn")
                .password("123456")
                .build();
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/login", customerRequest, ResultEntity.class);
        ResultEntity result = response.getBody();
        Map<String, Object> map =(Map<String, Object>) result.getData();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertTrue(map.containsKey("token")),
                () -> assertEquals("10175101152@stu.ecnu.edu.cn", map.get("email")),
                () -> assertEquals("621", map.get("customerId")),
                () -> assertEquals("haven", map.get("userName")),
                () -> assertTrue(map.containsKey("phoneNumber")),
                () -> assertNotNull(map.get("registeredTime"))
        );
    }

    @Test
    @DisplayName("修改用户信息时，未携带TOKEN，则返回错误信息")
    void shouldGetExceptionMessageWhenSavingUserInfoWithoutToken(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(621).userName("root")
                .phoneNumber("18900001111").build();

        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/save-user-info", customerRequest, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }


    @Test
    @DisplayName("修改用户信息时，若TOKEN不匹配，则返回错误信息")
    void shouldGetExceptionMessageWhenSavingUserInfoWithTokenNotMatch(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(621).userName("root")
                .phoneNumber("18900001111").build();

        // 在头部添加token
        HttpHeaders headers = new HttpHeaders();
        // token for 620
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MjAiLCJpYXQiOjE1Nzc2OTY1MzEsImV4cCI6MTU3OTc3MDEzMX0.demvakZwpAOzt_hZEJkjGNvBEKQsAIQQCTKMtouicUc");
        HttpEntity<CustomerRequest> entity = new HttpEntity<>(customerRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/save-user-info", entity, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("修改用户信息时，若修改的用户名长度不在要求的范围内，则返回错误信息")
    void shouldGetExceptionMessageWhenChangingInfoWithUserNameNotValid(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(621).userName("TheUserNameIsNotValidForMeetHereSystem")
                .phoneNumber("18900001111").build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MjEiLCJpYXQiOjE1Nzc2OTU2NjIsImV4cCI6MTU3OTc2OTI2Mn0.moSEFHCMWRLNZSLhYK9IZG_zPGTNMMDv3DrUI4eD_K4");
        HttpEntity<CustomerRequest> entity = new HttpEntity<>(customerRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/save-user-info", entity, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode())
        );
    }

    @Test
    @DisplayName("修改用户信息时，若修改的手机号码不符合格式，则返回错误信息")
    void shouldGetExceptionMessageWhenChangingInfoWithPhoneNumberNotValid(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(621).userName("root")
                .phoneNumber("189abcdefgh").build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MjEiLCJpYXQiOjE1Nzc2OTU2NjIsImV4cCI6MTU3OTc2OTI2Mn0.moSEFHCMWRLNZSLhYK9IZG_zPGTNMMDv3DrUI4eD_K4");
        HttpEntity<CustomerRequest> entity = new HttpEntity<>(customerRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/save-user-info", entity, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode())
        );
    }

    @Test
    @DisplayName("修改用户信息时，若TOKEN匹配且修改的信息格式符合要求，则修改成功")
    void shouldSaveCorrectUserInfo(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(621).userName("root")
                .phoneNumber("18900001111").build();

        // 在头部添加token
        HttpHeaders headers = new HttpHeaders();
        // token for 621
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MjEiLCJpYXQiOjE1Nzc2OTU2NjIsImV4cCI6MTU3OTc2OTI2Mn0.moSEFHCMWRLNZSLhYK9IZG_zPGTNMMDv3DrUI4eD_K4");
        HttpEntity<CustomerRequest> entity = new HttpEntity<>(customerRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/save-user-info", entity, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );

        // restore
        CustomerRequest restoreRequest = CustomerRequest.builder()
                .customerId(621).userName("haven").build();
        HttpEntity<CustomerRequest> restoreEntity = new HttpEntity<>(restoreRequest, headers);
        testRestTemplate.postForEntity(BASE_URL + "/save-user-info", restoreEntity, ResultEntity.class);
    }

    @Test
    @DisplayName("修改用户密码时，未携带TOKEN，则返回错误信息")
    void shouldGetExceptionMessageWhenChangingPasswordWithoutToken(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(621).newPassword("456789").password("123456").build();
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/change-password", customerRequest, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("修改用户密码时，若TOKEN不匹配，则返回错误信息")
    void shouldGetExceptionMessageWhenChangingPasswordWithTokenNotMatch(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(621).newPassword("456789").password("123456").build();

        // 在头部添加token
        HttpHeaders headers = new HttpHeaders();
        // token for 620
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MjAiLCJpYXQiOjE1Nzc2OTY1MzEsImV4cCI6MTU3OTc3MDEzMX0.demvakZwpAOzt_hZEJkjGNvBEKQsAIQQCTKMtouicUc");
        HttpEntity<CustomerRequest> entity = new HttpEntity<>(customerRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/change-password", entity, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("修改用户密码时，若旧密码不匹配，则返回错误信息")
    void shouldGetExceptionMessageWhenChangingPasswordWithPasswordNotMatch(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(621).newPassword("456789").password("wrongOldPassWord").build();

        // 在头部添加token
        HttpHeaders headers = new HttpHeaders();
        // token for 621
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MjEiLCJpYXQiOjE1Nzc2OTU2NjIsImV4cCI6MTU3OTc2OTI2Mn0.moSEFHCMWRLNZSLhYK9IZG_zPGTNMMDv3DrUI4eD_K4");
        HttpEntity<CustomerRequest> entity = new HttpEntity<>(customerRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/change-password", entity, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("密码错误", result.getMessage())
        );
    }


    @Test
    @DisplayName("修改用户密码时，若新密码格式不符合要求，则返回错误信息 ")
    void shouldGetExceptionMessageWhenChangingPasswordWithNewPasswordNotValid(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(621).newPassword("newPasswordTooLongWithWrongFormat").password("123456").build();

        // 在头部添加token
        HttpHeaders headers = new HttpHeaders();
        // token for 621
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MjEiLCJpYXQiOjE1Nzc2OTU2NjIsImV4cCI6MTU3OTc2OTI2Mn0.moSEFHCMWRLNZSLhYK9IZG_zPGTNMMDv3DrUI4eD_K4");
        HttpEntity<CustomerRequest> entity = new HttpEntity<>(customerRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/change-password", entity, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode())
        );
    }

    @Test
    @DisplayName("修改用户密码时，若新密码格式符合要求且旧密码匹配，则成功修改密码")
    void shouldChangeCorrectPassword(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(621).newPassword("456789").password("123456").build();

        // 在头部添加token
        HttpHeaders headers = new HttpHeaders();
        // token for 621
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MjEiLCJpYXQiOjE1Nzc2OTU2NjIsImV4cCI6MTU3OTc2OTI2Mn0.moSEFHCMWRLNZSLhYK9IZG_zPGTNMMDv3DrUI4eD_K4");
        HttpEntity<CustomerRequest> entity = new HttpEntity<>(customerRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/change-password", entity, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );

        // restore
        CustomerRequest restoreRequest = CustomerRequest.builder()
                .customerId(621).password("456789").newPassword("123456").build();
        HttpEntity<CustomerRequest> restoreEntity = new HttpEntity<>(restoreRequest, headers);
        testRestTemplate.postForEntity(BASE_URL + "/change-password", restoreEntity, ResultEntity.class);
    }
}
