package com.ectd.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * eCTD 4.0 Application Management Backend
 * Main application class for Spring Boot
 */
@SpringBootApplication
@MapperScan("com.ectd.backend.mapper")
public class EctdBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EctdBackendApplication.class, args);
    }
}

