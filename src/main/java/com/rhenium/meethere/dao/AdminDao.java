package com.rhenium.meethere.dao;

import com.rhenium.meethere.domain.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author HavenTong
 * @date 2019/12/17 5:19 下午
 */
@Mapper
@Repository
public interface AdminDao {
    @Select("SELECT * FROM admin WHERE email = #{email}")
    Admin findAdminByEmail(@Param("email") String email);
}
