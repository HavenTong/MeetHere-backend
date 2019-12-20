package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.AdminDao;
import com.rhenium.meethere.dao.CommentDao;
import com.rhenium.meethere.domain.Admin;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.dto.CommentRequest;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HavenTong
 * @date 2019/12/20 1:20 下午
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private AdminDao adminDao;

    @Override
    public void deleteCommentByAdmin(AdminRequest adminRequest) {
        Admin admin = adminDao.findAdminById(adminRequest.getAdminId());
        if (admin == null){
            throw new MyException(ResultEnum.ADMIN_NOT_EXIST);
        }
        commentDao.deleteCommentById(adminRequest.getCommentId());
    }
}
