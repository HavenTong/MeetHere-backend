package com.rhenium.meethere.service;

import com.rhenium.meethere.domain.Booking;
import com.rhenium.meethere.dto.AdminRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author JJAYCHEN
 * @date 2019/12/17 1:07 下午
 */

@Service
public interface AdminService {

    /**
     * 获取注册的用户个数
     * @return 注册的用户个数
     */
    Map<String, String> getUserCount();

    List<Map<String, String>> getUserList(int offset, int limit);

    Map<String, String> login(AdminRequest adminRequest);

    void deleteUser(AdminRequest adminRequest);

    List<Booking> getBookingList(int offset, int limit);
}
