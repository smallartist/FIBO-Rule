package com.fibo.ddp.enginex.runner.canal;

import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.datax.datasource.SimpleMapper;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cache")
public class CacheController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SimpleMapper simpleMapper;
    @Autowired
    private RedisManager redisManager;

    @RequestMapping(value = "initCache", method = RequestMethod.GET)
    public void initCache() {
        logger.info("===================== 缓存初始化开始 =====================");
        long start = System.currentTimeMillis();
        // 遍历表
        for (TableEnum tableEnum : TableEnum.values()) {
            String tableName = tableEnum.getTableName();
            logger.info("===================== 开始初始化缓存表[{}] =====================", tableName);

            String sqlStr = "select * from " + tableName;
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put("sqlStr", sqlStr);
            List<LinkedHashMap<String, Object>> result = simpleMapper.customSelect(parameterMap);
            // 遍历行
            for (LinkedHashMap<String, Object> map : result) {
                row(tableEnum, map);
            }
            logger.info("===================== 结束初始化缓存表[{}]，共[{}]条数据 =====================", tableName, result.size());
        }
        long end = System.currentTimeMillis();
        logger.info("===================== 缓存初始化成功！！耗时：{}ms =====================", (end - start));
    }

    private void row(TableEnum tableEnum, LinkedHashMap<String, Object> map) {
        String tableName = tableEnum.getTableName();
        String primaryKey = null;
        String foreignKey = null;

        if (StringUtils.isNotBlank(tableEnum.getPrimaryId())) {
            String primaryId = map.get(tableEnum.getPrimaryId()).toString();
            primaryKey = RedisUtils.getPrimaryKey(tableName, primaryId);
        }

        if (StringUtils.isNotBlank(tableEnum.getForeignId())) {
            Object obj = map.get(tableEnum.getForeignId());
            if (obj != null && !"".equals(obj.toString())) {
                String foreignId = obj.toString();
                foreignKey = RedisUtils.getForeignKey(tableName, foreignId);
            }
        }

        if (StringUtils.isNotBlank(primaryKey)) {
            // 遍历列
            for (String field : map.keySet()) {
                String value = map.get(field) == null ? null : map.get(field).toString();
                setColumnCache(primaryKey, field, value);
            }
        }

        if (StringUtils.isNotBlank(foreignKey)) {
            setForeignKeyCache(primaryKey, foreignKey);
        }

        // 指标表特殊处理
        dealSpecialTable(tableName, map);
    }

    private void setColumnCache(String primaryKey, String field, String value) {
        logger.info("开始主键缓存设置, primaryKey:{}, field:{}, value:{}", primaryKey, field, value);

        redisManager.hset(primaryKey, field, value);

        logger.info("结束主键缓存设置, primaryKey:{}, field:{}, value:{}", primaryKey, field, value);
    }

    private void setForeignKeyCache(String primaryKey, String foreignKey) {
        logger.info("开始外键缓存设置, primaryKey:{}, foreignKey:{}", primaryKey, foreignKey);

        redisManager.sadd(foreignKey, primaryKey);

        logger.info("结束外键缓存设置, primaryKey:{}, foreignKey:{}", primaryKey, foreignKey);
    }

    private void dealSpecialTable(String tableName, LinkedHashMap<String, Object> map) {
        if(tableName.equals(TableEnum.T_FIELD.getTableName())){
            String fieldEn = "field_en:" + map.get("organ_id") + ":" + map.get("field_en");
            String fieldEnKey = RedisUtils.getPrimaryKey(tableName, fieldEn);

            String fieldCn = "field_cn:" + map.get("organ_id") + ":" + map.get("field_cn");
            String fieldCnKey = RedisUtils.getPrimaryKey(tableName, fieldCn);

            for (String field : map.keySet()) {
                String value = map.get(field) == null ? null : map.get(field).toString();
                setColumnCache(fieldEnKey, field, value);
                setColumnCache(fieldCnKey, field, value);
            }
        }
    }
}
