package com.rhenium.meethere.service;

import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.dto.StadiumRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/17 18:21
 */
public interface StadiumService {

    ArrayList<Stadium> listStadiumItems();

    Map<String, String> getStadiumById(Integer id);

    List<Map<String, Object>> findStadiumsForAdmin(int offset, int limit);

    Map<String, String> getStadiumCount();

    void deleteStadium(StadiumRequest stadiumRequest);

    void createStadium(StadiumRequest stadiumRequest) throws Exception;

    void updateStadium(StadiumRequest stadiumRequest);

    List<Map<String, Object>> getStadiumTypes();
}
