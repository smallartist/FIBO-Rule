package com.fibo.ddp.common.utils.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

@Component
@Slf4j
public class SnowFlakUtil {
    //    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private static long workerId = 1;//为终端ID
    private static long datacenterId = 1;//数据中心ID
    private static Snowflake snowflake;
    @PostConstruct
    public void init() {
        try {
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr()) % 32;
        }catch (Exception e){
            workerId = (1+new Random().nextInt(30));
        }
        log.info("当前机器的workId:{}", workerId);
        snowflake = IdUtil.createSnowflake(workerId, datacenterId);
    }

    public static synchronized long snowflakeId() {
        return snowflake.nextId();
    }

    public static synchronized String snowflakeIdStr() {
        return snowflake.nextId() + "";
    }

    public static synchronized long snowflakeId(long workerId, long datacenterId) {
        Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);
        return snowflake.nextId();
    }
}