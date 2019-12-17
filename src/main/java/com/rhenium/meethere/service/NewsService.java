package com.rhenium.meethere.service;

import com.rhenium.meethere.domain.News;

import java.util.List;

/**
 * @author HavenTong
 * @date 2019/12/16 6:03 下午
 */
public interface NewsService {
    List<News> listNewsItems(int offset, int limit);
}
