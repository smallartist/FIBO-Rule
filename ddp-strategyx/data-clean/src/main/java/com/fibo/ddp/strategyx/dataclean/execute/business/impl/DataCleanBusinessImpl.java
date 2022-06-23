package com.fibo.ddp.strategyx.dataclean.execute.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.strategyx.dataclean.*;
import com.fibo.ddp.common.model.strategyx.dataclean.param.RunnerDataCleanParam;
import com.fibo.ddp.common.service.strategyx.dataclean.DataCleanService;
import com.fibo.ddp.common.service.strategyx.dataclean.DataCleanVersionService;
import com.fibo.ddp.common.utils.util.strategyx.DataCleanUtils;
import com.fibo.ddp.strategyx.dataclean.execute.business.DataCleanBusiness;
import com.fibo.ddp.strategyx.dataclean.execute.handler.DataCleanFilterConditionHandler;
import com.fibo.ddp.strategyx.dataclean.execute.handler.DataCleanModifyElementHandler;
import com.fibo.ddp.strategyx.dataclean.execute.handler.ListOpConditionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dataCleanBusiness")
@Slf4j
public class DataCleanBusinessImpl implements DataCleanBusiness {

    @Resource
    private DataCleanVersionService versionService;
    @Resource
    private DataCleanService dataCleanService;
    @Resource
    private DataCleanModifyElementHandler modifyElementHandler;
    @Resource
    private DataCleanFilterConditionHandler filterConditionHandler;
    @Resource
    private ListOpConditionHandler listOpConditionHandler;

    @Override
    public JSONObject runner(RunnerDataCleanParam param) {
//        DataClean dataClean = param.getDataClean();
        long startTime = System.currentTimeMillis();
        JSONObject result = null;
        //查出内容
        DataClean dataClean = dataCleanService.queryByCode(param.getDataCleanCode(),param.getVersionCode());
        if (dataClean==null){
            log.debug("【规则查询】拒绝执行:未查找到规则版本信息,耗时:{}, dataCleanCode:{} versionCode:{}",
                    System.currentTimeMillis() - startTime, param.getDataCleanCode(), param.getVersionCode());
            return result;
        }
        List<DataCleanVersion> versionList = dataClean.getVersionList();
        DataCleanVersion version = null;
        if (CollectionUtils.isNotEmpty(versionList)){
            version = versionList.get(0);
        }
//        version = versionService.queryById(param.getVersionId());
        long queryEnd = System.currentTimeMillis();
        log.debug("【规则查询】查询规则详情完成,耗时:{}, ruleCode:{} versionId:{}",
                queryEnd - startTime, param.getDataCleanCode(), param.getVersionCode());
        if (version == null) {
            log.debug("【规则查询】拒绝执行:未查找到规则版本信息,耗时:{}, dataCleanId:{} versionCode:{}",
                    System.currentTimeMillis() - startTime, version.getDataCleanId(), param.getVersionCode());
            return result;
        }

//        Date date = new Date();
//        //判断有效期 如果有有效期则需要判断
//        if (dataClean == null
//                || (dataClean.getStartTime() != null && date.compareTo(dataClean.getStartTime()) < 0)
//                || (dataClean.getEndTime() != null && date.compareTo(dataClean.getEndTime()) > 0)
//                || dataClean.getStatus() != 1) {
//            return result;
//        }

        String inputFieldEn = "default";
        switch (version.getInputFieldType()) {
            case "map":
                result = this.mapHandlerV1(inputFieldEn, param.getParam(), version, version.getVersionCode());
                break;
            case "list":
                break;
        }
        long endTime = System.currentTimeMillis();
        log.debug("【规则执行】执行规则完成,耗时:{}, ruleCode:{}, versionCode:{}",
                (endTime - queryEnd), version.getVersionCode(), param.getVersionCode());
        return result;
    }

