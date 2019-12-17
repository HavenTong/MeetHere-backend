package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author HavenTong
 * @date 2019/12/16 6:04 下午
 */
@Repository
@Mapper
public interface NewsDao {
    @Select("SELECT * FROM news ORDER BY news_post_time DESC LIMIT #{offset}, #{limit}")
    List<News> findNewsByOffsetAndLimit(@Param("offset") int offset, @Param("limit") int limit);
}
