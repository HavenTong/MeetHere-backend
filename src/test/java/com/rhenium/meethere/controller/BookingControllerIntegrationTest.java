package com.rhenium.meethere.controller;

import com.rhenium.meethere.dao.BookingDao;
import com.rhenium.meethere.domain.Booking;
import com.rhenium.meethere.dto.BookingRequest;
import com.rhenium.meethere.dto.CustomerRequest;
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

import java.awt.print.Book;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    BookingDao bookingDao;

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
        HashMap<String, String> request = new HashMap<>();
        request.put("stadiumId", "1");
        request.put("daysAfterToday", "1");
        request.put("customerId", "506");
        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-empty-time", request);
        ResponseEntity<ResultEntity> response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ResultEntity.class);
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
        HashMap<String, String> request = new HashMap<>();
        request.put("stadiumId", "1");
        request.put("daysAfterToday", "1");
        request.put("customerId", "507");
        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-empty-time", request);
        ResponseEntity<ResultEntity> response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }

    @Test
    @DisplayName("添加订单时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenAddNewBookingWithoutToken() {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(507).stadiumId(1).daysAfterToday(2).startTime(10).endTime(10).build();

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/add-new-booking", bookingRequest, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("添加订单时，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenAddNewBookingWithWrongToken() {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(506).stadiumId(1).daysAfterToday(2).startTime(10).endTime(10).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<BookingRequest> httpEntity = new HttpEntity<>(bookingRequest, httpHeaders);

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/add-new-booking", httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("添加订单时，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldAddNewBookingWithCorrectToken() {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(507).stadiumId(1).daysAfterToday(2).startTime(10).endTime(14).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<BookingRequest> httpEntity = new HttpEntity<>(bookingRequest, httpHeaders);

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/add-new-booking", httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        Booking booking = bookingDao.getLatestBooking();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage()),
                () -> assertEquals(507, booking.getCustomerId()),
                () -> assertEquals(1, booking.getStadiumId())
        );

        if(result.getCode() == 0) {
            bookingDao.deleteLatestBooking();
        }
    }

    @Test
    @DisplayName("用户删除订单时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenDeleteBookingByCustomerWithoutToken() {

        BookingRequest bookingRequest = BookingRequest.builder().customerId(507).bookingId(10000).build();
        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/delete-by-customer", bookingRequest, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户删除订单时，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenDeleteBookingByCustomerWithWrongToken() {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(506).bookingId(10000).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<BookingRequest> entity = new HttpEntity<>(bookingRequest, httpHeaders);

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/delete-by-customer", entity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户删除订单时，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldDeleteBookingByCustomerWithCorrectToken() {
        Booking booking = Booking.builder().customerId(507).stadiumId(1).startTime(LocalDateTime.now()).endTime(LocalDateTime.now()).paid(false).priceSum(BigDecimal.valueOf(0)).build();
        bookingDao.addNewBooking(booking);
        Integer bookingId = booking.getBookingId();
        BookingRequest bookingRequest = BookingRequest.builder().customerId(507).bookingId(bookingId).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<BookingRequest> entity = new HttpEntity<>(bookingRequest, httpHeaders);

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/delete-by-customer", entity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户更新订单时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenUpdateBookingWithoutToken() {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(507).stadiumId(1).daysAfterToday(2).startTime(10).endTime(14).build();
        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/update-booking", bookingRequest, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户更新订单时，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenUpdateBookingWithWrongToken() {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(506).stadiumId(1).daysAfterToday(2).startTime(10).endTime(14).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<BookingRequest> entity = new HttpEntity<>(bookingRequest, httpHeaders);

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/update-booking", entity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户更新订单时，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldUpdateBookingWithCorrectToken() {
        Booking booking = Booking.builder().customerId(507).stadiumId(1).startTime(LocalDateTime.now()).endTime(LocalDateTime.now()).paid(false).priceSum(BigDecimal.valueOf(0)).build();
        bookingDao.addNewBooking(booking);
        Integer bookingId = booking.getBookingId();
        BookingRequest bookingRequest = BookingRequest.builder().customerId(507).stadiumId(1).daysAfterToday(2).startTime(10).endTime(14).bookingId(bookingId).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<BookingRequest> entity = new HttpEntity<>(bookingRequest, httpHeaders);

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/update-booking", entity, ResultEntity.class);
        ResultEntity result = response.getBody();

        Booking latestBooking = bookingDao.getLatestBooking();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage()),
                () -> assertEquals(507, latestBooking.getCustomerId()),
                () -> assertEquals(1, latestBooking.getStadiumId())
        );

        if(result.getCode() == 0) {
            bookingDao.deleteLatestBooking();
        }
    }

    @Test
    @DisplayName("通过用户ID获取订单，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetBookingByCustomerIdWithoutToken() {
        HashMap<String, String> request = new HashMap<>();
        request.put("offset", "1");
        request.put("limit", "1");
        request.put("customerId", "507");

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/items", request);

        ResponseEntity<ResultEntity> response = testRestTemplate.getForEntity(url, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("通过用户ID获取订单，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetBookingByCustomerIdWithWrongToken() {
        HashMap<String, String> request = new HashMap<>();
        request.put("offset", "1");
        request.put("limit", "1");
        request.put("customerId", "506");
        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/items", request);
        ResponseEntity<ResultEntity> response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("通过用户ID获取订单，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldGetBookingByCustomerIdWithCorrectToken() {
        Integer timeHour = LocalDateTime.now().getHour();
        Booking booking = Booking.builder().customerId(507).stadiumId(1).startTime(LocalDateTime.now()).endTime(LocalDateTime.now()).paid(false).priceSum(BigDecimal.valueOf(0)).build();
        bookingDao.addNewBooking(booking);
        Integer bookingId = booking.getBookingId();
        HashMap<String, String> request = new HashMap<>();
        request.put("offset", "0");
        request.put("limit", "1");
        request.put("customerId", "507");
        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/items", request);
        ResponseEntity<ResultEntity> response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        List<Map<String, String>> resultList = (List<Map<String, String>>) result.getData();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage()),
                () -> assertEquals(bookingId, resultList.get(0).get("bookingId")),
                () -> assertEquals("中北网球场", resultList.get(0).get("stadiumName")),
                () -> assertEquals(0.0, resultList.get(0).get("priceSum"))
        );
        if(result.getCode() == 0) {
            bookingDao.deleteLatestBooking();
        }
    }

    //===========================================================================================
    @Test
    @DisplayName("用户获取订单数目，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetBookingCountForCustomerWithoutToken() {
        HashMap<String, String> request = new HashMap<>();
        request.put("customerId", "507");

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/count-for-customer", request);

        ResponseEntity<ResultEntity> response = testRestTemplate.getForEntity(url, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("通过用户ID获取订单数目，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetBookingCountForCustomerWithWrongToken() {
        HashMap<String, String> request = new HashMap<>();
        request.put("customerId", "506");
        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/count-for-customer", request);
        ResponseEntity<ResultEntity> response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("通过用户ID获取订单数目，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldGetBookingCountForCustomerWithCorrectToken() {
        HashMap<String, String> request = new HashMap<>();
        request.put("customerId", "507");
        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/count-for-customer", request);
        ResponseEntity<ResultEntity> response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }
}
