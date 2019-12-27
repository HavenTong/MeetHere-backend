package com.rhenium.meethere.controller;

import com.alibaba.fastjson.JSON;
import com.rhenium.meethere.dto.StadiumRequest;
import com.rhenium.meethere.service.impl.StadiumServiceImpl;
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
 * @author JJAYCHEN
 * @date 2019/12/27 9:08 上午
 */
@SpringBootTest
@AutoConfigureMockMvc
class StadiumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StadiumServiceImpl stadiumService;

    @Test
    @DisplayName("获取场馆个数时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetStadiumCountWithoutToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/get-stadium-count")
                .param("adminId", "1"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(stadiumService, never())
                .getStadiumCount();
    }

    @Test
    @DisplayName("获取场馆个数时，若HTTP头部携带的TOKEN与adminId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetStadiumCountWithWrongToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/get-stadium-count")
                .param("adminId", "1")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc2ODMyNzY1LCJleHAiOjE1Nzc0Mzc1NjV9.Ei9A3vq1uKrVCPVLNqsY7q2kuvlyBjkyQWuxmueAuR0"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(stadiumService, never())
                .getStadiumCount();
    }

    @Test
    @DisplayName("获取场馆个数时，若HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    void shouldGetStadiumCountWithCorrectToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/get-stadium-count")
                .param("adminId", "2")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc2ODMyNzY1LCJleHAiOjE1Nzc0Mzc1NjV9.Ei9A3vq1uKrVCPVLNqsY7q2kuvlyBjkyQWuxmueAuR0"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(stadiumService, times(1))
                .getStadiumCount();
    }

    @Test
    @DisplayName("删除场馆时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenDeleteStadiumWithoutToken() throws Exception {
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(1).build();

        ResultActions perform = mockMvc.perform(post("/stadium/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(stadiumRequest)));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(stadiumService, never())
                .deleteStadium(stadiumRequest);
    }

    @Test
    @DisplayName("删除场馆时，若HTTP头部携带的TOKEN与adminId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenDeleteStadiumWithWrongToken() throws Exception {
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(1).build();

        ResultActions perform = mockMvc.perform(post("/stadium/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(stadiumRequest))
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc2ODMyNzY1LCJleHAiOjE1Nzc0Mzc1NjV9.Ei9A3vq1uKrVCPVLNqsY7q2kuvlyBjkyQWuxmueAuR0"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(stadiumService, never())
                .deleteStadium(stadiumRequest);
    }

    @Test
    @DisplayName("删除场馆时，若HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    void shouldDeleteStadiumWithCorrectToken() throws Exception {
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(2).build();

        ResultActions perform = mockMvc.perform(post("/stadium/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(stadiumRequest))
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc2ODMyNzY1LCJleHAiOjE1Nzc0Mzc1NjV9.Ei9A3vq1uKrVCPVLNqsY7q2kuvlyBjkyQWuxmueAuR0"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(stadiumService, times(1))
                .deleteStadium(stadiumRequest);
    }

    @Test
    void postStadium() {
    }

    @Test
    void updateStadium() {
    }

    @Test
    void getStadiumTypes() {
    }

    @Test
    void listStadiumItems() {
    }

    @Test
    void getStadiumById() {
    }

    @Test
    void getStadiumsForAdmin() {
    }
}