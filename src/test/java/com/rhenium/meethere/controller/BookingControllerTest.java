package com.rhenium.meethere.controller;

import com.alibaba.fastjson.JSON;
import com.rhenium.meethere.domain.Booking;
import com.rhenium.meethere.dto.BookingRequest;
import com.rhenium.meethere.dto.StadiumRequest;
import com.rhenium.meethere.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/27 17:21
 */
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    @DisplayName("获取场馆空闲时间时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetEmptyTimeByStadiumAndDateWithoutToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/booking/get-empty-time")
                .param("stadiumId", "1")
                .param("daysAfterToday", "1")
                .param("customerId", "2"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(bookingService, never())
                .getEmptyTimeByStadiumIdAndDate(1, 1);
    }

    @Test
    @DisplayName("获取场馆空闲时间，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetEmptyTimesWithWrongToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/booking/get-empty-time")
                .param("stadiumId", "1")
                .param("daysAfterToday", "1")
                .param("customerId", "506")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(bookingService, never())
                .getEmptyTimeByStadiumIdAndDate(1, 1);
    }

    @Test
    @DisplayName("获取场馆空闲时间时，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldGetEmptyTimeByStadiumWithCorrectToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/booking/get-empty-time")
                .param("stadiumId", "1")
                .param("daysAfterToday", "1")
                .param("customerId", "507")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(bookingService, times(1))
                .getEmptyTimeByStadiumIdAndDate(1, 1);
    }

    @Test
    @DisplayName("添加订单时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenAddNewBookingWithoutToken() throws Exception {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(507).build();

        ResultActions perform = mockMvc.perform(post("/booking/add-new-booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(bookingRequest)));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(bookingService, never())
                .addNewBooking(bookingRequest);
    }

    @Test
    @DisplayName("添加订单时，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenAddNewBookingWithWrongToken() throws Exception {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(506).build();

        ResultActions perform = mockMvc.perform(post("/booking/add-new-booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(bookingRequest))
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(bookingService, never())
                .addNewBooking(bookingRequest);
    }

    @Test
    @DisplayName("添加订单时，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldAddNewBookingWithCorrectToken() throws Exception {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(507).build();

        ResultActions perform = mockMvc.perform(post("/booking/add-new-booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(bookingRequest))
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(bookingService, times(1))
                .addNewBooking(bookingRequest);
    }


    @Test
    @DisplayName("用户删除订单时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenDeleteBookingByCustomerWithoutToken() throws Exception {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(507).build();

        ResultActions perform = mockMvc.perform(post("/booking/delete-by-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(bookingRequest)));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(bookingService, never())
                .deleteBookingByCustomer(bookingRequest);
    }

    @Test
    @DisplayName("用户删除订单时，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenDeleteBookingByCustomerWithWrongToken() throws Exception {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(506).build();

        ResultActions perform = mockMvc.perform(post("/booking/delete-by-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(bookingRequest))
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(bookingService, never())
                .deleteBookingByCustomer(bookingRequest);
    }

    @Test
    @DisplayName("用户删除订单时，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldDeleteBookingByCustomerWithCorrectToken() throws Exception {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(507).build();

        ResultActions perform = mockMvc.perform(post("/booking/delete-by-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(bookingRequest))
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(bookingService, times(1))
                .deleteBookingByCustomer(bookingRequest);
    }

    @Test
    @DisplayName("用户更新订单时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenUpdateBookingWithoutToken() throws Exception {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(507).build();

        ResultActions perform = mockMvc.perform(post("/booking/update-booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(bookingRequest)));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(bookingService, never())
                .updateBooking(bookingRequest);
    }

    @Test
    @DisplayName("用户更新订单时，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenUpdateBookingWithWrongToken() throws Exception {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(506).build();

        ResultActions perform = mockMvc.perform(post("/booking/update-booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(bookingRequest))
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(bookingService, never())
                .updateBooking(bookingRequest);
    }

    @Test
    @DisplayName("用户更新订单时，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldUpdateBookingWithCorrectToken() throws Exception {
        BookingRequest bookingRequest = BookingRequest.builder().customerId(507).build();

        ResultActions perform = mockMvc.perform(post("/booking/update-booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(bookingRequest))
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo"));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(bookingService, times(1))
                .updateBooking(bookingRequest);
    }

    @Test
    @DisplayName("通过用户ID获取订单，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetBookingByCustomerIdWithoutToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/booking/items")
                .param("offset", "1")
                .param("limit", "1")
                .param("customerId", "507"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(bookingService, never())
                .getBookingsByCustomer(1, 1, 507);
    }

    @Test
    @DisplayName("通过用户ID获取订单，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetBookingByCustomerIdWithWrongToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/booking/items")
                .param("offset", "1")
                .param("limit", "1")
                .param("customerId", "506")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(bookingService, never())
                .getBookingsByCustomer(1, 1, 506);
    }

    @Test
    @DisplayName("通过用户ID获取订单，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldGetBookingByCustomerIdWithCorrectToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/booking/items")
                .param("offset", "1")
                .param("limit", "1")
                .param("customerId", "507")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(bookingService, times(1))
                .getBookingsByCustomer(1, 1, 507);
    }


    @Test
    @DisplayName("用户获取订单数目，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetBookingCountForCustomerWithoutToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/booking/count-for-customer")
                .param("customerId", "507"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
        verify(bookingService, never())
                .getBookingCountForCustomer(507);
    }

    @Test
    @DisplayName("通过用户ID获取订单数目，若HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetBookingCountForCustomerWithWrongToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/booking/count-for-customer")
                .param("customerId", "506")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("TOKEN不匹配"));
        verify(bookingService, never())
                .getBookingCountForCustomer(506);
    }

    @Test
    @DisplayName("通过用户ID获取订单数目，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldGetBookingCountForCustomerWithCorrectToken() throws Exception {
        ResultActions perform = mockMvc.perform(get("/booking/count-for-customer")
                .param("customerId", "507")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo"));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("$.message").value("success"));
        verify(bookingService, times(1))
                .getBookingCountForCustomer(507);
    }
}
