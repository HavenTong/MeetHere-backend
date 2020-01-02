package com.rhenium.meethere.controller;

import com.rhenium.meethere.dao.StadiumDao;
import com.rhenium.meethere.dto.StadiumRequest;
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


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author JJAYCHEN
 * @date 2019/12/28 10:02 下午
 */

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StadiumControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StadiumDao stadiumDao;

    private String BASE_URL = "/stadium";

    @Test
    @DisplayName("获取场馆个数时，若HTTP头部未携带TOKEN，返回异常结果")
    public void shouldGetExceptionMessageWhenRegisteringWithEmailExisted() {
        Map<String, String> map = new HashMap<>();
        map.put("adminId", "1");
        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-stadium-count", map);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                getForEntity(url, ResultEntity.class);
        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("获取场馆个数时，若HTTP头部携带的TOKEN与adminId不匹配，返回异常结果")
    public void shouldReturnExceptionMessageWhenGetStadiumCountWithWrongToken() {
        Map<String, String> map = new HashMap<>();
        map.put("adminId", "1");
        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-stadium-count", map);

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);


        ResponseEntity<ResultEntity> response = testRestTemplate.
                exchange(url, HttpMethod.GET, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("获取场馆个数时，若HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    public void shouldGetStadiumCountWithCorrectToken() {
        Map<String, String> map = new HashMap<>();
        map.put("adminId", "2");
        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/get-stadium-count", map);

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);


        ResponseEntity<ResultEntity> response = testRestTemplate.
                exchange(url, HttpMethod.GET, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员新增场馆，若HTTP头部未携带TOKEN，返回异常结果")
    public void shouldReturnExceptionMessageWhenAdminCreateStadiumWithoutToken() throws IOException {
        String url = BASE_URL + "/post";
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(2)
                .stadiumName("测试用场馆名")
                .type(-1)
                .price(new BigDecimal(100))
                .location("测试用场地名")
                .description("测试用描述")
                .build();

        HttpEntity<StadiumRequest> requestEntity = new HttpEntity<>(stadiumRequest);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                postForEntity(url, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员新增场馆，且HTTP头部携带的TOKEN与adminId不匹配，返回异常结果")
    public void shouldReturnExceptionMessageWhenAdminCreateStadiumWithIncorrectToken() throws IOException {
        String url = BASE_URL + "/post";

        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(1)
                .stadiumName("测试用场馆名")
                .type(-1)
                .price(new BigDecimal(100))
                .location("测试用场地名")
                .description("测试用描述")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo");
        HttpEntity<StadiumRequest> requestEntity = new HttpEntity<>(stadiumRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                postForEntity(url, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员新增场馆，且HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    public void shouldReturnCorrectMessageWhenAdminCreateStadiumWithCorrectToken() throws IOException {
        String url = BASE_URL + "/post";

        String pictureRaw = null;
        try {
            InputStream inputStream = null;
            byte[] data = null;

            inputStream = new FileInputStream("src/main/resources/images/East_China_Normal_University.png");
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();

            pictureRaw = "data:image/png;base64," + Base64.getEncoder().encodeToString(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false, "图片文件编码失败(可能原因是: 图片不存在)");
        }

        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(2)
                .stadiumName("测试用场馆名")
                .type(-1)
                .price(new BigDecimal(100))
                .location("测试用场地名")
                .description("测试用描述")
                .pictureRaw(pictureRaw)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo");
        HttpEntity<StadiumRequest> requestEntity = new HttpEntity<>(stadiumRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                postForEntity(url, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );

        int[] Ids = stadiumDao.getStadiumIdByName("测试用场馆名");
        assertEquals(1, Ids.length);

        for (int i : Ids) {
            stadiumDao.deletestadiumbyId(i);
        }
    }

    @Test
    @DisplayName("管理员删除场馆，若HTTP头部未携带TOKEN，返回异常结果")
    public void shouldReturnExceptionMessageWhenAdminDeleteStadiumWithoutToken() throws IOException {
        String url = BASE_URL + "/delete";

        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(2)
                .build();

        HttpEntity<StadiumRequest> requestEntity = new HttpEntity<>(stadiumRequest);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                postForEntity(url, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员删除场馆，且HTTP头部携带的TOKEN与adminId不匹配，返回异常结果")
    public void shouldReturnExceptionMessageWhenAdminDeleteStadiumWithIncorrectToken() throws IOException {
        String url = BASE_URL + "/delete";

        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(1)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo");
        HttpEntity<StadiumRequest> requestEntity = new HttpEntity<>(stadiumRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                postForEntity(url, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员删除场馆，且HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    public void shouldReturnCorrectMessageWhenAdminDeleteStadiumWithCorrectToken() throws IOException {

        /** 创造测试环境 */
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(2)
                .stadiumName("测试用场馆名")
                .type(-1)
                .price(new BigDecimal(100))
                .location("测试用场地名")
                .description("测试用描述")
                .picture("url")
                .build();

        stadiumDao.createStadium(stadiumRequest);
        /** 创造测试环境完毕 */

        int[] Ids = stadiumDao.getStadiumIdByName("测试用场馆名");
        assertEquals(1, Ids.length);

        String url = BASE_URL + "/delete";

        stadiumRequest = StadiumRequest.builder()
                .adminId(2)
                .stadiumId(Ids[0])
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo");
        HttpEntity<StadiumRequest> requestEntity = new HttpEntity<>(stadiumRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                postForEntity(url, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员更新场馆，若HTTP头部未携带TOKEN，返回异常结果")
    public void shouldReturnExceptionMessageWhenAdminUpdateStadiumWithoutToken() {
        String url = BASE_URL + "/update";

        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(2)
                .stadiumName("测试用场馆名1")
                .type(1)
                .price(new BigDecimal(1000))
                .location("测试用场地名1")
                .description("测试用描述1")
                .build();

        HttpEntity<StadiumRequest> requestEntity = new HttpEntity<>(stadiumRequest);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                postForEntity(url, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }


    @Test
    @DisplayName("管理员更新场馆，且HTTP头部携带的TOKEN与adminId不匹配，返回异常结果")
    public void shouldReturnExceptionMessageWhenAdminUpdateStadiumWithIncorrectToken() {
        String url = BASE_URL + "/update";

        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(1)
                .stadiumName("测试用场馆名1")
                .type(1)
                .price(new BigDecimal(1000))
                .location("测试用场地名1")
                .description("测试用描述1")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo");
        HttpEntity<StadiumRequest> requestEntity = new HttpEntity<>(stadiumRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                postForEntity(url, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员更新场馆，且HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    public void shouldReturnCorrectMessageWhenAdminUpdateStadiumWithCorrectToken() {

        /** 创造测试环境 */
        StadiumRequest stadiumRequest = StadiumRequest.builder()
                .adminId(2)
                .stadiumName("测试用场馆名")
                .type(-1)
                .price(new BigDecimal(100))
                .location("测试用场地名")
                .description("测试用描述")
                .picture("url")
                .build();

        stadiumDao.createStadium(stadiumRequest);
        /** 创造测试环境完毕 */

        int[] Ids = stadiumDao.getStadiumIdByName("测试用场馆名");
        assertEquals(1, Ids.length);

        String url = BASE_URL + "/update";

        stadiumRequest = StadiumRequest.builder()
                .adminId(2)
                .stadiumId(Ids[0])
                .stadiumName("测试用场馆名1")
                .type(1)
                .price(new BigDecimal(1000))
                .location("测试用场地名1")
                .description("测试用描述1")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo");
        HttpEntity<StadiumRequest> requestEntity = new HttpEntity<>(stadiumRequest, headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                postForEntity(url, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );

        /** 恢复测试环境 */
        Ids = stadiumDao.getStadiumIdByName("测试用场馆名1");
        assertEquals(1, Ids.length);

        for (int i : Ids) {
            stadiumDao.deletestadiumbyId(i);
        }
        /** 恢复测试环境完毕 */
    }

    @Test
    @DisplayName("用户获取场馆列表，若HTTP头部未携带TOKEN，返回异常结果")
    public void shouldReturnExceptionMessageWhenGetStadiumListWithoutToken() {
        Map<String, String> map = new HashMap<>();
        map.put("customerId", "621");
        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/items", map);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                getForEntity(url, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户获取场馆列表，且HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    public void shouldReturnExceptionMessageWhenGetStadiumListWithWrongToken() {
        Map<String, String> map = new HashMap<>();
        map.put("customerId", "622");
        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/items", map);

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MjEiLCJpYXQiOjE1Nzc2OTU2NjIsImV4cCI6MTU3OTc2OTI2Mn0.moSEFHCMWRLNZSLhYK9IZG_zPGTNMMDv3DrUI4eD_K4");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                exchange(url, HttpMethod.GET, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户获取场馆列表，且HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    public void shouldGetStadiumListWithCorrectToken() {
        Map<String, String> map = new HashMap<>();
        map.put("customerId", "621");
        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/items", map);

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MjEiLCJpYXQiOjE1Nzc2OTU2NjIsImV4cCI6MTU3OTc2OTI2Mn0.moSEFHCMWRLNZSLhYK9IZG_zPGTNMMDv3DrUI4eD_K4");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                exchange(url, HttpMethod.GET, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户根据id获取场馆时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetStadiumByIdWithoutToken() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("customerId", "621");
        map.put("id", "1");
        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/message", map);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                getForEntity(url, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户根据id获取场馆时，且HTTP头部携带的TOKEN与customerId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetStadiumByIdWithWrongToken() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("customerId", "622");
        map.put("id", "1");
        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/message", map);

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MjEiLCJpYXQiOjE1Nzc2OTU2NjIsImV4cCI6MTU3OTc2OTI2Mn0.moSEFHCMWRLNZSLhYK9IZG_zPGTNMMDv3DrUI4eD_K4");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                exchange(url, HttpMethod.GET, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("用户根据id获取场馆时，若HTTP头部携带的TOKEN与customerId匹配，返回正常结果")
    void shouldGetStadiumByIdWithCorrectToken() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("customerId", "621");
        map.put("id", "1");
        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/message", map);

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MjEiLCJpYXQiOjE1Nzc2OTU2NjIsImV4cCI6MTU3OTc2OTI2Mn0.moSEFHCMWRLNZSLhYK9IZG_zPGTNMMDv3DrUI4eD_K4");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                exchange(url, HttpMethod.GET, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取场馆类型时，若HTTP头部未携带TOKEN，返回异常结果")
    void shouldReturnExceptionMessageWhenGetStadiumTypesWithoutToken() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("adminId", "2");
        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/types", map);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                getForEntity(url, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("HTTP头部未携带TOKEN", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取场馆类型时，且HTTP头部携带的TOKEN与adminId不匹配，返回异常结果")
    void shouldReturnExceptionMessageWhenGetStadiumTypesWithWrongToken() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("adminId", "1");
        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/types", map);

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                exchange(url, HttpMethod.GET, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(-1, result.getCode()),
                () -> assertEquals("TOKEN不匹配", result.getMessage())
        );
    }

    @Test
    @DisplayName("管理员获取场馆类型时，若HTTP头部携带的TOKEN与adminId匹配，返回正常结果")
    void shouldGetStadiumTypesWithCorrectToken() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("adminId", "2");
        String url = HttpRequestUtil.getGetRequestUrl(BASE_URL + "/types", map);

        HttpHeaders headers = new HttpHeaders();
        headers.add("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTc3NDU0Nzg4LCJleHAiOjE1Nzk1MjgzODh9.njy2edCCEzqqK8_w6Fd3u08uoXZXlvUqcimEBzQRWOo");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResultEntity> response = testRestTemplate.
                exchange(url, HttpMethod.GET, requestEntity, ResultEntity.class);

        ResultEntity result = response.getBody();
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(0, result.getCode()),
                () -> assertEquals("success", result.getMessage())
        );
    }
}













