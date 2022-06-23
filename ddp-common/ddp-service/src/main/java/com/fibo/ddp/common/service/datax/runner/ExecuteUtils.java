package com.fibo.ddp.common.service.datax.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.datax.datamanage.Field;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.runner.ExpressionParam;
import com.fibo.ddp.common.service.datax.datamanage.FieldService;
import com.fibo.ddp.common.utils.common.MD5;
import com.fibo.ddp.common.utils.constant.runner.ParamTypeConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.runner.JevalUtil;
import com.fibo.ddp.common.utils.util.runner.StrUtils;
import com.fibo.ddp.common.utils.util.runner.jeval.EvaluationException;
import com.fibo.ddp.common.utils.util.runner.jeval.Evaluator;
import com.fibo.ddp.common.utils.util.runner.jeval.function.math.Groovy;
import com.fibo.ddp.common.utils.util.runner.jeval.function.math.Python;
import com.fibo.ddp.common.utils.util.strategyx.DataCleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//底层执行的工具类
@Component
public class ExecuteUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExecuteUtils.class);
    private static Groovy groovy;
    private static CommonService commonService;
    private static Python python;
    private static FieldService fieldService;

    @Autowired
    public ExecuteUtils(Groovy groovy, CommonService commonService, Python python, FieldService fieldService) {
        ExecuteUtils.groovy = groovy;
        ExecuteUtils.commonService = commonService;
        ExecuteUtils.python = python;
        ExecuteUtils.fieldService = fieldService;
    }

    //获取基本单元的执行结果
    public final static boolean getExpressionResult(ExpressionParam expressionParam, Map<String, Object> params) {
        //如果是规则的条件的话，判断是否为叶子节点，如果不是则直接返回false
        if (expressionParam.getConditionType() != null && expressionParam.getConditionType() != 2) {
            return false;
        }
        String operator = expressionParam.getOperator();

        //获取第二个参数的类型
        Integer variableType = expressionParam.getVariableType();
        //给每个参数取值
        Object paramOne = getValueByKey(2, params, expressionParam.getFieldEn(), null);
        //默认为常量
        Object paramTwo = getValueByKey(variableType, params, expressionParam.getFieldValue(), null);

        if (paramOne == null || "".equals(paramOne) || paramTwo == null || "".equals(paramTwo)) {
            return false;
        }
        return getCondResult(operator, paramOne, paramTwo);
    }

    //传入两个参数和一个操作符进行比对获取结果
    public final static boolean getCondResult(String operator, Object paramOne, Object paramTwo) {
        boolean result = false;
        Double numOne = StrUtils.strToDouble(paramOne.toString());
        Double numTwo = StrUtils.strToDouble(paramTwo.toString());
        switch (operator) {
            //数值之间的比较
            case "==":
                if (numOne != null && numTwo != null) {
                    result = numOne.equals(numTwo);
                } else if (paramOne != null && paramTwo != null) {
                    result = paramOne.toString().equals(paramTwo.toString());
                }
                break;
            case "!=":
                if (numOne != null && numTwo != null) {
                    result = !numOne.equals(numTwo);
                } else if (paramOne != null && paramTwo != null) {
                    result = !paramOne.toString().equals(paramTwo.toString());
                }
                break;
            case ">":
                if (numOne != null && numTwo != null) {
                    result = numOne > numTwo;
                }
                break;
            case "<":
                if (numOne != null && numTwo != null) {
                    result = numOne < numTwo;
                }
                break;
            case ">=":
                if (numOne != null && numTwo != null) {
                    result = numOne >= numTwo;
                }
                break;
            case "<=":
                if (numOne != null && numTwo != null) {
                    result = numOne <= numTwo;
                }
                break;
            //字符串之间的比较
            case "equals":
                result = paramOne.toString().equals(paramTwo.toString());
                break;
            case "not equals":
                result = !paramOne.toString().equals(paramTwo.toString());
                break;
            case "contains":
                result = paramOne.toString().contains(paramTwo.toString());
                break;
            case "not contains":
                result = !paramOne.toString().contains(paramTwo.toString());
                break;
            case "regex":
                Pattern pattern = Pattern.compile(paramTwo.toString());
                Matcher matcher = pattern.matcher(paramOne.toString());
                result = matcher.find();
                break;
            case "in":
                if (paramTwo instanceof List) {
                    List list = (List) paramTwo;
                    result = list.contains(paramOne);
                } else if (paramTwo instanceof Map) {
                    Map map = (Map) paramTwo;
                    result = map.containsKey(paramOne);
                }
                break;
            case "not in":
                if (paramTwo instanceof List) {
                    List list = (List) paramTwo;
                    result = !list.contains(paramOne);
                } else if (paramTwo instanceof Map) {
                    Map map = (Map) paramTwo;
                    result = !map.containsKey(paramOne);
                }
                break;
        }
        return result;
    }

    //根据key，分不同类型取出值
    public final static Object getValueByKey(Integer variableType, Map<String, Object> params, String paramKey, List<JSONObject> list) {
        Object result = paramKey;
        if (variableType != null) {
            switch (variableType) {
                case ParamTypeConst
                        .CONSTANT:
                    //常量类型
                    result = paramKey;
                    break;
                case ParamTypeConst
                        .VARIABLE:
                    //变量类型
                    result = getObjFromMap(params, paramKey);
                    break;
                case ParamTypeConst
                        .CUSTOM:
                    //自定义脚本类型
                    if (list == null || list.isEmpty()) {
                        result = getObjFromScript(params, paramKey);
                    } else {
                        result = getObjFromScript(params, paramKey, list);
                    }
                    break;
                case ParamTypeConst
                        .REGEX:
                    //正则表达式类型
                    result = getObjFromRegex(params, paramKey);
            }
        }
        return result;
    }

    //从map中取值
    public final static Object getObjFromMap(Map<String, Object> input, String key) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        if (input == null) {
            input = new ConcurrentHashMap<>();
        }
        String[] array = key.split("\\.");
        //如果当前变量池中未找到此变量则需要获取
        if (input.get(array[0]) == null && !array[0].startsWith("%")) {
            List<String> strings = new ArrayList<String>();
            strings.add(array[0]);
            boolean result = getFieldToInputByEns(strings, input);
            if (!result) {
                return "";
            }
        }
        return getObjFromMap(input, array);
    }

    //从map中找到需要的对象并返回
    public final static Object getObjFromMap(Map<String, Object> input, String[] array) {
        if (array.length == 1) {
            Object o = input.get(array[0]);
            if (o == null) {
                return "";
            }
            return o;
        }
        Map map = input;
        for (int i = 0; i < array.length; i++) {
            String childKey = array[i];
            //判断是否能找到key
            if (map.containsKey(childKey)) {
                Object o = map.get(childKey);
                if (i == array.length - 1) {
                    return map.get(childKey);
                }
                //如果是数组取length
                if (i == array.length - 2) {
                    if ("length()".equals(array[array.length - 1])) {
                        return JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(o)), ArrayList.class).size();
                    }
                }
                //未找到最后一个数组元素则将其识别为map
                map = JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(o)), Map.class);
            }
        }
        return "";
    }

    //根据入参map,通过公式和groovy计算出返回结果
    public final static Object getObjFromScript(Map<String, Object> input, String fieldValue) {
        JSONObject formulaJson = JSON.parseObject(fieldValue);
        //找到脚本中引用的字段,放入data中
        Map<String, Object> data = new HashMap<>();
        Object farr = formulaJson.get("farr");
        List<Long> fieldIds = new ArrayList<>();
        //字段cn为key，字段en为value
        Map<String, Field> fieldMap = new HashMap<>();
        if (farr != null && !"".equals(farr)) {
            List<Field> fieldList = JSONArray.parseArray(JSON.toJSONString(farr), Field.class);
            for (Field field : fieldList) {
                String fieldCn = field.getFieldCn();
                String fieldEn = field.getFieldEn();
                if (fieldCn != null && fieldEn != null && !"".equals(fieldCn) && !"".equals(fieldEn)) {
                    fieldMap.put(fieldCn, field);
                }
                fieldIds.add(field.getId());
            }
        }
        if (fieldIds.size() > 0) {
            getFieldToInputByIds(fieldIds, input);
        }
        Object result = executeScript(formulaJson, fieldMap, input);
        return result;
    }

    //处理集合中的特殊自定义
    private final static Object getObjFromScript(Map<String, Object> input, String fieldValue, List<JSONObject> current) {
        JSONObject formulaJson = JSON.parseObject(fieldValue);
        //找到脚本中引用的字段,放入data中
        Object farr = formulaJson.get("farr");
        List<Long> fieldIds = new ArrayList<>();
        //字段cn为key，字段en为value
        Map<String, Field> fieldMap = new HashMap<>();
        if (farr != null && !"".equals(farr)) {
            List<JSONObject> fieldList = JSONArray.parseArray(JSON.toJSONString(farr), JSONObject.class);
            String inputParamStr = JSON.toJSONString(input);
            for (JSONObject jsonObject : fieldList) {
                String fieldCn = jsonObject.getString("fieldCn");
                String fieldEn = jsonObject.getString("fieldEn");
                Field field = new Field();
                field.setFieldEn(fieldEn);
                field.setFieldCn(fieldCn);
                field.setValueType(jsonObject.getInteger("valueType"));
                Object thisFieldEnValue = null;
                if (fieldCn != null && fieldEn != null && !"".equals(fieldCn) && !"".equals(fieldEn)) {
                    fieldMap.put(fieldCn, field);
                }
                if (jsonObject.containsKey("temp") && jsonObject.getBoolean("temp")) {
                    continue;
                }
                Long id = jsonObject.getLong("id");
                if (!jsonObject.containsKey("paramList")) {
                    if (id != null) {
                        fieldIds.add(id);
                    }
                    continue;
                }

                //存在paramList证明是参数绑定过的需要单个字段取出
                JSONArray paramList = jsonObject.getJSONArray("paramList");
                //input中根据fieldEn和绑定参数设置了缓存，此缓存需要规则中管理缓存层级。
                String thisFieldEnKey = MD5.GetMD5Code(fieldEn + ":" + paramList.toJSONString());
                if (input.containsKey(thisFieldEnKey)) {
                    thisFieldEnValue = input.get(thisFieldEnKey);
                } else {
                    Map<String, Object> temp = JSON.parseObject(inputParamStr, Map.class);
                    for (int i = 0; i < paramList.size(); i++) {
                        JSONObject param = paramList.getJSONObject(i);
                        String paramEn = param.getString("en");
                        String paramValue = param.getString("value");
                        switch (param.getIntValue("type")) {
                            case ParamTypeConst.CONSTANT:
                                temp.put(paramEn, paramValue);
                                break;
                            case ParamTypeConst.VARIABLE:
                                temp.put(paramEn, DataCleanUtils.getObjByKeyAndJson(current.get(0), paramValue));
                                break;
                        }
                    }
                    getFieldToInputByIds(Arrays.asList(id), temp);
                    thisFieldEnValue = temp.get(fieldEn);
                    input.put(thisFieldEnKey, thisFieldEnValue);
                }
                input.put(fieldEn, thisFieldEnValue);
            }
        }

        if (!fieldIds.isEmpty()) {
            getFieldToInputByIds(fieldIds, input);
        }
        //取出groovy脚本
        Object result = executeScript(formulaJson, fieldMap, input);
        return result;
    }

    //对正则取值
    public final static Object getObjFromRegex(Map<String, Object> input, String fieldValue) {
        String result = fieldValue;
        //校验是否使用了字段如果使用了则需要替换为值
        Pattern pattern = Pattern.compile("@[a-zA-Z0-9_\u4e00-\u9fa5()（）-]+@");
        Matcher matcher = pattern.matcher(fieldValue);
        while (matcher.find()) {
            String fieldEn = matcher.group().replace("@", "");
            Object value = ExecuteUtils.getObjFromMap(input, fieldEn);
            String valueStr = "";
            if (value != null) {
                valueStr = value.toString();
            }
            result = result.replace("@" + fieldEn + "@", valueStr);
        }
        return result;
    }

    //执行自定义脚本
    private final static Object executeScript(JSONObject formulaJson, Map<String, Field> fieldMap, Map<String, Object> input) {
        String formula = formulaJson.getString("formula");
        //替换掉特殊的字符
        formula = formula.replace("&gt;", ">"); //3&gt;=6 && 3&lt; 12
        formula = formula.replace("&lt;", "<");
        //正则匹配自定义中用到的变量对其进行替换
        Pattern pattern = Pattern.compile("@[a-zA-Z0-9_\u4e00-\u9fa5()（）-]+@");
        Matcher matcher = pattern.matcher(formula);
        String subexp = formula;
        String exp = "";
        int j = 0;
        Map<String, Object> data = new HashMap<>();
        while (matcher.find()) {
            String fieldCN = matcher.group(0).replace("@", "");
            Field subField = fieldMap.get(fieldCN);
            if (subField == null) {
                return "";
            }
            String fieldEn = subField.getFieldEn();
            String v = "";
            v = "" + input.get(fieldEn);
            data.put(fieldEn, input.get(fieldEn));
            if (subexp.contains("def main")) {
                // groovy脚本替换为动态参数
                v = "_['" + fieldEn + "']";
                exp += subexp.substring(j, matcher.end()).replace("@" + fieldCN + "@", v);
            } else {
                if (subField.getValueType() == 1 || subField.getValueType() == 4) {
                    exp += subexp.substring(j, matcher.end()).replace("@" + fieldCN + "@", v);
                } else {
                    exp += subexp.substring(j, matcher.end()).replace("@" + fieldCN + "@", "'" + v + "'");
                }
            }
            j = matcher.end();
        }


        exp += formula.substring(j, formula.length());
        Evaluator evaluator = new Evaluator();
        Object result = "";
        try {
            if (exp.contains("def main")) {
                // 执行groovy脚本

                logger.warn("groovy:{},{}", exp, data);
                result = groovy.executeForObject(exp, data);
            } else if (exp.contains("def python_main(_):")) {
                //执行python脚本
                result = python.executeForObject(exp, data);
            } else {
                //执行公式
                result = evaluator.evaluate(exp);
            }
            if (result.toString().startsWith("'")) {
                //字符串
                result = result.toString().replace("'", "");
            } else {
                //数值
                if (StrUtils.isNum(result.toString())) {
                    String[] split = result.toString().split("\\.");
                    if (split.length > 1 && StrUtils.strToLong(split[1]) > 0) {
                        result = StrUtils.strToDouble(result.toString());
                    } else {
                        result = StrUtils.strToLong(split[0]);
                    }
                }
            }
        } catch (EvaluationException e) {
            logger.error("自定义执行异常", e);
            throw new ApiException(ErrorCodeEnum.RUNNER_CUSTOM_ERROR.getCode(), ErrorCodeEnum.RUNNER_CUSTOM_ERROR.getMessage());
        }
        return result;
    }

    //对groovy脚本执行结果进一步处理
    public static Map handleGroovyResult(Map map) {

        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            if (entry.getKey().startsWith("_['") && entry.getKey().endsWith("']")) {
                map.remove(entry.getKey());
                String key = entry.getKey().replace("_['", "").replace("']", "");
                map.put(key, entry.getValue());
            }
        }
        return map;
    }

    //调用commonService根据ens取参数
    public static boolean getFieldToInputByEns(List<String> fieldEns, Map<String, Object> input) {
        boolean result = commonService.getEngineField(fieldService.selectFieldListByEns(fieldEns), input);
        return result;
    }
    //调用commonService根据ids取参数
    private static boolean getFieldToInputByIds(List<Long> ids, Map<String, Object> input) {
        boolean result = commonService.getFieldByIds(ids, input);
        return result;
    }

    // 解析nodeJson
    public final static List<Map> getExecuteListFromNodeJson(EngineNode engineNode) {
        JSONObject nodeJson = JSON.parseObject(engineNode.getNodeJson());
        String strategyStr = null;
        switch (engineNode.getNodeType()) {
            case 2://规则集
                strategyStr = JSON.toJSONString(nodeJson.getJSONObject("executeGroup").get("strategyList"));
                break;
            case 4://评分卡
                strategyStr = JSON.toJSONString(nodeJson.getJSONArray("scorecardList"));
                break;
            case 5://名单库
                strategyStr = JSON.toJSONString(nodeJson.getJSONArray("listDbList"));
                break;
            case 15://模型
                strategyStr = JSON.toJSONString(nodeJson.getJSONArray("modelList"));
                break;
            case 16://决策表
                strategyStr = JSON.toJSONString(nodeJson.getJSONArray("decisionTableList"));
                break;
            case 17://决策树
                strategyStr = JSON.toJSONString(nodeJson.getJSONArray("decisionTreeList"));
                break;

        }
        List<Map> maps = JSON.parseArray(strategyStr, Map.class);
        return maps;
    }

    //获取执行用的id列表
    public final static List<Long> getExecuteIdList(EngineNode engineNode, String idKey) {
        List<Map> maps = ExecuteUtils.getExecuteListFromNodeJson(engineNode);
        List<Long> executeIdList = new ArrayList<>();
        if (maps != null && maps.size() > 0) {
            for (Map map : maps) {
                if (map.containsKey(idKey)) {
                    Object o = map.get(idKey);
                    if (o != null) {
                        Long id = StrUtils.strToLong(String.valueOf(o));
                        if (id != null) {
                            executeIdList.add(id);
                        }
                    }

                }
            }
        }
        return executeIdList;
    }

    //判断终止条件是否满足，满足则结束
    public final static void terminalCondition(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap, Map<String, Object> variablesMap) {
        if (StringUtils.isBlank(engineNode.getNodeScript())) {
            return;
        }
        JSONObject nodeScript = JSONObject.parseObject(engineNode.getNodeScript());
        JSONObject terminationInfo = nodeScript.getJSONObject("terminationInfo");
        JSONArray selectedRule = terminationInfo.getJSONArray("selectedRule");
        if(selectedRule == null || selectedRule.isEmpty()){
            return;
        }

        String conditions = terminationInfo.getString("conditions");
        Map<String, Integer> fieldTypeMap = terminationInfo.getObject("fieldTypeMap", Map.class);
        JevalUtil.convertVariables(fieldTypeMap, variablesMap);
        // 判断终止条件
        boolean result = false;
        try {
            result = JevalUtil.evaluateBoolean(conditions, variablesMap);
        } catch (EvaluationException e) {
            logger.error("终止条件执行异常,执行内容：{},参数：{}", conditions, variablesMap);
            e.printStackTrace();
        }

        if (result) {
            Object outValue = "";
            JSONObject output = terminationInfo.getJSONObject("output");
            String fieldValue = output.getString("fieldValue");
            String fieldCode = output.getString("fieldCode");
            int variableType = output.getInteger("variableType");
            switch (variableType) {
                case 1:
                    outValue = fieldValue;
                    break;
                case 2:
                    outValue = ExecuteUtils.getObjFromMap(inputParam, fieldValue);
                    break;
                case 3:
                    outValue = ExecuteUtils.getObjFromScript(inputParam, fieldValue);
                    break;
            }
            // 输出终止结果
            if (outValue == null) {
                outValue = "";
            }
            if (outValue instanceof String) {
                outMap.put("result", outValue);
            } else {
                outMap.put("result", JSONObject.toJSON(outValue));
            }
            if (StringUtils.isNotBlank(fieldCode)) {
                inputParam.put(fieldCode, outValue);
            }
            engineNode.setNextNodes(null);
        }
    }

    //根据key，分不同类型取出值
    public final static Object getValueByKeyYiHao(Integer variableType, Map<String, Object> params, String paramKey, JSONObject collectionObject) {
        Object result = paramKey;
        if (variableType != null) {
            switch (variableType) {
                case ParamTypeConst
                        .CONSTANT:
                    //常量类型
                    result = paramKey;
                    break;
                case ParamTypeConst
                        .VARIABLE:
                    //变量类型
                    result = getObjFromMap(params, paramKey);
                    break;
                case ParamTypeConst
                        .CUSTOM:
                    //自定义脚本类型
                    result = getObjFromScriptYiHao(paramKey, collectionObject);
                    break;
                case ParamTypeConst
                        .REGEX:
                    //正则表达式类型
                    result = getObjFromRegex(params, paramKey);
            }
        }
        return result;
    }

    //处理集合中的特殊自定义
    private final static Object getObjFromScriptYiHao(String outputValue, JSONObject collectionObject) {
        JSONObject formulaJson = JSON.parseObject(outputValue);
        Object farr = formulaJson.get("farr");
        Map<String, Object> fieldMap = new HashMap<>();
        if (farr != null && !"".equals(farr)) {
            List<JSONObject> fieldList = JSONArray.parseArray(JSON.toJSONString(farr), JSONObject.class);
            for (JSONObject jsonObject : fieldList) {
                String fieldCn = jsonObject.getString("fieldCn");
                String fieldEn = jsonObject.getString("fieldEn");
                Object opValue = DataCleanUtils.getObjByKeyAndJson(collectionObject, fieldEn);
                fieldMap.put(fieldCn, opValue);
            }
        }
        //取出groovy脚本
        Object result = executeScriptYiHao(formulaJson, fieldMap);
        return result;
    }

    //执行自定义脚本
    private final static Object executeScriptYiHao(JSONObject formulaJson, Map<String, Object> fieldMap) {
        String formula = formulaJson.getString("formula");
        //替换掉特殊的字符
        formula = formula.replace("&gt;", ">"); //3&gt;=6 && 3&lt; 12
        formula = formula.replace("&lt;", "<");
        //正则匹配自定义中用到的变量对其进行替换
        Pattern pattern = Pattern.compile("@[a-zA-Z0-9_\u4e00-\u9fa5()（）-]+@");
        Matcher matcher = pattern.matcher(formula);
        String subexp = formula;
        String exp = "";
        int j = 0;
        while (matcher.find()) {
            String fieldCn = matcher.group(0).replace("@", "");
            Object fieldCnValue = fieldMap.get(fieldCn);
            if (fieldCnValue == null) {
                return "";
            }
            exp += subexp.substring(j, matcher.end()).replace("@" + fieldCn + "@", fieldCnValue.toString());
            j = matcher.end();
        }

        exp += formula.substring(j, formula.length());
        Evaluator evaluator = new Evaluator();
        Object result = "";
        try {
            //执行公式
            result = evaluator.evaluate(exp);

            if (result.toString().startsWith("'")) {
                //字符串
                result = result.toString().replace("'", "");
            } else {
                //数值
                if (StrUtils.isNum(result.toString())) {
                    String[] split = result.toString().split("\\.");
                    if (split.length > 1 && StrUtils.strToLong(split[1]) > 0) {
                        result = StrUtils.strToDouble(result.toString());
                    } else {
                        result = StrUtils.strToLong(split[0]);
                    }
                }
            }
        } catch (EvaluationException e) {
            logger.error("自定义执行异常", e);
            throw new ApiException(ErrorCodeEnum.RUNNER_CUSTOM_ERROR.getCode(), ErrorCodeEnum.RUNNER_CUSTOM_ERROR.getMessage());
        }
        return result;
    }
}
