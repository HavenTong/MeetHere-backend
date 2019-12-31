package com.rhenium.meethere.controller;

import com.rhenium.meethere.dao.CommentDao;
import com.rhenium.meethere.domain.Comment;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.dto.CommentRequest;
import com.rhenium.meethere.util.HttpRequestUtil;
import com.rhenium.meethere.vo.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/31 17:33
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private String BASE_URL = "/comment";

    @Autowired
    CommentDao commentDao;

    @Test
    @DisplayName("管理员删除评论时，若HTTP头部未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenDeletingCommentByAdminWithNoToken() {
        AdminRequest adminRequest = AdminRequest.builder().adminId(1).commentId(10000).build();

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/delete-by-admin", adminRequest, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员删除评论时，若TOKEN不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenDeletingCommentByAdminWithTokenNotMatch() {
        AdminRequest adminRequest = AdminRequest.builder().adminId(2).commentId(10000).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<AdminRequest> httpEntity = new HttpEntity<>(adminRequest, httpHeaders);

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/delete-by-admin", httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员正确删除评论")
    void shouldDeleteCorrectComment() {
        Comment comment = Comment.builder().stadiumId(1).customerId(507).commentContent("TestYes").build();
        commentDao.addNewComment(comment);
        AdminRequest adminRequest = AdminRequest.builder().adminId(1).commentId(comment.getCommentId()).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<AdminRequest> httpEntity = new HttpEntity<>(adminRequest, httpHeaders);

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/delete-by-admin", httpEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户获取场馆相关的评论时，若HTTP头部未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingCommentByCustomerWithoutToken() {
        HashMap<String, String> request = new HashMap<>();
        request.put("stadiumId", "1");
        request.put("customerId", "507");

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-by-user", request);

        ResponseEntity<ResultEntity> response = testRestTemplate.getForEntity(url, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户获取场馆相关的评论时，若TOKEN不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingCommentByCustomerWithWrongToken() {
        HashMap<String, String> request = new HashMap<>();
        request.put("stadiumId", "1");
        request.put("customerId", "506");
        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-by-user", request);
        ResponseEntity<ResultEntity> response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户正确获取场馆相关的评论")
    void shouldGetCorrectCommentByStadium() {
        HashMap<String, String> request = new HashMap<>();
        request.put("stadiumId", "1");
        request.put("customerId", "507");
        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-by-user", request);
        ResponseEntity<ResultEntity> response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        List<Map<String, String>> resultList = (List<Map<String, String>>) result.getData();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage()),
                () -> assertEquals("1", resultList.get(0).get("commentId")),
                () -> assertEquals("网球场不错，张勇老师赛高", resultList.get(0).get("commentContent"))
        );
    }

    @Test
    @DisplayName("管理员获取评论数量时，若HTTP头部未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingCommentCountByAdminWithoutToken() {
        HashMap<String, String> request = new HashMap<>();
        request.put("adminId", "1");

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-comment-count", request);

        ResponseEntity<ResultEntity> response = testRestTemplate.getForEntity(url, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取评论数量时，若TOKEN不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingCommentCountByAdminWithWrongToken() {
        HashMap<String, String> request = new HashMap<>();
        request.put("adminId", "2");
        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-comment-count", request);
        ResponseEntity<ResultEntity> response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取正确评论数量")
    void shouldGetCorrectCommentCount() {
        HashMap<String, String> request = new HashMap<>();
        request.put("adminId", "1");
        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-comment-count", request);
        ResponseEntity<ResultEntity> response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }


    @Test
    @DisplayName("管理员获取评论列表时，若HTTP头部未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingCommentListWithoutToken() {
        HashMap<String, String> request = new HashMap<>();
        request.put("offset", "0");
        request.put("limit", "1");
        request.put("adminId", "1");

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-comment-list", request);

        ResponseEntity<ResultEntity> response = testRestTemplate.getForEntity(url, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取评论列表时，若TOKEN不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenGettingCommentListWithWrongToken() {
        HashMap<String, String> request = new HashMap<>();
        request.put("offset", "0");
        request.put("limit", "1");
        request.put("adminId", "2");
        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-comment-list", request);
        ResponseEntity<ResultEntity> response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员正确获取评论列表")
    void shouldGetCorrectCommentList() {
        HashMap<String, String> request = new HashMap<>();
        request.put("offset", "0");
        request.put("limit", "1");
        request.put("adminId", "1");
        HttpHeaders headers = new HttpHeaders();
        headers.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTc3NDExMjE4LCJleHAiOjE1NzgwMTYwMTh9.HA5pcOCRn1VPVftATjQLADXEMJE7UJKbQg2FlazIhok");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-comment-list", request);
        ResponseEntity<ResultEntity> response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        List<Map<String, String>> resultList = (List<Map<String, String>>) result.getData();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage()),
                () -> assertEquals("1", resultList.get(0).get("commentId")),
                () -> assertEquals("网球场不错，张勇老师赛高", resultList.get(0).get("commentContent")),
                () -> assertEquals("1", resultList.get(0).get("stadiumId")),
                () -> assertEquals("507", resultList.get(0).get("customerId")),
                () -> assertEquals("YueChen", resultList.get(0).get("customerName"))
        );
    }

    @Test
    @DisplayName("用户添加评论时，若HTTP头部未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenAddingCommentWithNoToken() {
        CommentRequest commentRequest = CommentRequest.builder().stadiumId(1).commentContent("TestYes").customerId(507).build();

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/add", commentRequest, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户添加评论时，若TOKEN不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenAddingCommentWithWrongToken() {
        CommentRequest commentRequest = CommentRequest.builder().stadiumId(1).commentContent("TestYes").customerId(506).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<CommentRequest> httpEntity = new HttpEntity<>(commentRequest, httpHeaders);

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/add", httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户成功添加评论")
    void shouldAddCorrectComment() {
        CommentRequest commentRequest = CommentRequest.builder().stadiumId(1).commentContent("TestYes").customerId(507).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<CommentRequest> httpEntity = new HttpEntity<>(commentRequest, httpHeaders);

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/add", httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );

        if(result.getCode() == 0) {
            commentDao.deleteLatestComment();
        }
    }

    @Test
    @DisplayName("用户删除评论时，若HTTP头部未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenDeletingCommentWithoutToken() {
        CommentRequest commentRequest = CommentRequest.builder().commentId(10000).customerId(507).build();

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/delete-by-user", commentRequest, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户删除评论时，若若TOKEN不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenDeletingCommentWithWrongToken() {
        CommentRequest commentRequest = CommentRequest.builder().commentId(10000).customerId(506).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<CommentRequest> httpEntity = new HttpEntity<>(commentRequest, httpHeaders);

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/delete-by-user", httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户正确删除评论")
    void shouldDeleteCorrectCommentByCustomer() {
        Comment comment = Comment.builder().stadiumId(1).customerId(507).commentContent("TestYes").build();
        commentDao.addNewComment(comment);

        CommentRequest commentRequest = CommentRequest.builder().commentId(comment.getCommentId()).customerId(507).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<CommentRequest> httpEntity = new HttpEntity<>(commentRequest, httpHeaders);

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/delete-by-user", httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户更新点赞信息时，若HTTP头部未携带TOKEN，则返回异常信息")
    void shouldGetExceptionMessageWhenUpdateLikesWithoutToken() {
        CommentRequest commentRequest = CommentRequest.builder().commentId(10000).customerId(507).build();

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/update-likes", commentRequest, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户更新点赞信息时，若TOKEN不匹配，则返回异常信息")
    void shouldGetExceptionMessageWhenUpdateLikesWithWrongToken() {
        CommentRequest commentRequest = CommentRequest.builder().commentId(10000).customerId(506).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<CommentRequest> httpEntity = new HttpEntity<>(commentRequest, httpHeaders);

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/update-likes", httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户正确更新点赞信息")
    void shouldUpdateCorrectLikes() {
        Comment comment = Comment.builder().stadiumId(1).customerId(507).commentContent("TestYes").build();
        commentDao.addNewComment(comment);

        CommentRequest commentRequest = CommentRequest.builder().commentId(comment.getCommentId()).customerId(507).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MDciLCJpYXQiOjE1Nzc0NTQ0NDMsImV4cCI6MTU3OTUyODA0M30.iT_dyXZ13mqjND1XfDNqvELEWwvgusJwp6mHUmLKNmo");
        HttpEntity<CommentRequest> httpEntity = new HttpEntity<>(commentRequest, httpHeaders);

        ResponseEntity<ResultEntity> response = testRestTemplate.postForEntity(BASE_URL + "/update-likes", httpEntity, ResultEntity.class);
        ResultEntity result = response.getBody();

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage()),
                () -> assertEquals(1, commentDao.getLikesByCommentId(comment.getCommentId()))
        );

        if(result.getCode() == 0) {
            commentDao.deleteLatestComment();
        }
    }
}
