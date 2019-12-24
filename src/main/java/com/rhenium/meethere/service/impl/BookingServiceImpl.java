package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.BookingDao;
import com.rhenium.meethere.domain.Booking;
import com.rhenium.meethere.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/24 14:39
 */
@Slf4j
@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    BookingDao bookingDao;

    @Override
    public ArrayList<Map<String, Integer>> getEmptyTimeByStadiumIdAndDate(Integer stadiumId, Integer daysAfterToday) {
        ArrayList<Booking> bookings = getBookingsByStadiumAndDate(stadiumId, daysAfterToday);
        ArrayList<Map<String, Integer>> emptyTimesByBooksInADay = getEmptyTimesByBookingsInADay(bookings, daysAfterToday);
        return emptyTimesByBooksInADay;
    }

    public ArrayList<Booking> getBookingsByStadiumAndDate(Integer stadiumId, Integer daysAfterToday) {
        LocalDate date = LocalDate.now().plusDays(daysAfterToday);
        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(0, 0));
        LocalDateTime end = LocalDateTime.of(date.plusDays(1), LocalTime.of(0, 0));
        ArrayList<Booking> bookings = bookingDao.getBookingsByStadiumIdAndStartAndEnd(stadiumId, start, end);
        return bookings;
    }

    public ArrayList<Map<String, Integer>> getEmptyTimesByBookingsInADay(ArrayList<Booking> bookings, Integer daysAfterToday) {
        ArrayList<Map<String, Integer>> emptyTimes = new ArrayList<>();
        int currentHour = 8;
        if(daysAfterToday == 0)
            currentHour = LocalTime.now().getHour() + 2;
        int start, end;
        for(Booking booking : bookings) {
            if(currentHour != booking.getStartTime().getHour()) {
                start = currentHour;
                end = booking.getStartTime().getHour();
                Map<String, Integer> emptyTime = new HashMap<>();
                emptyTime.put("start", start);
                emptyTime.put("end", end);
                emptyTimes.add(emptyTime);
                currentHour = booking.getEndTime().getHour();
            }
        }
        if(currentHour != 20) {
            Map<String, Integer> emptyTime = new HashMap<>();
            emptyTime.put("start", currentHour);
            emptyTime.put("end", 20);
            emptyTimes.add(emptyTime);
        }
        return emptyTimes;
    }
}
