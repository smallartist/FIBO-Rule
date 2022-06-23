package com.fibo.ddp.common.utils.constant.strategyx;


import java.util.UUID;

public class DecisionTablesRunnerConst {

    public static final String DECISION_FILE_HEAD = " package com.baoying.enginex.executor.drools \\r\\n" +
            " import java.util.Map;\\r\\n" +
            " import java.util.List;\\r\\n" +
            " import java.util.ArrayList;\\r\\n" +
            " import java.util.HashMap;\\r\\n" +
            " import com.baoying.enginex.executor.engine.model.InputParam;\\r\\n" +
            " import com.baoying.enginex.executor.engine.model.Result;\\r\\n" +
            " import com.baoying.enginex.executor.engine.model.EngineRule;\\r\\n";
    public static final String RULE_NAME_PREFIX = " rule \t";
    public static final String RULE_SALIENCE_PREFIX = "\\r\\n salience\\t ";
    public static final String RULE_CONDITION_PREFIX = "\\r\\n when \\r\\n";
    public static final String CONDITION_DETAIL_PREFIX = "\\t $inputParam : InputParam();\\r\\n Map";
    public static final String CONDITION_DETAIL_SUFFIX = " \t from $inputParam.inputParam;\\r\\n";
    public static final String RULE_DISPOSE_PREFIX = "\\t then \\r\\n";
    public static final String DISPOSE_PREFIX =
            "\\t List<Result>  resultList =$inputParam.getResult();\\r\\n" +
                    "\\t Result result =new Result(); \\r\\n" +
                    "\\t result.setResultType(\"";
    public static final String DISPOSE_INFIX = "\"); \\r\\n" +
            "\\t result.setVersionCode(\"";
    public static final String DISPOSE_SUFFIX = "\"); \\r\\n" +
            "\\t Map<String, Object> map =new HashMap<>(); \\r\\n";

    public static final String HIT_RESULT = "\\t map.put(\"result\",true); \\r\\n";

    public static final String RULE_END = "\\t result.setMap(map); \\r\\n" +
            " resultList.add(result); \\r\\n" +
            "\\t $inputParam.setResult(resultList); \\r\\n" +
            " end\\r\\n";

    public static final int DEFAULT_TYPE = 1;

    //拼装规则执行的content
    public static String fitContent(String condition) {
        String content = "";
        String name = "decisionTables"+UUID.randomUUID().toString().replace("-", "");
        content += DECISION_FILE_HEAD +
                RULE_NAME_PREFIX + name +
                RULE_SALIENCE_PREFIX + 10 +
                RULE_CONDITION_PREFIX +
                CONDITION_DETAIL_PREFIX + condition +
                CONDITION_DETAIL_SUFFIX +
                RULE_DISPOSE_PREFIX +
                DISPOSE_PREFIX + DEFAULT_TYPE +
                DISPOSE_INFIX + name +
                DISPOSE_SUFFIX;
        content += HIT_RESULT + RULE_END;
        return content;
    }

//    public static void main(String[] args) {
//        String versionCode = "rule1";
//        Integer salience = null;
//        String rule = "age>10";
//        Integer type = null;
//        Integer score = 10;
//        Map<String,String> contentMap = new HashMap<>();
//        contentMap.put("a","a");
//        contentMap.put("b","b");
//        contentMap.put("c","c");
//        String s = fitContent(versionCode, salience, rule, type, score,contentMap);
//        System.out.println(s);
//    }
}


