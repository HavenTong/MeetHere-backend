package com.rhenium.meethere.controller;

import com.rhenium.meethere.annotation.AdminLoginRequired;
import com.rhenium.meethere.domain.Booking;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.service.AdminService;
import com.rhenium.meethere.vo.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/get-booking-list", method = RequestMethod.GET)
    @AdminLoginRequired
    public ResultEntity getBookingList(@RequestParam int offset, @RequestParam int limit, @RequestParam int adminId) {
        List<Booking> data = adminService.getBookingList(offset, limit);
        return ResultEntity.succeed(data);
    }

    @PostMapping("/login")
    public ResultEntity login(@RequestBody AdminRequest adminRequest) {
        Map<String, String> loginInfo = adminService.login(adminRequest);
        return ResultEntity.succeed(loginInfo);
    }
}
