package com.fibo.ddp.manager.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan({"com.fibo.ddp.common.dao.**"})
@ComponentScan(basePackages = "com.fibo.ddp.**")
@EnableAsync
public class JarDdpManagerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(JarDdpManagerWebApplication.class, args);
    }

}
