package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.Customer;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author HavenTong
 * @date 2019/12/7 9:43 下午
 */
@Repository
@Mapper
public interface CustomerDao {

    @Select("SELECT * FROM customer WHERE email = #{email}")
    Customer findCustomerByEmail(@Param("email") String email);

    @Insert("INSERT INTO customer(email, phone_number, user_name, password, registered_time)" +
            " values (#{email}, #{phoneNumber}, #{userName}, #{password}, #{registeredTime})")
    int saveCustomer(Customer customer);

    @Update("UPDATE customer SET user_name = #{userName} WHERE customer_id = #{customerId} ")
    int updateUserName(Customer customer);
}
