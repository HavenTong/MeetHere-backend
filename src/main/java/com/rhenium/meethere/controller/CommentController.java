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
import java.util.List;
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

    // TODO: 鉴权
    @GetMapping("/get-by-user")
    @UserLoginRequired
    public ResultEntity getCommentByStadiumId(@RequestParam int stadiumId,
                                              @RequestParam int customerId) {
        ArrayList<Map<String, String>> comments = commentService.getCommentByStadiumId(stadiumId);
        return ResultEntity.succeed(comments);
    }

    @RequestMapping(value = "/get-comment-count", method = RequestMethod.GET)
    @AdminLoginRequired
    public ResultEntity getCommentCount(@RequestParam int adminId) {
        Map<String, String> data = commentService.getCommentCount();
        return ResultEntity.succeed(data);
    }

    @RequestMapping(value = "/get-comment-list", method = RequestMethod.GET)
    @AdminLoginRequired
    public ResultEntity getCommentList(@RequestParam int offset, @RequestParam int limit, @RequestParam int adminId) {
        List<Map<String, String>> data = commentService.getCommentList(offset, limit);
        return ResultEntity.succeed(data);
    }

    @PostMapping("/add")
    @UserLoginRequired
    public ResultEntity addComment(@RequestBody CommentRequest commentRequest) {
        commentService.addNewComment(commentRequest);
        return ResultEntity.succeed();
    }

    @PostMapping("/delete-by-user")
    @UserLoginRequired
    public ResultEntity deleteByCustomer(@RequestBody CommentRequest commentRequest) {
        commentService.deleteCommentByCustomer(commentRequest);
        return ResultEntity.succeed();
    }

    @PostMapping("/update-likes")
    @UserLoginRequired
    public ResultEntity updateLikes(@RequestBody CommentRequest commentRequest){
        commentService.updateLikes(commentRequest);
        return ResultEntity.succeed();
    }
}
