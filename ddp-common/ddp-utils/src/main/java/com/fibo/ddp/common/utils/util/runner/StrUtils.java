package com.fibo.ddp.common.utils.util.runner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtils {
    //判断是否为一个数字
    public static boolean isNum(String str){
        if (str==null||"".equals(str)){
            return false;
        }
        Pattern pattern = Pattern.compile("^(-|\\+)?\\d+(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    //将字符串转为Long类型
    public static Long strToLong(String str){
        if (isNum(str)){
            return Long.valueOf(str);
        }
        return null;
    }
    public static Double strToDouble(String str){
        if (isNum(str)){
            return Double.valueOf(str);
        }
        return null;
    }
}
