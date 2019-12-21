package com.rhenium.meethere.controller;

import com.rhenium.meethere.annotation.AdminLoginRequired;
import com.rhenium.meethere.annotation.UserLoginRequired;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.dto.CommentRequest;
import com.rhenium.meethere.service.CommentService;
import com.rhenium.meethere.vo.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author HavenTong
 * @date 2019/12/20 1:33 下午
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/delete-by-admin")
    @AdminLoginRequired
    public ResultEntity deleteByAdmin(@RequestBody AdminRequest adminRequest){
        commentService.deleteCommentByAdmin(adminRequest);
        return ResultEntity.succeed();
    }

    @GetMapping("/get-by-user")
    @UserLoginRequired
    public ResultEntity getCommentByStadiumId(@RequestParam int stadiumId, int customerId) {
        ArrayList<Map<String, String>> comments = commentService.getCommentByStadiumId(stadiumId);
        return ResultEntity.succeed(comments);
    }

    @PostMapping("/add")
    @UserLoginRequired
    public ResultEntity addComment(@RequestBody CommentRequest commentRequest) {
        commentService.addNewComment(commentRequest);
        return ResultEntity.succeed();
    }

    @PostMapping("/delete-by-user")
    @UserLoginRequired
    public ResultEntity deleteComment(@RequestBody CommentRequest commentRequest) {
        commentService.deleteComment(commentRequest);
        return ResultEntity.succeed();
    }
}
