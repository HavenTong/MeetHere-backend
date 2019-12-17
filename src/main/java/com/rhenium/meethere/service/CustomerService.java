package com.rhenium.meethere.service;

import com.rhenium.meethere.domain.Customer;
import com.rhenium.meethere.dto.CustomerRequest;
import com.rhenium.meethere.vo.ResultEntity;

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

    void changePassword(CustomerRequest customerRequest);

    Customer getOne(String email);
}
