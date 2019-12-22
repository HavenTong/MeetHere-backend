package com.rhenium.meethere.service;

import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.dto.CommentRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author HavenTong
 * @date 2019/12/20 1:19 下午
 */
public interface CommentService {

    void deleteCommentByAdmin(AdminRequest adminRequest);

    ArrayList<Map<String, String>> getCommentByStadiumId(Integer stadiumId);

    void addNewComment(CommentRequest commentRequest);
    void deleteComment(CommentRequest commentRequest);

    List<Map<String, String>> getCommentList(int offset, int limit);

    Map<String, String> getCommentCount();
    void updateLikes(CommentRequest commentRequest);
}
