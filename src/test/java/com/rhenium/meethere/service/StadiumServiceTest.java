package com.rhenium.meethere.service;

import com.rhenium.meethere.dao.AdminDao;
import com.rhenium.meethere.dao.StadiumDao;
import com.rhenium.meethere.domain.Admin;
import com.rhenium.meethere.domain.Booking;
import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.dto.StadiumRequest;
import com.rhenium.meethere.enums.StadiumTypeEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.impl.StadiumServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/21 21:34
 */
@SpringBootTest
class StadiumServiceTest {
    @Mock
    private StadiumDao stadiumDao;

    @Mock
    private AdminDao adminDao;

    @Mock
    private Stadium stadium;

    @InjectMocks
    private StadiumServiceImpl stadiumService;

    @Test
    void shouldListStadiumItems() {
        ArrayList<Stadium> stadiums = stadiumService.listStadiumItems();
        verify(stadiumDao, times(1))
                .getStadiumList();
    }

    @Test
    void shouldGetStadiumById() {
        Stadium stadium = new Stadium();
        stadium.setStadiumId(1);
        stadium.setStadiumName("Song");
        stadium.setType(1);
        stadium.setDescription("Just for test");
        stadium.setPrice(BigDecimal.valueOf(12.0));
        stadium.setLocation("ZhongShanBei road");
        when(stadiumDao.getStadiumById(1)).thenReturn(stadium);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        Map<String, String> stadiumMsg = stadiumService.getStadiumById(1);

        verify(stadiumDao, times(1)).getStadiumById(idCaptor.capture());
        assertAll(
                () -> assertEquals(1, idCaptor.getValue()),
                () -> assertEquals(stadiumMsg.get("stadiumId"), "1"),
                () -> assertEquals(stadiumMsg.get("stadiumName"), "Song"),
                () -> assertEquals(stadiumMsg.get("typeName"), StadiumTypeEnum.getByCode(1).getType()),
                () -> assertEquals(stadiumMsg.get("location"), "ZhongShanBei road"),
                () -> assertEquals(stadiumMsg.get("price"), "12.0")
        );
    }

    @Test
    @DisplayName("修改场馆信息时，若管理员不存在，抛出异常")
    void shouldThrowExceptionWhenUpdatingStadiumWithAdminNotExist(){
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .stadiumId(1).build();
        Throwable exception = assertThrows(MyException.class,
                () -> stadiumService.updateStadiumInfoByAdmin(stadiumRequest));
        assertEquals("管理员不存在", exception.getMessage());
    }

    @Test
    @DisplayName("修改场馆信息时，修改正确的信息")
    void shouldUpdateCorrectInfoWhenUpdatingStadium(){
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .stadiumId(1).stadiumName("gym").location("home")
                .description("good").price(BigDecimal.valueOf(100)).adminId(1).build();
        when(adminDao.findAdminById(1)).thenReturn(new Admin());
        stadiumService.updateStadiumInfoByAdmin(stadiumRequest);
        verify(stadiumDao, times(1))
                .updateStadiumInfoByAdmin(stadiumRequest);
    }

    @Test
    @DisplayName("删除场馆时，若管理员不存在，抛出异常")
    void shouldThrowExceptionWhenDeletingStadiumWithAdminNotExist(){
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .stadiumId(1).build();
        Throwable exception = assertThrows(MyException.class,
                () -> stadiumService.deleteStadiumInfoByAdmin(stadiumRequest));
        assertEquals("管理员不存在", exception.getMessage());
    }

    @Test
    @DisplayName("删除场馆时，删除正确的场馆")
    void shouldDeleteCorrectStadium(){
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .stadiumId(1).adminId(1).build();
        when(adminDao.findAdminById(1)).thenReturn(new Admin());
        stadiumService.deleteStadiumInfoByAdmin(stadiumRequest);
        verify(stadiumDao, times(1))
                .deleteStadiumByAdmin(stadiumRequest);
    }

    @Test
    @DisplayName("当某一场馆订单列表为空，管理员获取正确的场馆空闲时间")
    void shouldGetCorrectFreeTimeWhenBookingListIsEmpty(){
        List<Booking> bookingList = new ArrayList<>();
        LocalDate date = LocalDate.of(2019, 12, 25);
        List<String> freeTime = stadiumService.getSpareTimeFromBookingList(bookingList, date);
        assertAll(
                () -> assertEquals(1, freeTime.size()),
                () -> assertEquals("08:00-20:00", freeTime.get(0))
        );
    }

