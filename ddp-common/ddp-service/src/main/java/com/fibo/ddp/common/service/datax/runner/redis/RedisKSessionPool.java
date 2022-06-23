package com.fibo.ddp.common.service.datax.runner.redis;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * kSession连接池
 */
@Component
public class RedisKSessionPool implements InitializingBean {

    private GenericKeyedObjectPool<String, Jedis> pool;

    @Autowired
    private RedisKSessionFactory kSessionFactory;

    /**
     * 初始化方法
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        initPool();
    }

    /**
     * 初始化连接池
     * @return
     * @throws Exception
     */
    public void initPool() throws Exception {
        GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
        poolConfig.setMaxTotalPerKey(200);
        poolConfig.setMaxIdlePerKey(50);
        poolConfig.setMinIdlePerKey(5);
        poolConfig.setMaxTotal(2000);
        this.pool = new GenericKeyedObjectPool(kSessionFactory, poolConfig);
    }

    /**
     * 获取一个连接对象
     * @return
     * @throws Exception
     */
    public Jedis borrowObject(String key) throws Exception {
        return pool.borrowObject(key);
    }

    /**
     * 归还一个连接对象
     * @param
     */
    public void returnObject(String key, Jedis kSession) {
        if(kSession != null){
            pool.returnObject(key, kSession);
        }
    }

    public void setkSessionFactory(RedisKSessionFactory kSessionFactory) {
        this.kSessionFactory = kSessionFactory;
    }

}
