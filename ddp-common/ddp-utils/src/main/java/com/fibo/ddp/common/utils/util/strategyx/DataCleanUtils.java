package com.fibo.ddp.common.utils.util.strategyx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataCleanUtils {
    public static Map<String,List<JSONObject>> recursionGroup(List<JSONObject> param, List<String> keys){
        return param.stream().collect(Collectors.groupingBy(item->{
            String cond = "";
            for (String key : keys) {
                if (StringUtils.isNotBlank(cond)){
                    cond+="_";
                }
                cond += getObjByKeyAndJson(item,key);
//                String[] split = key.split("\\.");
//                if (split.length>1){
//                    JSONObject jsonObject = item;
//                    for (int i = 0; i < split.length; i++) {
//                        if (i<split.length-1){
//                            jsonObject = handlerToJson(jsonObject,split[i]);
//                        }else {
//                            cond += handlerToObj(jsonObject,split[i]).toString();
//                        }
//                    }
//                }else {
//                    cond += handlerToObj(item,split[0]).toString();
//
//                }
            }
            return cond;
        }));
    }

    public static Object handlerToJson(Object preObject, String key){
        Object result = null;
        if ("[]".equals(key)){
            //是数组 取第一个元素
//            Object object = JSONObject.parseArray(JSONObject.toJSONString(preObject)).get(0);
            Object object = ((JSONArray)preObject).get(0);
            result = object;
        }else {
            //不是数组
//            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(preObject));
            JSONObject jsonObject = (JSONObject) preObject;
            Object object = jsonObject.get(key);
            result = object;
        }
        return result;
    }

    public static Object handlerToObj(JSONObject preObject, String key){
        Pattern pattern = Pattern.compile("(\\[(0|([1-9]([0-9])*))\\])+$");
        Matcher matcher = pattern.matcher(key);
        Object result = null;
        if (matcher.find()){
            //是数组
            String group = matcher.group();
            JSONArray jsonArray = preObject.getJSONArray(key.replace(group, ""));
            String[] split = group.replace("[", "").split("]");
            int length = split.length;
            for (int i = 0; i <length ; i++) {
                String indexStr = split[i];
                if (i<length-1){
                    jsonArray = jsonArray.getJSONArray(Integer.valueOf(indexStr));
                }else {
                    result = jsonArray.get(Integer.valueOf(indexStr));
                }
            }
        }else {
            //不是数组
            result = preObject.get(key);
        }
        return result;
    }

    public static Object getObjByKeyAndJson(JSONObject preObject, String key){
        if (StringUtils.isBlank(key)){
            return preObject;
        }
        String[] split = key.split("\\.");
        int length = split.length;
        Object temp = preObject;
        for (int i = 0; i <length ; i++) {
            temp = DataCleanUtils.handlerToJson(temp,split[i]);
        }
        return temp;
    }
}
