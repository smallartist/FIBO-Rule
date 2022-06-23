package com.fibo.ddp.common.service.cignacmb.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.cignacmb.BusinessEventLogMapper;
import com.fibo.ddp.common.model.cignacmb.BusinessEventLog;
import com.fibo.ddp.common.model.cignacmb.request.EventLogParam;
import com.fibo.ddp.common.model.cignacmb.response.EventLogResponse;
import com.fibo.ddp.common.service.cignacmb.BusinessEventLogService;
import com.fibo.ddp.common.service.common.SessionManager;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 事件调用记录(BusinessEventLog)表服务实现类
 */
@Service
public class BusinessEventLogServiceImpl extends ServiceImpl<BusinessEventLogMapper, BusinessEventLog> implements BusinessEventLogService {

    @Autowired
    private BusinessEventLogMapper eventLogMapper;

    @Override
    public PageInfo<EventLogResponse> getEventLogList(EventLogParam param) {
        Long organId = SessionManager.getSession().getSysUser().getOrganId();
        param.setOrganId(organId);
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        List<EventLogResponse> eventLogResponses = eventLogMapper.queryAllByLimit(param);
        PageInfo<EventLogResponse> pageInfo = new PageInfo<>(eventLogResponses);
        return pageInfo;
    }
}
