package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.CommentDao;
import com.rhenium.meethere.dao.StadiumDao;
import com.rhenium.meethere.domain.Comment;
import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.dto.StadiumRequest;
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

    @Autowired
    private CommentDao commentDao;

    @Override
    public ArrayList<Stadium> listStadiumItems() {
        return stadiumDao.getStadiumList();
    }

    @Override
    public StadiumRequest getStadiumById(Integer id) {
        Stadium stadium = stadiumDao.getStadiumById(id);
        StadiumRequest stadiumRequest = new StadiumRequest(stadium);
        return stadiumRequest;
    }

    @Override
    public ArrayList<Comment> getCommentByStadiumId(Integer stadiumId) {
        return commentDao.getCommentByStadiumId(stadiumId);
    }
}
