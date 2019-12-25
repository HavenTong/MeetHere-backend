package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.Stadium;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

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
    Stadium getStadiumById(@Param("id") Integer stadiumId);

    @Select("SELECT stadium.stadium_name FROM stadium WHERE stadium_id = #{id}")
    String getStadiumNameById(@Param("id") Integer id);

    @Select("SELECT * FROM stadium LIMIT #{offset}, #{limit}")
    @Results({
            @Result(property = "stadiumId", column = "stadium_id"),
            @Result(property = "bookingList", column = "stadium_id",
            many = @Many(select = "com.rhenium.meethere.dao.BookingDao.findBookingByStadiumId", fetchType = FetchType.EAGER))
    })
    List<Stadium> findAllStadiumsForAdmin(@Param("offset") int offset,
                                          @Param("limit") int limit);
}
