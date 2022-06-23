package com.fibo.ddp.enginex.runner.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.utils.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;

/**
 * Canal数据同步
 * 实现ApplicationRunner接口，springboot启动成功后会执行run方法
 */
@Component
public class CanalClient implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static int BATCH_SIZE = 1000;
    @Autowired
    private RedisManager redisManager;
    // canal缓存同步是否开启
    @Value("${switch.canal.cache}")
    private String canalCacheSwitch;
    // canal主机地址
    @Value("${canal.hostname}")
    private String canalHostName;
    // canal端口
    @Value("${canal.port}")
    private int canalPort;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(Constants.switchFlag.OFF.equals(canalCacheSwitch)){
            return;
        }

        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(canalHostName, canalPort),
                "example", "", "");
        try {
            //打开连接
            connector.connect();
            //订阅数据库表,全部表
            connector.subscribe(".*\\..*");
            //回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
            connector.rollback();
            while (true) {
                logger.info("canal数据同步监听中...");
                // 获取指定数量的数据
                Message message = connector.getWithoutAck(BATCH_SIZE);
                //获取批量ID
                long batchId = message.getId();
                //获取批量的数量
                int size = message.getEntries().size();
                //如果没有数据
                if (batchId == -1 || size == 0) {
                    try {
                        //线程休眠2秒
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    //如果有数据,处理数据
                    printEntry(message.getEntries());
                }
                //进行 batch id 的确认。确认之后，小于等于此 batchId 的 Message 都会被确认。
                connector.ack(batchId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.disconnect();
        }
    }

    /**
     * 解析binlog获得的实体类信息
     */
    private void printEntry(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                //开启/关闭事务的实体类型，跳过
                continue;
            }

            String tableName = entry.getHeader().getTableName();
            TableEnum tableEnum = TableEnum.getByTableName(tableName);
            if(tableEnum == null){
                // 没有在枚举中定义的表，跳过
                continue;
            }

            //RowChange对象，包含了一行数据变化的所有特征
            //比如isDdl 是否是ddl变更操作 sql 具体的ddl sql beforeColumns afterColumns 变更前后的数据字段等等
            CanalEntry.RowChange rowChage;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }

            //获取操作类型：insert/update/delete类型
            CanalEntry.EventType eventType = rowChage.getEventType();
            //打印Header信息
            logger.info(String.format("============= binlog[%s:%s] , name[%s,%s] , eventType : %s =============",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

            //判断是否是DDL语句
            if (rowChage.getIsDdl()) {
                logger.info("============= isDdl: true,sql:" + rowChage.getSql());
            }

            //获取RowChange对象里的每一行数据
            for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                //如果是删除语句
                if (eventType == CanalEntry.EventType.DELETE) {
                    row(rowData.getBeforeColumnsList(), tableName);
                    //如果是新增语句
                } else if (eventType == CanalEntry.EventType.INSERT) {
                    row(rowData.getAfterColumnsList(), tableName);
                    //如果是更新的语句
                } else {
                    //变更前的数据
//                    printColumn(rowData.getBeforeColumnsList(), tableName);
                    //变更后的数据
                    row(rowData.getAfterColumnsList(), tableName);
                }
            }
        }
    }

    private void row(List<CanalEntry.Column> columns, String tableName) {
        Optional<CanalEntry.Column> keyColumn = columns.stream().filter(item -> item.getIsKey()).findFirst();
        if(keyColumn.isPresent()){
            // 获取主键id
            String id = keyColumn.get().getValue();
            // 拼接主键key
            String key = RedisUtils.getPrimaryKey(tableName, id);
            // 拼接外键key
            String foreignKey = null;
            // 子表的redis key需要拼接上主表的id
            TableEnum tableEnum = TableEnum.getByTableName(tableName);
            if(tableEnum != null){
                Optional<CanalEntry.Column> foreignKeyColumn = columns.stream().filter(item -> item.getName().equals(tableEnum.getForeignId())).findFirst();
                if(foreignKeyColumn.isPresent()){
                    String foreignKeyValue = foreignKeyColumn.get().getValue();
                    foreignKey = RedisUtils.getForeignKey(tableName, foreignKeyValue);
                }
            }

            for (CanalEntry.Column column : columns) {
                // 更新发生改变的字段缓存
                setUpdatedColumnCache(column, key, foreignKey);
            }

            // 指标表特殊处理
            dealSpecialTable(columns, tableName);
        }
    }

    private void setUpdatedColumnCache(CanalEntry.Column column, String key, String foreignKey){
        if(column.getUpdated()) {
            logger.info("开始主键缓存更新, {}, {}, {}", key, column.getName(), column.getValue());

            redisManager.hset(key, column.getName(), column.getValue());

            logger.info("结束主键缓存更新, {}, {}, {}", key, column.getName(), column.getValue());

            if(foreignKey != null){
                logger.info("开始外键缓存更新, {}, {}", key, foreignKey);

                redisManager.sadd(foreignKey, key);

                logger.info("结束外键缓存更新, {}, {}", key, foreignKey);
            }
        }
    }

    private void setAllColumnCache(CanalEntry.Column column, String key){
        logger.info("开始主键缓存更新, {}, {}, {}", key, column.getName(), column.getValue());

        redisManager.hset(key, column.getName(), column.getValue());

        logger.info("结束主键缓存更新, {}, {}, {}", key, column.getName(), column.getValue());
    }

    private void dealSpecialTable(List<CanalEntry.Column> columns, String tableName){
        if(tableName.equals(TableEnum.T_FIELD.getTableName())){
            String organ_id = null;
            String field_en = null;
            String field_cn = null;
            for (CanalEntry.Column column : columns) {
                String name = column.getName();
                switch (name) {
                    case "organ_id":
                        organ_id = column.getValue();
                        break;
                    case "field_en":
                        field_en = column.getValue();
                        break;
                    case "field_cn":
                        field_cn = column.getValue();
                        break;
                    default:
                        break;
                }
            }

            String fieldEn = "field_en:" + organ_id + ":" + field_en;
            String fieldEnKey = RedisUtils.getPrimaryKey(tableName, fieldEn);

            String fieldCn = "field_cn:" + organ_id + ":" + field_cn;
            String fieldCnKey = RedisUtils.getPrimaryKey(tableName, fieldCn);

            // 如果field_en或field_cn发生变化，则对应的key为新生成的，需要保存所有字段缓存
            Optional<CanalEntry.Column> fieldEnOptional = columns.stream().filter(item -> item.getName().equals("field_en") && item.getUpdated()).findFirst();
            Optional<CanalEntry.Column> fieldCnOptional = columns.stream().filter(item -> item.getName().equals("field_cn") && item.getUpdated()).findFirst();
            for (CanalEntry.Column column : columns) {
                if(fieldEnOptional.isPresent()){
                    // 更新所有字段缓存
                    setAllColumnCache(column, fieldEnKey);
                } else {
                    // 更新发生改变的字段缓存
                    setUpdatedColumnCache(column, fieldEnKey, null);
                }

                if(fieldCnOptional.isPresent()){
                    setAllColumnCache(column, fieldCnKey);
                } else {
                    setUpdatedColumnCache(column, fieldCnKey, null);
                }
            }
        }
    }

}