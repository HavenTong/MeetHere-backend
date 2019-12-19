package com.rhenium.meethere.service;

import com.rhenium.meethere.domain.Stadium;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/17 18:21
 */
public interface StadiumService {

    ArrayList<Stadium> listStadiumItems();
    Map<String, String> getStadiumById(Integer id);
    ArrayList<Map<String, String>> getCommentByStadiumId(Integer stadiumId);
}
