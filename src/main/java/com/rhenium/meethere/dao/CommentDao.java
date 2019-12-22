package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.Comment;
import org.apache.ibatis.annotations.*;
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
//    @Select("SELECT comment.customer_id, comment_post_time, comment_content, likes, user_name " +
//            "FROM comment, customer " +
//            "WHERE comment.customer_id = customer.customer_id AND comment.stadium_id = #{stadiumId} " +
//            "ORDER BY comment_post_time DESC ")
//    ArrayList<Comment> getCommentByStadiumId(@Param("stadiumId") Integer id);

    @Select("SELECT *" +
            "FROM comment " +
            "WHERE stadium_id = #{stadiumId}")
    @Results({@Result(property = "customer", column = "customer_id",
              one = @One(select = "com.rhenium.meethere.dao.CustomerDao.findCustomerById"))})
    ArrayList<Comment> getCommentByStadiumId(@Param("stadiumId") Integer id);

    @Insert("INSERT INTO comment(stadium_id, customer_id, comment_content) " +
            "VALUES (#{stadiumId}, #{customerId}, #{commentContent})")
    int addNewComment(Comment comment);

    @Delete("DELETE FROM comment WHERE comment_id = #{commentId}")
    void deleteCommentById(@Param("commentId") int commentId);

    @Update("UPDATE comment SET likes = likes + 1 WHERE comment_id = #{commentId}")
    int increaseLikesById(@Param("commentId") int commentId);

    @Update("UPDATE comment SET likes = likes - 1 WHERE comment_id = #{commentId}")
    int decreaseLikesById(@Param("commentId") int commentId);

    @Select("SELECT * FROM comment WHERE comment_id = #{commentId}")
    Comment getCommentByCommentId(@Param("commentId") int commentId);
}
