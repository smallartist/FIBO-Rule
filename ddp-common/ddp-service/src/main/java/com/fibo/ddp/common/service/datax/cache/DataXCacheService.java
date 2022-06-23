package com.fibo.ddp.common.service.datax.cache;


import java.util.Map;


public interface DataXCacheService {
    Map queryForChange(Map<String, String> param, long time);

    Map queryChangeInfo();

   void  notifyCondion();

   boolean  sendChangeToWeb();

   void initRedisSub();

   void pushRedisSub(String channel,String message);

    String refreshCacheInfo(String changeName);
}
