package com.rhenium.meethere.service;

import com.rhenium.meethere.dao.BookingDao;
import com.rhenium.meethere.domain.Booking;
import com.rhenium.meethere.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
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
    @DisplayName("通过场馆ID和选定日期获取选定日期的空闲时间列表")
    void shouldGetEmptyTimeByStadiumIdAndDate() {
        ArrayList<Map<String, Integer>> emptyTimes = bookingService.getEmptyTimeByStadiumIdAndDate(1, 1);
    }
}