    @Test
    @DisplayName("当某一场馆在某一日期中没有订单，管理员获取正确的场馆空闲时间")
    void shouldGetCorrectFreeTimeWhenNoBookingInDate(){
        LocalDate date = LocalDate.of(2019, 12, 26);
        Booking item1 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 25, 8, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 25, 9, 0, 0))
                .build();
        Booking item2 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 25, 11, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 25, 13, 0, 0))
                .build();
        List<Booking> bookings = new ArrayList<>(Arrays.asList(item1, item2));
        List<String> freeTime = stadiumService.getSpareTimeFromBookingList(bookings, date);
        assertAll(
                () -> assertEquals(1, freeTime.size()),
                () -> assertEquals("08:00-20:00", freeTime.get(0))
        );
    }

    @Test
    @DisplayName("当某一场馆仅预约某天8:00-9:00时，管理员获取正确的场馆空闲时间")
    void shouldGetCorrectFreeTimeWhenBookingFrom8amTo9amInTheDate(){
        LocalDate date = LocalDate.of(2019, 12, 25);
        Booking item1 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 25, 8, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 25, 9, 0, 0))
                .build();
        List<Booking> bookings = new ArrayList<>(Arrays.asList(item1));
        List<String> freeTime = stadiumService.getSpareTimeFromBookingList(bookings, date);
        assertAll(
                () -> assertEquals(1, bookings.size()),
                () -> assertEquals("09:00-20:00", freeTime.get(0))
        );
    }

    @Test
    @DisplayName("当某一场馆一天中预约多个时段，管理员获取正确的空闲时间")
    void shouldGetCorrectFreeTimeWhenBookingManyHoursInTheDate(){
        LocalDate date = LocalDate.of(2019, 12, 25);
        Booking item1 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 25, 9, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 25, 10, 0, 0))
                .build();
        Booking item2 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 25, 11, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 25, 12, 0, 0))
                .build();
        Booking item3 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 25, 12, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 25, 15, 0, 0))
                .build();
        List<Booking> bookings = new ArrayList<>(Arrays.asList(item1, item2, item3));
        List<String> freeTime = stadiumService.getSpareTimeFromBookingList(bookings, date);
        assertAll(
                () -> assertEquals(3, freeTime.size()),
                () -> assertEquals("08:00-09:00", freeTime.get(0)),
                () -> assertEquals("10:00-11:00", freeTime.get(1)),
                () -> assertEquals("15:00-20:00", freeTime.get(2))
        );
    }

    @Test
    @DisplayName("当某一场馆预约多个时段且包含20:00时，管理员获取正确的空闲时间")
    void shouldGetCorrectFreeTimeWhenBookingInclude2000(){
        LocalDate date = LocalDate.of(2019, 12, 25);
        Booking item1 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 25, 8, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 25, 11, 0, 0))
                .build();
        Booking item2 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 25, 19, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 25, 20, 0, 0))
                .build();
        List<Booking> bookings = new ArrayList<>(Arrays.asList(item1, item2));
        List<String> freeTime = stadiumService.getSpareTimeFromBookingList(bookings, date);
        assertAll(
                () -> assertEquals(1, freeTime.size()),
                () -> assertEquals("11:00-19:00", freeTime.get(0))
        );
    }

    @Test
    @DisplayName("当某一个场馆当天被预约满，管理员获得正确的空闲时间")
    void shouldGetCorrectFreeTimeWhenBookingsAreFull(){
        LocalDate date = LocalDate.of(2019, 12, 25);
        Booking item1 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 25, 8, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 25, 20, 0, 0))
                .build();
        List<Booking> bookings = new ArrayList<>(Arrays.asList(item1));
        List<String> freeTime = stadiumService.getSpareTimeFromBookingList(bookings, date);
        assertEquals(0, freeTime.size());
    }

    @Test
    @DisplayName("管理员获取场馆信息时，若偏移数据条数小于0，抛出异常")
    void shouldThrowExceptionWhenOffsetLessThanZero(){
        Throwable exception = assertThrows(MyException.class,
                () -> stadiumService.findStadiumsForAdmin(-1, 2));
        assertEquals("偏移数据条目数必须为正整数", exception.getMessage());
    }

    @Test
    @DisplayName("管理员获取场馆信息时，若获取数据条数小于1，抛出异常")
    void shouldThrowExceptionWhenLimitLessThanZero(){
        Throwable exception = assertThrows(MyException.class,
                () -> stadiumService.findStadiumsForAdmin(2, 0));
        assertEquals("数据条目数必须为正整数", exception.getMessage());
    }

    // 该方法的测试用例中的时间需要根据测试发生时间设置
    // 当前时间为2019-12-26，选取测试用例中的时间为当前时间及其明后天:
    // 2019-12-26/2019-12-27/2019-12-28
    @Test
    @DisplayName("管理员获取正确的场馆信息")
    void shouldGetCorrectStadiumInfoByAdmin(){
        Booking booking1ForStadium1 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 26, 8, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 26, 11, 0, 0))
                .build();
        Booking booking2ForStadium1 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 27, 10, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 27, 12, 0, 0))
                .build();
        Booking booking3ForStadium1 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 28, 12, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 28, 13, 0, 0))
                .build();
        List<Booking> bookingsForStadium1
                = new ArrayList<>(Arrays.asList(booking1ForStadium1, booking2ForStadium1, booking3ForStadium1));
        Booking booking1ForStadium2 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 26, 12, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 26, 13, 0, 0))
                .build();
        Booking booking2ForStadium2 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 27, 14, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 27, 16, 0, 0))
                .build();
        Booking booking3ForStadium2 = Booking.builder()
                .startTime(LocalDateTime.of(2019, 12, 28, 19, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 28, 20, 0, 0))
                .build();
        List<Booking> bookingsForStadium2
                = new ArrayList<>(Arrays.asList(booking1ForStadium2, booking2ForStadium2, booking3ForStadium2));
        Stadium tennisStadium = Stadium.builder()
                .stadiumName("中北网球场")
                .stadiumId(1)
                .description("good").location("tennis court").type(StadiumTypeEnum.TENNIS_BALL_STADIUM.getCode())
                .price(BigDecimal.valueOf(200))
                .bookingList(bookingsForStadium1)
                .build();
        Stadium volleyBallStadium = Stadium.builder()
                .stadiumName("中北排球场")
                .stadiumId(2)
                .description("great").location("volleyball court").type(StadiumTypeEnum.VOLLEYBALL_STADIUM.getCode())
                .price(BigDecimal.valueOf(400))
                .bookingList(bookingsForStadium2)
                .build();
        List<Stadium> stadiums = new ArrayList<>(Arrays.asList(tennisStadium, volleyBallStadium));
        when(stadiumDao.findAllStadiumsForAdmin(0, 2))
                .thenReturn(stadiums);
        List<Map<String, Object>> stadiumInfo = stadiumService.findStadiumsForAdmin(0, 2);
        verify(stadiumDao, times(1))
                .findAllStadiumsForAdmin(0, 2);
        Map<String, Object> freeTimeMapForTennis = (Map<String, Object>) stadiumInfo.get(0).get("freeTime");
        List<String> todayFreeTimeForTennis = (List<String>) freeTimeMapForTennis.get("2019-12-26");
        List<String> tomorrowFreeTimeForTennis = (List<String>) freeTimeMapForTennis.get("2019-12-27");
        List<String> theDayAfterTomorrowFreeTimeForTennis = (List<String>) freeTimeMapForTennis.get("2019-12-28");
        Map<String, Object> freeTimeMapForVolleyBall = (Map<String, Object>) stadiumInfo.get(1).get("freeTime");
        List<String> todayFreeTimeForVolleyBall = (List<String>) freeTimeMapForVolleyBall.get("2019-12-26");
        List<String> tomorrowFreeTimeForVolleyBall = (List<String>) freeTimeMapForVolleyBall.get("2019-12-27");
        List<String> theDayAfterTomorrowFreeTimeForVolleyBall = (List<String>) freeTimeMapForVolleyBall.get("2019-12-28");
        assertAll(
                () -> assertEquals("11:00-20:00", todayFreeTimeForTennis.get(0)),
                () -> assertEquals("08:00-10:00", tomorrowFreeTimeForTennis.get(0)),
                () -> assertEquals("12:00-20:00", tomorrowFreeTimeForTennis.get(1)),
                () -> assertEquals("08:00-12:00", theDayAfterTomorrowFreeTimeForTennis.get(0)),
                () -> assertEquals("13:00-20:00", theDayAfterTomorrowFreeTimeForTennis.get(1)),
                () -> assertEquals( 1, stadiumInfo.get(0).get("stadiumId")),
                () -> assertEquals("中北网球场", stadiumInfo.get(0).get("stadiumName")),
                () -> assertEquals(BigDecimal.valueOf(200), stadiumInfo.get(0).get("price")),
                () -> assertEquals("tennis court", stadiumInfo.get(0).get("location")),
                () -> assertEquals("good", stadiumInfo.get(0).get("description")),
                () -> assertEquals("网球场", stadiumInfo.get(0).get("type")),
                () -> assertEquals("08:00-12:00", todayFreeTimeForVolleyBall.get(0)),
                () -> assertEquals("13:00-20:00", todayFreeTimeForVolleyBall.get(1)),
                () -> assertEquals("08:00-14:00", tomorrowFreeTimeForVolleyBall.get(0)),
                () -> assertEquals("16:00-20:00", tomorrowFreeTimeForVolleyBall.get(1)),
                () -> assertEquals("08:00-19:00", theDayAfterTomorrowFreeTimeForVolleyBall.get(0)),
                () -> assertEquals( 2, stadiumInfo.get(1).get("stadiumId")),
                () -> assertEquals("中北排球场", stadiumInfo.get(1).get("stadiumName")),
                () -> assertEquals(BigDecimal.valueOf(400), stadiumInfo.get(1).get("price")),
                () -> assertEquals("volleyball court", stadiumInfo.get(1).get("location")),
                () -> assertEquals("great", stadiumInfo.get(1).get("description")),
                () -> assertEquals("排球场", stadiumInfo.get(1).get("type"))
        );
    }
}
