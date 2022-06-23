package com.fibo.ddp.common.service.datax.runner.redis;

import com.alibaba.fastjson.JSON;
import com.fibo.ddp.common.model.datax.datasource.DataSource;
import com.fibo.ddp.common.service.redis.RedisManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

/**
 * kSession工厂类
 */
@Component
public class RedisKSessionFactory extends BaseKeyedPooledObjectFactory<String, Jedis> {

    @Autowired
    private RedisManager redisManager;

    @Override
    public Jedis create(String key) throws Exception {
        Jedis jedis = null;
        try {
//            long start = System.currentTimeMillis();
            String json = redisManager.get(key);
            if(json == null){
                throw new Exception("create jedis kSession fail, key is "+ key + ", ruleString is null！");
            }
            DataSource dataSource = JSON.parseObject(json, DataSource.class);
            String userName = dataSource.getUserName();
            if (StringUtils.isBlank(userName)){
                userName = "root";
            }
            JedisShardInfo jedisShardInfo = new JedisShardInfo( dataSource.getHost(),Integer.valueOf(dataSource.getPort()),3000,userName);
            jedisShardInfo.setPassword(dataSource.getPassword());
            jedis = new Jedis(jedisShardInfo);
            jedis.select(Integer.valueOf(dataSource.getDbName()));

//            long end = System.currentTimeMillis();
//            System.out.println("------------------jedis连接创建耗时：" + (end - start) + " ----------------------");
        } catch (Exception e) {
            throw e;
        }

        return jedis;
    }

    @Override
    public PooledObject<Jedis> wrap(Jedis kSession) {
        return new DefaultPooledObject<Jedis>(kSession);
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }
}
