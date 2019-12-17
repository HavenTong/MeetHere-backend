package com.rhenium.meethere.controller;

import com.rhenium.meethere.annotation.PublicLoginRequired;
import com.rhenium.meethere.domain.News;
import com.rhenium.meethere.dto.PublicRequest;
import com.rhenium.meethere.service.NewsService;
import com.rhenium.meethere.vo.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author HavenTong
 * @date 2019/12/16 6:07 下午
 */
@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/items")
    @PublicLoginRequired
    public ResultEntity listNewsItems(@RequestParam int offset,
                                      @RequestParam int limit,
                                      @RequestBody PublicRequest publicRequest){
        List<News> newsList = newsService.listNewsItems(offset, limit);
        return ResultEntity.succeed(newsList);
    }
}
