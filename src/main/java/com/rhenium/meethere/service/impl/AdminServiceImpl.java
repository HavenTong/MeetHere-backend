package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.AdminDao;
import com.rhenium.meethere.dao.BookingDao;
import com.rhenium.meethere.dao.CustomerDao;
import com.rhenium.meethere.domain.Admin;
import com.rhenium.meethere.domain.Booking;
import com.rhenium.meethere.domain.Customer;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.AdminService;
import com.rhenium.meethere.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    private BookingDao bookingDao;

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
            customerInfo.put("registeredTime", customer.getRegisteredTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
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

    // TODO: 测试脚本
    @Override
    public void deleteUser(AdminRequest adminRequest) {
        customerDao.deleteCustomerById(adminRequest.getCustomerId());
    }

    @Override
    public List<Map<String, String>> getBookingList(int offset, int limit) {
        if (offset < 0){
            throw new MyException(ResultEnum.INVALID_OFFSET);
        }
        if (limit < 1){
            throw new MyException(ResultEnum.INVALID_LIMIT);
        }
        List<Booking> bookingList = bookingDao.getBookingList(offset, limit);
        List<Map<String, String>> data = new ArrayList<>();
        for(Booking booking : bookingList) {
            Map<String, String> bookingItem = new HashMap<>(limit);
            bookingItem.put("bookingId", String.valueOf(booking.getBookingId()));
            bookingItem.put("startTime", booking.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            bookingItem.put("endTime", booking.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            bookingItem.put("priceSum", String.valueOf(booking.getPriceSum()));
            bookingItem.put("paid", String.valueOf(booking.getPaid()));
            bookingItem.put("customerId", String.valueOf(booking.getCustomerId()));
            bookingItem.put("stadiumId", String.valueOf(booking.getStadiumId()));
            bookingItem.put("customerName", String.valueOf(booking.getCustomer().getUserName()));
            bookingItem.put("stadiumName", String.valueOf(booking.getStadium().getStadiumName()));

            data.add(bookingItem);
        }
        return data;
    }

    @Override
    public Map<String, String> getBookingCount() {
        Map<String, String> data = new HashMap<>();
        data.put("count", String.valueOf(bookingDao.getBookingCount()));
        return data;
    }

    @Override
    public void deleteBooking(AdminRequest adminRequest) {
        bookingDao.deleteBookingById(adminRequest.getBookingId());
    }


    @Override
    public int getBookingCountByDate(String date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, df);
        LocalDateTime start = LocalDateTime.of(localDate, LocalTime.parse("00:00:00"));
        LocalDateTime end = start.plusDays(1).minusSeconds(1);
        int count = bookingDao.getBookingCountBetweenStartAndEnd(start, end);
        return count;
    }


    @Override
    public Map<String, Object> getBookingCountGroupByStadium(){
        List<Map<String, Object>> result = bookingDao.getBookingCountGroupByStadium();
        List<String> stadiums = new ArrayList<>();
        List<Long> count = new ArrayList<>();
        for (Map<String, Object> map : result){
            stadiums.add((String) map.get("stadiumName"));
            count.add((Long) map.get("count"));
        }
        Map<String, Object> bookingCountInfo = new HashMap<>();
        bookingCountInfo.put("stadiums", stadiums);
        bookingCountInfo.put("count", count);
        return bookingCountInfo;
    }
}
