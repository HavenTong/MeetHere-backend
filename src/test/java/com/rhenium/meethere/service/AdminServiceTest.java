package com.rhenium.meethere.service;

import com.rhenium.meethere.dao.AdminDao;
import com.rhenium.meethere.dao.BookingDao;
import com.rhenium.meethere.dao.CustomerDao;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.service.impl.AdminServiceImpl;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author HavenTong
 * @date 2019/12/17 5:56 下午
 */
@SpringBootTest
class AdminServiceTest {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private AdminDao adminDao;

    @Mock
    private BookingDao bookingDao;

    @Spy
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    @DisplayName("获取用户数量时，获取结果正确")
    void shouldGetCorrectUserCount(){
        when(customerDao.getUserCount()).thenReturn(3);
        Map<String, String> result = adminService.getUserCount();
        verify(customerDao, times(1))
                .getUserCount();
        assertEquals("3", result.get("count"));
    }

    @Test
    @DisplayName("删除指定用户ID的用户，应该调用customerDao")
    void shouldCallCustomerDaoWhenDeleteCustomerById() {
        AdminRequest adminRequest = AdminRequest.builder().customerId(1).build();
        adminService.deleteUser(adminRequest);

        verify(customerDao, times(1)).deleteCustomerById(1);
    }

    @Test
    @DisplayName("根据日期统计订单数目时，当日期格式不正确，抛出异常")
    void shouldThrowExceptionWhenGetBookingCountWithWrongDateFormat(){
        assertThrows(Exception.class,
                () -> adminService.getBookingCountByDate("2020-1-1"));
        verify(bookingDao, never())
                .getBookingCountBetweenStartAndEnd(any(), any());
    }

    @Test
    @DisplayName("根据日期统计订单数目时，查询正确的日期区间")
    void shouldGetBookingCountWithCorrectDate(){
        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        adminService.getBookingCountByDate("2019-12-21");
        verify(bookingDao, times(1))
                .getBookingCountBetweenStartAndEnd(startCaptor.capture(), endCaptor.capture());
        assertAll(
                () -> assertEquals(LocalDateTime.of(2019, 12, 21, 0,0, 0),
                        startCaptor.getValue()),
                () -> assertEquals(LocalDateTime.of(2019, 12, 21, 23, 59, 59),
                        endCaptor.getValue())
        );
    }

    @Test
    @DisplayName("根据场馆统计订单数目时，返回正确的数据list")
    void shouldGetCorrectListsWhenGettingBookingCountByStadium(){
        Map<String, Object> item1 = new HashMap<>();
        item1.put("stadiumName", "中北网球馆");
        item1.put("count",2L);
        Map<String, Object> item2 = new HashMap<>();
        item2.put("stadiumName", "国家体育馆");
        item2.put("count", 22L);
        List<Map<String, Object>> itemList = new ArrayList<>(Arrays.asList(item1, item2));
        when(bookingDao.getBookingCountGroupByStadium()).thenReturn(itemList);
        Map<String, Object> result = adminService.getBookingCountGroupByStadium();
        List<String> stadiums = (List<String>) result.get("stadiums");
        List<Long> count = (List<Long>) result.get("count");
        assertAll(
                () -> assertEquals("中北网球馆", stadiums.get(0)),
                () -> assertEquals("国家体育馆", stadiums.get(1)),
                () -> assertEquals(2, count.get(0)),
                () -> assertEquals(22, count.get(1))
        );
    }
}