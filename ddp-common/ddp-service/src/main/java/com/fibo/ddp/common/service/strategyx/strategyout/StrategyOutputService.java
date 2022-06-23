package com.fibo.ddp.common.service.strategyx.strategyout;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;

import java.util.List;
import java.util.Map;

public interface StrategyOutputService extends IService<StrategyOutput> {

    boolean insertTacticsOutput(Long tacticsId, List<StrategyOutput> list);

    boolean insertTacticsOutput(Long tacticsId, List<StrategyOutput> list, String outType);

    boolean updateTacticsOutput(Long tacticsId, List<StrategyOutput> list, String tacticsType);

    boolean updateTacticsOutput(Long tacticsId, List<StrategyOutput> successList, List<StrategyOutput> failList, String tacticsType);

    boolean deleteByTactics(StrategyOutput entity);

    // runner
    List<StrategyOutput> queryByTactics(StrategyOutput entity);

    List<JSONObject> setOutput(StrategyOutput entity, Map<String,Object> input);

    boolean judgeOutCondition(String condition,Map<String,Object> input);
}
