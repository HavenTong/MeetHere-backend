package com.rhenium.meethere.service;

import com.rhenium.meethere.domain.Comment;
import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.dto.StadiumRequest;

import java.util.ArrayList;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/17 18:21
 */
public interface StadiumService {

    ArrayList<Stadium> listStadiumItems();
    StadiumRequest getStadiumById(Integer id);
    ArrayList<Comment> getCommentByStadiumId(Integer stadiumId);
}
