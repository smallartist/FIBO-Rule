package com.fibo.ddp.common.utils.constant.monitor;

public class RowKeyUtil {
    /**
     * Long.MAX_VALUE - lastUpdate 得到的值再补齐20位
     * @param lastUpdate
     * @return
     */
    public static String formatLastUpdate(long lastUpdate){
        if(lastUpdate<0){
            lastUpdate = 0;
        }
        long diff = Long.MAX_VALUE - lastUpdate;
        return String.format("%0"+20+"d",diff);
    }
}
