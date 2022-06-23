package com.fibo.ddp.common.service.analyse.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.analyse.AnalyseDecisionResultMapper;
import com.fibo.ddp.common.model.analyse.AnalyseDecisionResult;
import com.fibo.ddp.common.model.analyse.vo.AnalyseRequestParam;
import com.fibo.ddp.common.service.analyse.AnalyseDecisionResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (AnalyseDecisionResult)表服务实现类
 */
@Service("analyseDecisionResultService")
public class AnalyseDecisionResultServiceImpl extends ServiceImpl<AnalyseDecisionResultMapper, AnalyseDecisionResult> implements AnalyseDecisionResultService {

    @Autowired
    private AnalyseDecisionResultMapper mapper;
    @Override
    public List<List<AnalyseDecisionResult>> getAnalyseData(AnalyseRequestParam param) {
        LambdaQueryWrapper<AnalyseDecisionResult> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AnalyseDecisionResult::getEngineId,param.getEngineId());
        wrapper.eq(AnalyseDecisionResult::getVersionId,param.getVersionId());
        wrapper.eq(AnalyseDecisionResult::getOrganId,param.getUser().getOrganId());
        wrapper.ge(AnalyseDecisionResult::getCallDate,param.getStart());
        wrapper.lt(AnalyseDecisionResult::getCallDate,param.getEnd());
        wrapper.orderByAsc(AnalyseDecisionResult::getCallDate);
        List<AnalyseDecisionResult> analyseDecisionResults = mapper.selectList(wrapper);
        Map<String,List<AnalyseDecisionResult>> map = new HashMap<>();
        for (AnalyseDecisionResult item : analyseDecisionResults) {
            List<AnalyseDecisionResult> list = null;
            String key = item.getResult();
            if (map.containsKey(key)){
                list = map.get(key);
            }else {
                list= new ArrayList<>();
            }
            list.add(item);
            map.put(key,list);
        }
        List<List<AnalyseDecisionResult>> result = new ArrayList<>(map.values());
        return result;
    }
}
