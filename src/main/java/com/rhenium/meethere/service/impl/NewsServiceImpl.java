package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.AdminDao;
import com.rhenium.meethere.dao.NewsDao;
import com.rhenium.meethere.domain.Admin;
import com.rhenium.meethere.domain.News;
import com.rhenium.meethere.dto.NewsRequest;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author HavenTong
 * @date 2019/12/16 6:04 下午
 */
@Service
@Slf4j
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private AdminDao adminDao;

    @Override
    public List<News> listNewsItems(int offset, int limit) {
        if (offset < 0){
            throw new MyException(ResultEnum.INVALID_OFFSET);
        }
        if (limit < 1){
            throw new MyException(ResultEnum.INVALID_LIMIT);
        }
        int itemOffset = offset * limit;
        return newsDao.findNewsByOffsetAndLimit(itemOffset, limit);
    }

    @Override
    public void createNews(NewsRequest newsRequest) {
        Admin admin = adminDao.findAdminById(newsRequest.getAdminId());
        if (admin == null){
            throw new MyException(ResultEnum.ADMIN_NOT_EXIST);
        }
        newsRequest.setNewsPostTime(LocalDateTime.now());
        newsDao.createNews(newsRequest);
    }

    @Override
    public void updateNews(NewsRequest newsRequest) {
        Admin admin = adminDao.findAdminById(newsRequest.getAdminId());
        if (admin == null){
            throw new MyException(ResultEnum.ADMIN_NOT_EXIST);
        }
        newsDao.updateNews(newsRequest);
    }

    @Override
    public void deleteNews(NewsRequest newsRequest) {
        Admin admin = adminDao.findAdminById(newsRequest.getAdminId());
        if (admin == null){
            throw new MyException(ResultEnum.ADMIN_NOT_EXIST);
        }
        newsDao.deleteNews(newsRequest);
    }
}
