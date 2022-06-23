package com.fibo.ddp.runner.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ConfigurationContainor {

    @Resource
    private ConfigHolder configHolder;

    @Bean(name = "threadPoolTaskExecutor")
    ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2000);
        executor.setMaxPoolSize(10000);
        executor.setQueueCapacity(100000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }

    @Bean(name = "jedisPool")
    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(configHolder.getRedisMaxTotal());
        config.setMaxIdle(configHolder.getRedisMaxIdle());
        config.setMaxWaitMillis(configHolder.getRedisMaxWait());
        config.setTestOnBorrow(true);
//        config.setTestOnReturn(true);

        JedisPool pool = new JedisPool(config,
                configHolder.getRedisHost(),
                configHolder.getRedisPort(),
                configHolder.getRedisTimeout(),
                configHolder.getRedisPwd(),
                configHolder.getRedisDb());
        return pool;
    }

}
