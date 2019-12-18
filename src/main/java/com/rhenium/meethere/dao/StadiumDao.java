package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.Stadium;
import org.apache.ibatis.annotations.Mapper;
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
    @Select("SELECT * FROM stadium")
    ArrayList<Stadium> getAllStadium();
}
