package com.rhenium.meethere.controller;

import com.rhenium.meethere.annotation.AdminLoginRequired;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.service.CommentService;
import com.rhenium.meethere.vo.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
