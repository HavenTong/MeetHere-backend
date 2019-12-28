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
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));
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
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo"));
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
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));
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
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(stadiumService, times(1))
                .deleteStadium(stadiumRequest);
    }

    @Test
    @DisplayName("新增场馆时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenPostStadiumWithoutToken() throws Exception {
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(1).build();

        ResultActions perform = mockMvc.perform(post("/stadium/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(stadiumRequest)));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(stadiumService, never())
                .createStadium(stadiumRequest);
    }

    @Test
    @DisplayName("新增场馆时，若HTTP头部携带的TOKEN与adminId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenPostStadiumWithWrongToken() throws Exception {
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(1).build();

        ResultActions perform = mockMvc.perform(post("/stadium/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(stadiumRequest))
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(stadiumService, never())
                .createStadium(stadiumRequest);
    }

    @Test
    @DisplayName("新增场馆时，若HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    void shouldPostStadiumWithCorrectToken() throws Exception {
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(2).build();

        ResultActions perform = mockMvc.perform(post("/stadium/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(stadiumRequest))
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(stadiumService, times(1))
                .createStadium(stadiumRequest);
    }

    @Test
    @DisplayName("更新场馆时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenUpdateStadiumWithoutToken() throws Exception {
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(1).build();

        ResultActions perform = mockMvc.perform(post("/stadium/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(stadiumRequest)));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(stadiumService, never())
                .updateStadium(stadiumRequest);
    }

    @Test
    @DisplayName("更新场馆时，若HTTP头部携带的TOKEN与adminId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenUpdateStadiumWithWrongToken() throws Exception {
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(1).build();

        ResultActions perform = mockMvc.perform(post("/stadium/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(stadiumRequest))
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(stadiumService, never())
                .updateStadium(stadiumRequest);
    }

    @Test
    @DisplayName("更新场馆时，若HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    void shouldUpdateStadiumWithCorrectToken() throws Exception {
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(2).build();

        ResultActions perform = mockMvc.perform(post("/stadium/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(stadiumRequest))
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(stadiumService, times(1))
                .updateStadium(stadiumRequest);
    }

    @Test
    @DisplayName("用户获取场馆列表时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetStadiumListWithoutToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/items")
                .param("customerId", "502"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(stadiumService, never())
                .listStadiumItems();
    }

    @Test
    @DisplayName("用户获取场馆列表时，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetStadiumListWithWrongToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/items")
                .param("customerId", "501")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(stadiumService, never())
                .listStadiumItems();
    }

    @Test
    @DisplayName("用户获取场馆列表时，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldGetStadiumListWithCorrectToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/items")
                .param("customerId", "502")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(stadiumService, times(1))
                .listStadiumItems();
    }

    @Test
    @DisplayName("管理员获取场馆列表时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetStadiumListForAdminWithoutToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/items-for-admin")
                .param("offset", "0")
                .param("limit", "20")
                .param("adminId", "2"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(stadiumService, never())
                .findStadiumsForAdmin(0,20);
    }

    @Test
    @DisplayName("管理员获取场馆列表时，若HTTP头部携带的TOKEN与adminId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetStadiumListForAdminWithWrongToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/items-for-admin")
                .param("offset", "0")
                .param("limit", "20")
                .param("adminId", "1")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(stadiumService, never())
                .findStadiumsForAdmin(0,20);
    }

    @Test
    @DisplayName("管理员获取场馆列表时，若HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    void shouldGetStadiumListForAdminWithCorrectToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/items-for-admin")
                .param("offset", "0")
                .param("limit", "20")
                .param("adminId", "2")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(stadiumService, times(1))
                .findStadiumsForAdmin(0,20);
    }

    @Test
    @DisplayName("用户根据id获取场馆时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetStadiumByIdWithoutToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/message")
                .param("id", "1")
                .param("customerId", "502"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(stadiumService, never())
                .getStadiumById(1);
    }

    @Test
    @DisplayName("用户根据id获取场馆时，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetStadiumByIdWithWrongToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/message")
                .param("id", "1")
                .param("customerId", "501")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(stadiumService, never())
                .getStadiumById(1);
    }

    @Test
    @DisplayName("用户根据id获取场馆时，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldGetStadiumByIdWithCorrectToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/message")
                .param("id", "1")
                .param("customerId", "502")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(stadiumService, times(1))
                .getStadiumById(1);
    }


    @Test
    @DisplayName("管理员获取场馆类型时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetStadiumTypesWithoutToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/types")
                .param("adminId", "2"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(stadiumService, never())
                .getStadiumTypes();
    }

    @Test
    @DisplayName("管理员获取场馆类型时，若HTTP头部携带的TOKEN与adminId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetStadiumTypesWithWrongToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/types")
                .param("adminId", "1")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(stadiumService, never())
                .getStadiumTypes();
    }

    @Test
    @DisplayName("管理员获取场馆类型时，若HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    void shouldGetStadiumTypesWithCorrectToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/stadium/types")
                .param("adminId", "2")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(stadiumService, times(1))
                .getStadiumTypes();
    }
}