    public JSONObject mapHandlerV1(String inputEn, Map<String, Object> param, DataCleanVersion version, String ruleCode) {
        List<Map<String, Object>> result = new ArrayList<>();
        JSONObject intputObject = null;
        Object paramMap = param.get(inputEn);
        if (paramMap == null) {
            //未能获取到参数直接返回
            return null;
        }
        if (paramMap instanceof Map) {
            intputObject = JSONObject.parseObject(JSONObject.toJSONString(paramMap));
        }

        DataCleanOriginalDataOp originalDataOp = version.getOriginalDataOp();
        String opField = originalDataOp.getOpField();
        JSONArray forTarget = intputObject.getJSONArray(opField);
        List<JSONObject> forTargetJson = new ArrayList<>();
        for(Object obj : forTarget){
            forTargetJson.add((JSONObject) obj);
        }
        // 循环对象
        for(JSONObject forObject : forTargetJson){
            this.listHandlerV1(intputObject, forObject, version, ruleCode);
        }
        intputObject.put(opField, forTargetJson);
        return intputObject;
    }

    public List<Map<String, Object>> listHandlerV1(JSONObject intputObject, JSONObject forObject, DataCleanVersion version, String ruleCode) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<DataCleanConditionAndOutPut> conditionAndOutPutList = version.getConditionAndOutPutList();
        // 新ratePlanList 返回每个组价格最低的一个元素
        List<JSONObject> newRatePlanList = new ArrayList<>();
        // 多块处理
        for (DataCleanConditionAndOutPut conditionAndOutPut : conditionAndOutPutList) {
            DataCleanBlock dataCleanBlock = conditionAndOutPut.getDataCleanBlock();
            String handleCollection = dataCleanBlock.getHandleCollection();
            JSONArray targetCollection = forObject.getJSONArray(handleCollection);
            if(targetCollection == null){
                //未能获取到参数直接返回
                return null;
            }
            List<JSONObject> inputArray = new ArrayList<>();
            for (Object obj : targetCollection) {
                inputArray.add((JSONObject) obj);
            }

            DataCleanFilterCondition filterCondition = conditionAndOutPut.getInputFilterCondition();
            List<JSONObject> executeArray = inputArray;
            if (filterCondition != null) {
                //执行进入过滤条件
                executeArray = filterConditionHandler.filterByCondition(executeArray, filterCondition);
            }
            // 执行分组
            Map<String, List<JSONObject>> listMap = null;
            String groupFieldsStr = conditionAndOutPut.getDataCleanBlock().getGroupFields();
            List<String> groupFields = null;
            if (groupFieldsStr != null && !"[]".equals(groupFieldsStr)) {
                groupFields = JSON.parseArray(groupFieldsStr, String.class);
            }
            if (groupFields != null && !groupFields.isEmpty()) {
                listMap = groupBy(groupFields, executeArray);
            } else {
                listMap = new HashMap<>();
                listMap.put("default", executeArray);
            }

            // 遍历每一个组
            for (Map.Entry<String, List<JSONObject>> entry : listMap.entrySet()) {
                Map<String, Object> localParamMap = new HashMap<>();
                Map<String, Object> localOutPutMap = new HashMap<>();
                List<JSONObject> list = entry.getValue();
                DataCleanCondition condition = conditionAndOutPut.getCondition();
                if (condition != null || condition.getChildren() != null) {
                    for(JSONObject collectionObject : list){
                        // 执行修改元素条件
                        boolean conditionResult = modifyElementHandler.conditionHandler(condition, localParamMap, intputObject, forObject, collectionObject);
                        if (conditionResult) {
                            // 命中修改元素
                            modifyElementHandler.outputListHandler(conditionAndOutPut.getSuccessOutput(), localParamMap, localOutPutMap, collectionObject);
                        } else {
                            // 未命中修改元素
                            modifyElementHandler.outputListHandler(conditionAndOutPut.getFailOutput(), localParamMap, localOutPutMap, collectionObject);
                        }
                    }
                }
                if (!localOutPutMap.isEmpty()) {
                    result.add(localOutPutMap);
                }
                // 组内返回结果过滤
                DataCleanFilterCondition resultFilterCondition = conditionAndOutPut.getResultFilterCondition();
                JSONObject resultFilterJson = listOpConditionHandler.conditionHandler(resultFilterCondition, null, list);
                newRatePlanList.add(resultFilterJson);
            }
        }
        forObject.put("ratePlanList", newRatePlanList);
        return result;
    }

    public Map<String, List<JSONObject>> groupBy(List<String> groupFields, List<JSONObject> groupArray) {
        Map<String, List<JSONObject>> listMap = DataCleanUtils.recursionGroup(groupArray, groupFields);
        return listMap;
    }
}
