package com.rhenium.meethere.controller;

import com.rhenium.meethere.annotation.UserLoginRequired;
import com.rhenium.meethere.domain.Comment;
import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.dto.StadiumRequest;
import com.rhenium.meethere.service.StadiumService;
import com.rhenium.meethere.vo.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

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
    public ResultEntity listNewsItems() {
        ArrayList<Stadium> stadiums = stadiumService.listStadiumItems();
        return ResultEntity.succeed(stadiums);
    }

    @GetMapping("/message")
    @UserLoginRequired
    public ResultEntity getStadiumById(@RequestParam int id) {
        StadiumRequest stadium = stadiumService.getStadiumById(id);
        return ResultEntity.succeed(stadium);
    }

    @GetMapping("/comments")
//    @UserLoginRequired
    public ResultEntity getCommentByStadiumId(@RequestParam int stadiumId) {
        ArrayList<Comment> comments = stadiumService.getCommentByStadiumId(stadiumId);
        return ResultEntity.succeed(comments);
    }
}
