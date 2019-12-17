package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.CustomerDao;
import com.rhenium.meethere.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author JJAYCHEN
 * @date 2019/12/17 1:07 下午
 */

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private CustomerDao customerDao;

    @Override
    public int getUserCount() {
        return customerDao.getUserCount();
    }
}
