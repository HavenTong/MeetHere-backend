package com.rhenium.meethere.controller;

import com.rhenium.meethere.dao.AdminDao;
import com.rhenium.meethere.dao.BookingDao;
import com.rhenium.meethere.dao.CustomerDao;
import com.rhenium.meethere.domain.Admin;
import com.rhenium.meethere.domain.Booking;
import com.rhenium.meethere.domain.Customer;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.dto.CustomerRequest;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.util.HttpRequestUtil;
import com.rhenium.meethere.vo.ResultEntity;
import com.sun.mail.iap.Response;
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

import javax.servlet.http.HttpUtils;
import java.awt.print.Book;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author HavenTong
 * @date 2019/12/30 8:11 下午
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private BookingDao bookingDao;

    private String BASE_URL =  "/admin";

    @Test
    @DisplayName("管理员获取用户数量时，若头部未携带TOKEN，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingUserCountWithoutToken(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .getForEntity(BASE_URL + "/get-user-count?adminId={adminId}", ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取用户数量时，若TOKEN不匹配，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingUserCountWithWrongToken(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 2);

        // header
        HttpHeaders headers = new HttpHeaders();
        // token for id = 1
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/get-user-count?adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取正确的用户数量(需要视当前用户数量而定)")
    void shouldGetCorrectUserCount(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);

        // header
        HttpHeaders headers = new HttpHeaders();
        // token for id = 1
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/get-user-count?adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();
        Map<String, Object> resultMap =(Map<String, Object>) result.getData();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取用户列表时，若头部未携带TOKEN，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingUserListWithoutToken(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);
        map.put("offset", 0);
        map.put("limit", 2);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .getForEntity(BASE_URL + "/get-user-list?offset={offset}&limit={limit}&adminId={adminId}", ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取用户列表时，若TOKEN不匹配，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingUserListWithWrongToken(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 2);
        map.put("offset", 0);
        map.put("limit", 2);

        // header
        HttpHeaders headers = new HttpHeaders();
        // token for id = 1
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/get-user-list?offset={offset}&limit={limit}&adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取用户列表时，若offset<0，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingUserListWithOffsetLessThanZero(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);
        map.put("offset", -1);
        map.put("limit", 2);

        // header
        HttpHeaders headers = new HttpHeaders();
        // token for id = 1
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/get-user-list?offset={offset}&limit={limit}&adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("偏移数据条目数必须为正整数", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取用户列表时，若limit<1，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingUserListWithLimitLessThanOne(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);
        map.put("offset", 0);
        map.put("limit", 0);

        // header
        HttpHeaders headers = new HttpHeaders();
        // token for id = 1
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/get-user-list?offset={offset}&limit={limit}&adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("数据条目数必须为正整数", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取正确的用户列表")
    void shouldGetCorrectUserList(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);
        map.put("offset", 0);
        map.put("limit", 2);

        // header
        HttpHeaders headers = new HttpHeaders();
        // token for id = 1
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/get-user-list?offset={offset}&limit={limit}&adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();
        List<Map<String, String>> resultList = (List<Map<String, String>>) result.getData();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage()),
                () -> assertEquals("502", resultList.get(0).get("customerId")),
                () -> assertEquals("705276106@qq.com", resultList.get(0).get("email")),
                () -> assertEquals("JJAYCHEN", resultList.get(0).get("userName")),
                () -> assertEquals("2020-01-01 20:53:16", resultList.get(0).get("registeredTime")),
                () -> assertEquals("空", resultList.get(0).get("phoneNumber")),
                () -> assertEquals("503", resultList.get(1).get("customerId")),
                () -> assertEquals("10175101148@stu.ecnu.edu.cn", resultList.get(1).get("email")),
                () -> assertEquals("2号选手", resultList.get(1).get("userName")),
                () -> assertEquals("2019-12-27 09:19:55", resultList.get(1).get("registeredTime")),
                () -> assertEquals("空", resultList.get(1).get("phoneNumber"))
        );
    }


    @Test
    @DisplayName("管理员删除用户时，若未携带TOKEN，则返回错误信息")
    void shouldGetExceptionMessageWhenDeletingUserWithoutToken(){
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(1).customerId(501).build();
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/delete-user", adminRequest, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员删除用户时，若TOKEN不匹配，则返回错误信息")
    void shouldGetExceptionMessageWhenDeletingUserWithTokenNotMatch(){
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(2).customerId(501).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<AdminRequest> entity = new HttpEntity<>(adminRequest, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/delete-user", entity, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员删除正确的用户")
    void shouldDeleteCorrectUser(){
        // 新增一个用户作为删除的用户
        Customer customer = Customer.builder()
                .userName("temp").email("temp@ecnu.com").password("123456").build();
        customerDao.saveNewCustomer(customer);
        int customerId = customer.getCustomerId();
        log.info("customerId: {}", customerId);

        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(1).customerId(customerId).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<AdminRequest> entity = new HttpEntity<>(adminRequest, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/delete-user", entity, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }

    // TODO: not finished
    @Test
    @DisplayName("管理员获取订单数量时，若头部未携带TOKEN，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingBookingCountWithoutToken(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .getForEntity(BASE_URL + "/get-booking-count?adminId={adminId}", ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取订单数量时，若TOKEN不匹配，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingBookingCountWithTokenNotMatch(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 2);

        HttpHeaders headers = new HttpHeaders();
        // token for id = 1
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/get-booking-count?adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取订单数量正确的订单数量(根据当前订单数决定)")
    void shouldGetCorrectBookingCount(){

        int count = bookingDao.getAllBookingCount();
        String countString = String.valueOf(count);

        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);

        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/get-booking-count?adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();
        Map<String, String> resultMap = (Map<String, String>) result.getData();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage()),
                () -> assertEquals(countString, resultMap.get("count"))
        );
    }

    @Test
    @DisplayName("管理员获取用户列表时，若头部未携带TOKEN，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingBookingListWithoutToken(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);
        map.put("offset", 0);
        map.put("limit", 2);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .getForEntity(BASE_URL + "/get-booking-list?offset={offset}&limit={limit}&adminId={adminId}", ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取用户列表时，若TOKEN不匹配，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingBookingListWithWrongToken(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 2);
        map.put("offset", 0);
        map.put("limit", 2);

        // header
        HttpHeaders headers = new HttpHeaders();
        // token for id = 1
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/get-booking-list?offset={offset}&limit={limit}&adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取订单列表时，若offset<0，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingBookingListWithOffsetLessThanZero(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);
        map.put("offset", -1);
        map.put("limit", 2);

        // header
        HttpHeaders headers = new HttpHeaders();
        // token for id = 1
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/get-booking-list?offset={offset}&limit={limit}&adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("偏移数据条目数必须为正整数", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取用户列表时，若limit<1，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingBookingListWithLimitLessThanOne(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);
        map.put("offset", 0);
        map.put("limit", 0);

        // header
        HttpHeaders headers = new HttpHeaders();
        // token for id = 1
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/get-booking-list?offset={offset}&limit={limit}&adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("数据条目数必须为正整数", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取正确的订单列表")
    void shouldGetCorrectBookingList(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);
        map.put("offset", 0);
        map.put("limit", 2);

        // header
        HttpHeaders headers = new HttpHeaders();
        // token for id = 1
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/get-booking-list?offset={offset}&limit={limit}&adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }


    @Test
    @DisplayName("管理员删除订单时，若未携带TOKEN，则返回错误信息")
    void shouldGetExceptionMessageWhenDeletingBookingWithoutToken(){
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(1).bookingId(12).build();
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/delete-booking", adminRequest, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员删除订单时，若TOKEN不匹配，则返回错误信息")
    void shouldGetExceptionMessageWhenDeletingBookingWithTokenNotMatch(){
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(2).bookingId(12).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<AdminRequest> entity = new HttpEntity<>(adminRequest, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/delete-user", entity, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员删除正确的订单")
    void shouldDeleteCorrectBooking(){
        // 新增一个订单作为删除的订单
        Booking booking = Booking.builder()
                .customerId(502).stadiumId(1).priceSum(BigDecimal.valueOf(200))
                .startTime(LocalDateTime.of(2019, 12, 25, 15, 0 , 0))
                .endTime(LocalDateTime.of(2019, 12, 25, 18, 0, 0))
                .paid(true)
                .build();
        bookingDao.addNewBooking(booking);
        int bookingId = booking.getBookingId();
        log.info("booking: {}", bookingId);
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(1).bookingId(bookingId).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<AdminRequest> entity = new HttpEntity<>(adminRequest, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/delete-booking", entity, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员登录密码不正确，返回错误信息")
    void shouldGetExceptionMessageWhenLoginWithWrongPassword(){
        AdminRequest adminRequest = AdminRequest.builder()
                .email("852092786@qq.com")
                .password("wrong-password")
                .build();
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/login", adminRequest, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("密码错误", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员登录密码正确，返回正确登录信息")
    void shouldGetCorrectLoginInfoWhenPasswordIsCorrect(){
        AdminRequest adminRequest = AdminRequest.builder()
                .email("852092786@qq.com")
                .password("123456")
                .build();
        ResponseEntity<ResultEntity> response = testRestTemplate
                .postForEntity(BASE_URL + "/login", adminRequest, ResultEntity.class);
        ResultEntity result = response.getBody();
        Map<String, Object> map =(Map<String, Object>) result.getData();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertNotNull(map.get("token")),
                () -> assertEquals("852092786@qq.com", map.get("email")),
                () -> assertEquals("1", map.get("adminId")),
                () -> assertEquals("root", map.get("adminName")),
                () -> assertTrue(map.containsKey("phoneNumber"))
        );
    }

    @Test
    @DisplayName("管理员获取某日订单数时，若头部未携带TOKEN，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingBookingCountInADayWithoutToken(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);
        map.put("date", "2019-12-26");
        ResponseEntity<ResultEntity> response = testRestTemplate
                .getForEntity(BASE_URL + "/booking-count-by-date?date={date}&adminId={adminId}", ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取某天订单数量时，若TOKEN不匹配，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingBookingCountInADayWithTokenNotMatch(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 2);
        map.put("date", "2019-12-26");
        HttpHeaders headers = new HttpHeaders();
        // token for id = 1
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/booking-count-by-date?date={date}&adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取正确的某天订单数量(根据数据库中的真实数据填充断言期望值)")
    void shouldGetCorrectBookingCountInADay(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);
        map.put("date", "2019-12-26");
        HttpHeaders headers = new HttpHeaders();
        // token for id = 1
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/booking-count-by-date?date={date}&adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取所有场馆及其订单数时，若头部未携带TOKEN，则返回错误信息")
    void shouldGetExceptionMessageWhenAdminGettingBookingCountByStadiumWithoutToken(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .getForEntity(BASE_URL + "/booking-count-by-stadium?adminId={adminId}", ResultEntity.class, map);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员正确获取所有场馆及其订单数(断言中的数据需要根据数据库中实际情况设置)")
    void shouldGetExceptionMessageWhenAdminGettingBookingCountByStadiumWithTokenNotMatch(){
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", 1);

        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ResultEntity> response = testRestTemplate
                .exchange(BASE_URL + "/booking-count-by-stadium?adminId={adminId}", HttpMethod.GET, entity, ResultEntity.class, map);
        ResultEntity result = response.getBody();
        Map<String, Object> resultMap = (Map<String, Object>) result.getData();
        List<String> stadiumNames = (List<String>) resultMap.get("stadiums");
        List<Integer> count = (List<Integer>) resultMap.get("count");
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }
}
