package com.rhenium.meethere.controller;

import com.alibaba.fastjson.JSON;
import com.rhenium.meethere.dto.CustomerRequest;
import com.rhenium.meethere.dto.NewsRequest;
import com.rhenium.meethere.service.NewsService;
import com.rhenium.meethere.service.impl.NewsServiceImpl;
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
 * @date 2019/12/26 9:40 下午
 */
@SpringBootTest
@AutoConfigureMockMvc
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsServiceImpl newsService;

    @Test
    @DisplayName("获取新闻个数时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetNewsCountWithoutToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/news/get-news-count")
                .param("userId", "1"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(newsService, never())
                .getNewsCount();
    }

    @Test
    @DisplayName("获取新闻个数时，若HTTP头部携带的TOKEN与userId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetNewsCountWithWrongToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/news/get-news-count")
                .param("userId", "1")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(newsService, never())
                .getNewsCount();
    }

    @Test
    @DisplayName("获取新闻个数时，若HTTP头部携带的TOKEN与userId匹配，返回正常结果")
    void shouldGetNewsCountWithCorrectToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/news/get-news-count")
                .param("userId", "502")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(newsService, times(1))
                .getNewsCount();
    }

    @Test
    @DisplayName("获取新闻列表时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetNewsListWithoutToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/news/get-news-list")
                .param("offset", "0")
                .param("limit", "20")
                .param("userId", "1"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(newsService, never())
                .listNewsItems(0,20);
    }

    @Test
    @DisplayName("获取新闻列表时，若HTTP头部携带的TOKEN与userId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetNewsListWithWrongToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/news/get-news-list")
                .param("offset", "0")
                .param("limit", "20")
                .param("userId", "1")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(newsService, never())
                .listNewsItems(0,20);
    }

    @Test
    @DisplayName("获取新闻列表时，若HTTP头部携带的TOKEN与userId匹配，返回正常结果")
    void shouldGetNewsListWithCorrectToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/news/get-news-list")
                .param("offset", "0")
                .param("limit", "20")
                .param("userId", "502")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(newsService, times(1))
                .listNewsItems(0,20);
    }

    @Test
    @DisplayName("发布新闻时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenPostNewsWithoutToken() throws Exception {
        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(2).build();
        ResultActions perform = mockMvc.
                perform(post("/news/post").
                        contentType(MediaType.APPLICATION_JSON).
                        content(JSON.toJSONString(newsRequest)));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(newsService, never())
                .createNews(newsRequest);
    }

    @Test
    @DisplayName("发布新闻时，若HTTP头部携带的TOKEN与adminId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenPostNewsWithWrongToken() throws Exception {
        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(1).build();
        ResultActions perform = mockMvc.
                perform(post("/news/post").
                        contentType(MediaType.APPLICATION_JSON).
                        content(JSON.toJSONString(newsRequest)).
                        header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(newsService, never())
                .createNews(newsRequest);
    }

    @Test
    @DisplayName("发布新闻时，若HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    void shouldPostNewsWithCorrectToken() throws Exception {
        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(2).build();
        ResultActions perform = mockMvc.
                perform(post("/news/post").
                        contentType(MediaType.APPLICATION_JSON).
                        content(JSON.toJSONString(newsRequest)).
                        header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(newsService, times(1))
                .createNews(newsRequest);
    }

    @Test
    @DisplayName("修改新闻时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenUpdateNewsWithoutToken() throws Exception {
        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(2).build();
        ResultActions perform = mockMvc.
                perform(post("/news/update").
                        contentType(MediaType.APPLICATION_JSON).
                        content(JSON.toJSONString(newsRequest)));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(newsService, never())
                .updateNews(newsRequest);
    }

    @Test
    @DisplayName("修改新闻时，若HTTP头部携带的TOKEN与adminId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenUpdateNewsWithWrongToken() throws Exception {
        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(1).build();
        ResultActions perform = mockMvc.
                perform(post("/news/update").
                        contentType(MediaType.APPLICATION_JSON).
                        content(JSON.toJSONString(newsRequest)).
                        header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(newsService, never())
                .updateNews(newsRequest);
    }

    @Test
    @DisplayName("修改新闻时，若HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    void shouldUpdateNewsWithCorrectToken() throws Exception {
        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(2).build();
        ResultActions perform = mockMvc.
                perform(post("/news/update").
                        contentType(MediaType.APPLICATION_JSON).
                        content(JSON.toJSONString(newsRequest)).
                        header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(newsService, times(1))
                .updateNews(newsRequest);
    }

    @Test
    @DisplayName("删除新闻时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenDeleteNewsWithoutToken() throws Exception {
        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(2).build();
        ResultActions perform = mockMvc.
                perform(post("/news/delete").
                        contentType(MediaType.APPLICATION_JSON).
                        content(JSON.toJSONString(newsRequest)));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(newsService, never())
                .deleteNews(newsRequest);
    }

    @Test
    @DisplayName("删除新闻时，若HTTP头部携带的TOKEN与adminId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenDeleteNewsWithWrongToken() throws Exception {
        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(1).build();
        ResultActions perform = mockMvc.
                perform(post("/news/delete").
                        contentType(MediaType.APPLICATION_JSON).
                        content(JSON.toJSONString(newsRequest)).
                        header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(newsService, never())
                .deleteNews(newsRequest);
    }

    @Test
    @DisplayName("删除新闻时，若HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    void shouldDeleteNewsWithCorrectToken() throws Exception {
        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(2).build();
        ResultActions perform = mockMvc.
                perform(post("/news/delete").
                        contentType(MediaType.APPLICATION_JSON).
                        content(JSON.toJSONString(newsRequest)).
                        header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(newsService, times(1))
                .deleteNews(newsRequest);
    }
}