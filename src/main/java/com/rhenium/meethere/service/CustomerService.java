package com.rhenium.meethere.service;

/**
 * @author HavenTong
 * @date 2019/12/7 6:05 下午
 */
public interface CustomerService {

    void sendCheckCode(String email);

    void register(String userName, String email, String password, String checkCode);
}
