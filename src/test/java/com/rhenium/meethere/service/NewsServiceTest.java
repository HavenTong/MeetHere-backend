package com.rhenium.meethere.service;

import com.rhenium.meethere.dao.NewsDao;
import com.rhenium.meethere.domain.News;
import com.rhenium.meethere.dto.PublicRequest;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.impl.NewsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author HavenTong
 * @date 2019/12/17 4:25 下午
 */
@SpringBootTest
class NewsServiceTest {

    @Mock
    private NewsDao newsDao;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Test
    @DisplayName("获取新闻列表时，当offset不是正整数，抛出异常")
    void shouldThrowExceptionWhenOffsetLessThanOne(){
        PublicRequest publicRequest = PublicRequest.builder()
                .userId(6).build();
        Throwable exception = assertThrows(MyException.class,
                () -> newsService.listNewsItems(0, 3));
        assertEquals("页数必须为正整数", exception.getMessage());
    }

    @Test
    @DisplayName("获取新闻列表时，当limit不是正整数，抛出异常")
    void shouldThrowExceptionWhenLimitLessThanOne(){
        PublicRequest publicRequest = PublicRequest.builder()
                .userId(6).build();
        Throwable exception = assertThrows(MyException.class,
                () -> newsService.listNewsItems(1, 0));
        assertEquals("每页条目数必须为正整数", exception.getMessage());
    }

    @Test
    @DisplayName("获取新闻列表时，当参数符合要求时，查找正确")
    void shouldGetCorrectNewsList(){
        PublicRequest publicRequest = PublicRequest.builder()
                .userId(6).build();
        ArgumentCaptor<Integer> offsetCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> limitCaptor = ArgumentCaptor.forClass(Integer.class);
        List<News> list =  newsService.listNewsItems(1,3);
        verify(newsDao, times(1))
                .findNewsByOffsetAndLimit(offsetCaptor.capture(), limitCaptor.capture());
        assertAll(
                () -> assertEquals(0, offsetCaptor.getValue()),
                () -> assertEquals(3, limitCaptor.getValue())
        );
    }

}