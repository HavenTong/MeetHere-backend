package com.rhenium.meethere.service;

import com.rhenium.meethere.dao.AdminDao;
import com.rhenium.meethere.dao.CommentDao;
import com.rhenium.meethere.domain.Admin;
import com.rhenium.meethere.domain.Comment;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.dto.CommentRequest;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @author HavenTong
 * @date 2019/12/20 3:06 下午
 */
@SpringBootTest
class CommentServiceTest {

    @Mock
    private CommentDao commentDao;

    @Mock
    private AdminDao adminDao;

    @Mock
    private StringRedisTemplate redisTemplate;

    @InjectMocks
    private CommentServiceImpl commentService;




    @Test
    @DisplayName("管理员删除留言时，若管理员不存在，抛出异常")
    public void shouldThrowExceptionWhenDeletingWithAdminNotExist(){
        AdminRequest adminRequest = AdminRequest.builder().adminId(10).commentId(10).build();
        Throwable exception = assertThrows(MyException.class,
                () -> commentService.deleteCommentByAdmin(adminRequest));
        assertEquals("管理员不存在", exception.getMessage());
    }

    @Test
    @DisplayName("管理员删除留言时，若管理员ID正确，则删除正确评论")
    public void shouldDeleteCommentWhenAdminIsCorrect(){
        when(adminDao.findAdminById(anyInt())).thenReturn(new Admin());
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(1).commentId(1).build();
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        commentService.deleteCommentByAdmin(adminRequest);
        verify(commentDao, times(1))
                .deleteCommentById(idCaptor.capture());
        assertEquals(1, idCaptor.getValue());
    }

    @Test
    void shouldGetCommentByStadiumId() {
        when(commentDao.getCommentByStadiumId(1)).thenReturn(new ArrayList<>());
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        ArrayList<Map<String, String>> comments = commentService.getCommentByStadiumId(1);

        verify(commentDao, times(1)).getCommentByStadiumId(idCaptor.capture());
        assertAll(
                () -> assertEquals(1, idCaptor.getValue())
        );
    }

    @Test
    void shouldAddNewComment() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setStadiumId(1);
        commentRequest.setCustomerId(2);
        commentRequest.setCommentContent("Just for test");
        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);

        commentService.addNewComment(commentRequest);

        verify(commentDao, times(1)).addNewComment(commentCaptor.capture());
        assertAll(
                () -> assertEquals(1, commentCaptor.getValue().getStadiumId()),
                () -> assertEquals(2, commentCaptor.getValue().getCustomerId()),
                () -> assertEquals("Just for test", commentCaptor.getValue().getCommentContent())
        );

    }

    @Test
    @DisplayName("点赞时，若commentId为空，则抛出异常")
    void shouldThrowExceptionWhenCommentNotExist(){
        CommentRequest commentRequest = CommentRequest.builder()
                .customerId(1).build();
        Throwable exception = assertThrows(MyException.class,
                () -> commentService.updateLikes(commentRequest));
        assertEquals("评论不存在", exception.getMessage());

    }

    @Test
    @DisplayName("点赞时，若用户曾点过赞，则减少点赞数")
    void shouldDecreaseLikesWhenUserLikedOneComment(){
        HashOperations mockHashOperations = mock(HashOperations.class);
        when(mockHashOperations.get("comment:likes", "1:1")).thenReturn("1");
        when(redisTemplate.opsForHash()).thenReturn(mockHashOperations);
        CommentRequest commentRequest = CommentRequest.builder()
                .customerId(1).commentId(1).build();
        commentService.updateLikes(commentRequest);
        verify(mockHashOperations, times(1))
                .delete("comment:likes", "1:1");
        verify(commentDao, times(1))
                .decreaseLikesById(1);
    }

    @Test
    @DisplayName("点赞时，若用户没有点过赞，则增加点赞数")
    void shouldIncreaseLikesWhenUserNotLikedOneComment(){
        CommentRequest commentRequest = CommentRequest.builder()
                .commentId(1).customerId(1).build();
        HashOperations mockHashOperations = mock(HashOperations.class);
        when(redisTemplate.opsForHash()).thenReturn(mockHashOperations);
        when(mockHashOperations.get("comment:likes", "1:1")).thenReturn(null);
        commentService.updateLikes(commentRequest);
        verify(mockHashOperations, times(1))
                .put("comment:likes", "1:1", "1");
        verify(commentDao, times(1))
                .increaseLikesById(1);
    }
}