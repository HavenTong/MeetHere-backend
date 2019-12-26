package com.rhenium.meethere.controller;

import com.rhenium.meethere.annotation.UserLoginRequired;
import com.rhenium.meethere.dto.BookingRequest;
import com.rhenium.meethere.service.BookingService;
import com.rhenium.meethere.vo.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/24 15:49
 */
@Slf4j
@RestController
@Validated
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/get-empty-time")
    @UserLoginRequired
    public ResultEntity getEmptyTimeByStadiumIdAndDate(@RequestParam int stadiumId,
                                                       @RequestParam int daysAfterToday,
                                                       @RequestParam int customerId) {
        ArrayList<Map<String, Integer>> emptyTimes = bookingService.getEmptyTimeByStadiumIdAndDate(stadiumId, daysAfterToday);
        return ResultEntity.succeed(emptyTimes);
    }

    @PostMapping("/add-new-booking")
    @UserLoginRequired
    public ResultEntity addNewBooking(@RequestBody BookingRequest bookingRequest) {
        bookingService.addNewBooking(bookingRequest);
        return ResultEntity.succeed();
    }

    @PostMapping("/delete-by-customer")
    @UserLoginRequired
    public ResultEntity deleteBookingByCustomer(@RequestBody BookingRequest bookingRequest) {
        bookingService.deleteBookingByCustomer(bookingRequest);
        return ResultEntity.succeed();
    }

    @PostMapping("/update-booking")
    @UserLoginRequired
    public ResultEntity updateBooking(@RequestBody BookingRequest bookingRequest) {
        bookingService.updateBooking(bookingRequest);
        return ResultEntity.succeed();
    }

    @GetMapping("/items")
    @UserLoginRequired
    public ResultEntity getBookingsByCustomerId(@RequestParam int offset,
                                                @RequestParam int limit,
                                                @RequestParam int customerId){
        List<Map<String, Object>> bookingList = bookingService.getBookingsByCustomer(offset, limit, customerId);
        return ResultEntity.succeed(bookingList);
    }

    @GetMapping("/count-for-customer")
    @UserLoginRequired
    public ResultEntity getBookingCountForCustomer(@RequestParam int customerId){
        Map<String, Object> bookingCount =
                bookingService.getBookingCountForCustomer(customerId);
        return ResultEntity.succeed(bookingCount);
    }
}
