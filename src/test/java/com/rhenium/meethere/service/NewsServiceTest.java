package com.rhenium.meethere.service;

import com.rhenium.meethere.dao.AdminDao;
import com.rhenium.meethere.dao.NewsDao;
import com.rhenium.meethere.domain.Admin;
import com.rhenium.meethere.domain.News;
import com.rhenium.meethere.dto.NewsRequest;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.impl.NewsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.DisabledIf;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author HavenTong
 * @date 2019/12/17 4:25 下午
 */
@SpringBootTest
class NewsServiceTest {

    @Mock
    private NewsDao newsDao;

    @Mock
    private AdminDao adminDao;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Test
    @DisplayName("获取新闻数量时，获取结果正确")
    void shouldGetCorrectNewsCount() {
        when(newsDao.getNewsCount()).thenReturn(3);
        Map<String, String> result = newsService.getNewsCount();
        verify(newsDao, times(1))
                .getNewsCount();
        assertEquals("3", result.get("count"));
    }

    @Test
    @DisplayName("获取新闻列表时，当offset不是正整数，抛出异常")
    void shouldThrowExceptionWhenOffsetLessThanOne(){
        Throwable exception = assertThrows(MyException.class,
                () -> newsService.listNewsItems(-1, 3));
        assertEquals("偏移数据条目数必须为正整数", exception.getMessage());
    }

    @Test
    @DisplayName("获取新闻列表时，当limit不是正整数，抛出异常")
    void shouldThrowExceptionWhenLimitLessThanOne(){
        Throwable exception = assertThrows(MyException.class,
                () -> newsService.listNewsItems(1, 0));
        assertEquals("数据条目数必须为正整数", exception.getMessage());
    }

    @Test
    @DisplayName("获取新闻列表时，当参数符合要求时，查找正确")
    void shouldGetCorrectNewsList(){
        ArgumentCaptor<Integer> offsetCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> limitCaptor = ArgumentCaptor.forClass(Integer.class);
        List<News> list =  newsService.listNewsItems(0,3);
        verify(newsDao, times(1))
                .findNewsByOffsetAndLimit(offsetCaptor.capture(), limitCaptor.capture());
        assertAll(
                () -> assertEquals(0, offsetCaptor.getValue()),
                () -> assertEquals(3, limitCaptor.getValue())
        );
    }

    @Test
    @DisplayName("发布新闻时，发布信息正确")
    void shouldPostCorrectNews(){
        when(adminDao.findAdminById(anyInt())).thenReturn(new Admin());
        NewsRequest newsRequest = NewsRequest.builder()
                .newsTitle("title").newsContent("content").adminId(1).build();
        ArgumentCaptor<NewsRequest> newsCaptor = ArgumentCaptor.forClass(NewsRequest.class);
        newsService.createNews(newsRequest);
        verify(newsDao, times(1))
                .createNews(newsCaptor.capture());
        assertAll(
                () -> assertEquals(1, newsCaptor.getValue().getAdminId()),
                () -> assertEquals("title", newsCaptor.getValue().getNewsTitle()),
                () -> assertEquals("content", newsCaptor.getValue().getNewsContent())
        );
    }

    @Test
    @DisplayName("发布新闻时，若管理员不存在，抛出异常")
    void shouldThrowExceptionWhenPostingWithAdminNotExist(){
        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(1).build();
        Throwable exception = assertThrows(MyException.class,
                () -> newsService.createNews(newsRequest));
        assertEquals("管理员不存在", exception.getMessage());
    }

    @Test
    @DisplayName("修改新闻时，修改信息正确")
    void shouldUpdateCorrectNews(){
        when(adminDao.findAdminById(anyInt())).thenReturn(new Admin());
        NewsRequest newsRequest = NewsRequest.builder()
                .newsContent("update content")
                .newsTitle("update title")
                .newsId(1)
                .adminId(1)
                .build();
        ArgumentCaptor<NewsRequest> newsCaptor = ArgumentCaptor.forClass(NewsRequest.class);
        newsService.updateNews(newsRequest);
        verify(newsDao, times(1))
                .updateNews(newsCaptor.capture());
        assertAll(
                () -> assertEquals(1, newsCaptor.getValue().getNewsId()),
                () -> assertEquals("update title", newsCaptor.getValue().getNewsTitle()),
                () -> assertEquals("update content", newsCaptor.getValue().getNewsContent())
        );
    }

    @Test
    @DisplayName("修改新闻时，若管理员不存在，抛出异常")
    void shouldThrowExceptionWhenUpdatingWithAdminNotExist(){
        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(1).build();
        Throwable exception = assertThrows(MyException.class,
                () -> newsService.updateNews(newsRequest));
        assertEquals("管理员不存在", exception.getMessage());
    }

    @Test
    @DisplayName("删除新闻时，若管理员不存在，抛出异常")
    void shouldThrowExceptionWhenDeletingWithAdminNotExist(){
        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(1).build();
        Throwable exception = assertThrows(MyException.class,
                () -> newsService.deleteNews(newsRequest));
        assertEquals("管理员不存在", exception.getMessage());
    }

    @Test
    @DisplayName("删除新闻时，删除正确的新闻")
    void shouldDeleteCorrectNews(){
        when(adminDao.findAdminById(anyInt())).thenReturn(new Admin());
        NewsRequest newsRequest = NewsRequest.builder()
                .newsId(1)
                .adminId(1)
                .build();
        ArgumentCaptor<NewsRequest> newsCaptor = ArgumentCaptor.forClass(NewsRequest.class);
        newsService.deleteNews(newsRequest);
        verify(newsDao, times(1))
                .deleteNews(newsCaptor.capture());
        assertAll(
                () -> assertEquals(1, newsCaptor.getValue().getNewsId())
        );
    }
}