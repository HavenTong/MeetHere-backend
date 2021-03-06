package com.rhenium.meethere.service;

import com.rhenium.meethere.dao.CustomerDao;
import com.rhenium.meethere.domain.Customer;
import com.rhenium.meethere.dto.CustomerRequest;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.impl.CustomerServiceImpl;
import com.rhenium.meethere.util.CheckCodeUtil;
import com.rhenium.meethere.util.JwtUtil;
import com.sun.mail.iap.Argument;
import mockit.MockUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author HavenTong
 * @date 2019/12/10 10:28 上午
 */
@SpringBootTest
class CustomerServiceTest {

    @Spy
    private BCryptPasswordEncoder encoder;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private CustomerDao customerDao;

    @Mock
    private MailService mailService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    // 通过Fake对象，将redis相关操作的接口实现为空
    private class ValueOperationsFake implements ValueOperations<String, String> {
        @Override
        public void set(String s, String s2) {

        }

        @Override
        public void set(String s, String s2, long l, TimeUnit timeUnit) {

        }

        @Override
        public Boolean setIfAbsent(String s, String s2) {
            return null;
        }

        @Override
        public Boolean setIfAbsent(String s, String s2, long l, TimeUnit timeUnit) {
            return null;
        }

        @Override
        public Boolean setIfPresent(String s, String s2) {
            return null;
        }

        @Override
        public Boolean setIfPresent(String s, String s2, long l, TimeUnit timeUnit) {
            return null;
        }

        @Override
        public void multiSet(Map<? extends String, ? extends String> map) {

        }

        @Override
        public Boolean multiSetIfAbsent(Map<? extends String, ? extends String> map) {
            return null;
        }

        @Override
        public String get(Object o) {
            if ("code:852092786@qq.com".equals(o)){
                return "123456";
            }
            return null;
        }

        @Override
        public String getAndSet(String s, String s2) {
            return null;
        }

        @Override
        public List<String> multiGet(Collection<String> collection) {
            return null;
        }

        @Override
        public Long increment(String s) {
            return null;
        }

        @Override
        public Long increment(String s, long l) {
            return null;
        }

        @Override
        public Double increment(String s, double v) {
            return null;
        }

        @Override
        public Long decrement(String s) {
            return null;
        }

        @Override
        public Long decrement(String s, long l) {
            return null;
        }

        @Override
        public Integer append(String s, String s2) {
            return null;
        }

        @Override
        public String get(String s, long l, long l1) {
            return null;
        }

        @Override
        public void set(String s, String s2, long l) {

        }

        @Override
        public Long size(String s) {
            return null;
        }

        @Override
        public Boolean setBit(String s, long l, boolean b) {
            return null;
        }

        @Override
        public Boolean getBit(String s, long l) {
            return null;
        }

        @Override
        public List<Long> bitField(String s, BitFieldSubCommands bitFieldSubCommands) {
            return null;
        }

        @Override
        public RedisOperations<String, String> getOperations() {
            return null;
        }
    }

    @BeforeEach
    void init(){
        // 每次测试前，通过Fake对象为redisTemplate打桩
        when(redisTemplate.opsForValue()).thenReturn(new ValueOperationsFake());
    }

    @Test
    @DisplayName("注册时，当用户邮箱已存在时，抛出异常")
    void shouldThrowExceptionWhenRegisteringUserExists(){
        Customer customer = new Customer();
        when(customerDao.findCustomerByEmail(anyString())).thenReturn(customer);
        Throwable exception = assertThrows(MyException.class,
                () -> customerService.sendCheckCode("10175101152@stu.ecnu.edu.cn"));
        assertEquals("邮箱已被使用", exception.getMessage());
    }

