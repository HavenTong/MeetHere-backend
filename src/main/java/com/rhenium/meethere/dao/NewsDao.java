package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.News;
import com.rhenium.meethere.dto.NewsRequest;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
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
    @Results({
            @Result(property = "adminId", column = "admin_id"),
            @Result(property = "admin", column = "admin_id", one = @One(select = "com.rhenium.meethere.dao.AdminDao.findAdminById", fetchType = FetchType.EAGER))
    })
    List<News> findNewsByOffsetAndLimit(@Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO news (news_title, admin_id, news_post_time, news_content) " +
            "VALUES (#{newsTitle}, #{adminId}, #{newsPostTime}, #{newsContent})")
    int createNews(NewsRequest newsRequest);

    @Update("UPDATE news SET news_title = #{newsTitle}, news_content = #{newsContent} " +
            "WHERE news_id = #{newsId}")
    int updateNews(NewsRequest newsRequest);

    @Delete("DELETE FROM news WHERE news_id = #{newsId}")
    int deleteNews(NewsRequest newsRequest);

    @Select("SELECT COUNT(*) FROM news")
    int getNewsCount();
}
