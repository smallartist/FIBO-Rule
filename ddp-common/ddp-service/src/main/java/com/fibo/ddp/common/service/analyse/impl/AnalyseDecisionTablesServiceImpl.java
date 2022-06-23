package com.fibo.ddp.common.service.analyse.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.analyse.AnalyseDecisionTablesMapper;
import com.fibo.ddp.common.model.analyse.AnalyseDecisionTables;
import com.fibo.ddp.common.model.analyse.vo.AnalyseRequestParam;
import com.fibo.ddp.common.service.analyse.AnalyseDecisionTablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (AnalyseDecisionTables)表服务实现类
 */
@Service("analyseDecisonTablesService")
public class AnalyseDecisionTablesServiceImpl extends ServiceImpl<AnalyseDecisionTablesMapper, AnalyseDecisionTables> implements AnalyseDecisionTablesService {
    @Autowired
    private AnalyseDecisionTablesMapper mapper;

    @Override
    public List<List<List<AnalyseDecisionTables>>> getAnalyseData(AnalyseRequestParam param) {
        LambdaQueryWrapper<AnalyseDecisionTables> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AnalyseDecisionTables::getEngineId,param.getEngineId());
        wrapper.eq(AnalyseDecisionTables::getVersionId,param.getVersionId());
        wrapper.eq(AnalyseDecisionTables::getOrganId,param.getUser().getOrganId());
        wrapper.ge(AnalyseDecisionTables::getCallDate,param.getStart());
        wrapper.lt(AnalyseDecisionTables::getCallDate,param.getEnd());
        wrapper.orderByAsc(AnalyseDecisionTables::getCallDate);
        List<AnalyseDecisionTables> analyseDecisionTables = mapper.selectList(wrapper);
        Map<String,Map<String,List<AnalyseDecisionTables>>> map = new HashMap<>();

        for (AnalyseDecisionTables item : analyseDecisionTables) {
            List<AnalyseDecisionTables> list = null;
            String versionKey =  String.valueOf(item.getDecisonTablesId())+"|"+String.valueOf(item.getDecisonTablesVersionId());
            String resultKey = item.getResult();
            Map<String,List<AnalyseDecisionTables>> temp =null;
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
        List<List<List<AnalyseDecisionTables>>> result = new ArrayList<>();
        for (Map<String, List<AnalyseDecisionTables>> value : map.values()) {
            result.add(new ArrayList<>(value.values()));
        }
        return result;
    }
}
