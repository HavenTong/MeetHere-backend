package com.rhenium.meethere.controller;

import com.alibaba.fastjson.JSON;
import com.rhenium.meethere.domain.Customer;
import com.rhenium.meethere.dto.CustomerRequest;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.impl.CustomerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import mockit.Mock;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author HavenTong
 * @date 2019/12/20 3:32 下午
 * 若出现带TOKEN的测试方法失败，可能是JWT过期，想要通过测试需要更换测试用例中的JWT
 */
@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerServiceImpl customerService;

    @Test
    @DisplayName("发送验证码时，发送正确的邮箱")
    void shouldSendCorrectEmailWhenSendCheckCode() throws Exception{
        ResultActions perform = mockMvc.perform(get("/customer/check-code").
                param("email", "852092786@qq.com"));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.message").value("success"));
        verify(customerService, times(1))
                        .sendCheckCode("852092786@qq.com");

    }

    @Test
    @DisplayName("注册时，注册正确的用户")
    void shouldRegisterCorrectUser() throws Exception{
        CustomerRequest customerRequest = CustomerRequest.builder()
                .email("852092786@qq.com").userName("root")
                .password("123456").checkCode("123456").build();
        ResultActions perform = mockMvc.perform(post("/customer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JSON.toJSONString(customerRequest)));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
        verify(customerService, times(1))
                .register(customerRequest);
    }

    @Test
    @DisplayName("登录时，完成正确用户的登录")
    void shouldLoginWithCorrectUser() throws Exception{
        CustomerRequest customerRequest  = CustomerRequest.builder()
                .email("852092786@qq.com").password("123456").build();
        ResultActions perform = mockMvc.perform(post("/customer/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JSON.toJSONString(customerRequest)));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
        verify(customerService, times(1))
                .login(customerRequest);
    }

    @Test
    @DisplayName("修改用户信息时，若HTTP头部没有携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenUpdatingUserInfoWithoutToken() throws Exception{
        CustomerRequest customerRequest = CustomerRequest.builder()
                .userName("root").phoneNumber("18900001111")
                .customerId(501).build();
        ResultActions perform = mockMvc.perform(post("/customer/save-user-info")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JSON.toJSONString(customerRequest))); // 需要将Java对象转换为JSON

        // 需要使用jsonPath来取出返回的ResultEntity中的字段
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(customerService, never())
                .changePassword(customerRequest);
    }

    @Test
    @DisplayName("修改用户信息时，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenUpdatingUserInfoWithWrongToken() throws Exception {
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(501).phoneNumber("18900005555").userName("root").build();
        ResultActions perform = mockMvc.
                perform(post("/customer/save-user-info").
                        contentType(MediaType.APPLICATION_JSON).
                        content(JSON.toJSONString(customerRequest)).
                        header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(customerService, never())
                .changePassword(customerRequest);
    }

    @Test
    @DisplayName("修改用户信息时，若TOKEN匹配，则返回正确结果")
    void shouldSaveUserInfoWhenTokenMatches() throws Exception{
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(502).phoneNumber("18900005555").userName("root").build();
        ResultActions perform = mockMvc.
                perform(post("/customer/save-user-info").
                        contentType(MediaType.APPLICATION_JSON).
                        content(JSON.toJSONString(customerRequest)).
                        header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
        verify(customerService, times(1))
                .saveUserInfo(customerRequest);
    }

    @Test
    @DisplayName("修改密码时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnWrongMessageWhenChangePasswordWithoutToken() throws Exception{
        CustomerRequest customerRequest = CustomerRequest.builder()
                .userName("root").password("123456").newPassword("456789")
                .customerId(501).build();
        ResultActions perform = mockMvc.perform(post("/customer/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(customerRequest)));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(customerService, never())
                .changePassword(customerRequest);
    }

    @Test
    @DisplayName("修改密码时，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnWrongMessageWhenChangePasswordWithTokenNotMatch() throws Exception{
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(501).userName("root").password("123456").newPassword("456789").userName("root").build();
        ResultActions perform = mockMvc.
                perform(post("/customer/change-password").
                        contentType(MediaType.APPLICATION_JSON).
                        content(JSON.toJSONString(customerRequest)).
                        header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(customerService, never())
                .changePassword(customerRequest);
    }

    @Test
    @DisplayName("修改密码时，若TOKEN匹配，则返回正确结果")
    void shouldChangePasswordWhenTokenMatches() throws Exception{
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(502).userName("root").password("123456").newPassword("456789").userName("root").build();
        ResultActions perform = mockMvc.
                perform(post("/customer/change-password").
                        contentType(MediaType.APPLICATION_JSON).
                        content(JSON.toJSONString(customerRequest)).
                        header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
        verify(customerService, times(1))
                .changePassword(customerRequest);
    }
}