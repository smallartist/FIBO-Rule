package com.fibo.ddp.common.service.datax.runner.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.dao.datax.datasource.SimpleMapper;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.datax.datainterface.InterfaceInfo;
import com.fibo.ddp.common.model.datax.datamanage.Field;
import com.fibo.ddp.common.model.datax.datamanage.FieldCallLog;
import com.fibo.ddp.common.model.datax.datamanage.FieldCond;
import com.fibo.ddp.common.model.datax.datasource.DataSource;
import com.fibo.ddp.common.service.common.runner.RunnerSessionManager;
import com.fibo.ddp.common.service.common.runner.SessionData;
import com.fibo.ddp.common.service.datax.datainterface.InterfaceService;
import com.fibo.ddp.common.service.datax.datamanage.FieldCallLogService;
import com.fibo.ddp.common.service.datax.datamanage.FieldService;
import com.fibo.ddp.common.service.datax.datasource.DataSourceService;
import com.fibo.ddp.common.service.datax.runner.CommonService;
import com.fibo.ddp.common.service.datax.runner.DataBaseReSource;
import com.fibo.ddp.common.service.datax.runner.ExecuteUtils;
import com.fibo.ddp.common.service.datax.runner.FieldTypeConsts;
import com.fibo.ddp.common.service.datax.runner.mysql.DataSourceContextHolder;
import com.fibo.ddp.common.service.datax.runner.mysql.DynamicDataSource;
import com.fibo.ddp.common.service.datax.runner.redis.RedisKSessionPool;
import com.fibo.ddp.common.utils.common.MD5;
import com.fibo.ddp.common.utils.constant.CommonConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.StringUtil;
import com.fibo.ddp.common.utils.util.runner.DictVariableUtils;
import com.fibo.ddp.common.utils.util.runner.jeval.EvaluationException;
import com.fibo.ddp.common.utils.util.runner.jeval.Evaluator;
import com.fibo.ddp.common.utils.util.runner.jeval.function.math.Groovy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CommonServiceImpl implements CommonService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SimpleMapper simpleMapper;

    @Autowired
    public FieldService fieldService;

    @Autowired
    private Groovy groovy;

    @Autowired
    private InterfaceService interfaceService;

    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private RedisKSessionPool redisKSessionPool;
    @Autowired
    private FieldCallLogService fieldCallLogService;

    @Override
    public boolean getFieldByIds(List<Long> ids, Map<String, Object> inputParam) {
        if (ids == null || ids.size() == 0) {
            return true;
        }
        SessionData sessionData = RunnerSessionManager.getSession();
        Long organId = sessionData.getOrganId();
        List<Field> fieldList = fieldService.findFieldByIdsbyorganId(organId, ids);
        List<Field> list = new ArrayList<>();
        ids = new ArrayList<>();
        for (int i = 0; i < fieldList.size(); i++) {
            if (fieldList.get(i).getIsDerivative() == 1) {
                ids.addAll(StringUtil.toLongList(fieldList.get(i).getOrigFieldId()));
            } else
                list.add(fieldList.get(i));
        }
        if (ids.size() > 0) {
            List<Field> lists = fieldService.findFieldByIdsbyorganId(organId, ids);
            list.addAll(lists);
        }

        List<Field> fields = new ArrayList<>();
        fields.addAll(list);

        this.getEngineField(fields, inputParam);
//        迁移到getEngineField方法中执行
//        for (Field field : fieldList) {
//            if (field.getIsDerivative() == 1) {
//                inputParam.put(field.getFieldEn(), "");
//                this.getFieldResult(inputParam);
//            }
//        }
        return false;
    }

    /**
     * 调用http请求得到引擎节点所需要的字段
     *
     * @return 引擎所需字段
     * @see
     */
    @Override
    public boolean getEngineField(List<Field> fields, Map<String, Object> inputParam) {
        logger.info("start getEngineField, fields:{},inputParam:{}", JSONObject.toJSONString(fields), JSONObject.toJSONString(inputParam));

        if (null != fields && fields.size() < 1) {
            return true;
        }
        // 循环规则特殊处理
        List<Field> tempFields = new ArrayList<>(fields);
        for (Field field : fields) {
            if (field.getFieldEn().contains("[") && field.getFieldEn().contains("]")) {
                String fieldEn = field.getFieldEn().substring(0, field.getFieldEn().indexOf("["));
                Field nField = new Field();
                nField.setFieldEn(fieldEn);
                tempFields.add(nField);
                tempFields.remove(field);
            }
        }
        fields = new ArrayList<>(tempFields);

        // 需要调用指标系统的字段集合
        List<Field> remainFields = new ArrayList<>(fields);

        for (Field field : fields) {
            if (inputParam.containsKey(field.getFieldEn())) {
                // 优先从入参中获取指标
                remainFields.remove(field);
            } else if (field.getType() != null && field.getType() == 5) {
                // 常量指标
                // 常量JOSN指标直接获取定义好的常量的value
                String value = field.getJsonValue();
                inputParam.put(field.getFieldEn(), JSONObject.parseObject(value));
                remainFields.remove(field);
            } else if (field.getIsUseSql()) {
                // 通过sql方式获取指标
                Object value = getFieldValueBySql(field, inputParam);
                inputParam.put(field.getFieldEn(), value);
                remainFields.remove(field);
            } else if (field.getIsInterface()) {
                // 解析接口指标
                String value = getFieldValueByInterface(field, inputParam);
                inputParam.put(field.getFieldEn(), value);
                remainFields.remove(field);
            }
        }

        if (null != remainFields && remainFields.size() < 1) {
            return true;
        }
        for (Field field : remainFields) {
            if (field.getIsDerivative() == 1) {
                //衍生指标
                inputParam.put(field.getFieldEn(), "");
                this.getFieldResult(inputParam);
                Object value = inputParam.get(field.getFieldEn());
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> getFields(List<Field> fields, Map<String, Object> inputParam) {
        logger.info("start getEngineField, fields:{},inputParam:{}", JSONObject.toJSONString(fields), JSONObject.toJSONString(inputParam));
        Map<String, Object> result = new HashMap<>();
        if (null != fields && fields.size() < 1) {
            return result;
        }
        // 循环规则特殊处理
        List<Field> tempFields = new ArrayList<>(fields);
        for (Field field : fields) {
            if (field.getFieldEn().contains("[") && field.getFieldEn().contains("]")) {
                String fieldEn = field.getFieldEn().substring(0, field.getFieldEn().indexOf("["));
                Field nField = new Field();
                nField.setFieldEn(fieldEn);
                tempFields.add(nField);
                tempFields.remove(field);
            }
        }
        fields = new ArrayList<>(tempFields);

        // 需要调用指标系统的字段集合
        List<Field> remainFields = new ArrayList<>(fields);

        for (Field field : fields) {
            Object value = null;
            if (inputParam.containsKey(field.getFieldEn())) {
                // 优先从入参中获取指标
                remainFields.remove(field);
                value = inputParam.get(field.getFieldEn());
            } else if (field.getType() != null && field.getType() == 5) {
                // 常量指标
                // 常量JOSN指标直接获取定义好的常量的value
                value = field.getJsonValue();
                inputParam.put(field.getFieldEn(), value);
                remainFields.remove(field);
            } else if (field.getIsUseSql()) {
                // 通过sql方式获取指标
                value = getFieldValueBySql(field, inputParam);
                inputParam.put(field.getFieldEn(), value);
                remainFields.remove(field);
            } else if (field.getIsInterface()) {
                // 解析接口指标
                value = getFieldValueByInterface(field, inputParam);
                inputParam.put(field.getFieldEn(), value);
                remainFields.remove(field);
            }
            if (value != null) {
                result.put(field.getFieldEn(),value);
            }
        }

        if (null != remainFields && remainFields.size() < 1) {
            return result;
        }
        for (Field field : remainFields) {
            if (field.getIsDerivative() == 1) {
                //衍生指标
                inputParam.put(field.getFieldEn(), "");
                this.getFieldResult(inputParam);
                Object value = inputParam.get(field.getFieldEn());
                if (value != null) {
                    result.put(field.getFieldEn(),value);
                }
            }
        }
//        int type = 1;
//        Properties p = new Properties();
//        try {
//            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("datacenter.properties");
//            p.load(inputStream);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//            logger.error("remainFields:{},请求异常", JSONObject.toJSONString(remainFields), e1);
//        }
//        String act = p.getProperty("act");
//        //随机数
//        String nonce = UUID.randomUUID().toString();
//        String token = p.getProperty("token");
//        Date date = new Date();
//        long ts = date.getTime();
////        String sign = MD5.GetMD5Code(act.trim() + "," + date.getTime() + "," + nonce.trim() + "," + pid.trim() + "," + uid.trim() + "," + token.trim());
//        HttpClient httpClient = new HttpClient();
//        String url = p.getProperty("url") + "?token=" + token.trim() + "&ts=" + ts + "&act=" + act.trim() + "&nonce=" + nonce.trim();
//        Map<String, String> pam = new HashMap<>();
//        pam.put("fields", getListFieldByString(remainFields));
////        pam.put("sign", sign);
//        pam.put("type", String.valueOf(type));
//        try {
//            String result = httpClient.post(url, pam);
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            //返回成功状态下解析返回的字段
//            if (jsonObject.getString("status").equals("0x0000")) {
//                JSONArray array = jsonObject.getJSONArray("data");
//
//                if (type == 1) { //普通规则
//                    for (int i = 0; i < array.size(); i++) {
//                        JSONObject object = array.getJSONObject(i);
//                        for (Map.Entry<String, Object> entry : object.entrySet()) {
//                            inputParam.put(entry.getKey(), entry.getValue());
//                        }
//                    }
//                    return true;
//                } else { //复杂规则
//                    List<ComplexRule> list = new ArrayList<ComplexRule>();
//                    return true;
//                }
//            } else {
//                //返回状态不成功,直接返回null
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("remainFields:{},请求异常", JSONObject.toJSONString(remainFields), e);
//        }
        return result;
    }

//    /**
//     * 把list对象转换为以,号隔开的string字符串
//     *
//     * @inputParam list
//     * @return
//     * @see
//     */
//    private String getListFieldByString(List<Field> list) {
//        String fields = "";
//        for (int i = 0; i < list.size(); i++) {
//            fields = fields + list.get(i).getFieldEn() + ",";
//        }
//        return fields;
//
//    }

    /**
     * 通过sql获取指标
     *
     * @param field
     * @return
     */
    private Object getFieldValueBySql(Field field, Map<String, Object> inputParam) {
        logger.info("start【获取sql指标】, fieldEn:{}", field.getFieldEn());
        long start = System.currentTimeMillis();
        DataSource dataSource = dataSourceService.getDataSourceByIdRunner(field.getDataSourceId());
        if (dataSource == null || dataSource.getStatus() == 0) {
            return null;
        }
        DruidDataSource dynamicDataSource = null;
        Jedis jedis = null;
        boolean isDurid = false;
        String keyMd5 = null;
        Object resultValue = null;
        Map<String, Object> parameterMap = new HashMap<>(); // 可以传入通用参数
        //替换参数
        this.getSqlFieldParam(field, inputParam, parameterMap);
        try {
            switch (dataSource.getType()) {
                case DataBaseReSource.MySql.type:
                    dynamicDataSource = DataBaseReSource.MySql.getDataSource(dataSource);
                    isDurid = true;
                    break;
                case DataBaseReSource.Oracle.type:
                    dynamicDataSource = DataBaseReSource.Oracle.getDataSource(dataSource);
                    isDurid = true;
                    break;
                case DataBaseReSource.Redis.type:
                    String userName = dataSource.getUserName();
                    if (StringUtils.isBlank(userName)) {
                        userName = "root";
                    }
                    keyMd5 = CommonConst.JEDIS_KSESSION_KEY_PREFIX + MD5.GetMD5Code(dataSource.getHost() + ":" +
                            dataSource.getPort() + ":" + dataSource.getDbName() + ":" + userName + ":" + dataSource.getPassword());
                    jedis = DataBaseReSource.Redis.getDataSource(dataSource, keyMd5);
                    break;
            }

            if (isDurid) {
                // 创建动态数据源，并切换数据源
                Map<Object, Object> dataSourceMap = DynamicDataSource.getInstance().getDataSourceMap();
                String dataSourceKey = "dynamic-" + dataSource.getId();
                dataSourceMap.put(dataSourceKey, dynamicDataSource);
                DynamicDataSource.getInstance().setTargetDataSources(dataSourceMap);
                DataSourceContextHolder.setDBType(dataSourceKey);
                resultValue = handlerSqlFieldResult(field, parameterMap);
            } else if (jedis != null) {
                resultValue = jedis.eval(parameterMap.get("sqlStr").toString());
            }
            if (resultValue == null) {
                throw new ApiException(ErrorCodeEnum.GET_DATABASE_FIELD_ERROR.getCode(), ErrorCodeEnum.GET_DATABASE_FIELD_ERROR.getMessage());
            }
        } finally {
            // 执行完成后切换到系统数据源
            DataSourceContextHolder.setDBType("default");
            if (StringUtils.isNotBlank(keyMd5) && jedis != null) {
                redisKSessionPool.returnObject(keyMd5, jedis);
            }
        }
        long end = System.currentTimeMillis();
        fieldCallLogService.save(new FieldCallLog(field.getId(), FieldTypeConsts.DATABASE,dataSource.getType(),dataSource.getId().longValue(),JSON.toJSONString(parameterMap),
                JSON.toJSONString(resultValue),(end - start),field.getOrganId()));
        logger.info("【获取数据库指标】fieldEn:{}, 耗时:{}, result:{}, parameterMap:{}", field.getFieldEn(), (end - start), resultValue, parameterMap);
        return resultValue;

    }

    /**
     * 解析接口指标
     *
     * @param field
     * @return
     */
    private String getFieldValueByInterface(Field field, Map<String, Object> inputParam) {
        long start = System.currentTimeMillis();
        InterfaceInfo interfaceInfo = interfaceService.getInterfaceById(field.getInterfaceId());
        //http请求获得json响应体
        String response = interfaceService.getHttpResponse(interfaceInfo, inputParam, null);
        //解析接口指标
        String resultValue ;
        try {
            resultValue = interfaceService.interfaceParseField(field.getInterfaceParseField(), response);
            logger.info("【解析接口响应体完成】fieldEn:{},fieldCn:{}, fieldValue:{}, response:{}",field.getFieldEn(),field.getFieldCn(),resultValue,response);
        } catch (Exception e) {
            logger.error("【解析接口响应体失败】fieldEn:{},fieldCn:{},response:{}",field.getFieldEn(),field.getFieldCn(),response);
            e.printStackTrace();
            throw new ApiException(ErrorCodeEnum.GET_INTERFACE_FIELD_ERROR.getCode(),ErrorCodeEnum.GET_INTERFACE_FIELD_ERROR.getMessage());
        }
        long end = System.currentTimeMillis();
        fieldCallLogService.save(new FieldCallLog(field.getId(), FieldTypeConsts.INTERFACE,FieldTypeConsts.INTERFACE,interfaceInfo.getId().longValue(),JSON.toJSONString(inputParam),
                JSON.toJSONString(resultValue),(end - start),field.getOrganId()));
        return resultValue;
    }

    /**
     * 根据输入字段参数返回衍生字段的结果
     * 输入：引擎id及参数列表(原生字段英文字段名和值、待计算的衍生字段只有英文字段名)
     * 输出：待计算衍生字段的计算结果值
     */
    @Override
    public void getFieldResult(Map<String, Object> paramMap) {
        //参数传递中间临时Map
        Map<String, Object> paramMap2 = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            if (null != entry.getValue())
                paramMap2.put(entry.getKey(), entry.getValue());
            else
                paramMap2.put(entry.getKey(), "");
        }

        SessionData sessionData = RunnerSessionManager.getSession();
        Long organId = sessionData.getOrganId();
        paramMap2.put("organId", organId);

        List<FieldCallLog> fieldCallLogs = new ArrayList<>();
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            String fieldEn = entry.getKey();
            String fieldValue = "";
            if (null != entry.getValue())
                fieldValue = String.valueOf(entry.getValue());

            if (null == fieldValue || fieldValue.equals("")) {

                paramMap2.put("fieldEn", fieldEn);
                Field field = fieldService.findByFieldEnbyorganId(organId, fieldEn);

                if (field != null) {
                    if (field.getIsDerivative() == 1) {
                        long start = System.currentTimeMillis();
                        String result = "";
                        paramMap2.put("fieldCn", field.getFieldCn());
                        result = getExpAll(field.getFieldCn(), "", paramMap);
                        //单独返回数值结果会带上()，做处理去掉
                        result = result.replace("(", "");
                        result = result.replace(")", "");
                        long end = System.currentTimeMillis();
                        fieldCallLogs.add(new FieldCallLog(field.getId(),FieldTypeConsts.DERIVE,FieldTypeConsts.DERIVE,
                                FieldTypeConsts.DERIVE_SOURCE_ID,JSON.toJSONString(paramMap),result,(end-start),field.getOrganId()));
                        paramMap.put(fieldEn, result);
                    }
                }
            }
        }
        if (!fieldCallLogs.isEmpty()){
            fieldCallLogService.saveBatch(fieldCallLogs);
        }
    }

    /**
     * 递归生成衍生字段嵌套的逻辑表达式或公式计算的结果
     */
    private String getExpAll(String fieldCn, String exp, Map<String, Object> param) {

        String result = "";
        Map<String, Object> param2 = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            if (null != entry.getValue())
                param2.put(entry.getKey(), entry.getValue());
            else
                param2.put(entry.getKey(), "");
        }

        SessionData sessionData = RunnerSessionManager.getSession();
        Long organId = sessionData.getOrganId();

        Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("organId", organId);
        paramMap.put("engineId", param.get("engineId"));
        paramMap.put("fieldCn", fieldCn);

        //放大从组织通用字段拷贝生成的引擎字段里字段的获取范围，优先取引擎，没有再取组织里的（因为中文名在组织通用字段和引擎字段里有重复）
        String arrFormula = "";
        Field engField = fieldService.findByFieldCnbyorganId(organId, fieldCn);
        String engFormula = engField.getFormula();
        if (!engFormula.equals("") && engFormula != null) {
            arrFormula = engFormula;
        }

        if (arrFormula.equals("") || arrFormula == null) { //衍生字段只有条件区域

            List<FieldCond> fieldCondList = new ArrayList<FieldCond>();
            List<FieldCond> engfieldCondList = fieldService.findByFieldCnbyorganId(organId, fieldCn).getFieldCondList();
            if (engfieldCondList.size() > 0) {
                fieldCondList = engfieldCondList;
            }

            if (fieldCondList.size() > 0) {//需要进行条件区域逻辑运算
                for (FieldCond fieldCond : fieldCondList) {//这里的fieldCond是字段设置的逻辑运算符
                    String condValue = fieldCond.getConditionValue();
                    List<Object> condList = new ArrayList<>();
                    condList = JSONObject.parseArray(fieldCond.getContent());
                    exp = "";
                    for (int j = 0; j < condList.size(); j++) {
                        JSONObject cond = ((JSONArray) condList).getJSONObject(j);
                        //[{\"fieldId\":\"31\",\"operator\":\">\",\"fieldValue\":\"1000\",\"logical\":\"&&\"},{\"fieldId\":\"31\",\"operator\":\">=\",\"fieldValue\":\"7000\"}]
                        paramMap.put("id", cond.getString("fieldId"));

                        Field condfield = fieldService.queryById(Long.valueOf(cond.getString("fieldId")));
                        if (condfield == null) {
                            condfield = fieldService.findByFieldCnbyorganId(organId, fieldCn);
                        }

                        String condFieldEn = condfield.getFieldEn();//yqshouru 月收入
                        String condFieldCn = condfield.getFieldCn();
                        Integer condValueType = condfield.getValueType(); //1数值型
                        String condFieldValue = cond.getString("fieldValue"); //1000
                        String operator = cond.getString("operator"); //>大于号
                        String fieldValue = param2.get(condFieldEn).toString(); //取输入变量的值

                        String logical = "";

                        if (condfield.getIsDerivative() == 0) {
                            if (cond.containsKey("logical"))
                                logical = " " + cond.getString("logical") + " ";
                            if (operator.equals("in")) {
                                //exp += "(indexOf(#{"+fieldValue+"},'"+condFieldValue+"')>0"+logical;
                                exp += "(indexOf('" + condFieldValue + "','" + fieldValue + "',0) >= 0)" + logical;
                            } else if (operator.equals("not in")) {
                                //exp += "(indexOf(#{"+fieldValue+"},'"+condFieldValue+"')=0"+logical;
                                exp += "(indexOf('" + condFieldValue + "','" + fieldValue + "',0) = -1)" + logical;
                            } else if (operator.equals("like")) { //交换位置 (indexOf('abc','c',0) >= 0)
                                exp += "(indexOf('" + fieldValue + "','" + condFieldValue + "',0) >= 0)" + logical;
                            } else if (operator.equals("not like")) { //(indexOf('abc','x',0) = -1)
                                exp += "(indexOf('" + fieldValue + "','" + condFieldValue + "',0) = -1)" + logical;
                            } else {
                                if (condValueType == 1 || condValueType == 4) {
                                    exp += " (" + fieldValue + "" + operator + "" + condFieldValue + ") " + logical;
                                } else
                                    exp += " ('" + fieldValue + "'" + operator + "'" + condFieldValue + "') " + logical;
                            }
                        } else {//衍生字段
                            if (cond.containsKey("logical"))
                                logical = " " + cond.getString("logical") + " ";
                            if (operator.equals("in")) {
                                //exp += "(indexOf(#{"+getExpAll(condFieldCn,"",param2)+"},'"+condFieldValue+"')>0"+logical;
                                //(indexOf('abc','c',0) >= 0) && (indexOf('abc','x',0) = -1)
                                exp += "(indexOf('" + condFieldValue + "','" + getExpAll(condFieldCn, "", param2) + "',0) >= 0)" + logical;
                            } else if (operator.equals("not in")) {
                                exp += "(indexOf('" + condFieldValue + "','" + getExpAll(condFieldCn, "", param2) + "',0) = -1)" + logical;
                            } else if (operator.equals("like")) { //交换位置 (indexOf('abc','c',0) >= 0)
                                exp += "(indexOf('" + getExpAll(condFieldCn, "", param2) + "','" + condFieldValue + "',0) >= 0)" + logical;
                            } else if (operator.equals("not like")) { //(indexOf('abc','x',0) = -1)
                                exp += "(indexOf('" + getExpAll(condFieldCn, "", param2) + "','" + condFieldValue + "',0) = -1)" + logical;
                            } else {
                                if (condValueType == 1 || condValueType == 4) {
                                    exp += " (" + getExpAll(condFieldCn, "", param2) + "" + operator + "" + condFieldValue + ") " + logical;
                                } else
                                    exp += " ('" + getExpAll(condFieldCn, "", param2) + "'" + operator + "'" + condFieldValue + "') " + logical;
                            }
                        }
                    }
                    Evaluator evaluator = new Evaluator();
                    String b = "";
                    try {
                        System.out.println("========字段区域设置的的表达式输出：" + exp);
                        b = evaluator.evaluate(exp);
                    } catch (EvaluationException e) {
                        e.printStackTrace();
                        logger.error("请求异常", e);
                    }
                    if (b.equals("1.0")) {
                        result = condValue;
                        break; //遇到一个满足条件的则跳出循环
                    }
                }
            }
        } else { //衍生字段只有公式
            List<Object> formulaList = new ArrayList<>();
            formulaList = JSONObject.parseArray(arrFormula);
            for (int i = 0; i < formulaList.size(); i++) {
                JSONObject formulaJson = ((JSONArray) formulaList).getJSONObject(i);

                String formula = (String) formulaJson.get("formula");
                formula = formula.replace("&gt;", ">"); //3&gt;=6 && 3&lt; 12
                formula = formula.replace("&lt;", "<");
                Pattern pattern = Pattern.compile("@[a-zA-Z0-9_\u4e00-\u9fa5()（）-]+@");
                Matcher matcher = pattern.matcher(formula);
                String subexp = formula;
                int j = 0;
                exp = "";
                // 存放groovy脚本入参
                Map<String, Object> data = new HashMap<>();
                //System.out.println("待替换的字段串："+formula);
                while (matcher.find()) {
                    String fieldCN = matcher.group(0).replace("@", "");
                    Map<String, Object> fieldMap = new HashMap<String, Object>();
                    paramMap.put("organId", organId);
                    fieldMap.put("engineId", paramMap.get("engineId"));
                    fieldMap.put("fieldCn", fieldCN);
                    fieldMap.put("organId", organId);
                    Field subField = fieldService.findByFieldCnbyorganId(organId, fieldCN);

                    //构造字段条件区间计算输入参数
                    Map<String, Object> paramCond = new HashMap<String, Object>();
                    paramCond.put("fieldValue", param2.get(subField.getFieldEn()));
                    paramCond.put("fieldEn", subField.getFieldEn());
                    paramCond.put("fieldValueType", subField.getValueType());

                    //字段条件转换
                    JSONArray fieldCond = new JSONArray();
                    if (formulaJson.get("farr") != null && !"".equals(formulaJson.get("farr"))) {
                        JSONArray jsonArr = (JSONArray) formulaJson.get("farr");
                        for (Iterator iterator = jsonArr.iterator(); iterator.hasNext(); ) {
                            JSONObject job = (JSONObject) iterator.next();
                            if (job.get("fieldCN").equals(fieldCN) && !job.get("fieldCond").equals("")) {
                                fieldCond = (JSONArray) job.get("fieldCond");
                                break;
                            }
                        }
                    }

                    paramCond.put("fieldCond", fieldCond);
                    String v = "";
                    if (fieldCond.size() > 0) {
                        v = calcFieldCond(paramCond);
                    } else {
                        v = "" + param2.get(subField.getFieldEn());
                    }
                    data.put(subField.getFieldEn(), param2.get(subField.getFieldEn()));

                    if (subField.getIsDerivative() == 0) {
//						if(subexp.indexOf("substring")>=0||subexp.indexOf("equals")>=0){ //substring(@字段A@,3,6)
//							exp += subexp.substring(j, matcher.end()).replace("@"+fieldCN+"@", "'"+v+"'");
//						}else{
//							exp += subexp.substring(j, matcher.end()).replace("@"+fieldCN+"@", v);
//						}

                        if (subexp.contains("def main")) {
                            // groovy脚本替换为动态参数
                            v = "_['" + subField.getFieldEn() + "']";
                            exp += subexp.substring(j, matcher.end()).replace("@" + fieldCN + "@", v);
                        } else {
                            if (subField.getValueType() == 1 || subField.getValueType() == 4) {
                                exp += subexp.substring(j, matcher.end()).replace("@" + fieldCN + "@", v);
                            } else {
                                exp += subexp.substring(j, matcher.end()).replace("@" + fieldCN + "@", "'" + v + "'");
                            }
                        }

                    } else {

                        v = getExpAll(fieldCN, exp, param2);
                        // 存衍生字段
                        if (subField.getValueType() == 1 || subField.getValueType() == 4) {
                            data.put(subField.getFieldEn(), Integer.valueOf(v));
                        } else {
                            data.put(subField.getFieldEn(), v);
                        }

                        if (subexp.contains("def main")) {
                            // groovy脚本替换为动态参数
                            v = "_['" + subField.getFieldEn() + "']";
                        }
                        exp += subexp.substring(j, matcher.end()).replace("@" + fieldCN + "@", v);
                    }
                    j = matcher.end();
                }
                exp += formula.substring(j, formula.length());
                Evaluator evaluator = new Evaluator();
                String b = "";
                try {
//                    System.out.println("========字段公式编辑设置的表达式输出：" + exp);

                    if (exp.contains("def main")) {
                        // 执行groovy脚本
                        b = groovy.execute(exp, data);
//                        b = String.valueOf(groovy.execute(exp, data));
                    } else {
                        b = evaluator.evaluate(exp);
                    }

                } catch (EvaluationException e) {
                    e.printStackTrace();
                    logger.error("请求异常", e);
                }

                //
                if (engField.getValueType().intValue() == 1 || engField.getValueType().intValue() == 2) { //数值型或者字符型字段的衍生表达式b的结果返回值0.0代表计算结果
                    if (!b.equals("")) {
                        result = b;
                        if (StringUtil.isValidStr(result) && result.startsWith("'") && result.endsWith("'")) {
                            result = result.substring(1, result.length() - 1);
                        }
                    }
                } else if (engField.getValueType().intValue() == 3) { //枚举型字段的衍生表达式b的结果返回值0.0代表false即逻辑表达式无效
                    if (!b.equals("1.0") && !b.equals("0.0") && !b.equals("")) {
                        result = b;
                        if (StringUtil.isValidStr(result) && result.startsWith("'") && result.endsWith("'")) {
                            result = result.substring(1, result.length() - 1);
                        }
                    }
                    if (b.equals("1.0")) {
                        result = (String) formulaJson.get("fvalue");
                        //result = result.substring(result.indexOf(":")+1,result.length());// a:2 取2返回
                        if (isNumeric(result)) {
                            result = "(" + result + ")";
                        } else {
                            result = "'" + result + "'";
                        }
                        break; //遇到一个满足条件的则跳出循环
                    }
                }

            }
        }

        return result;

    }

    /**
     * 公式编辑里设置字段条件区域转换的通用方法
     * 输入：待转换字段的输入参数、值类型
     * 输出：输入参数所在区间对应的结果值
     */
    private String calcFieldCond(Map<String, Object> paramMap) {

        String fieldValue = (String) paramMap.get("fieldValue");
        Integer fieldValueType = (Integer) paramMap.get("fieldValueType");

        String result = "";
        //[{"fieldCN":"引擎字段1-1","fieldCond":[{"inputOne":"a","inputThree":"33"},{"inputOne":"b","inputThree":"490"},{"inputOne":"c","inputThree":"50"}]}]
        //[{"fieldCN":"引擎字段1-1","fieldCond":[{"inputOne":"(3,19]","inputThree":"1"},{"inputOne":"(19,200]","inputThree":"2"}]},{"fieldCN":"通用字段2贷前","fieldCond":""}]
        JSONArray jsonArr = (JSONArray) paramMap.get("fieldCond");
        for (Iterator iterator = jsonArr.iterator(); iterator.hasNext(); ) {
            JSONObject job = (JSONObject) iterator.next();
            String inputOne = (String) job.get("inputOne");
            String inputThree = (String) job.get("inputThree");

            if (fieldValueType == 3) {
                if (fieldValue.equals(inputOne)) {
                    result = inputThree;
                    break;
                }
            } else if (fieldValueType == 1 || fieldValueType == 4) {
                //(40,50]
                Double lv = Double.parseDouble(inputOne.substring(1, inputOne.indexOf(",")));
                Double rv = Double.parseDouble(inputOne.substring(inputOne.indexOf(",") + 1, inputOne.length() - 1));

                String exp = "";
                if (inputOne.startsWith("(") && !lv.equals("")) {
                    exp = fieldValue + ">" + lv;
                }
                if (inputOne.startsWith("[") && !lv.equals("")) {
                    exp = fieldValue + ">=" + lv;
                }
                if (inputOne.endsWith(")") && !rv.equals("")) {
                    if (exp.equals(""))
                        exp += fieldValue + "<" + rv;
                    else
                        exp += "&&" + fieldValue + "<" + rv;
                }
                if (inputOne.endsWith("]") && !rv.equals("")) {
                    if (exp.equals(""))
                        exp += fieldValue + "<=" + rv;
                    else
                        exp += "&&" + fieldValue + "<=" + rv;
                }

                Evaluator evaluator = new Evaluator();
                String b = "";
                try {
                    b = evaluator.evaluate(exp);
                } catch (EvaluationException e) {
                    e.printStackTrace();
                    logger.error("请求异常", e);
                }
                if (b.equals("1.0")) {
                    result = inputThree;
                    break; //遇到一个满足条件的则跳出循环
                }
            }
        }
        return result;
    }

    /**
     * 判断表达式的运算结果是否数值型的公共方法
     */
    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^(-|\\+)?\\d+(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 匹配数据库指令中的占位符
     */
    private Map getSqlFieldParam(Field field, Map<String, Object> inputParam, Map<String, Object> parameterMap) {
        String sqlStr = field.getSqlStatement();
        // 添加动态参数
        //处理in方法中的遍历
        Pattern sqlInPattern = Pattern.compile("[\\s]*in[\\s]*\\([\\s]*#\\{([a-zA-Z0-9_\u4e00-\u9fa5()（）-]+)\\}[\\s]*\\)");
        Matcher sqlInMatcher = sqlInPattern.matcher(sqlStr);
        while (sqlInMatcher.find()) {
            String replaceOld = sqlInMatcher.group(0);
            String sqlField = sqlInMatcher.group(1);
            String sqlVariable = field.getSqlVariable();
            String fieldEn = sqlField.split("\\.")[0];
            String convertStr = "";
            Object value = null;
            if (StringUtils.isNotBlank(sqlVariable)) {
                JSONArray sqlVariableArr = JSONArray.parseArray(sqlVariable);
                for (int i = 0; i < sqlVariableArr.size(); i++) {
                    JSONObject sqlVariableObj = sqlVariableArr.getJSONObject(i);
                    if (sqlField.equals(sqlVariableObj.getString("key"))) {
                        value = sqlVariableObj.getJSONArray("value");
                    }
                }
            }
            if (value == null) {
                if (!inputParam.containsKey(fieldEn)) {
                    //不存再需要重新获取
                    List<String> fieldEns = new ArrayList<>();
                    fieldEns.add(fieldEn);
                    //查询未入参的en是否是指标库中的指标，如果是则u需要继续查找
                    List<Field> fieldList = fieldService.selectFieldListByEns(fieldEns);
                    if (fieldList != null && !fieldList.isEmpty()) {
                        //查询引用到的指标
                        getEngineField(fieldList, inputParam);
                    }

                }
//                    value = inputParam.get(sqlField);
                value = ExecuteUtils.getObjFromMap(inputParam, sqlField);
            }
            if (StringUtils.isBlank(convertStr) && value != null) {
                if (value instanceof String) {
                    convertStr = value.toString();
                } else if (value instanceof List) {
                    List collection = (List) value;
                    int size = collection.size();

                    for (int i = 0; i < size; i++) {
                        convertStr += ("'" + String.valueOf(collection.get(i)) + "'");
                        if (i < size - 1) {
                            convertStr += ",";
                        }
                    }
                }
            }
            sqlStr = sqlStr.replace(replaceOld, " in (" + convertStr + ") ");
        }
        Pattern pattern = Pattern.compile("#\\{[a-zA-Z0-9_\u4e00-\u9fa5()（）-]+\\}");
        Matcher matcher = pattern.matcher(sqlStr);
        while (matcher.find()) {
            String sqlField = matcher.group(0).replace("#{", "").replace("}", "");
            String fieldEn = sqlField.split("\\.")[0];
            // sql动态参数从页面配置获取
            String sqlVariable = field.getSqlVariable();
            if (StringUtils.isNotBlank(sqlVariable)) {
                JSONArray sqlVariableArr = JSONArray.parseArray(sqlVariable);
                for (int i = 0; i < sqlVariableArr.size(); i++) {
                    JSONObject sqlVariableObj = sqlVariableArr.getJSONObject(i);
                    if (sqlField.equals(sqlVariableObj.getString("key"))) {
                        parameterMap.put(sqlField, sqlVariableObj.get("value"));
                    }
                }
            }

            // sql动态参数从变量池获取
            if (!parameterMap.containsKey(fieldEn)) {
                if (!inputParam.containsKey(fieldEn)) {
                    //不存再需要重新获取
                    List<String> fieldEns = new ArrayList<>();
                    fieldEns.add(fieldEn);
                    //查询未入参的en是否是指标库中的指标，如果是则u需要继续查找
                    List<Field> fieldList = fieldService.selectFieldListByEns(fieldEns);
                    if (fieldList != null && !fieldList.isEmpty()) {
                        //查询引用到的指标
                        getEngineField(fieldList, inputParam);
                    }

                }
                parameterMap.put(sqlField, ExecuteUtils.getObjFromMap(inputParam, sqlField));
            }
        }
        Pattern pattern$ = Pattern.compile("\\$\\{[a-zA-Z0-9_\u4e00-\u9fa5()（）-]+\\}");
        Matcher matcher$ = pattern$.matcher(sqlStr);
        while (matcher$.find()) {
            String sqlField = matcher$.group(0).replace("${", "").replace("}", "");
            String fieldEn = sqlField.split("\\.")[0];
            // sql绑定参数从页面配置获取
            String sqlVariable = field.getSqlVariable();
            String dictVariable = field.getDictVariable();
            String replaceStr = "";
            if (StringUtils.isNotBlank(sqlVariable)) {
                JSONArray sqlVariableArr = JSONArray.parseArray(sqlVariable);
                for (int i = 0; i < sqlVariableArr.size(); i++) {
                    JSONObject sqlVariableObj = sqlVariableArr.getJSONObject(i);

                    if (!sqlField.equals(sqlVariableObj.getString("key"))) {
                        continue;
                    }
                    if (inputParam.containsKey(fieldEn)) {
                        replaceStr = ExecuteUtils.getObjFromMap(inputParam, sqlField).toString();
                    } else if (sqlVariableObj.get("value") != null) {
                        replaceStr = String.valueOf(sqlVariableObj.get("value"));
                    }
                    if (StringUtils.isNotBlank(replaceStr)) {
                        break;
                    }

                }
            }
            if (StringUtils.isNotBlank(dictVariable)) {
                JSONArray jsonArray = JSONArray.parseArray(dictVariable);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (!sqlField.equals(jsonObject.getString("key"))) {
                        continue;
                    }
//                    if (inputParam.containsKey(fieldEn)) {
//                        replaceStr = ExecuteUtils.getObjFromMap(inputParam, sqlField).toString();
//                    } else
//                    if (jsonObject.get("value") != null) {
//                        switch (jsonObject.getString("type")){
//                            case "date":
//                                try {
//                                    replaceStr = DateUtil.format(new Date(),jsonObject.getString("value"));
//                                }catch (Exception e){
//                                    e.printStackTrace();
//                                    replaceStr = DateUtil.format(new Date(),"yyyyMMdd");
//                                }
//                                break;
//                            default:
//                                replaceStr = String.valueOf(jsonObject.get("value"));
//                        }
//                    }
                    replaceStr = DictVariableUtils.getValueFromJsonObject(jsonObject).toString();
                    if (StringUtils.isNotBlank(replaceStr)) {
                        break;
                    }
                }
            }
            if (StringUtils.isNotBlank(replaceStr)) {
                //已经取出则直接跳过
                sqlStr = sqlStr.replace("${" + sqlField + "}", replaceStr);
                continue;
            }
            // sql动态参数从变量池获取
            if (!parameterMap.containsKey(fieldEn)) {
                if (!inputParam.containsKey(fieldEn)) {
                    //不存再需要重新获取
                    //查询未入参的en是否是指标库中的指标，如果是则u需要继续查找
                    List<Field> fieldList = fieldService.selectFieldListByEns(Arrays.asList(fieldEn));
                    if (fieldList != null && !fieldList.isEmpty()) {
                        //查询引用到的指标
                        getEngineField(fieldList, inputParam);
                    }
                }
                replaceStr = ExecuteUtils.getObjFromMap(inputParam, sqlField).toString();
            }
            if (StringUtils.isNotBlank(replaceStr)) {
                sqlStr = sqlStr.replace("${" + sqlField + "}", replaceStr);
                break;
            }
        }
        parameterMap.put("sqlStr", sqlStr);
        return parameterMap;
    }

    /**
     * 调用第三方数据库执行sql获取结果
     */
    private Object handlerSqlFieldResult(Field field, Map<String, Object> parameterMap) {
        List<LinkedHashMap<String, Object>> result = simpleMapper.customSelect(parameterMap);
        Object resultValue = null;
        if (result == null || result.size() == 0) {
            resultValue = null;
        } else {
            //json类型的数据
            if (field.getValueType() == 6) {
                String json = field.getJsonValue();
                //示例数据为数组
                if (StringUtils.isNotBlank(json) && json.startsWith("[") && json.endsWith("]")) {
                    resultValue = result;
                } else {
                    resultValue = result.get(0);
                }
            } else if (result.size() == 1) {// 基本指标只能是一个值。所以只能取sql查出来的第一个值。否则抛出异常
                LinkedHashMap<String, Object> resultMap = result.get(0);
                if (resultMap.size() == 1) {
                    for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                        Object value = entry.getValue();

                        // 防止double表示为科学计数法
                        if (value instanceof Double) {
                            value = BigDecimal.valueOf((Double) value);
                        }
                        resultValue = value.toString();
                    }
                } else {
                    throw new RuntimeException("sql库指标计算异常，sql字段个数超出预期。查询结果resultMap:" + resultMap.toString());
                }
            } else {
                throw new RuntimeException("sql库指标计算异常，sql结果个数超出预期。查询结果result:" + result.toString());
            }
        }
        return resultValue;
    }
}
