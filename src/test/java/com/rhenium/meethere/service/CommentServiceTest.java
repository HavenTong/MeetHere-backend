package com.rhenium.meethere.service;

import com.rhenium.meethere.dao.AdminDao;
import com.rhenium.meethere.dao.CommentDao;
import com.rhenium.meethere.domain.*;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.dto.CommentRequest;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.time.LocalDateTime;
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
    @DisplayName("用户删除评论时，如果该评论是用户发的，则删除")
    void shouldDeleteCommentByCustomerWhenIsCustomersComment() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setCustomerId(1);
        commentRequest.setCommentId(2);
        Comment comment = new Comment();
        comment.setCustomerId(1);
        when(commentDao.getCommentByCommentId(2)).thenReturn(comment);
        ArgumentCaptor<Integer> commentIdCaptor = ArgumentCaptor.forClass(Integer.class);

        commentService.deleteCommentByCustomer(commentRequest);

        verify(commentDao, times(1)).deleteCommentById(commentIdCaptor.capture());
        assertAll(
                () -> assertEquals(2, commentIdCaptor.getValue())
        );
    }

    @Test
    @DisplayName("用户删除评论时，如果该评论不是该用户发的，则抛出错误")
    void shouldThrowExceptionWhenDeletedCommentIsNotCustomers() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setCustomerId(1);
        commentRequest.setCommentId(2);
        Comment comment = new Comment();
        comment.setCustomerId(3);
        when(commentDao.getCommentByCommentId(2)).thenReturn(comment);

        Throwable exception = assertThrows(MyException.class,
                () -> commentService.deleteCommentByCustomer(commentRequest));
        assertEquals("删除非本人的评论", exception.getMessage());
    }

    @Test
    @DisplayName("点赞时，若commentId为空，则抛出异常")
    void shouldThrowExceptionWhenCommentIdIsEmpty(){
        CommentRequest commentRequest = CommentRequest.builder()
                .customerId(1).build();
        Throwable exception = assertThrows(MyException.class,
                () -> commentService.updateLikes(commentRequest));
        assertEquals("评论不存在", exception.getMessage());
    }

    @Test
    @DisplayName("点赞时，若评论不存在，则抛出异常")
    void shouldThrowExceptionWhenCommentNotExist(){
        CommentRequest commentRequest = CommentRequest.builder()
                .customerId(1).commentId(10).build();
        when(commentDao.getCommentByCommentId(10)).thenReturn(null);
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

    @Test
    @DisplayName("当获取评论列表的offset参数小于0时，抛出异常")
    void shouldThrowExceptionWhenGetCommentListWithWrongOffset() {
        MyException exception = assertThrows(MyException.class, () -> commentService.getCommentList(-1, 20));
        assertEquals(ResultEnum.INVALID_OFFSET.getCode(), exception.getCode());

        verify(commentDao, never()).getCommentList(-1, 20);
    }

    @Test
    @DisplayName("当获取评论列表的limit参数小于1时，抛出异常")
    void shouldThrowExceptionWhenGetCommentListWithWrongLimit() {
        MyException exception = assertThrows(MyException.class, () -> commentService.getCommentList(0, 0));
        assertEquals(ResultEnum.INVALID_LIMIT.getCode(), exception.getCode());

        verify(commentDao, never()).getCommentList(0, 0);
    }

    @Test
    @DisplayName("当获取评论列表的参数正常时，返回符合预期格式的数据")
    void shouldThrowExceptionWhenGetCommentListWithCorrectOffsetAndLimit() {
        List<Comment> commentList = new ArrayList<>();
        commentList.add(
                Comment.builder().commentId(1)
                        .commentPostTime(LocalDateTime.of(2019, 12, 20, 12, 0, 0))
                        .commentContent("评论内容")
                        .likes(32)
                        .customerId(1)
                        .stadiumId(1)
                        .customer(Customer.builder().customerId(1).userName("JJAYCHEN").build())
                        .stadium(Stadium.builder().stadiumId(1).stadiumName("中北羽毛球馆").build())
                        .build()
        );
        when(commentDao.getCommentList(0, 20)).thenReturn(commentList);
        List<Map<String, String>> data = commentService.getCommentList(0, 20);

        verify(commentDao, times(1)).getCommentList(0, 20);

        Map<String, String> dataOne = data.get(0);
        assertEquals("1", dataOne.get("commentId"));
        assertEquals("2019-12-20 12:00:00", dataOne.get("commentPostTime"));
        assertEquals("评论内容", dataOne.get("commentContent"));
        assertEquals("32", dataOne.get("likes"));
        assertEquals("1", dataOne.get("customerId"));
        assertEquals("1", dataOne.get("stadiumId"));
        assertEquals("JJAYCHEN", dataOne.get("customerName"));
        assertEquals("中北羽毛球馆", dataOne.get("stadiumName"));
    }

    @Test
    @DisplayName("获取评论数量时，获取结果正确")
    void shouldGetCorrectCommentCount() {
        when(commentDao.getCommentCount()).thenReturn(3);
        Map<String, String> result = commentService.getCommentCount();
        verify(commentDao, times(1))
                .getCommentCount();
        assertEquals("3", result.get("count"));
    }
}