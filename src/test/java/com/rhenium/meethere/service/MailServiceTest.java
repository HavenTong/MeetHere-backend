package com.rhenium.meethere.service;

import com.rhenium.meethere.service.impl.MailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author HavenTong
 * @date 2019/12/9 8:36 下午
 */
@SpringBootTest
@ActiveProfiles("dev")
class MailServiceTest {
    @Value("${spring.mail.from}")
    private String from;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private MailServiceImpl mailService;

    private MimeMessage message;

    @BeforeEach
    void init(){
        mailService.setFrom(from);
        JavaMailSenderImpl testMailSender = new JavaMailSenderImpl();
        message = testMailSender.createMimeMessage();
        when(mailSender.createMimeMessage()).thenReturn(message);
    }

    @DisplayName("确保发送邮件")
    @Test
    void shouldSendMail(){
        mailService.sendHtmlMail("10175101152@stu.ecnu.edu.cn", "Test Subject", "Test Case");
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @DisplayName("确保邮件信息正确")
    @Test
    void shouldSendEmailCorrectly() throws Exception {
        ArgumentCaptor<MimeMessage> messageArgumentCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        mailService.sendHtmlMail("10175101152@stu.ecnu.edu.cn", "Test Subject", "<h1>Test Case</h1>");
        verify(mailSender, times(1)).send(messageArgumentCaptor.capture());
        assertAll(
                () -> assertEquals("10175101152@stu.ecnu.edu.cn",
                        messageArgumentCaptor.getValue().getAllRecipients()[0].toString()),
                () -> assertEquals("Test Subject",
                        messageArgumentCaptor.getValue().getSubject()),
                () -> assertEquals("852092786@qq.com",
                        messageArgumentCaptor.getValue().getFrom()[0].toString())
        );
    }


}