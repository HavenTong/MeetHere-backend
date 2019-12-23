package com.rhenium.meethere.controller;

import com.rhenium.meethere.annotation.UserLoginRequired;
import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.service.StadiumService;
import com.rhenium.meethere.vo.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/17 16:58
 */
@RestController
@RequestMapping("/stadium")
public class StadiumController {
    @Autowired
    private StadiumService stadiumService;

    @GetMapping("/items")
    @UserLoginRequired
    public ResultEntity listNewsItems(@RequestParam int customerId) {
        ArrayList<Stadium> stadiums = stadiumService.listStadiumItems();
        return ResultEntity.succeed(stadiums);
    }

    @GetMapping("/message")
    @UserLoginRequired
    public ResultEntity getStadiumById(@RequestParam int id, @RequestParam int customerId) {
        Map<String, String> stadium = stadiumService.getStadiumById(id);
        return ResultEntity.succeed(stadium);
    }
}
