package com.rhenium.meethere.controller;

import com.rhenium.meethere.annotation.AdminLoginRequired;
import com.rhenium.meethere.annotation.PublicLoginRequired;
import com.rhenium.meethere.domain.News;
import com.rhenium.meethere.dto.NewsRequest;
import com.rhenium.meethere.service.NewsService;
import com.rhenium.meethere.vo.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author HavenTong
 * @date 2019/12/16 6:07 下午
 */
@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/get-news-count")
    @PublicLoginRequired
    public ResultEntity getNewsCount(@RequestParam int userId) {
        Map<String, String> data = newsService.getNewsCount();
        return ResultEntity.succeed(data);
    }

    @GetMapping("/get-news-list")
//    @PublicLoginRequired
    public ResultEntity listNewsItems(@RequestParam int offset,
                                      @RequestParam int limit,
                                      @RequestParam int userId){
        List<News> newsList = newsService.listNewsItems(offset, limit);
        return ResultEntity.succeed(newsList);
    }

    @PostMapping("/post")
    @AdminLoginRequired
    public ResultEntity postNews(@Validated @RequestBody NewsRequest newsRequest){
        newsService.createNews(newsRequest);
        return ResultEntity.succeed();
    }

    @PostMapping("/update")
    @AdminLoginRequired
    public ResultEntity updateNews(@Validated @RequestBody NewsRequest newsRequest){
        newsService.updateNews(newsRequest);
        return ResultEntity.succeed();
    }

    @PostMapping("/delete")
    @AdminLoginRequired
    public ResultEntity deleteNews(@Validated @RequestBody NewsRequest newsRequest){
        newsService.deleteNews(newsRequest);
        return ResultEntity.succeed();
    }
}
