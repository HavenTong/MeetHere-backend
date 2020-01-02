package com.rhenium.meethere.service;

import com.rhenium.meethere.domain.Booking;
import com.rhenium.meethere.dto.BookingRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/24 12:43
 */
@Service
public interface BookingService {
    ArrayList<Map<String, Integer>> getEmptyTimeByStadiumIdAndDate(Integer stadiumId, Integer daysAfterToday);

    void addNewBooking(BookingRequest bookingRequest);

    List<Map<String, Object>> getBookingsByCustomer(int offset, int limit, int customerId);

    void deleteBookingByCustomer(BookingRequest bookingRequest);

    void updateBooking(BookingRequest bookingRequest);

    Map<String, Object> getBookingCountForCustomer(int customerId);
}
