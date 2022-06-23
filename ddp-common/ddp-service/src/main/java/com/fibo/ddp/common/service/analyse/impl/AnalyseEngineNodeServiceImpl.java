package com.fibo.ddp.common.service.analyse.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.analyse.AnalyseEngineNodeMapper;
import com.fibo.ddp.common.model.analyse.AnalyseEngineNode;
import com.fibo.ddp.common.model.analyse.vo.AnalyseRequestParam;
import com.fibo.ddp.common.service.analyse.AnalyseEngineNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (AnalyseEngineNode)表服务实现类
 */
@Service("analyseEngineNodeService")
public class AnalyseEngineNodeServiceImpl extends ServiceImpl<AnalyseEngineNodeMapper, AnalyseEngineNode> implements AnalyseEngineNodeService {

    @Autowired
    private AnalyseEngineNodeMapper mapper;
    @Override
    public List<List<AnalyseEngineNode>> getAnalyseData(AnalyseRequestParam param) {
        LambdaQueryWrapper<AnalyseEngineNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AnalyseEngineNode::getEngineId,param.getEngineId());
        wrapper.eq(AnalyseEngineNode::getVersionId,param.getVersionId());
        wrapper.eq(AnalyseEngineNode::getOrganId,param.getUser().getOrganId());
        wrapper.ge(AnalyseEngineNode::getCallDate,param.getStart());
        wrapper.lt(AnalyseEngineNode::getCallDate,param.getEnd());
        wrapper.orderByAsc(AnalyseEngineNode::getCallDate);
        List<AnalyseEngineNode> AnalyseEngineNodes = mapper.selectList(wrapper);
        Map<String,List<AnalyseEngineNode>> map = new HashMap<>();
        for (AnalyseEngineNode item : AnalyseEngineNodes) {
            List<AnalyseEngineNode> list = null;
            String key = String.valueOf(item.getNodeId());
            if (map.containsKey(key)){
                list = map.get(key);
            }else {
                list= new ArrayList<>();
            }
            list.add(item);
            map.put(key,list);
        }
        List<List<AnalyseEngineNode>> result = map.values().stream().collect(Collectors.toList());
        return result;
    }
}
