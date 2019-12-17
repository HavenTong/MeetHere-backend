package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.AdminDao;
import com.rhenium.meethere.dao.CustomerDao;
import com.rhenium.meethere.domain.Admin;
import com.rhenium.meethere.domain.Customer;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.AdminService;
import com.rhenium.meethere.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JJAYCHEN
 * @date 2019/12/17 1:07 下午
 */

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public Map<String, String> getUserCount() {
        Map<String, String> data = new HashMap<>();
        data.put("count", String.valueOf(customerDao.getUserCount()));
        return data;
    }

    @Override
    public List<Map<String, String>> getUserList(int offset, int limit) {
        List<Customer> customers = customerDao.getUserList(offset, limit);
        List<Map<String, String>> data = new ArrayList<>();
        for (Customer customer : customers) {
            Map<String, String> customerInfo = new HashMap<>(limit);
            customerInfo.put("customerId", String.valueOf(customer.getCustomerId()));
            customerInfo.put("registeredTime", String.valueOf(customer.getRegisteredTime()));
            customerInfo.put("userName", customer.getUserName());
            customerInfo.put("email", customer.getEmail());
            String phoneNumber = customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "空";
            customerInfo.put("phoneNumber", phoneNumber);

            data.add(customerInfo);
        }
        return data;
    }

    // TODO: 测试脚本
    @Override
    public Map<String, String> login(AdminRequest adminRequest) {
        Admin admin = adminDao.findAdminByEmail(adminRequest.getEmail());
        if (admin == null) {
            throw new MyException(ResultEnum.USER_NOT_EXIST);
        }
        if (!encoder.matches(adminRequest.getPassword(), admin.getPassword())) {
            throw new MyException(ResultEnum.PASSWORD_ERROR);
        }
        Map<String, String> adminLoginInfo = new HashMap<>();
        String adminName = admin.getAdminName();
        String email = admin.getEmail();
        String adminId = admin.getAdminId().toString();
        String token = JwtUtil.createJwt(admin);
        String phoneNumber = admin.getPhoneNumber();
        adminLoginInfo.put("token", token);
        adminLoginInfo.put("adminId", adminId);
        adminLoginInfo.put("email", email);
        adminLoginInfo.put("adminName", adminName);
        adminLoginInfo.put("phoneNumber", phoneNumber);
        return adminLoginInfo;
    }
}
