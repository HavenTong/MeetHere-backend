package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.Booking;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.List;

/**
 * @author JJAYCHEN
 * @date 2019/12/19 11:00 上午
 */

@Mapper
@Repository
public interface BookingDao {

    // TODO: 可以优化一下 LIMIT

    @Select("SELECT b.*, s.*, c.* FROM booking b NATURAL JOIN stadium s NATURAL JOIN customer c ORDER BY b.booking_id LIMIT #{offset}, #{limit}")
    @Results({
            @Result(id=true, property = "bookingId", column = "booking_id"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "priceSum", column = "price_sum"),
            @Result(property = "paid", column = "paid"),
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "stadiumId", column = "stadium_id"),
            @Result(property = "customerName", column = "customer_id", one = @One(select="com.rhenium.meethere.dao.CustomerDao.getCustomerNameById", fetchType = FetchType.EAGER)),
            @Result(property = "stadiumName", column = "stadium_id", one = @One(select="com.rhenium.meethere.dao.StadiumDao.getStadiumNameById", fetchType = FetchType.EAGER))
    })
    List<Booking> getBookingList(@Param("offset") int offset, @Param("limit") int limit);

    @Delete("DELETE FROM booking WHERE booking_id = #{bookingId}")
    void deleteBookingById(Integer bookingId);

    @Select("SELECT COUNT(*) FROM booking")
    int getBookingCount();
}
