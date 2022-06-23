package com.fibo.ddp.common.service.analyse.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.analyse.AnalyseEngineCallMapper;
import com.fibo.ddp.common.model.analyse.AnalyseEngineCall;
import com.fibo.ddp.common.model.analyse.vo.AnalyseRequestParam;
import com.fibo.ddp.common.service.analyse.AnalyseEngineCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (AnalyseEngineCall)表服务实现类
 */
@Service("analyseEngineCallService")
public class AnalyseEngineCallServiceImpl extends ServiceImpl<AnalyseEngineCallMapper, AnalyseEngineCall> implements AnalyseEngineCallService {

    @Autowired
    private AnalyseEngineCallMapper mapper;
    @Override
    public List<AnalyseEngineCall> getAnalyseData(AnalyseRequestParam param) {
        LambdaQueryWrapper<AnalyseEngineCall> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AnalyseEngineCall::getEngineId,param.getEngineId());
        wrapper.eq(AnalyseEngineCall::getVersionId,param.getVersionId());
        wrapper.eq(AnalyseEngineCall::getOrganId,param.getUser().getOrganId());
        wrapper.ge(AnalyseEngineCall::getCallDate,param.getStart());
        wrapper.lt(AnalyseEngineCall::getCallDate,param.getEnd());
        wrapper.orderByAsc(AnalyseEngineCall::getCallDate);
        List<AnalyseEngineCall> result = mapper.selectList(wrapper);
        return result;
    }
}
