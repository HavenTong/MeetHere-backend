package com.rhenium.meethere.controller;

import com.alibaba.fastjson.JSON;
import com.rhenium.meethere.dto.CustomerRequest;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
    void shouldReturnWrongMessageWhenGetNewsCountWithoutToken() throws Exception{
        ResultActions perform = mockMvc.perform(get("/news/get-news-count")
                .param("userId", "1"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(newsService, never())
                .getNewsCount();
    }

    @Test
    void listNewsItems() {
    }

    @Test
    void postNews() {
    }

    @Test
    void updateNews() {
    }

    @Test
    void deleteNews() {
    }
}