package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.Booking;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author JJAYCHEN
 * @date 2019/12/19 11:00 上午
 */

@Mapper
@Repository
public interface BookingDao {

    // TODO: 可以优化一下 LIMIT

    @Select("SELECT b.* FROM booking b ORDER BY b.booking_id LIMIT #{offset}, #{limit}")
    @Results({
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "stadiumId", column = "stadium_id"),
            @Result(property = "customer", column = "customer_id", one = @One(select="com.rhenium.meethere.dao.CustomerDao.findCustomerById", fetchType = FetchType.EAGER)),
            @Result(property = "stadium", column = "stadium_id", one = @One(select="com.rhenium.meethere.dao.StadiumDao.getStadiumById", fetchType = FetchType.EAGER))
    })
    List<Booking> getBookingList(@Param("offset") int offset, @Param("limit") int limit);

    @Delete("DELETE FROM booking WHERE booking_id = #{bookingId}")
    void deleteBookingById(Integer bookingId);

    @Select("SELECT COUNT(*) FROM booking")
    int getBookingCount();

    @Select("SELECT COUNT(*) FROM booking WHERE start_time <= #{end} && end_time >= #{start} ")
    int getBookingCountBetweenStartAndEnd(@Param("start")LocalDateTime start,
                                          @Param("end") LocalDateTime end);

    @Select("SELECT stadium_name AS stadiumName,  count(booking_id) AS count\n" +
            "FROM booking NATURAL JOIN stadium\n" +
            "GROUP BY stadium_name;")
    List<Map<String, Object>> getBookingCountGroupByStadium();

    @Select("SELECT * FROM booking WHERE stadium_id = #{stadiumId} AND start_time >= #{start} AND end_time <= #{end} " +
            "ORDER BY start_time;")
    ArrayList<Booking> getBookingsByStadiumIdAndStartAndEnd(@Param("stadiumId") int stadiumId, @Param("start")LocalDateTime start, @Param("end") LocalDateTime end);


    @Insert("INSERT INTO booking(customer_id, stadium_id, start_time, end_time, price_sum, paid) VALUES (#{customerId}, #{stadiumId}, #{startTime}, #{endTime}, #{priceSum}, #{paid})")
    int addNewBooking(Booking booking);

}
