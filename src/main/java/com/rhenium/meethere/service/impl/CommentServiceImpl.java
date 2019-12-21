package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.AdminDao;
import com.rhenium.meethere.dao.CommentDao;
import com.rhenium.meethere.domain.Admin;
import com.rhenium.meethere.domain.Comment;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.dto.CommentRequest;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HavenTong
 * @date 2019/12/20 1:20 下午
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private AdminDao adminDao;

    @Override
    public void deleteCommentByAdmin(AdminRequest adminRequest) {
        Admin admin = adminDao.findAdminById(adminRequest.getAdminId());
        if (admin == null){
            throw new MyException(ResultEnum.ADMIN_NOT_EXIST);
        }
        commentDao.deleteCommentById(adminRequest.getCommentId());
    }

    @Override
    public ArrayList<Map<String, String>> getCommentByStadiumId(Integer stadiumId) {
        ArrayList<Comment> commentList = commentDao.getCommentByStadiumId(stadiumId);
        ArrayList<Map<String, String>> comments = new ArrayList<>();
        for(Comment comment : commentList) {
            HashMap<String, String> commentMap = new HashMap<>();
            commentMap.put("customerId", String.valueOf(comment.getCustomer().getCustomerId()));
            commentMap.put("userName", comment.getCustomer().getUserName());
            commentMap.put("commentPostTime", comment.getCommentPostTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            commentMap.put("commentContent", comment.getCommentContent());
            commentMap.put("likes", String.valueOf(comment.getLikes()));
            commentMap.put("commentId", String.valueOf(comment.getCommentId()));
            comments.add(commentMap);
        }
        return comments;
    }

    @Override
    public void addNewComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setStadiumId(commentRequest.getStadiumId());
        comment.setCustomerId(commentRequest.getCustomerId());
        comment.setCommentContent(commentRequest.getCommentContent());
        commentDao.addNewComment(comment);
    }

    @Override
    public void deleteComment(CommentRequest commentRequest) {
        commentDao.deleteCommentById(commentRequest.getCommentId());
    }
}
