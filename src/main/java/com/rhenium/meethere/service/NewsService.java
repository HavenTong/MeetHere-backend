package com.rhenium.meethere.service;

import com.rhenium.meethere.domain.News;
import com.rhenium.meethere.dto.NewsRequest;

import java.util.List;
import java.util.Map;

/**
 * @author HavenTong
 * @date 2019/12/16 6:03 下午
 */
public interface NewsService {
    List<News> listNewsItems(int offset, int limit);

    void createNews(NewsRequest newsRequest);

    void updateNews(NewsRequest newsRequest);

    void deleteNews(NewsRequest newsRequest);

    Map<String,String> getNewsCount();
}
