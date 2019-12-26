package com.rhenium.meethere.service;

import com.rhenium.meethere.dao.BookingDao;
import com.rhenium.meethere.dao.StadiumDao;
import com.rhenium.meethere.domain.Booking;
import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.dto.BookingRequest;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/24 15:02
 */
@SpringBootTest
class BookingServiceTest {
    @Mock
    private BookingDao bookingDao;

    @Mock
    private StadiumDao stadiumDao;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @InjectMocks
    private BookingServiceFake bookingServiceFake;

    @Test
    @DisplayName("通过场馆ID和选定日期，来获取该场馆在选定日期的订单列表")
    void shouldGetBookingsByStadiumAndDate() {
        Integer stadiumId = 1;
        Integer daysAfterToday = 1;
        LocalDate date = LocalDate.now().plusDays(daysAfterToday);
        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(0, 0));
        LocalDateTime end = LocalDateTime.of(date.plusDays(1), LocalTime.of(0, 0));
        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        when(bookingDao.getBookingsByStadiumIdAndStartAndEnd(stadiumId, start, end)).thenReturn(new ArrayList<>());

        bookingService.getBookingsByStadiumAndDate(stadiumId, daysAfterToday);

        verify(bookingDao, times(1)).getBookingsByStadiumIdAndStartAndEnd(idCaptor.capture(), startCaptor.capture(), endCaptor.capture());
        assertAll(
                () -> assertEquals(1, idCaptor.getValue()),
                () -> assertEquals(start, startCaptor.getValue()),
                () -> assertEquals(end, endCaptor.getValue())
        );
    }

    @Test
    @DisplayName("根据非今天的订单确定该日的空闲时间")
    void shouldGetEmptyTimesByBooksInADay() {
        Booking booking = new Booking();
        booking.setStartTime(LocalDateTime.of(2010, 10, 10, 10, 0));
        booking.setEndTime(LocalDateTime.of(2010, 10, 10, 11, 0));
        ArrayList<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        ArrayList<Map<String, Integer>> emptyTimes = bookingService.getEmptyTimesByBookingsInADay(bookings, 1);

        assertAll(
                () -> assertEquals(8, emptyTimes.get(0).get("start")),
                () -> assertEquals(10, emptyTimes.get(0).get("end")),
                () -> assertEquals(11, emptyTimes.get(1).get("start")),
                () -> assertEquals(20, emptyTimes.get(1).get("end"))
        );
    }

    @Test
    @DisplayName("根据今天的订单确定该日的空闲时间")
    void shouldGetEmptyTimesByBooksInToday () {
        Booking booking = new Booking();
        booking.setStartTime(LocalDateTime.of(2010, 10, 10, 8, 0));
        booking.setEndTime(LocalDateTime.of(2010, 10, 10, 9, 0));
        ArrayList<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        ArrayList<Map<String, Integer>> emptyTimes = bookingService.getEmptyTimesByBookingsInADay(bookings, 0);

        if(LocalTime.now().getHour() >= 9 && LocalTime.now().getHour() < 18) {
            assertAll(
                    () -> assertEquals(LocalTime.now().getHour() + 2, emptyTimes.get(0).get("start")),
                    () -> assertEquals(20, emptyTimes.get(0).get("end"))
            );
        } else if(LocalTime.now().getHour() < 9) {
            assertAll(
                    () -> assertEquals(9, emptyTimes.get(0).get("start")),
                    () -> assertEquals(20, emptyTimes.get(0).get("end"))
            );
        }
    }

    @Test
    @DisplayName("通过场馆ID和选定日期获取选定日期的空闲时间列表")
    void shouldGetEmptyTimeByStadiumIdAndDate() {
        BookingServiceFake bookingServiceFake = new BookingServiceFake();

        ArrayList<Map<String, Integer>> emptyTimes = bookingServiceFake.getEmptyTimeByStadiumIdAndDate(1, 1);

        assertAll(
                () -> assertEquals(8, emptyTimes.get(0).get("start")),
                () -> assertEquals(11, emptyTimes.get(0).get("end")),
                () -> assertEquals(15, emptyTimes.get(1).get("start")),
                () -> assertEquals(20, emptyTimes.get(1).get("end"))
        );
    }

    @Test
    @DisplayName("检测该待添加订单无时间冲突")
    void shouldJudgeTheBookingIsValid() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setStartTime(9);
        bookingRequest.setEndTime(10);
        bookingRequest.setDaysAfterToday(1);

        boolean isValid = bookingServiceFake.judgeBookingIsValid(bookingRequest);

        assertEquals(true, isValid);
    }

    @Test
    @DisplayName("检测该待添加订单有时间冲突")
    void shouldJudgeTheBookingIsNotValid() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setStartTime(10);
        bookingRequest.setEndTime(12);
        bookingRequest.setDaysAfterToday(1);

        boolean isValid = bookingServiceFake.judgeBookingIsValid(bookingRequest);

        assertEquals(false, isValid);
    }

    @Test
    @DisplayName("添加一个新的订单")
    void shouldAddNewBooking() {
        Stadium stadium = new Stadium();
        stadium.setPrice(BigDecimal.valueOf(100));
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setDaysAfterToday(1);
        bookingRequest.setCustomerId(7);
        bookingRequest.setStadiumId(1);
        bookingRequest.setStartTime(9);
        bookingRequest.setEndTime(11);
        when(stadiumDao.getStadiumById(1)).thenReturn(stadium);
        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);

        bookingServiceFake.addNewBooking(bookingRequest);

        verify(bookingDao, times(1)).addNewBooking(bookingCaptor.capture());
        assertAll(
                () -> assertEquals(7, bookingCaptor.getValue().getCustomerId()),
                () -> assertEquals(1, bookingCaptor.getValue().getStadiumId()),
                () -> assertEquals(9, bookingCaptor.getValue().getStartTime().getHour()),
                () -> assertEquals(11, bookingCaptor.getValue().getEndTime().getHour()),
                () -> assertEquals(BigDecimal.valueOf(200), bookingCaptor.getValue().getPriceSum())
        );
    }

    @Test
    @DisplayName("修改更新订单")
    void shouldUpdateBooking() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setBookingId(1);
        bookingRequest.setDaysAfterToday(1);
        bookingRequest.setCustomerId(7);
        bookingRequest.setStadiumId(1);
        bookingRequest.setStartTime(9);
        bookingRequest.setEndTime(11);
        Booking booking = new Booking();
        booking.setBookingId(1);
        Stadium stadium = new Stadium();
        stadium.setPrice(BigDecimal.valueOf(100));
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        when(bookingDao.getBookingByBookingId(1)).thenReturn(booking);
        when(stadiumDao.getStadiumById(1)).thenReturn(stadium);

        bookingServiceFake.updateBooking(bookingRequest);

        verify(bookingDao, times(1)).deleteBookingById(idCaptor.capture());
        assertEquals(1, idCaptor.getValue());
    }

    @Test
    @DisplayName("当修改后的订单中时间发生冲突时，应该将旧的订单恢复")
    void shouldRecoveryWhenTimeConflict() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setBookingId(1);
        bookingRequest.setDaysAfterToday(1);
        bookingRequest.setCustomerId(7);
        bookingRequest.setStadiumId(1);
        bookingRequest.setStartTime(10);
        bookingRequest.setEndTime(12);
        Booking booking = new Booking();
        booking.setBookingId(10);
        Stadium stadium = new Stadium();
        stadium.setPrice(BigDecimal.valueOf(100));
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        when(bookingDao.getBookingByBookingId(1)).thenReturn(booking);
        when(stadiumDao.getStadiumById(1)).thenReturn(stadium);

        bookingServiceFake.updateBooking(bookingRequest);

        verify(bookingDao, times(1)).deleteBookingById(idCaptor.capture());
        verify(bookingDao, times(1)).addNewBooking(bookingCaptor.capture());
        assertAll(
                () -> assertEquals(1, idCaptor.getValue()),
                () -> assertEquals(10, bookingCaptor.getValue().getBookingId())
        );
    }

    @Test
    @DisplayName("用户删除正确的订单")
    void shouldDeleteCorrectBookingByCustomer(){
        BookingRequest bookingRequest = BookingRequest.builder()
                .bookingId(12).customerId(7).build();
        bookingService.deleteBookingByCustomer(bookingRequest);
        verify(bookingDao, times(1))
                .deleteBookingById(12);
    }

    @Test
    @DisplayName("用户获取自己订单时，若offset小于0，则抛出异常")
    void shouldThrowExceptionWhenGettingBookingWithOffsetLessThanZero(){
        Throwable exception = assertThrows(MyException.class,
                () -> bookingService.getBookingsByCustomer(-1, 2, 1));
        assertEquals("偏移数据条目数必须为正整数", exception.getMessage());
    }

    @Test
    @DisplayName("用户获取自己订单时，若limit小于1，则抛出异常")
    void shouldThrowExceptionWhenGettingBookingWithLimitLessThanOne(){
        Throwable exception = assertThrows(MyException.class,
                () -> bookingService.getBookingsByCustomer(0, 0, 1));
        assertEquals("数据条目数必须为正整数", exception.getMessage());
    }

    // 改测试方法的测试用例需要根据测试方法运行的时间来进行设计，否则会测试失败
    // 当前测试日期为12-27
    @Test
    @DisplayName("用户获取正确的订单列表")
    void shouldGetCorrectBookingsForCustomer(){
        Stadium tennisCourt = Stadium.builder()
                .stadiumName("tennis").build();
        Stadium volleyBallCourt = Stadium.builder()
                .stadiumName("volleyball").build();
        Booking bookingForTennis = Booking.builder()
                .bookingId(1)
                .startTime(LocalDateTime.of(2019, 12, 26, 8, 0, 0))
                .endTime(LocalDateTime.of(2019, 12, 26, 10, 0, 0 ))
                .paid(true).stadium(tennisCourt).priceSum(BigDecimal.valueOf(200)).build();
        Booking bookingForVolleyball = Booking.builder()
                .bookingId(2)
                .startTime(LocalDateTime.of(2019, 12, 28, 18, 0, 0 ))
                .endTime(LocalDateTime.of(2019, 12, 28, 20, 0, 0))
                .paid(false).stadium(volleyBallCourt).priceSum(BigDecimal.valueOf(400)).build();
        List<Booking> bookingList = new ArrayList<>(Arrays.asList(bookingForTennis, bookingForVolleyball));
        when(bookingDao.findBookingsByCustomerId(0, 2, 1)).thenReturn(bookingList);
        List<Map<String, Object>> bookingInfoList
                = bookingService.getBookingsByCustomer(0, 2, 1);
        verify(bookingDao, times(1))
                .findBookingsByCustomerId(0, 2, 1);
        assertEquals(2, bookingInfoList.size());
        Map<String, Object> firstBooking = bookingInfoList.get(0);
        Map<String, Object> secondBooking = bookingInfoList.get(1);
        assertAll(
                () -> assertEquals(1, firstBooking.get("bookingId")),
                () -> assertEquals("tennis", firstBooking.get("stadiumName")),
                () -> assertEquals("2019-12-26 08:00:00", firstBooking.get("startTime")),
                () -> assertEquals("2019-12-26 10:00:00", firstBooking.get("endTime")),
                () -> assertTrue((Boolean) firstBooking.get("expired")),
                () -> assertTrue((Boolean) firstBooking.get("paid")),
                () -> assertEquals(BigDecimal.valueOf(200), firstBooking.get("priceSum")),
                () -> assertEquals(2, secondBooking.get("bookingId")),
                () -> assertEquals("volleyball", secondBooking.get("stadiumName")),
                () -> assertEquals("2019-12-28 18:00:00", secondBooking.get("startTime")),
                () -> assertEquals("2019-12-28 20:00:00", secondBooking.get("endTime")),
                () -> assertFalse((Boolean) secondBooking.get("expired")),
                () -> assertFalse((Boolean) secondBooking.get("paid")),
                () -> assertEquals(BigDecimal.valueOf(400), secondBooking.get("priceSum"))
        );

    }

}

class BookingServiceFake extends BookingServiceImpl {
    @Override
    public ArrayList<Booking> getBookingsByStadiumAndDate(Integer stadiumId, Integer daysAfterToday) {
        Booking booking = new Booking();
        booking.setStartTime(LocalDateTime.of(2020, 10, 10, 11, 00));
        booking.setEndTime(LocalDateTime.of(2020, 10, 10, 15, 00));
        ArrayList<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        return bookings;
    }
}
