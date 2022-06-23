package com.fibo.ddp.common.service.analyse.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.analyse.AnalyseRuleMapper;
import com.fibo.ddp.common.model.analyse.AnalyseRule;
import com.fibo.ddp.common.model.analyse.vo.AnalyseRequestParam;
import com.fibo.ddp.common.service.analyse.AnalyseRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (AnalyseRule)表服务实现类
 */
@Service("analyseRuleService")
public class AnalyseRuleServiceImpl extends ServiceImpl<AnalyseRuleMapper, AnalyseRule> implements AnalyseRuleService {

    @Autowired
    private AnalyseRuleMapper mapper;
    @Override
    public List<List<AnalyseRule>> getAnalyseData(AnalyseRequestParam param) {
        LambdaQueryWrapper<AnalyseRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AnalyseRule::getEngineId,param.getEngineId());
        wrapper.eq(AnalyseRule::getVersionId,param.getVersionId());
        wrapper.eq(AnalyseRule::getOrganId,param.getUser().getOrganId());
        wrapper.ge(AnalyseRule::getCallDate,param.getStart());
        wrapper.lt(AnalyseRule::getCallDate,param.getEnd());
        wrapper.orderByAsc(AnalyseRule::getCallDate);
        List<AnalyseRule> AnalyseRules = mapper.selectList(wrapper);
        Map<String,List<AnalyseRule>> map = new HashMap<>();
        for (AnalyseRule item : AnalyseRules) {
            List<AnalyseRule> list = null;
            String key = String.valueOf(item.getRuleId())+"|"+String.valueOf(item.getRuleVersionId());
            if (map.containsKey(key)){
                list = map.get(key);
            }else {
                list= new ArrayList<>();
            }
            list.add(item);
            map.put(key,list);
        }
        List<List<AnalyseRule>> result = new ArrayList<>(map.values());
        return result;
    }
}
