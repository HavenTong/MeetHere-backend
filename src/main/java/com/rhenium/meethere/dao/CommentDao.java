package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/18 20:01
 */
@Repository
@Mapper
public interface CommentDao {
    @Select("SELECT comment.customer_id, comment_post_time, comment_content, likes, user_name " +
            "FROM comment, customer " +
            "WHERE comment.customer_id = customer.customer_id AND comment.stadium_id = #{stadiumId} " +
            "ORDER BY comment_post_time DESC ")
    ArrayList<Comment> getCommentByStadiumId(@Param("stadiumId") Integer id);
}
