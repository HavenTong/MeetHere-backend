package com.rhenium.meethere.controller;

import com.rhenium.meethere.annotation.AdminLoginRequired;
import com.rhenium.meethere.domain.Booking;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.service.AdminService;
import com.rhenium.meethere.vo.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.List;
import java.util.Map;

/**
 * @author JJAYCHEN
 * @date 2019/12/17 1:04 下午
 */

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/get-user-count", method = RequestMethod.GET)
    @AdminLoginRequired
    public ResultEntity getUserCount(@RequestParam int adminId) {
        Map<String, String> data = adminService.getUserCount();
        return ResultEntity.succeed(data);
    }

    @RequestMapping(value = "/get-user-list", method = RequestMethod.GET)
    @AdminLoginRequired
    public ResultEntity getUserList(@RequestParam int offset, @RequestParam int limit, @RequestParam int adminId) {
        List<Map<String, String>> data = adminService.getUserList(offset, limit);
        return ResultEntity.succeed(data);
    }

    @RequestMapping(value = "/delete-user", method = RequestMethod.POST)
    @AdminLoginRequired
    public ResultEntity deleteUser(@RequestBody AdminRequest adminRequest) {
        adminService.deleteUser(adminRequest);
        return ResultEntity.succeed();
    }

    @RequestMapping(value = "/get-booking-count", method = RequestMethod.GET)
    @AdminLoginRequired
    public ResultEntity getBookingCount(@RequestParam int adminId){
        Map<String, String> data = adminService.getBookingCount();
        return ResultEntity.succeed(data);
    }

    @RequestMapping(value = "/get-booking-list", method = RequestMethod.GET)
    @AdminLoginRequired
    public ResultEntity getBookingList(@RequestParam int offset, @RequestParam int limit, @RequestParam int adminId) {
        List<Map<String, String>> data = adminService.getBookingList(offset, limit);
        return ResultEntity.succeed(data);
    }

    @RequestMapping(value = "/delete-booking", method = RequestMethod.POST)
    @AdminLoginRequired
    public ResultEntity deleteBooking(@RequestBody AdminRequest adminRequest) {
        adminService.deleteBooking(adminRequest);
        return ResultEntity.succeed();
    }


    @PostMapping("/login")
    public ResultEntity login(@RequestBody AdminRequest adminRequest) {
        Map<String, String> loginInfo = adminService.login(adminRequest);
        return ResultEntity.succeed(loginInfo);
    }

    /**
     * 获取一天当中的订单数
     * @param date 日期 yyyy-MM-dd格式
     * @return 返回该天存在的订单数
     */
    @GetMapping("/booking-count-by-date")
    @AdminLoginRequired
    public ResultEntity getBookingCountByDate(@RequestParam String date, @RequestParam int adminId){
        int result = adminService.getBookingCountByDate(date);
        return ResultEntity.succeed(result);
    }

    /**
     * 获取每个场馆的订单数
     * @return 返回一个map，其中有两个list，stadiums为场馆名称的list, count为对应的订单数的list
     */
    @GetMapping("/booking-count-by-stadium")
    @AdminLoginRequired
    public ResultEntity getBookingCountGroupByStadium(@RequestParam int adminId){
        Map<String, Object> bookingCountInfo = adminService.getBookingCountGroupByStadium();
        return ResultEntity.succeed(bookingCountInfo);
    }
}
