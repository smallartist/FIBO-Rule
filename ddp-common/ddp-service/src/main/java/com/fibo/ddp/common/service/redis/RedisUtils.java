package com.fibo.ddp.common.service.redis;


import com.fibo.ddp.common.dao.canal.TableEnum;

import java.util.List;
import java.util.stream.Collectors;

public class RedisUtils {

    /**
     * 获取主键redis key
     * @param tableName
     * @param primaryId
     * @return
     */
    public static String getPrimaryKey(String tableName, Object primaryId){
        String key = "primary_key" + ":" + tableName + ":" + primaryId;
        return key;
    }

    public static String getPrimaryKey(TableEnum tableEnum, Object primaryId){
        return getPrimaryKey(tableEnum.getTableName(), primaryId);
    }

    public static List<String> getPrimaryKey(TableEnum tableEnum, List<Long> primaryIds){
        List<String> keys = primaryIds.stream().map(item -> getPrimaryKey(tableEnum, item)).collect(Collectors.toList());
        return keys;
    }

    /**
     * 获取外键redis key
     * @param tableName
     * @param foreignId
     * @return
     */
    public static String getForeignKey(String tableName, Object foreignId){
        String key = "foreign_key" + ":" + tableName + ":" + foreignId;
        return key;
    }

    public static String getForeignKey(TableEnum tableEnum, Object foreignId){
        return getForeignKey(tableEnum.getTableName(), foreignId);
    }

    public static List<String> getForeignKey(TableEnum tableEnum, List<Long> foreignIds){
        List<String> keys = foreignIds.stream().map(item -> getForeignKey(tableEnum, item)).collect(Collectors.toList());
        return keys;
    }
}
