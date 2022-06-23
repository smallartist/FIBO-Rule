package com.fibo.ddp.common.service.strategyx.strategyout.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.strategyout.StrategyOutputMapper;
import com.fibo.ddp.common.model.enginex.runner.ExpressionParam;
import com.fibo.ddp.common.model.strategyx.strategyout.OutCondition;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import com.fibo.ddp.common.service.datax.runner.ExecuteUtils;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.strategyout.StrategyOutputService;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.constant.strategyx.StrategyType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StrategyOutputServiceImpl extends ServiceImpl<StrategyOutputMapper, StrategyOutput> implements StrategyOutputService {

    @Autowired
    private RedisManager redisManager;

    @Value("${switch.use.cache}")
    private String cacheSwitch;

    @Transactional
    @Override
    public boolean insertTacticsOutput(Long tacticsId, List<StrategyOutput> list) {
        if (tacticsId == null) {
            return false;
        }
        if (list == null || list.size() == 0) {
            return true;
        }
        for (StrategyOutput strategyOutput : list) {
            strategyOutput.setStrategyId(tacticsId);
        }
        boolean saveResult = this.saveBatch(list);
        return saveResult;
    }

    @Override
    public boolean insertTacticsOutput(Long tacticsId, List<StrategyOutput> list, String outType) {
        if (tacticsId == null || StringUtils.isBlank(outType)) {
            return false;
        }
        if (list == null || list.size() == 0) {
            return true;
        }
        for (StrategyOutput strategyOutput : list) {
            strategyOutput.setStrategyId(tacticsId);
            strategyOutput.setOutType(outType);
        }
        boolean saveResult = this.saveBatch(list);
        return saveResult;
    }

//    @Override
//    public List<StrategyOutput> queryByTactics(StrategyOutput entity) {
//        return this.list(new QueryWrapper<>(entity));
//    }

    @Override
    public List<StrategyOutput> queryByTactics(StrategyOutput entity) {
        List<StrategyOutput> strategyOutputList = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getForeignKey(TableEnum.T_STRATEGY_OUTPUT, entity.getStrategyId());
            strategyOutputList = redisManager.getByForeignKey(key, StrategyOutput.class);
            if(strategyOutputList != null){
                strategyOutputList = strategyOutputList.stream().filter(item -> item.getStrategyType().equals(entity.getStrategyType()))
                        .collect(Collectors.toList());
            }
            if (strategyOutputList !=null&&!strategyOutputList.isEmpty()&& StrategyType.COMPLEX_RULE.equals(entity.getVariableType())&&entity.getOutType()!=null){
                //复杂规则需要过滤出类型
                strategyOutputList = strategyOutputList.stream().filter(item ->entity.getOutType().equals( item.getOutType())).collect(Collectors.toList());
            }
        } else {
            strategyOutputList = this.list(new QueryWrapper<>(entity));
        }

        return strategyOutputList;
    }

    @Transactional
    @Override
    public boolean updateTacticsOutput(Long tacticsId, List<StrategyOutput> list, String tacticsType) {
        StrategyOutput strategyOutput = new StrategyOutput(tacticsId, tacticsType);
        boolean delete = this.deleteByTactics(strategyOutput);
        if (!delete && this.queryByTactics(strategyOutput).size() != 0) {
            return false;
        }
        return this.insertTacticsOutput(tacticsId, list);
    }

    @Transactional
    @Override
    public boolean updateTacticsOutput(Long tacticsId, List<StrategyOutput> successList, List<StrategyOutput> failList, String tacticsType) {
        StrategyOutput strategyOutput = new StrategyOutput(tacticsId, tacticsType);
        boolean delete = this.deleteByTactics(strategyOutput);
        if (!delete && this.queryByTactics(strategyOutput).size() != 0) {
            return false;
        }
        if (successList != null && !successList.isEmpty()) {
            this.insertTacticsOutput(tacticsId, successList, StrategyType.OutType.SUCCESS_OUT);
        }
        if (failList != null && !failList.isEmpty()) {
            this.insertTacticsOutput(tacticsId, failList, StrategyType.OutType.FAIL_OUT);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean deleteByTactics(StrategyOutput entity) {
        return this.remove(new QueryWrapper<>(entity));
    }

    //设置输出，传入map向map中放入输出并且返回输出列表
    @Override
    public List<JSONObject> setOutput(StrategyOutput entity, Map<String, Object> input) {
        List<StrategyOutput> strategyOutputList = this.queryByTactics(entity);
        List<JSONObject> jsonList = new ArrayList<>();
        if (strategyOutputList != null && strategyOutputList.size() > 0) {
            for (StrategyOutput strategyOutput : strategyOutputList) {
                if (!this.judgeOutCondition(strategyOutput.getOutCondition(),input)){
                    continue;
                }
                JSONObject json = new JSONObject();
                String fieldEn = strategyOutput.getFieldEn();
                String fieldValue = strategyOutput.getFieldValue();
                Object value = fieldValue;
                Integer variableType = strategyOutput.getVariableType();
                if (variableType != null) {
                    switch (variableType) {
                        case 2:
                            value = ExecuteUtils.getObjFromMap(input, fieldValue);
                            break;
                        case 3:
                            value = ExecuteUtils.getObjFromScript(input,fieldValue);
                            break;
                    }
                }
                if (value != null ) {
                    if (!"".equals(value)&&!"'".equals(value)&&value.toString().startsWith("'")&&value.toString().endsWith("'")){
                        value = value.toString().substring(1,value.toString().length()-1);
                    }
                    json.put(fieldEn, value);
                    input.put(fieldEn, value);
                    jsonList.add(json);
                }
            }
        }
        return jsonList;
    }

    //判断是否符合输出条件
    @Override
    public boolean judgeOutCondition(String condition, Map<String, Object> input) {
        //条件为空则符合输出
        if (null == condition || "".equals(condition)) {
            return true;
        }
        OutCondition outCondition;
        try {
            outCondition = JSON.parseObject(condition, OutCondition.class);
        }catch (Exception e){
            //字符串转json失败
            return true;
        }
        String logical = outCondition.getLogical();
        List<ExpressionParam> conditionList = outCondition.getConditionList();
        if (null == logical || null == conditionList||conditionList.size()<1){
            return true;
        }
        boolean result=false;
        switch (logical) {
            case "||":
                result = false;
                for (ExpressionParam expression : conditionList) {
                    if (ExecuteUtils.getExpressionResult(expression, input)){
                        return true;
                    }
                }
                break;
            case "&&":
                result = true;
                for (ExpressionParam expression : conditionList) {
                    if (!ExecuteUtils.getExpressionResult(expression, input)){
                        return false;
                    }
                }
                break;
        }
        return result;
    }
}
