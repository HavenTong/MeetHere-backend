package com.rhenium.meethere.controller;

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

    // TODO: 管理员的请求也应该被检测
    @RequestMapping(value = "/get-user-count", method = RequestMethod.GET)
    public ResultEntity getUserCount() {
        Map<String, String> data = adminService.getUserCount();
        return ResultEntity.succeed(data);
    }

    // TODO: 管理员的请求也应该被检测，另外，id 应该作为 param 还是 body?(暂时没传)
    @RequestMapping(value = "/get-user-list", method = RequestMethod.GET)
    public ResultEntity getUserList(@RequestParam int offset, @RequestParam int limit) {
        List<Map<String, String>> data = adminService.getUserList(offset, limit);
        return ResultEntity.succeed(data);
    }


    @PostMapping("/login")
    public ResultEntity login(@RequestBody AdminRequest adminRequest) {
        Map<String, String> loginInfo = adminService.login(adminRequest);
        return ResultEntity.succeed(loginInfo);
    }

}