    @Test
    @DisplayName("注册时，注册的为新用户则发送邮件")
    void shouldSendEmailWhenUserIsNew(){
        customerService.sendCheckCode("10175101152@stu.ecnu.edu.cn");
        verify(mailService, times(1)).
                sendHtmlMail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("注册时，发送邮件内容正确")
    void shouldSendCorrectEmailWhenUserIsNew(){
        // 使用JMockit对静态方法进行打桩
        new MockUp<CheckCodeUtil>(){
            @mockit.Mock
            public String generateCheckCode(){
                return "123456";
            }
        };
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        customerService.sendCheckCode("10175101152@stu.ecnu.edu.cn");
        verify(mailService, times(1))
                .sendHtmlMail(emailCaptor.capture(), subjectCaptor.capture(), contentCaptor.capture());
        assertAll(
                () -> assertEquals("10175101152@stu.ecnu.edu.cn", emailCaptor.getValue()),
                () -> assertEquals("Registration from MeetHere", subjectCaptor.getValue()),
                () -> assertEquals("<h1>Welcome to MeetHere!</h1><p>Your check code is <u>" +
                        "123456" + "</u></p>", contentCaptor.getValue())
        );
    }

    @Test
    @DisplayName("注册时，当用户的验证码为空时，抛出异常")
    void shouldThrowExceptionWhenCheckCodeIsNull(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .userName("user")
                .email("10175101152@stu.ecnu.edu.cn")
                .password("123456").build();
        Throwable exception = assertThrows(MyException.class,
                () -> customerService.register(customerRequest)
        );
        assertEquals("邮箱未发送验证码或验证码已失效", exception.getMessage());
    }

    @Test
    @DisplayName("注册时，当用户验证码和数据库中不匹配时，抛出异常")
    void shouldThrowExceptionWhenCheckCodeNotMatch(){
        CustomerRequest customerRequest = CustomerRequest
                .builder().userName("user")
                .email("852092786@qq.com")
                .password("123456")
                .checkCode("456789").build();
        Throwable exception = assertThrows(MyException.class,
                () -> customerService.register(customerRequest)
        );
        assertEquals("验证码错误", exception.getMessage());
    }

    @Test
    @DisplayName("注册时，当用户验证码匹配时，完成注册")
    void shouldRegisterUserWhenCheckCodeMatches(){
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        CustomerRequest customerRequest = CustomerRequest.builder()
                .userName("user").email("852092786@qq.com")
                .password("123456").checkCode("123456").build();
        customerService.register(customerRequest);
        verify(customerDao, times(1))
                .saveNewCustomer(customerArgumentCaptor.capture());
        assertAll(
                () -> assertEquals("user", customerArgumentCaptor.getValue().getUserName()),
                () -> assertEquals("852092786@qq.com", customerArgumentCaptor.getValue().getEmail()),
                () -> assertTrue(encoder.matches("123456", encoder.encode("123456"))),
                () -> assertNull(customerArgumentCaptor.getValue().getPhoneNumber()),
                () -> assertEquals(LocalDate.now(), customerArgumentCaptor.getValue().getRegisteredTime().toLocalDate())
        );

    }

    @Test
    @DisplayName("登录时，当登录用户不存在时，抛出异常")
    void shouldThrowExceptionWhenLoginUserNotExist(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .email("10175101152@stu.ecnu.edu.cn").password("123456").build();
        Throwable exception = assertThrows(MyException.class,
                () -> customerService.login(customerRequest));
        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    @DisplayName("登录时，当登录用户密码错误时，抛出异常")
    void shouldThrowExceptionWhenLoginWithWrongPassword(){
        Customer mockedCustomer = Customer.builder()
                .password("456789").build();
        CustomerRequest customerRequest = CustomerRequest.builder()
                .email("10175101152@stu.ecnu.edu.cn").password("123456").build();
        when(customerDao.findCustomerByEmail(anyString())).thenReturn(mockedCustomer);
        Throwable exception = assertThrows(MyException.class,
                () -> customerService.login(customerRequest));
        assertEquals("密码错误", exception.getMessage());
    }

    @Test
    @DisplayName("登录时，当登录密码正确时，允许登录并返回正确登录信息")
    void shouldReturnCorrectLoginInfoWhenPasswordIsRight(){
        Customer mockedCustomer = Customer.builder()
                .customerId(6)
                .email("10175101152@stu.ecnu.edu.cn")
                .userName("root")
                .password("$2a$10$tcuOLfVB3Ld0c5DCD/F/UuWpH.mbrmaOHVxYJ1Hd0ttoke5NzWxsK")
                .phoneNumber("18900001111")
                .registeredTime(LocalDateTime.of(2019, 12, 21, 14, 30, 30))
                .build();
        CustomerRequest customerRequest = CustomerRequest.builder()
                .email("10175101152@stu.ecnu.edu.cn")
                .password("123456")
                .build();
        String token = JwtUtil.createJwt(mockedCustomer);
        when(customerDao.findCustomerByEmail(anyString())).thenReturn(mockedCustomer);
        Map<String, String> loginInfo = customerService.login(customerRequest);
        assertAll(
                () -> assertEquals(token, loginInfo.get("token")),
                () -> assertEquals("6", loginInfo.get("customerId")),
                () -> assertEquals("10175101152@stu.ecnu.edu.cn", loginInfo.get("email")),
                () -> assertEquals("root", loginInfo.get("userName")),
                () -> assertEquals("18900001111", loginInfo.get("phoneNumber")),
                () -> assertEquals("2019-12-21", loginInfo.get("registeredTime"))
        );
    }

    @Test
    @DisplayName("更新用户信息时，当将要更新的用户名为空时，抛出异常")
    void shouldThrowExceptionWhenSaveUserInfoWithEmptyUserName(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(6).build();
        Throwable exception = assertThrows(MyException.class,
                () -> customerService.saveUserInfo(customerRequest));
        assertEquals("用户名不允许为空", exception.getMessage());
    }

    @Test
    @DisplayName("更新用户信息时，确保用户更新正确信息")
    void shouldUpdateCorrectInfoWhenSaveUserInfo(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(6).userName("user").phoneNumber("18900001111").build();
        customerService.saveUserInfo(customerRequest);
        ArgumentCaptor<String> customerIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> userNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> phoneNumberCaptor = ArgumentCaptor.forClass(String.class);
        verify(customerDao, times(1))
                .saveCustomerInfo( customerIdCaptor.capture(),
                                    userNameCaptor.capture(),
                                    phoneNumberCaptor.capture());
        assertAll(
                () -> assertEquals("6", customerIdCaptor.getValue()),
                () -> assertEquals("user", userNameCaptor.getValue()),
                () -> assertEquals("18900001111", phoneNumberCaptor.getValue())
        );
    }

    @Test
    @DisplayName("修改密码时，当用户修改的新密码为空时，抛出异常")
    void shouldThrowExceptionWhenChangePasswordWithEmptyPassword(){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(6).password("123456").build();
        Throwable exception = assertThrows(MyException.class,
                () -> customerService.changePassword(customerRequest));
        assertEquals("新密码不允许为空", exception.getMessage());
    }

    @Test
    @DisplayName("修改密码时，当用户输入的旧密码错误时，抛出异常")
    void shouldThrowExceptionWhenChangePasswordWithWrongPassword(){
        Customer customer = Customer.builder()
                .customerId(6)
                .password("$2a$10$S1wJ1fbSdi7zymmqMAHNRORkBnrmElDWF55mwUrOMY5QiCzgJQ81q")
                .build();
        when(customerDao.findCustomerById(anyInt())).thenReturn(customer);
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(6).password("456789").newPassword("123456").build();
        Throwable exception = assertThrows(MyException.class,
                () -> customerService.changePassword(customerRequest));
        assertEquals("密码错误", exception.getMessage());
    }

    @Test
    @DisplayName("修改密码时，当旧密码正确且新密码不为空时，修改成功")
    void shouldSaveCorrectPasswordWhenChangePassword(){
        Customer customer = Customer.builder()
                .customerId(6)
                .password("$2a$10$Ebx04j4OxmRbnfEnXx4fiuWITXR14esb5WQWCJHIEphO30l9WI2yq")
                .build();
        when(customerDao.findCustomerById(anyInt())).thenReturn(customer);
        CustomerRequest customerRequest = CustomerRequest.builder()
                .customerId(6)
                .password("123456")
                .newPassword("456789").build();
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        customerService.changePassword(customerRequest);
        verify(customerDao, times(1))
                .saveNewPassword(customerArgumentCaptor.capture());
        assertAll(
                () -> assertEquals(6, customerArgumentCaptor.getValue().getCustomerId()),
                () -> assertTrue(encoder.matches("456789", customerArgumentCaptor.getValue().getPassword()))
        );
    }

}