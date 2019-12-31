package com.rhenium.meethere.controller;

import com.rhenium.meethere.dao.NewsDao;
import com.rhenium.meethere.dto.NewsRequest;
import com.rhenium.meethere.util.HttpRequestUtil;
import com.rhenium.meethere.vo.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author JJAYCHEN
 * @date 2019/12/31 3:09 下午
 */


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewsControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private NewsDao newsDao;

    private String BASE_URL = "/news";

    @Test
    @DisplayName("获取新闻个数时，若HTTP头部携带的TOKEN与userId匹配，返回正常结果")
    void shouldGetNewsCountWithCorrectToken() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "621");
        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-news-count", map);

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MjEiLCJpYXQiOjE1Nzc2OTU2NjIsImV4cCI6MTU3OTc2OTI2Mn0.moSEFHCMWRLNZSLhYK9IZG_zPGTNMMDv3DrUI4eD_K4");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                exchange(url, HttpMethod.GET, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }

    @Test
    @DisplayName("获取新闻列表时，若HTTP头部携带的TOKEN与userId匹配，返回正常结果")
    void shouldGetNewsListWithCorrectToken() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "621");
        map.put("offset", "0");
        map.put("limit", "20");
        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-news-list", map);

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MjEiLCJpYXQiOjE1Nzc2OTU2NjIsImV4cCI6MTU3OTc2OTI2Mn0.moSEFHCMWRLNZSLhYK9IZG_zPGTNMMDv3DrUI4eD_K4");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                exchange(url, HttpMethod.GET, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }

    @Test
    @DisplayName("发布新闻时，若HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    void shouldPostNewsWithCorrectToken() throws Exception {
        String url = BASE_URL + "/post";

        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(2)
                .newsTitle("测试新闻标题")
                .newsContent("测试新闻内容")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo");
        HttpEntity<NewsRequest> requestEntity = new HttpEntity<>(newsRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                postForEntity(url, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );

        int[] Ids = newsDao.getNewsIdByNewsTitle("测试新闻标题");
        assertEquals(1, Ids.length);

        for (int i : Ids) {
            newsDao.deleteNewsByNewsId(i);
        }
    }

    @Test
    @DisplayName("修改新闻时，若HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    void shouldUpdateNewsWithCorrectToken() throws Exception {
        String url = BASE_URL + "/update";

        /** 准备测试环境 */
        int[] Ids = newsDao.getNewsIdByNewsTitle("测试新闻标题");
        assertEquals(0, Ids.length);

        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(2)
                .newsTitle("测试新闻标题")
                .newsContent("测试新闻内容")
                .build();

        newsDao.createNews(newsRequest);
        /** 测试环境准备完毕 */

        Ids = newsDao.getNewsIdByNewsTitle("测试新闻标题");
        assertEquals(1, Ids.length);

        newsRequest = NewsRequest.builder()
                .adminId(2)
                .newsId(Ids[0])
                .newsTitle("测试新闻标题1")
                .newsContent("测试新闻内容1")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo");
        HttpEntity<NewsRequest> requestEntity = new HttpEntity<>(newsRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                postForEntity(url, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );

        for (int i : Ids) {
            newsDao.deleteNewsByNewsId(i);
        }
    }

    @Test
    @DisplayName("删除新闻时，若HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    void shouldDeleteNewsWithCorrectToken() throws Exception {
        String url = BASE_URL + "/delete";

        /** 准备测试环境 */
        int[] Ids = newsDao.getNewsIdByNewsTitle("测试新闻标题");
        assertEquals(0, Ids.length);

        NewsRequest newsRequest = NewsRequest.builder()
                .adminId(2)
                .newsTitle("测试新闻标题")
                .newsContent("测试新闻内容")
                .build();

        newsDao.createNews(newsRequest);
        /** 测试环境准备完毕 */

        Ids = newsDao.getNewsIdByNewsTitle("测试新闻标题");
        assertEquals(1, Ids.length);

        newsRequest = NewsRequest.builder()
                .adminId(2)
                .newsId(Ids[0])
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo");
        HttpEntity<NewsRequest> requestEntity = new HttpEntity<>(newsRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                postForEntity(url, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }
}
