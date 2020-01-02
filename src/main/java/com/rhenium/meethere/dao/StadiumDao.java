package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.dto.StadiumRequest;
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

    @Update("UPDATE stadium SET stadium_name = #{stadiumName}, " +
            "location = #{location}, description = #{description}, " +
            "price = #{price} WHERE stadium_id = #{stadiumId}")
    int updateStadiumInfoByAdmin(StadiumRequest stadiumRequest);

    @Delete("DELETE FROM stadium WHERE stadium_id = #{stadiumId}")
    int deleteStadiumByAdmin(StadiumRequest stadiumRequest);

    @Select("SELECT COUNT(*) FROM stadium")
    int getStadiumCount();

    @Delete("DELETE FROM stadium WHERE stadium_id = #{stadiumId}")
    void deleteStadium(StadiumRequest stadiumRequest);

    @Insert("INSERT INTO stadium (stadium_name, type, location, description, price, picture) " +
            "VALUES (#{stadiumName}, #{type}, #{location}, #{description}, #{price}, #{picture})")
    void createStadium(StadiumRequest stadiumRequest);

    @Insert("UPDATE stadium SET stadium_name = #{stadiumName}, type = #{type}, " +
            "location = #{location}, description = #{description}, price = #{price} " +
            "WHERE stadium_id = #{stadiumId}")
    void updateStadium(StadiumRequest stadiumRequest);

    @Insert("UPDATE stadium SET stadium_name = #{stadiumName}, type = #{type}, " +
            "location = #{location}, description = #{description}, price = #{price}, " +
            "picture = #{picture} WHERE stadium_id = #{stadiumId}")
    void updateStadiumWithPicture(StadiumRequest stadiumRequest);

    /** 集成测试用 **/

    @Select("SELECT stadium_id FROM stadium WHERE stadium_name = #{stadiumName}")
    int[] getStadiumIdByName(String stadiumName);

    @Select("DELETE FROM stadium WHERE stadium_id = #{id}")
    int[] deletestadiumbyId(Integer id);
}
