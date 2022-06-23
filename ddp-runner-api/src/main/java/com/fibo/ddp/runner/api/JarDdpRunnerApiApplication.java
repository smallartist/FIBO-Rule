package com.fibo.ddp.runner.api;

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
public class JarDdpRunnerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JarDdpRunnerApiApplication.class, args);
    }

}
