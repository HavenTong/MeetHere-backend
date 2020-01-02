package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.BookingDao;
import com.rhenium.meethere.dao.StadiumDao;
import com.rhenium.meethere.domain.Booking;
import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.dto.BookingRequest;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.cache.decorators.BlockingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    StadiumDao stadiumDao;

    @Override
    public void updateBooking(BookingRequest bookingRequest) {
        Integer bookingId = bookingRequest.getBookingId();
        Booking oldBooing = bookingDao.getBookingByBookingId(bookingId);
        bookingDao.deleteBookingById(bookingId);
        try {
            addNewBooking(bookingRequest);
        } catch (MyException e) {
            bookingDao.addNewBooking(oldBooing);
        }
    }

    @Override
    public void addNewBooking(BookingRequest bookingRequest) {
        boolean isValid = judgeBookingIsValid(bookingRequest);
        if (!isValid) {
            throw new MyException(ResultEnum.INVALID_TIME_IN_BOOKING);
        }
        Booking booking = new Booking();
        booking.setCustomerId(bookingRequest.getCustomerId());
        booking.setStadiumId(bookingRequest.getStadiumId());
        LocalDate today = LocalDate.now();
        LocalDateTime startTime = LocalDateTime.of(today.plusDays(bookingRequest.getDaysAfterToday()), LocalTime.of(bookingRequest.getStartTime(), 0));
        LocalDateTime endTime = LocalDateTime.of(today.plusDays(bookingRequest.getDaysAfterToday()), LocalTime.of(bookingRequest.getEndTime(), 0));
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        Stadium stadium = stadiumDao.getStadiumById(bookingRequest.getStadiumId());
        BigDecimal priceSum = BigDecimal.valueOf(bookingRequest.getEndTime() - bookingRequest.getStartTime()).multiply(stadium.getPrice());
        booking.setPriceSum(priceSum);
        booking.setPaid(false);
        bookingDao.addNewBooking(booking);
    }

    @Override
    public ArrayList<Map<String, Integer>> getEmptyTimeByStadiumIdAndDate(Integer stadiumId, Integer daysAfterToday) {
        ArrayList<Booking> bookings = getBookingsByStadiumAndDate(stadiumId, daysAfterToday);
        ArrayList<Map<String, Integer>> emptyTimesByBooksInADay = getEmptyTimesByBookingsInADay(bookings, daysAfterToday);
        return emptyTimesByBooksInADay;
    }

    public boolean judgeBookingIsValid(BookingRequest bookingRequest) {
        ArrayList<Map<String, Integer>> emptyTimes = getEmptyTimeByStadiumIdAndDate(bookingRequest.getStadiumId(), bookingRequest.getDaysAfterToday());
        Integer startTime = bookingRequest.getStartTime();
        Integer endTime = bookingRequest.getEndTime();
        for (Map<String, Integer> emptyTime : emptyTimes) {
            if ((emptyTime.get("start") <= startTime) && (endTime <= emptyTime.get("end"))) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Booking>  getBookingsByStadiumAndDate(Integer stadiumId, Integer daysAfterToday) {
        LocalDate date = LocalDate.now().plusDays(daysAfterToday);
        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(0, 0));
        LocalDateTime end = LocalDateTime.of(date.plusDays(1), LocalTime.of(0, 0));
        ArrayList<Booking> bookings = bookingDao.getBookingsByStadiumIdAndStartAndEnd(stadiumId, start, end);
        return bookings;
    }

    public ArrayList<Map<String, Integer>> getEmptyTimesByBookingsInADay(ArrayList<Booking> bookings, Integer daysAfterToday) {
        ArrayList<Map<String, Integer>> emptyTimes = new ArrayList<>();
        int currentHour = 8;
        if (daysAfterToday == 0)
            currentHour = LocalTime.now().getHour() + 2;
        int start, end;
        for (Booking booking : bookings) {
            if (currentHour < booking.getStartTime().getHour()) {
                start = currentHour;
                end = booking.getStartTime().getHour();
                Map<String, Integer> emptyTime = new HashMap<>();
                emptyTime.put("start", start);
                emptyTime.put("end", end);
                emptyTimes.add(emptyTime);
                currentHour = booking.getEndTime().getHour();
            }
        }
        if (currentHour != 20) {
            Map<String, Integer> emptyTime = new HashMap<>();
            emptyTime.put("start", currentHour);
            emptyTime.put("end", 20);
            emptyTimes.add(emptyTime);
        }
        return emptyTimes;
    }

    //TODO: 用户获取订单信息
    @Override
    public List<Map<String, Object>> getBookingsByCustomer(int offset, int limit, int customerId) {
        if (offset < 0) {
            throw new MyException(ResultEnum.INVALID_OFFSET);
        }
        if (limit < 1){
            throw new MyException(ResultEnum.INVALID_LIMIT);
        }
        List<Booking> bookings = bookingDao.findBookingsByCustomerId(offset, limit, customerId);
        List<Map<String, Object>> bookingInfoList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Booking booking : bookings){
            Map<String, Object> bookingInfo = new HashMap<>();
            LocalDateTime startTime = booking.getStartTime();
            LocalDateTime endTime = booking.getEndTime();
            bookingInfo.put("bookingId", booking.getBookingId());
            bookingInfo.put("stadiumName", booking.getStadium().getStadiumName());
            bookingInfo.put("startTime", formatter.format(startTime));
            bookingInfo.put("endTime", formatter.format(endTime));
            bookingInfo.put("stadiumId", booking.getStadium().getStadiumId());
//            bookingInfo.put("daysAfterToday", booking.getStartTime().getDayOfYear() - LocalDate.now().getDayOfYear());
            bookingInfo.put("daysAfterToday", LocalDate.now().until(booking.getStartTime(), ChronoUnit.DAYS));

            bookingInfo.put("start", booking.getStartTime().getHour());
            bookingInfo.put("end", booking.getEndTime().getHour());
            LocalDateTime lastUpdatableTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0, 0));
            if (lastUpdatableTime.isBefore(startTime)){
                bookingInfo.put("expired", false);
            } else {
                bookingInfo.put("expired", true);
            }
            bookingInfo.put("paid", booking.getPaid());
            bookingInfo.put("priceSum", booking.getPriceSum());
            bookingInfoList.add(bookingInfo);
        }
        return bookingInfoList;
    }

    @Override
    public Map<String, Object> getBookingCountForCustomer(int customerId) {
        int count = bookingDao.findBookingCountForCustomer(customerId);
        Map<String, Object> bookingCount = new HashMap<>();
        bookingCount.put("count", String.valueOf(count));
        return bookingCount;
    }


    @Override
    public void deleteBookingByCustomer(BookingRequest bookingRequest) {
        bookingDao.deleteBookingById(bookingRequest.getBookingId());
    }
}
