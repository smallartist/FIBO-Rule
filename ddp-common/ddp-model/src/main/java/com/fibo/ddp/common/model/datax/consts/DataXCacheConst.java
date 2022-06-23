package com.fibo.ddp.common.model.datax.consts;


public class DataXCacheConst {
    //web端需要的缓存更新时间信息
    public static final String DATAX_WEB_CACHE_REDIS_KEY = "DATAX:WEB:CACHE";
    //发布订阅的key
    public static final String DATAX_PUBSUB_CACHE_REDIS_CHANNEL = "DATAX:PUBSUB:CACHE";
    //返回给前端的改变列表名
    public static final String DATAX_CACHE_CHANGE_LIST_KEY = "changeList";
    //返回给前端的缓存信息
    public static final String DATAX_CACHE_INFO_KEY = "cacheInfo";

    public static class Type{
        public static final String[] TYPE_LIST= new String[]{Type.DATA_SOURCE, Type.MQ_SOURCE, Type.FIELD, Type.INTERFACE};
        public static final String DATA_SOURCE = "data_source";
        public static final String MQ_SOURCE = "mq_source";
        public static final String FIELD = "field";
        public static final String INTERFACE = "interface";

    }

}
