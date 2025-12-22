package com.example.rbac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * RBAC系统启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.example.rbac.web",
    "com.example.rbac.application",
    "com.example.rbac.infrastructure",
    "com.example.rbac.core"
})
public class RbacApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(RbacApplication.class, args);
    }
}

