package com.rhenium.meethere.service;

import com.rhenium.meethere.dto.CustomerRequest;

import java.util.Map;

/**
 * @author HavenTong
 * @date 2019/12/7 6:05 下午
 */
public interface CustomerService {

    void sendCheckCode(String email);

    void register(CustomerRequest customerRequest);

    Map<String, String> login(CustomerRequest customerRequest);

    void saveUserInfo(CustomerRequest customerRequest);
}
