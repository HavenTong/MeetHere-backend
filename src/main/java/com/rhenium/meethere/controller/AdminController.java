package com.rhenium.meethere.controller;

import com.rhenium.meethere.service.AdminService;
import com.rhenium.meethere.vo.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping(value = "get-user-count", method = RequestMethod.GET)
    public ResultEntity getUserCount() {
        int count = adminService.getUserCount();
        return ResultEntity.succeed(count);
    }
}
