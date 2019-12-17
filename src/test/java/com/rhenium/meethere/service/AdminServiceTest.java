package com.rhenium.meethere.service;

import com.rhenium.meethere.dao.AdminDao;
import com.rhenium.meethere.dao.CustomerDao;
import com.rhenium.meethere.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author HavenTong
 * @date 2019/12/17 5:56 下午
 */
@SpringBootTest
class AdminServiceTest {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private AdminDao adminDao;

    @Spy
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    @DisplayName("获取用户数量时，获取结果正确")
    void shouldGetCorrectUserCount(){
        when(customerDao.getUserCount()).thenReturn(3);
        int result = adminService.getUserCount();
        verify(customerDao, times(1))
                .getUserCount();
        assertEquals(3, result);
    }
}