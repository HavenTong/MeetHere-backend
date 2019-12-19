package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.dto.StadiumEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/16 19:36
 */
@Repository
@Mapper
public interface StadiumDao {
    @Select("SELECT stadium_id, stadium_name, description, picture FROM stadium")
    ArrayList<Stadium> getStadiumList();

    @Select("SELECT * FROM stadium WHERE stadium_id = #{id}")
    StadiumEntity getStadiumById(@Param("id") Integer stadiumId);
}
