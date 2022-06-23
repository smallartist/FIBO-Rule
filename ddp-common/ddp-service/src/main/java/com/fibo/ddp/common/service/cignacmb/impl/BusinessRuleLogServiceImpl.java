package com.fibo.ddp.common.service.cignacmb.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.cignacmb.BusinessRuleLogMapper;
import com.fibo.ddp.common.model.cignacmb.BusinessRuleLog;
import com.fibo.ddp.common.model.cignacmb.request.RuleLogParam;
import com.fibo.ddp.common.model.cignacmb.response.EventLogDetailResponse;
import com.fibo.ddp.common.model.cignacmb.response.RuleLogResponse;
import com.fibo.ddp.common.service.cignacmb.BusinessRuleLogService;
import com.fibo.ddp.common.service.common.SessionManager;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 规则执行记录(BusinessRuleLog)表服务实现类
 */
@Service
public class BusinessRuleLogServiceImpl extends ServiceImpl<BusinessRuleLogMapper, BusinessRuleLog> implements BusinessRuleLogService {

    @Autowired
    private BusinessRuleLogMapper ruleLogMapper;

    @Override
    public PageInfo<RuleLogResponse> getRuleLogList(RuleLogParam param) {
        Long organId = SessionManager.getSession().getSysUser().getOrganId();
        param.setOrganId(organId);
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        List<RuleLogResponse> ruleLogResponses = ruleLogMapper.queryAllByLimit(param);
        PageInfo<RuleLogResponse> pageInfo = new PageInfo<>(ruleLogResponses);
        return pageInfo;
    }

    @Override
    public List<EventLogDetailResponse> queryByIds(List<String> ids) {
        return ruleLogMapper.queryByIds(ids);
    }
}
