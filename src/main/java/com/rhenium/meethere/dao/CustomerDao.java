package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.Customer;
import org.apache.ibatis.annotations.*;
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

    @Select("SELECT * FROM customer WHERE customerId = #{id}")
    Customer findCustomerById(@Param("id") String id);

    @Insert("INSERT INTO customer(email, phone_number, user_name, password, registered_time)" +
            " values (#{email}, #{phoneNumber}, #{userName}, #{password}, #{registeredTime})")
    int saveNewCustomer(Customer customer);
    //TODO: 验证这个返回值会是什么？

    @Update("UPDATE customer SET user_name = #{userName}, phone_number = #{phoneNumber} WHERE customer_id = #{id}")
    int saveCustomerInfo(@Param("id") String id, @Param("userName") String userName, @Param("phoneNumber") String phoneNumber);

}
