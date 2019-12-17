package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.CustomerDao;
import com.rhenium.meethere.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author JJAYCHEN
 * @date 2019/12/17 1:07 下午
 */
public class AdminServiceImpl implements AdminService {
    @Autowired
    private CustomerDao customerDao;

    @Override
    public int getUserCount() {
        return customerDao.getUserCount();
    }
}
