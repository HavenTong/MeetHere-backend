package com.rhenium.meethere;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.rhenium.meethere.dao")
@SpringBootApplication
public class MeethereApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeethereApplication.class, args);
    }

}
