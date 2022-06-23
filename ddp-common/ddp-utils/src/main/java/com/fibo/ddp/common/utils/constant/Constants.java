package com.fibo.ddp.common.utils.constant;

/**
 * 公共变量约定
 */
public class Constants {
    // token名称
    public static final String SYSTEM_KEY_TOKEN = "token";
    // token时间 单位秒
    public static final Long LOGIN_TOKEN_TIME = 7200L;
    // token最大剩余时间，需刷新 单位秒
    public static final Long LOGIN_TOKEN_REFRESH_TIME = 600L;

    // 规则集节点相关常量
    public interface ruleNode {
        // 互斥组
        int MUTEXGROUP = 1;
        // 执行组
        int EXECUTEGROUP = 2;
    }

    public interface switchFlag {
        // 开关打开
        String ON = "on";
        // 开关关闭
        String OFF = "off";
    }

    public interface fieldName {
        // 字段英文名
        String fieldEn = "field_en";
        //字段中文名
        String fieldCn = "field_cn";
    }

    public interface cacheConf {
        // 缓存时间
        int cacheSecond = 120;
        String cachePrefix = "runner:cache:";
    }
}