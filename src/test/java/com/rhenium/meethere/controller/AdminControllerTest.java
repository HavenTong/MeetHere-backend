package com.rhenium.meethere.controller;

import com.alibaba.fastjson.JSON;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.service.impl.AdminServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.xml.transform.Result;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author HavenTong
 * @date 2019/12/27 9:25 上午
 */
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminServiceImpl adminService;

    @Test
    @DisplayName("管理员获取用户数量时，若未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingUserCountWithoutToken() throws Exception {
        ResultActions perform = mockMvc
                .perform(get("/admin/get-user-count")
                .param("adminId", "1"));
        verify(adminService, never()).getUserCount();
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("管理员获取用户数量时，若携带TOKEN与adminId不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingUserCountWithWrongToken() throws Exception {
        ResultActions perform = mockMvc
                .perform(get("/admin/get-user-count")
                .param("adminId", "2")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, never())
                .getUserCount();
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }

    @Test
    @DisplayName("管理员获取用户数量时，若TOKEN匹配，返回正确结果")
    void shouldGetCorrectUserCount() throws Exception{
        Map<String, String> countInfo = new HashMap<>();
        countInfo.put("count", "123");
        when(adminService.getUserCount()).thenReturn(countInfo);
        ResultActions perform = mockMvc
                .perform(get("/admin/get-user-count")
                        .param("adminId", "1")
                        .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, times(1))
                .getUserCount();
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value("123"));
    }

    @Test
    @DisplayName("管理员获取用户列表时，若未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingUserListWithoutToken() throws Exception {
        ResultActions perform = mockMvc
                .perform(get("/admin/get-user-list")
                        .param("offset", "1")
                        .param("limit", "5")
                        .param("adminId", "1"));
        verify(adminService, never()).getUserList(anyInt(), anyInt());
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("管理员获取用户列表时，若携带TOKEN与adminId不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingUserListWithWrongToken() throws Exception {
        ResultActions perform = mockMvc
                .perform(get("/admin/get-user-count")
                        .param("offset", "1")
                        .param("limit", "2")
                        .param("adminId", "2")
                        .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, never())
                .getUserList(anyInt(), anyInt());
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }

    @Test
    @DisplayName("管理员获取用户列表时，若TOKEN匹配，返回正确结果")
    void shouldGetCorrectUserList() throws Exception{
        Map<String, String> firstCustomer = new HashMap<>();
        firstCustomer.put("customerId", "501");
        firstCustomer.put("registeredTime", "2019-12-25 12:00:00");
        firstCustomer.put("userName", "test 1st user");
        firstCustomer.put("email", "852092786@qq.com");
        firstCustomer.put("phoneNumber", "空");
        Map<String, String> secondCustomer = new HashMap<>();
        secondCustomer.put("customerId", "502");
        secondCustomer.put("registeredTime", "2019-12-27 13:00:00");
        secondCustomer.put("userName", "test 2nd user");
        secondCustomer.put("email", "852092788@qq.com");
        secondCustomer.put("phoneNumber", "18900001111");
        List<Map<String, String>> userList = new ArrayList<>();
        userList.add(firstCustomer);
        userList.add(secondCustomer);
        when(adminService.getUserList(0, 2)).thenReturn(userList);
        ResultActions perform = mockMvc
                .perform(get("/admin/get-user-list")
                        .param("offset", "0")
                        .param("limit", "2")
                        .param("adminId", "1")
                        .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, times(1))
                .getUserList(0, 2);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].customerId").value("501"))
                .andExpect(jsonPath("$.data[0].registeredTime").value("2019-12-25 12:00:00"))
                .andExpect(jsonPath("$.data[0].userName").value("test 1st user"))
                .andExpect(jsonPath("$.data[0].email").value("852092786@qq.com"))
                .andExpect(jsonPath("$.data[0].phoneNumber").value("空"))
                .andExpect(jsonPath("$.data[1].customerId").value("502"))
                .andExpect(jsonPath("$.data[1].registeredTime").value("2019-12-27 13:00:00"))
                .andExpect(jsonPath("$.data[1].userName").value("test 2nd user"))
                .andExpect(jsonPath("$.data[1].email").value("852092788@qq.com"))
                .andExpect(jsonPath("$.data[1].phoneNumber").value("18900001111"))
                .andExpect(jsonPath("$.data[2]").doesNotExist());
    }

    @Test
    @DisplayName("管理员删除用户时，若未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenDeletingUserWithoutToken() throws Exception{
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(1).customerId(501).build();
        ResultActions perform = mockMvc.perform(post("/admin/delete-user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JSON.toJSONString(adminRequest)));
        verify(adminService, never())
                .deleteUser(adminRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("管理员删除用户时,若携带TOKEN与adminId不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenDeletingUserWithTokenNotMatch() throws Exception {
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(2).customerId(501).build();
        ResultActions perform = mockMvc
                        .perform(post("/admin/delete-user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JSON.toJSONString(adminRequest))
                                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, never())
                .deleteUser(adminRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }

    @Test
    @DisplayName("管理员删除正确的用户")
    void shouldDeleteCorrectUser() throws Exception{
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(1).customerId(501).build();
        ResultActions perform = mockMvc
                .perform(post("/admin/delete-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(adminRequest))
                        .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, times(1))
                .deleteUser(adminRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @DisplayName("管理员获取订单数量时，若未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingBookingCountByAdminWithoutToken() throws Exception {
        ResultActions perform = mockMvc
                .perform(get("/admin/get-booking-count")
                        .param("adminId", "1"));
        verify(adminService, never()).getBookingCount();
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("管理员获取订单数量时，若携带TOKEN与adminId不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingBookingCountByAdminWithWrongToken() throws Exception {
        ResultActions perform = mockMvc
                .perform(get("/admin/get-booking-count")
                        .param("adminId", "2")
                        .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, never()).getBookingCount();
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }

    @Test
    @DisplayName("管理员获取正确的订单数量")
    void shouldGetCorrectBookingCount() throws Exception {
        Map<String, String> countMap = new HashMap<>();
        countMap.put("count", "20");
        when(adminService.getBookingCount()).thenReturn(countMap);
        ResultActions perform = mockMvc
                .perform(get("/admin/get-booking-count")
                        .param("adminId", "1")
                        .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, times(1)).getBookingCount();
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value("20"));
    }

    @Test
    @DisplayName("管理员获取订单列表时，若未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingBookingListWithoutToken() throws Exception {
        ResultActions perform = mockMvc
                .perform(get("/admin/get-booking-list")
                        .param("offset", "1")
                        .param("limit", "5")
                        .param("adminId", "1"));
        verify(adminService, never()).getBookingList(1, 5);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("管理员获取订单列表时,若携带TOKEN与adminId不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingBookingListWithWrongToken() throws Exception {
        ResultActions perform = mockMvc
                .perform(get("/admin/get-booking-list")
                        .param("offset", "1")
                        .param("limit", "5")
                        .param("adminId", "2")
                        .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, never()).getBookingList(1, 5);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }

    @Test
    @DisplayName("管理员获取正确的订单列表")
    void shouldGetCorrectBookingList() throws Exception {
        Map<String, String> firstBooking = new HashMap<>();
        firstBooking.put("bookingId", "1");
        firstBooking.put("startTime", "2019-12-25 08:00:00");
        firstBooking.put("endTime", "2019-12-25 09:00:00");
        firstBooking.put("priceSum", "200");
        firstBooking.put("paid", "true");
        firstBooking.put("customerId", "501");
        firstBooking.put("stadiumId", "1");
        firstBooking.put("customerName", "first user");
        firstBooking.put("stadiumName", "tennis court");
        Map<String, String> secondBooking = new HashMap<>();
        secondBooking.put("bookingId", "2");
        secondBooking.put("startTime", "2019-12-30 18:00:00");
        secondBooking.put("endTime", "2019-12-30 20:00:00");
        secondBooking.put("priceSum", "450");
        secondBooking.put("paid", "false");
        secondBooking.put("customerId", "502");
        secondBooking.put("stadiumId", "2");
        secondBooking.put("customerName", "second user");
        secondBooking.put("stadiumName", "volleyball court");
        List<Map<String, String>> bookingList = new ArrayList<>(Arrays.asList(firstBooking, secondBooking));
        when(adminService.getBookingList(0, 2)).thenReturn(bookingList);
        ResultActions perform = mockMvc
                .perform(get("/admin/get-booking-list")
                        .param("offset", "0")
                        .param("limit", "2")
                        .param("adminId", "1")
                        .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, times(1))
                .getBookingList(0, 2);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].bookingId").value("1"))
                .andExpect(jsonPath("$.data[0].startTime").value("2019-12-25 08:00:00"))
                .andExpect(jsonPath("$.data[0].endTime").value("2019-12-25 09:00:00"))
                .andExpect(jsonPath("$.data[0].priceSum").value("200"))
                .andExpect(jsonPath("$.data[0].paid").value("true"))
                .andExpect(jsonPath("$.data[0].customerId").value("501"))
                .andExpect(jsonPath("$.data[0].stadiumId").value("1"))
                .andExpect(jsonPath("$.data[0].customerName").value("first user"))
                .andExpect(jsonPath("$.data[0].stadiumName").value("tennis court"))
                .andExpect(jsonPath("$.data[1].bookingId").value("2"))
                .andExpect(jsonPath("$.data[1].startTime").value("2019-12-30 18:00:00"))
                .andExpect(jsonPath("$.data[1].endTime").value("2019-12-30 20:00:00"))
                .andExpect(jsonPath("$.data[1].priceSum").value("450"))
                .andExpect(jsonPath("$.data[1].paid").value("false"))
                .andExpect(jsonPath("$.data[1].customerId").value("502"))
                .andExpect(jsonPath("$.data[1].stadiumId").value("2"))
                .andExpect(jsonPath("$.data[1].customerName").value("second user"))
                .andExpect(jsonPath("$.data[1].stadiumName").value("volleyball court"))
                .andExpect(jsonPath("$.data[2]").doesNotExist());
    }

    @Test
    @DisplayName("管理员删除订单时，若未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenDeletingBookingWithoutToken() throws Exception{
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(1).bookingId(1).build();
        ResultActions perform = mockMvc.perform(post("/admin/delete-booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(adminRequest)));
        verify(adminService, never())
                .deleteBooking(adminRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("管理员删除订单时，若携带TOKEN与adminId不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenDeletingBookingWithWrongToken() throws Exception{
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(2).bookingId(1).build();
        ResultActions perform = mockMvc.perform(post("/admin/delete-booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(adminRequest))
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, never())
                .deleteBooking(adminRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }

    @Test
    @DisplayName("管理员删除正确的订单")
    void shouldDeleteCorrectBooking() throws Exception{
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(1).bookingId(1).build();
        ResultActions perform = mockMvc.perform(post("/admin/delete-booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(adminRequest))
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, times(1))
                .deleteBooking(adminRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @DisplayName("管理员登录返回正确的信息")
    void shouldGetCorrectInfoWhenAdminLogin() throws Exception{
        AdminRequest adminRequest = AdminRequest.builder()
                .email("852092786@qq.com").password("123456Thw").build();
        Map<String, String> loginInfo = new HashMap<>();
        loginInfo.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        loginInfo.put("adminId", "1");
        loginInfo.put("email", "852092786@qq.com");
        loginInfo.put("adminName", "first admin");
        loginInfo.put("phoneNumber", "18900001111");
        when(adminService.login(adminRequest)).thenReturn(loginInfo);
        ResultActions perform = mockMvc.perform(post("/admin/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JSON.toJSONString(adminRequest)));
        verify(adminService, times(1))
                .login(adminRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"))
                .andExpect(jsonPath("$.data.adminId").value("1"))
                .andExpect(jsonPath("$.data.email").value("852092786@qq.com"))
                .andExpect(jsonPath("$.data.adminName").value("first admin"))
                .andExpect(jsonPath("$.data.phoneNumber").value("18900001111"));
    }

    @Test
    @DisplayName("管理员获取某天订单数时，若未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingBookingCountByDateWithoutToken() throws Exception{
        ResultActions perform = mockMvc
                .perform(get("/admin/booking-count-by-date")
                        .param("date", "2019-12-25")
                        .param("adminId", "1"));
        verify(adminService, never()).getBookingCountByDate("2019-12-25");
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("管理员获取某天订单数时，若携带TOKEN与adminId不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingBookingCountByDateWithWrongToken() throws Exception{
        ResultActions perform = mockMvc
                .perform(get("/admin/booking-count-by-date")
                        .param("date", "2019-12-25")
                        .param("adminId", "2")
                        .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, never()).getBookingCountByDate("2019-12-25");
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }

    @Test
    @DisplayName("管理员正确获取某天订单数")
    void shouldGetCorrectBookingCountByDate() throws Exception{
        when(adminService.getBookingCountByDate("2019-12-25")).thenReturn(20);
        ResultActions perform = mockMvc
                .perform(get("/admin/booking-count-by-date")
                        .param("date", "2019-12-25")
                        .param("adminId", "1")
                        .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, times(1))
                .getBookingCountByDate("2019-12-25");
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(20));
    }

    @Test
    @DisplayName("管理员获取每个场馆的订单数时，若未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingBookingCountByStadiumWithoutToken() throws Exception{
        ResultActions perform = mockMvc
                .perform(get("/admin/booking-count-by-stadium")
                        .param("adminId", "1"));
        verify(adminService, never()).getBookingCountGroupByStadium();
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("管理员获取每个场馆的订单数时，若携带TOKEN与adminId不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingBookingCountByStadiumWithWrongToken() throws Exception{
        ResultActions perform = mockMvc
                .perform(get("/admin/booking-count-by-stadium")
                        .param("adminId", "2")
                        .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, never()).getBookingCountGroupByStadium();
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }

    @Test
    @DisplayName("管理员正确获取每个场馆的订单数")
    void shouldGetCorrectBookingCountByStadium() throws Exception {
        List<String> stadiums = new ArrayList<>
                (Arrays.asList("tennis court", "volleyball court", "basketball court"));
        List<Integer> count = new ArrayList<>
                (Arrays.asList(12, 24, 8));
        Map<String, Object> bookingCount = new HashMap<>();
        bookingCount.put("stadiums", stadiums);
        bookingCount.put("count", count);
        when(adminService.getBookingCountGroupByStadium()).thenReturn(bookingCount);
        ResultActions perform = mockMvc.perform(get("/admin/booking-count-by-stadium")
                .param("adminId", "1")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok"));
        verify(adminService, times(1))
                .getBookingCountGroupByStadium();
        perform.andExpect(jsonPath("$.data.stadiums[0]").value("tennis court"))
                .andExpect(jsonPath("$.data.stadiums[1]").value("volleyball court"))
                .andExpect(jsonPath("$.data.stadiums[2]").value("basketball court"))
                .andExpect(jsonPath("$.data.stadiums[3]").doesNotExist())
                .andExpect(jsonPath("$.data.count[0]").value(12))
                .andExpect(jsonPath("$.data.count[1]").value(24))
                .andExpect(jsonPath("$.data.count[2]").value(8))
                .andExpect(jsonPath("$.data.count[3]").doesNotExist());

    }
}