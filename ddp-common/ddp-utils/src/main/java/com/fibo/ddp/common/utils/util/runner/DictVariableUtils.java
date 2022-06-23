package com.fibo.ddp.common.utils.util.runner;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class DictVariableUtils {

    public static Object getValueFromJsonObject(JSONObject jsonObject){
        Object result = "";
        if (jsonObject.get("value") != null) {
            switch (jsonObject.getString("type")){
                case "date":
                    try {
                        result = DateUtil.format(new Date(),jsonObject.getString("value"));
                    }catch (Exception e){
                        e.printStackTrace();
                        result = DateUtil.format(new Date(),"yyyyMMdd");
                    }
                    break;
                default:
                    result = jsonObject.get("value");
            }
        }
        return result;
    }

}
