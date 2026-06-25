package com.example.aics;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.aics.mapper")
public class AicsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AicsApplication.class, args);
    }
}