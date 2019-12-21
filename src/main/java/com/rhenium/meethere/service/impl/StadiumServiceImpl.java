package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.StadiumDao;
import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.enums.StadiumTypeEnum;
import com.rhenium.meethere.service.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    /**
     *
     * @param id
     * @return 返回场馆相关信息数据
     */
    @Override
    public Map<String, String> getStadiumById(Integer id) {
        Stadium stadium = stadiumDao.getStadiumById(id);
        Map<String, String> stadiumMsg = new HashMap<>();
        stadiumMsg.put("stadiumId", String.valueOf(stadium.getStadiumId()));
        stadiumMsg.put("stadiumName", stadium.getStadiumName());
        StadiumTypeEnum stadiumType = StadiumTypeEnum.getByCode(stadium.getType());
        String stadiumTypeName = stadiumType.getType();
        stadiumMsg.put("typeName", stadiumTypeName);
        stadiumMsg.put("location", stadium.getLocation());
        stadiumMsg.put("description", stadium.getDescription());
        stadiumMsg.put("price", String.valueOf(stadium.getPrice()));
        return stadiumMsg;
    }

}
