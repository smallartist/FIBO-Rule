package com.fibo.ddp.common.service.strategyx.collectionrule.impl;

import com.fibo.ddp.common.model.strategyx.collectionrule.*;
import com.fibo.ddp.common.service.strategyx.collectionrule.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConditionAndOutPutServiceImpl implements ConditionAndOutPutService {
    @Resource
    private ListOperationConditionService conditionService;
    @Resource
    private ListOperationOutputService outputService;
    @Resource
    private ListOperationBlockService blockService;
    @Resource
    private ListOperationFilterConditionService filterConditionService;
    @Override
    public List<ConditionAndOutPut> queryByListOpVersion(ListOperationVersion version) {
        List<ConditionAndOutPut> resultList = new ArrayList<>();
        Map<Long,ConditionAndOutPut> map = new HashMap<>();
        List<ListOperationBlock> blocks = blockService.queryByVersion(version);
        if (blocks==null||blocks.isEmpty()){
            return resultList;
        }
        ConditionAndOutPut conditionAndOutPut = null;
        for (ListOperationBlock block : blocks) {
            conditionAndOutPut = new ConditionAndOutPut();
            conditionAndOutPut.setListOperationBlock(block);
            map.put(block.getId(),conditionAndOutPut);
        }
        List<ListOperationCondition> conditions = conditionService.queryByVersion(version);
        List<ListOperationOutput> outputs = outputService.queryByVersion(version);
        boolean haveOut = !(outputs == null || outputs.isEmpty());
        Map<String, List<ListOperationOutput>> outPutMap = null;
        if (haveOut) {
            outPutMap = outputs.stream().collect(Collectors.groupingBy(item -> {
                return item.getListOpBlockId()+"_"+item.getOutputType()+"_"+item.getListOpConditionId();
            }));
            //将默认输出写入
            if (outPutMap != null && !outPutMap.isEmpty()) {
                for (Map.Entry<String, List<ListOperationOutput>> entry : outPutMap.entrySet()) {
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
        for (ListOperationCondition condition : conditions) {
            if (!map.containsKey(condition.getListOpBlockId())){
                continue;
            }
            conditionAndOutPut = map.get(condition.getListOpBlockId());
            conditionAndOutPut.setCondition(condition);
            if (haveOut) {
                List<ListOperationOutput> successList = outPutMap.get(condition.getListOpBlockId() + "_1_" + condition.getId());
                if (successList!=null){
                    conditionAndOutPut.setSuccessOutput(successList);
                }
                List<ListOperationOutput> fails = outPutMap.get(condition.getListOpBlockId() + "_2_" + condition.getId());
                if (fails!=null){
                    conditionAndOutPut.setFailOutput(fails);
                }

            }
        }
        List<ListOperationFilterCondition> filterConditions = filterConditionService.queryByVersion(version);
        if (filterConditions!=null&&!filterConditions.isEmpty()){
            for (ListOperationFilterCondition condition : filterConditions) {
                if (!map.containsKey(condition.getListOpBlockId())){
                    continue;
                }
                conditionAndOutPut = map.get(condition.getListOpBlockId());
                conditionAndOutPut.setFilterCondition(condition);
            }
        }
        resultList.addAll(map.values());
        return resultList;
    }

    @Override
    @Transactional
    public boolean addBatch(ListOperationVersion version, List<ConditionAndOutPut> conditionAndOutPutList) {
        if (conditionAndOutPutList == null || conditionAndOutPutList.isEmpty()) {
            return true;
        }
        List<ListOperationOutput> outputs = new ArrayList<>();
        for (ConditionAndOutPut conditionAndOutPut : conditionAndOutPutList) {
            ListOperationBlock block = conditionAndOutPut.getListOperationBlock();
            block.setListOpVersionId(version.getId());
            //存入块
            blockService.save(block);
            ListOperationFilterCondition filterCondition = conditionAndOutPut.getFilterCondition();
            if (filterCondition != null) {
                filterCondition.setParentId(0L);
                filterConditionService.recursionSave(filterCondition,version,block.getId());
            }
            ListOperationCondition condition = conditionAndOutPut.getCondition();
            Long conditionId = 0L;
            if (condition != null) {
                condition.setParentId(0L);
                conditionId = conditionService.recursionSave(condition,version,block.getId());
                List<ListOperationOutput> successOutput = conditionAndOutPut.getSuccessOutput();
                if (successOutput != null && !successOutput.isEmpty()) {
                    for (ListOperationOutput output : successOutput) {
                        output.setListOpBlockId(block.getId());
                        output.setListOpConditionId(conditionId);
                        output.setListOpVersionId(version.getId());
                        output.setOutputType(1);
                    }
                    outputs.addAll(successOutput);
                }
                List<ListOperationOutput> failOutput = conditionAndOutPut.getFailOutput();
                if (failOutput != null && !failOutput.isEmpty()) {
                    for (ListOperationOutput output : failOutput) {
                        output.setListOpBlockId(block.getId());
                        output.setListOpConditionId(conditionId);
                        output.setListOpVersionId(version.getId());
                        output.setOutputType(2);
                    }
                    outputs.addAll(failOutput);
                }
            }else {
                List<ListOperationOutput> defaultOutput = conditionAndOutPut.getDefaultOutput();
                if (defaultOutput != null && !defaultOutput.isEmpty()) {
                    for (ListOperationOutput output : defaultOutput) {
                        output.setListOpBlockId(block.getId());
                        output.setListOpConditionId(conditionId);
                        output.setListOpVersionId(version.getId());
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
    public boolean updateBatch(ListOperationVersion version, List<ConditionAndOutPut> conditionAndOutPutList) {
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
//    private List<ListOperationCondition> decoupling(ListOperationCondition condition, Long versionId, boolean needTempId) {
//        List<ListOperationCondition> list = new ArrayList<>();
//        List<ListOperationCondition> children = condition.getChildren();
//        if (children != null && children.size() > 0) {
//            for (int i = 0; i < children.size(); i++) {
//                ListOperationCondition child = children.get(i);
//                if (needTempId) {
//                    child.setInsertTempId(UUID.randomUUID().toString().replace("-", ""));
//                    child.setTempParentId(condition.getInsertTempId());
//                }
//                List<ListOperationCondition> childList = decoupling(child, versionId, needTempId);
//                list.addAll(childList);
//            }
//        }
//        condition.setDataCleanVersionId(versionId);
//        list.add(condition);
//        return list;
//    }

}
