package com.fibo.ddp.common.service.datax.cache.impl;


import com.alibaba.fastjson.JSON;
import com.fibo.ddp.common.model.datax.consts.DataXCacheConst;
import com.fibo.ddp.common.service.datax.cache.DataXCacheService;
import com.fibo.ddp.common.service.datax.cache.Subscriber;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.utils.util.SnowFlakUtil;
import com.fibo.ddp.common.utils.websocket.constant.SendToWebConst;
import com.fibo.ddp.common.utils.websocket.manager.MyWebSocketSessionManager;
import com.fibo.ddp.common.utils.websocket.model.vo.SendToWebModel;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class DataXCacheServiceImpl implements DataXCacheService {
    @Autowired
    private RedisManager redisManager;
    //等待的锁
    private static final ReentrantLock lock = new ReentrantLock();
    //锁的等待队列
    private static final Condition condition = lock.newCondition();
    //监听缓存改变通知的redis单线程线程池
    private static final ExecutorService redisSingleExecutor = Executors.newSingleThreadExecutor();
    //订阅者
    @Autowired
    private Subscriber subscriber;

    @Override
    public Map queryForChange(Map<String, String> param, long time) {
        Map<String, Object> result = new HashMap<>();
        List<String> changeList = new ArrayList<>();
        Map<String, String> cacheMap = queryChangeInfo();
        handlerChangeList(param,cacheMap,changeList);
        //双重检查
        if (changeList.isEmpty()) {
            try {
                lock.lock();
                cacheMap = queryChangeInfo();
                handlerChangeList(param,cacheMap,changeList);
                if (changeList.isEmpty()){
                    condition.await(time, TimeUnit.SECONDS);
                    cacheMap = queryChangeInfo();
                    handlerChangeList(param,cacheMap,changeList);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
        result.put(DataXCacheConst.DATAX_CACHE_CHANGE_LIST_KEY, changeList);
        result.put(DataXCacheConst.DATAX_CACHE_INFO_KEY, cacheMap);
        return result;
    }

    public Map<String, String> queryChangeInfo() {
        Map<String, String> result = redisManager.hgetAll(DataXCacheConst.DATAX_WEB_CACHE_REDIS_KEY);
        return result;
    }

    private void  handlerChangeList(Map<String,String> paramMap,Map<String,String> cacheMap,List<String> changeList){
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String cacheValue = cacheMap.get(entry.getKey());
            String paramValue = entry.getValue();
            if (paramValue != null && !paramValue.equals(cacheValue)) {
                //匹配到有更新,添加到更新列表中
                changeList.add(entry.getKey());
            }
        }
    }


    public  void  notifyCondion(){
        try {
            lock.lock();
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public boolean sendChangeToWeb() {
        Map<String, String> map = queryChangeInfo();
        MyWebSocketSessionManager.sendTextToWeb(JSON.toJSONString(new SendToWebModel(SendToWebConst.DATAX_CACHE_CHANGE,map)));
        return false;
    }


    @Override
    public void initRedisSub() {
        redisSingleExecutor.submit(new Runnable() {
            @Override
            public void run() {
                redisManager.subscribe(subscriber,DataXCacheConst.DATAX_PUBSUB_CACHE_REDIS_CHANNEL);
            }
        });
        for (String type : DataXCacheConst.Type.TYPE_LIST) {
            redisManager.hsetnx(DataXCacheConst.DATAX_WEB_CACHE_REDIS_KEY,type, SnowFlakUtil.snowflakeIdStr());
        }
    }

    @Override
    public void pushRedisSub(String channel,String message) {
        if (StringUtils.isBlank(channel)){
            channel = DataXCacheConst.DATAX_PUBSUB_CACHE_REDIS_CHANNEL;
        }
         redisManager.publish(channel,message);
    }

    @Override
    public String refreshCacheInfo(String changeName) {
        redisManager.hset(DataXCacheConst.DATAX_WEB_CACHE_REDIS_KEY,changeName, SnowFlakUtil.snowflakeIdStr());
        return null;
    }
}
