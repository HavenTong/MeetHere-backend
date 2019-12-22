package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.AdminDao;
import com.rhenium.meethere.dao.CommentDao;
import com.rhenium.meethere.domain.Admin;
import com.rhenium.meethere.domain.Comment;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.dto.CommentRequest;
import com.rhenium.meethere.enums.LikesEnum;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author HavenTong
 * @date 2019/12/20 1:20 下午
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private AdminDao adminDao;

    @Override
    public void deleteCommentByAdmin(AdminRequest adminRequest) {
        Admin admin = adminDao.findAdminById(adminRequest.getAdminId());
        if (admin == null) {
            throw new MyException(ResultEnum.ADMIN_NOT_EXIST);
        }
        commentDao.deleteCommentById(adminRequest.getCommentId());
    }

    @Override
    public ArrayList<Map<String, String>> getCommentByStadiumId(Integer stadiumId) {
        ArrayList<Comment> commentList = commentDao.getCommentByStadiumId(stadiumId);
        ArrayList<Map<String, String>> comments = new ArrayList<>();
        for (Comment comment : commentList) {
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

    @Override
    public List<Map<String, String>> getCommentList(int offset, int limit) {
        if (offset < 0) {
            throw new MyException(ResultEnum.INVALID_OFFSET);
        }
        if (limit < 1) {
            throw new MyException(ResultEnum.INVALID_LIMIT);
        }
        List<Comment> commentList = commentDao.getCommentList(offset, limit);
        List<Map<String, String>> data = new ArrayList<>();

        for (Comment comment : commentList) {
            Map<String, String> commentItem = new HashMap<>();
            commentItem.put("commentId", String.valueOf(comment.getCommentId()));
            commentItem.put("commentPostTime", comment.getCommentPostTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            commentItem.put("commentContent", comment.getCommentContent());
            commentItem.put("likes", String.valueOf(comment.getLikes()));
            commentItem.put("customerId", String.valueOf(comment.getCustomerId()));
            commentItem.put("stadiumId", String.valueOf(comment.getStadiumId()));
            commentItem.put("customerName", comment.getCustomer().getUserName());
            commentItem.put("stadiumName", comment.getStadium().getStadiumName());

            data.add(commentItem);
        }
        return data;
    }

    @Override
    public Map<String, String> getCommentCount() {
        Map<String, String> data = new HashMap<>();
        data.put("count", String.valueOf(commentDao.getCommentCount()));
        return data;
    }

    @Override
    public void updateLikes(CommentRequest commentRequest) {
        if (Objects.isNull(commentRequest.getCommentId())){
            throw new MyException(ResultEnum.COMMENT_NOT_EXIST);
        }
        String hashName = "comment:likes";
        int customerId = commentRequest.getCustomerId();
        int commentId = commentRequest.getCommentId();
        String key = commentId + ":" + customerId;
        String liked = (String) redisTemplate.opsForHash().get(hashName, key);
        // 还未点赞
        if (Objects.isNull(liked)){
            redisTemplate.opsForHash().put(hashName, key, LikesEnum.LIKED.getCode().toString());
            commentDao.increaseLikesById(commentId);
        } else {
            // 已点赞
            redisTemplate.opsForHash().delete(hashName, key);
            commentDao.decreaseLikesById(commentId);
        }
    }
}
