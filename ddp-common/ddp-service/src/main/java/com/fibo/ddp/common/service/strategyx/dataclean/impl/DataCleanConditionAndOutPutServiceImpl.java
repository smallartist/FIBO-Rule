package com.fibo.ddp.common.service.strategyx.dataclean.impl;

import com.fibo.ddp.common.model.strategyx.dataclean.*;
import com.fibo.ddp.common.service.strategyx.dataclean.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataCleanConditionAndOutPutServiceImpl implements DataCleanConditionAndOutPutService {
    @Resource
    private DataCleanConditionService conditionService;
    @Resource
    private DataCleanOutputService outputService;
    @Resource
    private DataCleanBlockService blockService;
    @Resource
    private DataCleanFilterConditionService filterConditionService;
    @Override
    public List<DataCleanConditionAndOutPut> queryByListOpVersion(DataCleanVersion version) {
        List<DataCleanConditionAndOutPut> resultList = new ArrayList<>();
        Map<Long,DataCleanConditionAndOutPut> map = new HashMap<>();
        List<DataCleanBlock> blocks = blockService.queryByVersion(version);
        if (blocks==null||blocks.isEmpty()){
            return resultList;
        }
        DataCleanConditionAndOutPut conditionAndOutPut = null;
        for (DataCleanBlock block : blocks) {
            conditionAndOutPut = new DataCleanConditionAndOutPut();
            conditionAndOutPut.setDataCleanBlock(block);
            map.put(block.getId(),conditionAndOutPut);
        }
        List<DataCleanCondition> conditions = conditionService.queryByVersion(version);
        List<DataCleanOutput> outputs = outputService.queryByVersion(version);
        boolean haveOut = !(outputs == null || outputs.isEmpty());
        Map<String, List<DataCleanOutput>> outPutMap = null;
        if (haveOut) {
            outPutMap = outputs.stream().collect(Collectors.groupingBy(item -> {
                return item.getDataCleanBlockId()+"_"+item.getOutputType()+"_"+item.getDataCleanConditionId();
            }));
            //将默认输出写入
            if (outPutMap != null && !outPutMap.isEmpty()) {
                for (Map.Entry<String, List<DataCleanOutput>> entry : outPutMap.entrySet()) {
                    String[] keyArray = entry.getKey().split("_");
                    Long key = Long.valueOf(keyArray[0]);
                    //查找到对应块则存入信息
                    if ("3".equals(keyArray[1])&&map.containsKey(key)){
                        map.get(key).setDefaultOutput(entry.getValue());
                    }
                }
            }
        }
        if (conditions == null || conditions.isEmpty()) {
            return resultList;
        }
        for (DataCleanCondition condition : conditions) {
            if (!map.containsKey(condition.getDataCleanBlockId())){
                continue;
            }
            conditionAndOutPut = map.get(condition.getDataCleanBlockId());
            conditionAndOutPut.setCondition(condition);
            if (haveOut) {
                List<DataCleanOutput> successList = outPutMap.get(condition.getDataCleanBlockId() + "_1_" + condition.getId());
                if (successList!=null){
                    conditionAndOutPut.setSuccessOutput(successList);
                }
                List<DataCleanOutput> fails = outPutMap.get(condition.getDataCleanBlockId() + "_2_" + condition.getId());
                if (fails!=null){
                    conditionAndOutPut.setFailOutput(fails);
                }

            }
        }
        List<DataCleanFilterCondition> filterConditions = filterConditionService.queryByVersion(version);
        if (filterConditions!=null&&!filterConditions.isEmpty()){
            for (DataCleanFilterCondition condition : filterConditions) {
                if (!map.containsKey(condition.getDataCleanBlockId())){
                    continue;
                }
                //设置过滤条件
                conditionAndOutPut = map.get(condition.getDataCleanBlockId());
                if (FilterCondEnum.INPUT_FILTER.getCode().equals(condition.getFilterType())){
                    conditionAndOutPut.setInputFilterCondition(condition);
                }else  if (FilterCondEnum.RESULT_FILTER.getCode().equals(condition.getFilterType())){
                    conditionAndOutPut.setResultFilterCondition(condition);
                }
            }
        }
        resultList.addAll(map.values());
        return resultList;
    }

    @Override
    @Transactional
    public boolean addBatch(DataCleanVersion version, List<DataCleanConditionAndOutPut> conditionAndOutPutList) {
        if (conditionAndOutPutList == null || conditionAndOutPutList.isEmpty()) {
            return true;
        }
        List<DataCleanOutput> outputs = new ArrayList<>();
        for (DataCleanConditionAndOutPut conditionAndOutPut : conditionAndOutPutList) {
            DataCleanBlock block = conditionAndOutPut.getDataCleanBlock();
            block.setDataCleanVersionId(version.getId());
            //存入块
            blockService.save(block);
            DataCleanFilterCondition inputFilterCondition = conditionAndOutPut.getInputFilterCondition();
            if (inputFilterCondition != null) {
                inputFilterCondition.setParentId(0L);
                filterConditionService.recursionSave(inputFilterCondition,version,block.getId(),FilterCondEnum.INPUT_FILTER);
            }
            DataCleanFilterCondition resultFilterCondition = conditionAndOutPut.getResultFilterCondition();
            if (resultFilterCondition != null) {
                resultFilterCondition.setParentId(0L);
                filterConditionService.recursionSave(resultFilterCondition,version,block.getId(),FilterCondEnum.RESULT_FILTER);
            }

            DataCleanCondition condition = conditionAndOutPut.getCondition();
            Long conditionId = 0L;
            if (condition != null) {
                condition.setParentId(0L);
                conditionId = conditionService.recursionSave(condition,version,block.getId());
                List<DataCleanOutput> successOutput = conditionAndOutPut.getSuccessOutput();
                if (successOutput != null && !successOutput.isEmpty()) {
                    for (DataCleanOutput output : successOutput) {
                        output.setDataCleanBlockId(block.getId());
                        output.setDataCleanConditionId(conditionId);
                        output.setDataCleanVersionId(version.getId());
                        output.setOutputType(1);
                    }
                    outputs.addAll(successOutput);
                }
                List<DataCleanOutput> failOutput = conditionAndOutPut.getFailOutput();
                if (failOutput != null && !failOutput.isEmpty()) {
                    for (DataCleanOutput output : failOutput) {
                        output.setDataCleanBlockId(block.getId());
                        output.setDataCleanConditionId(conditionId);
                        output.setDataCleanVersionId(version.getId());
                        output.setOutputType(2);
                    }
                    outputs.addAll(failOutput);
                }
            }else {
                List<DataCleanOutput> defaultOutput = conditionAndOutPut.getDefaultOutput();
                if (defaultOutput != null && !defaultOutput.isEmpty()) {
                    for (DataCleanOutput output : defaultOutput) {
                        output.setDataCleanBlockId(block.getId());
                        output.setDataCleanConditionId(conditionId);
                        output.setDataCleanVersionId(version.getId());
                        output.setOutputType(3);
                    }
                    outputs.addAll(defaultOutput);
                }
            }

        }
        if (!outputs.isEmpty()){
            outputService.saveBatch(outputs);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updateBatch(DataCleanVersion version, List<DataCleanConditionAndOutPut> conditionAndOutPutList) {
         this.deleteByVersionId(version.getId());
         return this.addBatch(version,conditionAndOutPutList);
    }

    @Override
    @Transactional
    public boolean deleteByVersionId(Long versionId) {
        conditionService.deleteByVersionId(versionId);
        blockService.deleteByVersionId(versionId);
        outputService.deleteByVersionId(versionId);
        filterConditionService.deleteByVersionId(versionId);
        return true;
    }


//
//    //解耦方法：将规则树解耦为节点列表
//    private List<DataCleanCondition> decoupling(DataCleanCondition condition, Long versionId, boolean needTempId) {
//        List<DataCleanCondition> list = new ArrayList<>();
//        List<DataCleanCondition> children = condition.getChildren();
//        if (children != null && children.size() > 0) {
//            for (int i = 0; i < children.size(); i++) {
//                DataCleanCondition child = children.get(i);
//                if (needTempId) {
//                    child.setInsertTempId(UUID.randomUUID().toString().replace("-", ""));
//                    child.setTempParentId(condition.getInsertTempId());
//                }
//                List<DataCleanCondition> childList = decoupling(child, versionId, needTempId);
//                list.addAll(childList);
//            }
//        }
//        condition.setDataCleanVersionId(versionId);
//        list.add(condition);
//        return list;
//    }

}
