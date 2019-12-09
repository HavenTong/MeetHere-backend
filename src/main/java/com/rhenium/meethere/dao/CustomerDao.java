package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.Customer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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
}
