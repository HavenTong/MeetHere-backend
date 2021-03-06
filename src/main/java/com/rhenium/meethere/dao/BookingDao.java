package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.Booking;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Select("SELECT b.* FROM booking b ORDER BY b.booking_id DESC LIMIT #{offset}, #{limit}")
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


    @Select("SELECT * FROM booking WHERE stadium_id = #{stadiumId}")
    List<Booking> findBookingByStadiumId(@Param("stadiumId") int stadiumId);

    @Select("SELECT * FROM booking WHERE booking_id = #{bookingId}")
    Booking getBookingByBookingId(@Param("bookingId") int bookingId);

    @Select("SELECT * FROM booking WHERE stadium_id = #{stadiumId} AND start_time >= #{start} AND end_time <= #{end} " +
            "ORDER BY start_time;")
    ArrayList<Booking> getBookingsByStadiumIdAndStartAndEnd(@Param("stadiumId") int stadiumId, @Param("start")LocalDateTime start, @Param("end") LocalDateTime end);


    @Insert("INSERT INTO booking(customer_id, stadium_id, start_time, end_time, price_sum, paid) VALUES (#{customerId}, #{stadiumId}, #{startTime}, #{endTime}, #{priceSum}, #{paid})")
    @Options(keyProperty = "bookingId", keyColumn = "booking_id", useGeneratedKeys = true)
    int addNewBooking(Booking booking);

    @Select("SELECT * FROM booking WHERE customer_id = #{customerId} ORDER BY start_time DESC LIMIT #{offset}, #{limit}")
    @Results(
            @Result(property = "stadium", column = "stadium_id",
            one = @One(select = "com.rhenium.meethere.dao.StadiumDao.getStadiumById", fetchType = FetchType.EAGER))
    )
    List<Booking> findBookingsByCustomerId(@Param("offset") int offset,
                                           @Param("limit") int limit,
                                           @Param("customerId") int customerId);

    @Select("SELECT COUNT(*) FROM booking WHERE customer_id = #{customerId}")
    int findBookingCountForCustomer(@Param("customerId") int customerId);

    @Delete("DELETE a FROM booking a, (SELECT MAX(booking_id) AS max_id FROM booking) b WHERE booking_id = b.max_id")
    int deleteLatestBooking();

    @Select("SELECT * FROM booking WHERE booking_id = (SELECT MAX(booking_id) FROM booking)")
    Booking getLatestBooking();

    @Select("SELECT COUNT(*) FROM booking")
    int getAllBookingCount();
}
