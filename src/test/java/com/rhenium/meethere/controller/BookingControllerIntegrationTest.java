package com.rhenium.meethere.controller;

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
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/30 17:08
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingControllerIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private String BASE_URL = "/booking";

    @Test
    @DisplayName("获取场馆空闲时间时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetEmptyTimeByStadiumAndDateWithoutToken() {
        HashMap<String, String> request = new HashMap<>();
        request.put("stadiumId", "1");
        request.put("daysAfterToday", "1");
        request.put("customerId", "507");

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-empty-time", request);

        ResponseEntity<ResultEntity> response = testRestTemplate.getForEntity(url, ResultEntity.class);
        ResultEntity result = response.getBody();


        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("获取场馆空闲时间，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetEmptyTimesWithWrongToken() {
        HashMap<String, Object> request = new HashMap<>();
        request.put("stadiumId", 1);
        request.put("daysAfterToday", 1);
        request.put("customerId", 506);
        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.exchange(BASE_URL + "/get-empty-time?stadiumId={stadiumId}&daysAfterToday={daysAfterToday}&customerId={customerId}", HttpMethod.GET, httpEntity, ResultEntity.class, request);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("获取场馆空闲时间时，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldGetEmptyTimeByStadiumWithCorrectToken() {
        HashMap<String, Object> request = new HashMap<>();
        request.put("stadiumId", 1);
        request.put("daysAfterToday", 1);
        request.put("customerId", 507);
        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.exchange(BASE_URL + "/get-empty-time?stadiumId={stadiumId}&daysAfterToday={daysAfterToday}&customerId={customerId}", HttpMethod.GET, httpEntity, ResultEntity.class, request);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode())
        );
    }

}
