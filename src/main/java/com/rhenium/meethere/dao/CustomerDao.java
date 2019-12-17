package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.Customer;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author HavenTong
 * @date 2019/12/7 9:43 下午
 */
@Repository
@Mapper
public interface CustomerDao {

    @Select("SELECT * FROM customer WHERE email = #{email}")
    Customer findCustomerByEmail(@Param("email") String email);

    @Select("SELECT * FROM customer WHERE customer_id = #{id}")
    Customer findCustomerById(@Param("id") Integer id);

    @Select("SELECT COUNT(*) FROM customer")
    int getUserCount();

    @Select("SELECT * FROM customer ORDER BY customer_Id LIMIT #{offset}, #{limit}")
    List<Customer> getUserList(@Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO customer(email, phone_number, user_name, password, registered_time)" +
            " values (#{email}, #{phoneNumber}, #{userName}, #{password}, #{registeredTime})")
    int saveNewCustomer(Customer customer);

    @Update("UPDATE customer SET user_name = #{userName}, phone_number = #{phoneNumber} WHERE customer_id = #{id}")
    int saveCustomerInfo(@Param("id") String id, @Param("userName") String userName, @Param("phoneNumber") String phoneNumber);

    @Update("UPDATE customer SET password = #{password} WHERE customer_id = #{customerId}")
    int saveNewPassword(Customer customer);
}
