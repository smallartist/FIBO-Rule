package com.fibo.ddp.common.service.analyse.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.analyse.AnalyseScorecardMapper;
import com.fibo.ddp.common.model.analyse.AnalyseScorecard;
import com.fibo.ddp.common.model.analyse.vo.AnalyseRequestParam;
import com.fibo.ddp.common.service.analyse.AnalyseScorecardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (AnalyseScorecard)表服务实现类
 */
@Service("tAnalyseScorecardService")
public class AnalyseScorecardServiceImpl extends ServiceImpl<AnalyseScorecardMapper, AnalyseScorecard> implements AnalyseScorecardService {

    @Autowired
    private AnalyseScorecardMapper mapper;

    @Override
    public List<List<List<AnalyseScorecard>>> getAnalyseData(AnalyseRequestParam param) {
        LambdaQueryWrapper<AnalyseScorecard> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AnalyseScorecard::getEngineId,param.getEngineId());
        wrapper.eq(AnalyseScorecard::getVersionId,param.getVersionId());
        wrapper.eq(AnalyseScorecard::getOrganId,param.getUser().getOrganId());
        wrapper.ge(AnalyseScorecard::getCallDate,param.getStart());
        wrapper.lt(AnalyseScorecard::getCallDate,param.getEnd());
        wrapper.orderByAsc(AnalyseScorecard::getCallDate);
        List<AnalyseScorecard> AnalyseScorecard = mapper.selectList(wrapper);
        Map<String, Map<String,List<AnalyseScorecard>>> map = new HashMap<>();

        for (AnalyseScorecard item : AnalyseScorecard) {
            List<AnalyseScorecard> list = null;
            String versionKey = String.valueOf(item.getScorecardId())+"|"+String.valueOf(item.getScorecardVersionId());
            String resultKey = item.getResult();
            Map<String,List<AnalyseScorecard>> temp =null;
            if (map.containsKey(versionKey)){
                temp = map.get(versionKey);
            }else {
                temp = new HashMap<>();
            }
            if (temp.containsKey(resultKey)){
                list = temp.get(resultKey);
            }else {
                list = new ArrayList<>();
            }
            list.add(item);
            temp.put(resultKey,list);
            map.put(versionKey,temp);
        }
        List<List<List<AnalyseScorecard>>> result = new ArrayList<>();
        for (Map<String, List<AnalyseScorecard>> value : map.values()) {
            result.add(new ArrayList<>(value.values()));
        }
        return result;
    }
}
