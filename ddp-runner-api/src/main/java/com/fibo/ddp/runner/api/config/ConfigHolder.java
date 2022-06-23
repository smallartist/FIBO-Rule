package com.fibo.ddp.runner.api.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ConfigHolder {

    //redisConfig
    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.db}")
    private int redisDb;
    @Value("${redis.password}")
    private String redisPwd;
    @Value("${redis.pool.maxTotal}")
    private int redisMaxTotal;
    @Value("${redis.pool.maxIdle}")
    private int redisMaxIdle;
    @Value("${redis.pool.maxWait}")
    private int redisMaxWait;
    @Value("${redis.pool.timeout}")
    private int redisTimeout;

    // 业务逻辑是否使用缓存
    @Value("${switch.use.cache}")
    private String cacheSwitch;
    // canal缓存同步是否开启
    @Value("${switch.canal.cache}")
    private String canalCacheSwitch;
    // canal主机地址
    @Value("${canal.hostname}")
    private String canalHostName;
    // canal端口
    @Value("${canal.port}")
    private int canalPort;

    //jdbcConfig
    /*@Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.driver}")
    private String DriverName;
    @Value("${pool.maxPoolSize}")
    private int maxPoolSize;
    @Value("${jdbc.username}")
    private String jdbcUserName;
    @Value("${jdbc.password}")
    private String jdbcPwd;
    @Value("${pool.maxWait}")
    private int jdbcMaxWait;
    @Value("${pool.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${pool.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    @Value("${pool.validationQuery}")
    private String validationQuery;

    //rabbitconfig
    @Value("${rabbitMQ.host}")
    private String rabbitHost;
    @Value("${rabbitMQ.port}")
    private int rabbitPort;
    @Value("${rabbitMQ.username}")
    private String rabbitUsername;
    @Value("${rabbitMQ.password}")
    private String rabbitPassword;*/

}