package com.rhenium.meethere.service;

import com.rhenium.meethere.domain.Stadium;

import java.util.ArrayList;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/17 18:21
 */
public interface StadiumService {

    ArrayList<Stadium> listStadiumItems();
    Stadium getStadiumById(Integer id);
}
