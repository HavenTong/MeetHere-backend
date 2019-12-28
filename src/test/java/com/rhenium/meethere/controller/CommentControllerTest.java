package com.rhenium.meethere.controller;

import com.alibaba.fastjson.JSON;
import com.rhenium.meethere.domain.Admin;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.dto.CommentRequest;
import com.rhenium.meethere.service.CommentService;
import com.rhenium.meethere.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Array;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author HavenTong
 * @date 2019/12/27 11:23 下午
 */
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("管理员删除评论时，若HTTP头部未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenDeletingCommentByAdminWithNoToken() throws Exception{
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(1).commentId(1).build();
        ResultActions perform = mockMvc.perform(post("/comment/delete-by-admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JSON.toJSONString(adminRequest)));
        verify(commentService, never())
                .deleteCommentByAdmin(adminRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("管理员删除评论时，若TOKEN不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenDeletingCommentByAdminWithTokenNotMatch() throws Exception{
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(2).commentId(1).build();
        ResultActions perform = mockMvc.perform(post("/comment/delete-by-admin")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDYxMDI1LCJleHAiOjE1Nzk1MzQ2MjV9.GSbOPPHFr5IGon0SNMJPjldtD0S5XAyXyTwps2aCtYk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(adminRequest)));
        verify(commentService, never())
                .deleteCommentByAdmin(adminRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }

    @Test
    @DisplayName("管理员正确删除评论")
    void shouldDeleteCorrectComment() throws Exception{
        AdminRequest adminRequest = AdminRequest.builder()
                .adminId(1).commentId(1).build();
        ResultActions perform = mockMvc.perform(post("/comment/delete-by-admin")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDYxMDI1LCJleHAiOjE1Nzk1MzQ2MjV9.GSbOPPHFr5IGon0SNMJPjldtD0S5XAyXyTwps2aCtYk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(adminRequest)));
        verify(commentService, times(1))
                .deleteCommentByAdmin(adminRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @DisplayName("用户获取场馆相关的评论时，若HTTP头部未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingCommentByCustomerWithoutToken() throws Exception{
        ResultActions perform = mockMvc.perform(get("/comment/get-by-user")
            .param("stadiumId", "1")
            .param("customerId", "501"));
        verify(commentService, never())
                .getCommentByStadiumId(1, 501);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("用户获取场馆相关的评论时，若TOKEN不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingCommentByCustomerWithWrongToken() throws Exception{
        ResultActions perform = mockMvc.perform(get("/comment/get-by-user")
                .param("stadiumId", "1")
                .param("customerId", "501")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));
        verify(commentService, never())
                .getCommentByStadiumId(1, 501);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }

    @Test
    @DisplayName("用户正确获取场馆相关的评论")
    void shouldGetCorrectCommentByStadium() throws Exception{
        Map<String, String> firstComment = new HashMap<>();
        firstComment.put("customerId", "502");
        firstComment.put("userName", "first user");
        firstComment.put("commentPostTime", "2019-12-25 08:00:00");
        firstComment.put("commentContent", "good");
        firstComment.put("likes", "10");
        firstComment.put("commentId", "1");
        firstComment.put("liked", "0");
        Map<String, String> secondComment = new HashMap<>();
        secondComment.put("customerId", "502");
        secondComment.put("userName", "second user");
        secondComment.put("commentPostTime", "2019-12-26 18:00:00");
        secondComment.put("commentContent", "bad");
        secondComment.put("likes", "0");
        secondComment.put("commentId", "2");
        secondComment.put("liked", "1");
        ArrayList<Map<String, String>> commentList = new ArrayList<>(Arrays.asList(firstComment, secondComment));
        when(commentService.getCommentByStadiumId(1, 502))
                .thenReturn(commentList);
        ResultActions perform = mockMvc.perform(get("/comment/get-by-user")
                .param("stadiumId", "1")
                .param("customerId", "502")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw"));
        verify(commentService, times(1))
                .getCommentByStadiumId(1, 502);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].customerId").value("502"))
                .andExpect(jsonPath("$.data[0].userName").value("first user"))
                .andExpect(jsonPath("$.data[0].commentPostTime").value("2019-12-25 08:00:00"))
                .andExpect(jsonPath("$.data[0].commentContent").value("good"))
                .andExpect(jsonPath("$.data[0].likes").value("10"))
                .andExpect(jsonPath("$.data[0].commentId").value("1"))
                .andExpect(jsonPath("$.data[0].liked").value("0"))
                .andExpect(jsonPath("$.data[1].customerId").value("502"))
                .andExpect(jsonPath("$.data[1].userName").value("second user"))
                .andExpect(jsonPath("$.data[1].commentPostTime").value("2019-12-26 18:00:00"))
                .andExpect(jsonPath("$.data[1].commentContent").value("bad"))
                .andExpect(jsonPath("$.data[1].likes").value("0"))
                .andExpect(jsonPath("$.data[1].commentId").value("2"))
                .andExpect(jsonPath("$.data[1].liked").value("1"))
                .andExpect(jsonPath("$.data[2]").doesNotExist());
    }

    @Test
    @DisplayName("管理员获取评论数量时，若HTTP头部未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingCommentCountByAdminWithoutToken() throws Exception{
        ResultActions perform = mockMvc.perform(get("/comment/get-comment-count")
                .param("adminId", "1"));
        verify(commentService, never())
                .getCommentCount();
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("管理员获取评论数量时，若TOKEN不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingCommentCountByAdminWithWrongToken() throws Exception{
        ResultActions perform = mockMvc.perform(get("/comment/get-comment-count")
                .param("adminId", "2")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDYxMDI1LCJleHAiOjE1Nzk1MzQ2MjV9.GSbOPPHFr5IGon0SNMJPjldtD0S5XAyXyTwps2aCtYk"));
        verify(commentService, never())
                .getCommentCount();
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }

    @Test
    @DisplayName("管理员获取正确评论数量")
    void shouldGetCorrectCommentCount() throws Exception{
        Map<String, String> commentCount = new HashMap<>();
        commentCount.put("count", "20");
        when(commentService.getCommentCount()).thenReturn(commentCount);
        ResultActions perform = mockMvc.perform(get("/comment/get-comment-count")
                .param("adminId", "1")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDYxMDI1LCJleHAiOjE1Nzk1MzQ2MjV9.GSbOPPHFr5IGon0SNMJPjldtD0S5XAyXyTwps2aCtYk"));
        verify(commentService, times(1))
                .getCommentCount();
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value("20"));
    }

    @Test
    @DisplayName("管理员获取评论列表时，若HTTP头部未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingCommentListWithoutToken() throws Exception{
        ResultActions perform = mockMvc.perform(get("/comment/get-comment-list")
                .param("offset", "0")
                .param("limit", "2")
                .param("adminId", "1"));
        verify(commentService, never())
                .getCommentList(0, 2);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("管理员获取评论列表时，若TOKEN不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingCommentListWithWrongToken() throws Exception{
        ResultActions perform = mockMvc.perform(get("/comment/get-comment-list")
                .param("offset", "0")
                .param("limit", "2")
                .param("adminId", "2")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDYxMDI1LCJleHAiOjE1Nzk1MzQ2MjV9.GSbOPPHFr5IGon0SNMJPjldtD0S5XAyXyTwps2aCtYk"));
        verify(commentService, never())
                .getCommentList(0, 2);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }

    @Test
    @DisplayName("管理员正确获取评论列表")
    void shouldGetCorrectCommentList() throws Exception{
        Map<String, String> firstComment = new HashMap<>();
        firstComment.put("commentId", "1");
        firstComment.put("commentPostTime", "2019-12-25 08:00:00");
        firstComment.put("commentContent", "good");
        firstComment.put("likes", "10");
        firstComment.put("customerId", "501");
        firstComment.put("customerName", "first user");
        firstComment.put("stadiumId", "1");
        firstComment.put("stadiumName", "tennis court");
        Map<String, String> secondComment = new HashMap<>();
        secondComment.put("commentId", "2");
        secondComment.put("commentPostTime", "2019-12-26 18:00:00");
        secondComment.put("commentContent", "bad");
        secondComment.put("likes", "2");
        secondComment.put("customerId", "502");
        secondComment.put("customerName", "second user");
        secondComment.put("stadiumId", "2");
        secondComment.put("stadiumName", "volleyball court");
        List<Map<String, String>> commentList = new ArrayList<>(Arrays.asList(firstComment, secondComment));
        when(commentService.getCommentList(0, 2))
                .thenReturn(commentList);
        ResultActions perform = mockMvc.perform(get("/comment/get-comment-list")
                .param("offset", "0")
                .param("limit", "2")
                .param("adminId", "1")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDYxMDI1LCJleHAiOjE1Nzk1MzQ2MjV9.GSbOPPHFr5IGon0SNMJPjldtD0S5XAyXyTwps2aCtYk"));
        verify(commentService, times(1))
                .getCommentList(0, 2);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].commentId").value("1"))
                .andExpect(jsonPath("$.data[0].commentPostTime").value("2019-12-25 08:00:00"))
                .andExpect(jsonPath("$.data[0].commentContent").value("good"))
                .andExpect(jsonPath("$.data[0].likes").value("10"))
                .andExpect(jsonPath("$.data[0].customerId").value("501"))
                .andExpect(jsonPath("$.data[0].customerName").value("first user"))
                .andExpect(jsonPath("$.data[0].stadiumId").value("1"))
                .andExpect(jsonPath("$.data[0].stadiumName").value("tennis court"))
                .andExpect(jsonPath("$.data[1].commentId").value("2"))
                .andExpect(jsonPath("$.data[1].commentPostTime").value("2019-12-26 18:00:00"))
                .andExpect(jsonPath("$.data[1].commentContent").value("bad"))
                .andExpect(jsonPath("$.data[1].likes").value("2"))
                .andExpect(jsonPath("$.data[1].customerId").value("502"))
                .andExpect(jsonPath("$.data[1].customerName").value("second user"))
                .andExpect(jsonPath("$.data[1].stadiumId").value("2"))
                .andExpect(jsonPath("$.data[1].stadiumName").value("volleyball court"))
                .andExpect(jsonPath("$.data[2]").doesNotExist());
    }

    @Test
    @DisplayName("用户添加评论时，若HTTP头部未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenAddingCommentWithNoToken() throws Exception{
        CommentRequest commentRequest = CommentRequest.builder()
                .customerId(502).commentContent("good").build();
        ResultActions perform = mockMvc.perform(post("/comment/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(commentRequest)));
        verify(commentService, never())
                .addNewComment(commentRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("用户添加评论时，若TOKEN不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenAddingCommentWithWrongToken() throws Exception{
        CommentRequest commentRequest = CommentRequest.builder()
                .customerId(501).commentContent("good").build();
        ResultActions perform = mockMvc.perform(post("/comment/add")
                .header("TOKEN","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(commentRequest)));
        verify(commentService, never())
                .addNewComment(commentRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }

    @Test
    @DisplayName("用户成功添加评论")
    void shouldAddCorrectComment() throws Exception{
        CommentRequest commentRequest = CommentRequest.builder()
                .customerId(502).commentContent("good").build();
        ResultActions perform = mockMvc.perform(post("/comment/add")
                .header("TOKEN","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(commentRequest)));
        verify(commentService, times(1))
                .addNewComment(commentRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @DisplayName("用户删除评论时，若HTTP头部未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenDeletingCommentWithoutToken() throws Exception{
        CommentRequest commentRequest = CommentRequest.builder()
                .customerId(502).commentId(1).build();
        ResultActions perform = mockMvc.perform(post("/comment/delete-by-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(commentRequest)));
        verify(commentService, never())
                .deleteCommentByCustomer(commentRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("用户删除评论时，若若TOKEN不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenDeletingCommentWithWrongToken() throws Exception{
        CommentRequest commentRequest = CommentRequest.builder()
                .customerId(501).commentId(1).build();
        ResultActions perform = mockMvc.perform(post("/comment/delete-by-user")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(commentRequest)));
        verify(commentService, never())
                .deleteCommentByCustomer(commentRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }


    @Test
    @DisplayName("用户正确删除评论")
    void shouldDeleteCorrectCommentByCustomer() throws Exception{
        CommentRequest commentRequest = CommentRequest.builder()
                .customerId(502).commentId(1).build();
        ResultActions perform = mockMvc.perform(post("/comment/delete-by-user")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(commentRequest)));
        verify(commentService, times(1))
                .deleteCommentByCustomer(commentRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @DisplayName("用户更新点赞信息时，若HTTP头部未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenUpdateLikesWithoutToken() throws Exception{
        CommentRequest commentRequest = CommentRequest.builder()
                .customerId(502).commentId(1).build();
        ResultActions perform = mockMvc.perform(post("/comment/update-likes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(commentRequest)));
        verify(commentService, never())
                .updateLikes(commentRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("HTTP头部未携带TOKEN"));
    }

    @Test
    @DisplayName("用户更新点赞信息时，若TOKEN不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenUpdateLikesWithWrongToken() throws Exception{
        CommentRequest commentRequest = CommentRequest.builder()
                .customerId(501).commentId(1).build();
        ResultActions perform = mockMvc.perform(post("/comment/update-likes")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(commentRequest)));
        verify(commentService, never())
                .updateLikes(commentRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TOKEN不匹配"));
    }

    @Test
    @DisplayName("用户正确更新点赞信息")
    void shouldUpdateCorrectLikes() throws Exception{
        CommentRequest commentRequest = CommentRequest.builder()
                .customerId(502).commentId(1).build();
        ResultActions perform = mockMvc.perform(post("/comment/update-likes")
                .header("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDIiLCJpYXQiOjE1Nzc0NTI3MzgsImV4cCI6MTU3OTUyNjMzOH0.ozzYAgd56bNUFRm9VQoOK1nIkxdPKJTvmbkmgxug9Nw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(commentRequest)));
        verify(commentService, times(1))
                .updateLikes(commentRequest);
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
    }
}