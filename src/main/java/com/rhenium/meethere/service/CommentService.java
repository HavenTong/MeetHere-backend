package com.rhenium.meethere.service;

import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.dto.CommentRequest;

/**
 * @author HavenTong
 * @date 2019/12/20 1:19 下午
 */
public interface CommentService {

    void deleteCommentByAdmin(AdminRequest adminRequest);

}
