package com.example.graduationproject.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.graduationproject.web.mapper")
@SpringBootApplication
public class GraduationProjectWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraduationProjectWebApplication.class, args);
    }

}
