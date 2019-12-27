package com.rhenium.meethere.controller;

import com.rhenium.meethere.service.impl.AdminServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author HavenTong
 * @date 2019/12/27 9:25 上午
 */
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminServiceImpl adminService;

    @Test
    @DisplayName("管理员获取用户数量时，若未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingUserCountWithoutToken() throws Exception {
        ResultActions perform = mockMvc
                .perform(get("/admin/get-user-count")
                .param("adminId", "1"));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("管理员获取用户数量时，若携带TOKEN与adminId不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingUserCountWithWrongToken() throws Exception {
        ResultActions perform = mockMvc
                .perform(get("/admin/get-user-count")
                .param("adminId", "1")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDEwOTM4LCJleHAiOjE1NzgwMTU3Mzh9.z6moACNXcOsLP0VkAfnFE8fiihRKS3FLgQIJwGITlws"));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }
}