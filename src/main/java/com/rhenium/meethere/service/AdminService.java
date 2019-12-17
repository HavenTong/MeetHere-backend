package com.rhenium.meethere.service;

import org.springframework.stereotype.Service;

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
    int getUserCount();
}
