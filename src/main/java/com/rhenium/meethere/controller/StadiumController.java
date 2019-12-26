package com.rhenium.meethere.controller;

import com.rhenium.meethere.annotation.AdminLoginRequired;
import com.rhenium.meethere.annotation.UserLoginRequired;
import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.dto.StadiumRequest;
import com.rhenium.meethere.service.StadiumService;
import com.rhenium.meethere.vo.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

    @GetMapping("/items-for-admin")
    @AdminLoginRequired
    public ResultEntity getStadiumsForAdmin(@RequestParam int offset,
                                            @RequestParam int limit,
                                            @RequestParam int adminId){
        List<Map<String, Object>> stadiums =
                stadiumService.findStadiumsForAdmin(offset, limit);
        return ResultEntity.succeed(stadiums);
    }

    @PostMapping("/update")
    @AdminLoginRequired
    public ResultEntity updateStadiumInfo(@Validated @RequestBody StadiumRequest stadiumRequest){
        stadiumService.updateStadiumInfoByAdmin(stadiumRequest);
        return ResultEntity.succeed();
    }

    @PostMapping("/delete")
    @AdminLoginRequired
    public ResultEntity deleteStadium(@RequestBody StadiumRequest stadiumRequest){
        stadiumService.deleteStadiumInfoByAdmin(stadiumRequest);
        return ResultEntity.succeed();
    }
}
