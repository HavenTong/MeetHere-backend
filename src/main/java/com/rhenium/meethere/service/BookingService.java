package com.rhenium.meethere.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/24 12:43
 */
@Service
public interface BookingService {
    ArrayList<Map<String, Integer>> getEmptyTimeByStadiumIdAndDate(Integer stadiumId, Integer DaysAfterToday);
}