package com.fibo.ddp.common.service.datax.runner;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.datax.datasource.DataSource;
import com.fibo.ddp.common.service.datax.runner.redis.RedisKSessionPool;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.utils.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
@Slf4j
public class DataBaseReSource {

    private static RedisKSessionPool redisKSessionPool;

    private static RedisManager redisManager;

    @Autowired
    public DataBaseReSource(RedisKSessionPool redisKSessionPool, RedisManager redisManager) {
        DataBaseReSource.redisKSessionPool = redisKSessionPool;
        DataBaseReSource.redisManager = redisManager;
    }

    public static final  DruidDataSource getDataSource(String driverClassName, String url , String username, String passWord){
        DruidDataSource dynamicDataSource = new DruidDataSource();
        dynamicDataSource.setDriverClassName(driverClassName);
        dynamicDataSource.setUrl(url);
        dynamicDataSource.setUsername(username);
        dynamicDataSource.setPassword(passWord);
        return dynamicDataSource;
    }


    public static final class MySql {
        public static final String type = "MySQL";
        public static final String driverClassName="com.mysql.cj.jdbc.Driver";
        public static final String urlPrefix="jdbc:mysql://";
        public static final String urlPostfix ="?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull";
        public static DruidDataSource getDataSource( DataSource dataSource){
            String url = urlPrefix + dataSource.getHost() + ":" + dataSource.getPort() + "/" + dataSource.getDbName() + urlPostfix;
            DruidDataSource dynamicDataSource = DataBaseReSource.getDataSource(driverClassName, url, dataSource.getUserName(), dataSource.getPassword());
            return dynamicDataSource;
        }
    }
    public static final class Oracle {
        public static final String type = "Oracle";
        public static final String driverClassName="oracle.jdbc.driver.OracleDriver";
        public static final String urlPrefix="jdbc:oracle:thin:@";
        public static DruidDataSource getDataSource( DataSource dataSource){
            String url = urlPrefix + dataSource.getHost() + ":" + dataSource.getPort() + ":" + dataSource.getDbName();
            String username = dataSource.getUserName();
            String passWord = dataSource.getPassword();
            DruidDataSource dynamicDataSource = DataBaseReSource.getDataSource(driverClassName, url, username, passWord);
            return dynamicDataSource;
        }

    }
    public static final class SqlServer {
        public static final String type = "SqlServer";
        public static final String driverClassName="com.microsoft.jdbc.sqlserver.SQLServerDriver";
        public static final String urlPrefix="jdbc:sqlserver://";
        public static final String DBNamePrefix=";DatabaseName=";
        public static DruidDataSource getDataSource( DataSource dataSource){
            String url = urlPrefix + dataSource.getHost() + ":" + dataSource.getPort() + DBNamePrefix + dataSource.getDbName();
            String username = dataSource.getUserName();
            String passWord = dataSource.getPassword();
            DruidDataSource dynamicDataSource = DataBaseReSource.getDataSource(driverClassName, url, username, passWord);
            return dynamicDataSource;
        }

    }
    public static final class DB2 {
        public static final String type = "DB2";
        public static final String driverClassName="com.ibm.db2.jcc.DB2Driver";
        public static final String urlPrefix="jdbc:db2://";
        public static DruidDataSource getDataSource( DataSource dataSource){
            String url = urlPrefix + dataSource.getHost() + ":" + dataSource.getPort() + "/" + dataSource.getDbName();
            String username = dataSource.getUserName();
            String passWord = dataSource.getPassword();
            DruidDataSource dynamicDataSource = DataBaseReSource.getDataSource(driverClassName, url, username, passWord);
            return dynamicDataSource;
        }
    }
    public static final class Redis {
        public static final String type = "Redis";

        public static Jedis getDataSource(DataSource dataSource,String keyMd5){
            Jedis jedis = null;
            redisManager.set(keyMd5, JSON.toJSONString(dataSource), 120);
            try {
                jedis = redisKSessionPool.borrowObject(keyMd5);
            } catch (Exception e) {
                log.error("获取jedis连接失败,Md5Key:{},dataSource:{}",keyMd5,dataSource);
                throw new ApiException(ErrorCodeEnum.GET_REDIS_SOURCE_ERROR.getCode(), ErrorCodeEnum.GET_REDIS_SOURCE_ERROR.getMessage());
            }
            return jedis;
        }
        public static  void closeJedis(Jedis jedis){
            //关闭链接
            try {
                jedis.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    public static final class HiveConst{
        public static final String type = "Hive";
        public static final String driverClassName="";

    }
    public static final class SparkConst{
        public static final String type = "HSpark";
        public static final String driverClassName="";

    }

}
