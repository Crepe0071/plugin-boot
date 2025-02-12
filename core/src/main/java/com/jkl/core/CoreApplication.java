package com.jkl.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoreApplication {

    public static void main(String[] args) {
        // Spring Boot 애플리케이션 부트스트랩
        var context = SpringApplication.run(CoreApplication.class, args);
    }
}
