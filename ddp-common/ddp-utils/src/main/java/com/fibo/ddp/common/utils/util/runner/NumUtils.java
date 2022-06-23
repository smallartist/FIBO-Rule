package com.fibo.ddp.common.utils.util.runner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NumUtils {

    public static double toDouble(Object o){
        double d = 0D;
        if (o==null){
            return d;
        }
        try {
            d = Double.valueOf(o.toString()).doubleValue();

        }catch (Exception e){
            log.error("转换为double失败，原值：{}",o);
            e.printStackTrace();
        }
        return d;
    }

}
