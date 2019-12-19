package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.CommentDao;
import com.rhenium.meethere.dao.StadiumDao;
import com.rhenium.meethere.domain.Comment;
import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.dto.StadiumEntity;
import com.rhenium.meethere.enums.StadiumTypeEnum;
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
    public StadiumEntity getStadiumById(Integer id) {
        StadiumEntity stadiumEntity = stadiumDao.getStadiumById(id);
        stadiumEntity.setTypeName(StadiumTypeEnum.getByCode(stadiumEntity.getType()).getType());
        return stadiumEntity;
    }

    @Override
    public ArrayList<Comment> getCommentByStadiumId(Integer stadiumId) {
        return commentDao.getCommentByStadiumIdT(stadiumId);
    }
}
