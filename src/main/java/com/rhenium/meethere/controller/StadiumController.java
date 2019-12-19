package com.rhenium.meethere.controller;

import com.rhenium.meethere.annotation.UserLoginRequired;
import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.dto.CommentRequest;
import com.rhenium.meethere.service.StadiumService;
import com.rhenium.meethere.vo.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResultEntity listNewsItems() {
        ArrayList<Stadium> stadiums = stadiumService.listStadiumItems();
        return ResultEntity.succeed(stadiums);
    }

    @GetMapping("/message")
    @UserLoginRequired
    public ResultEntity getStadiumById(@RequestParam int id) {
        Map<String, String> stadium = stadiumService.getStadiumById(id);
        return ResultEntity.succeed(stadium);
    }

    @GetMapping("/comments")
    @UserLoginRequired
    public ResultEntity getCommentByStadiumId(@RequestParam int stadiumId) {
        ArrayList<Map<String, String>> comments = stadiumService.getCommentByStadiumId(stadiumId);
        return ResultEntity.succeed(comments);
    }

    @PostMapping("/addComment")
//    @UserLoginRequired
    public ResultEntity addComment(@RequestBody CommentRequest commentRequest) {
        stadiumService.addNewComment(commentRequest);
        return ResultEntity.succeed();
    }

    @PostMapping("/deleteComment")
//    @UserLoginRequired
    public ResultEntity deleteComment(@RequestBody CommentRequest commentRequest) {
        stadiumService.deleteComment(commentRequest);
        return ResultEntity.succeed();
    }
}
