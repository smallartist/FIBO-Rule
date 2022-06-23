package com.fibo.ddp.runner.api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(@Qualifier("clientHttpRequestFactory") ClientHttpRequestFactory factory){
        return new RestTemplate(factory);
    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate(@Qualifier("asyncClientHttpRequestFactory") AsyncClientHttpRequestFactory factory){
        return new AsyncRestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(){
        // 创建一个 httpCilent 简单工厂
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 设置连接超时
        factory.setConnectTimeout(15000);
        // 设置读取超时
        factory.setReadTimeout(5000);
        return factory;
    }

    @Bean
    public AsyncClientHttpRequestFactory asyncClientHttpRequestFactory(){
        // 创建一个 httpCilent 简单工厂
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 设置连接超时
//        factory.setConnectTimeout(15000);
        // 设置读取超时
//        factory.setReadTimeout(5000);
        //设置异步任务（线程不会重用，每次调用时都会重新启动一个新的线程）
        factory.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return factory;
    }
}
