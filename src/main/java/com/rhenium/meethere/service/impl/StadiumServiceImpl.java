package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.StadiumDao;
import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.service.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/17 22:53
 */
@Service
public class StadiumServiceImpl implements StadiumService {
    @Autowired
    private StadiumDao stadiumDao;

    @Override
    public ArrayList<Stadium> listStadiumItems() {
        return stadiumDao.getStadiumList();
    }

    @Override
    public Stadium getStadiumById(Integer id) {
        return stadiumDao.getStadiumById(id);
    }
}
