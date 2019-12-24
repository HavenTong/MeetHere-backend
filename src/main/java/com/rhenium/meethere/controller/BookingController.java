package com.rhenium.meethere.controller;

import com.rhenium.meethere.annotation.UserLoginRequired;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.service.BookingService;
import com.rhenium.meethere.vo.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
}
