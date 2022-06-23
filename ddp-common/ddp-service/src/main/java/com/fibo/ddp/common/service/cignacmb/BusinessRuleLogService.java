package com.fibo.ddp.common.service.cignacmb;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.cignacmb.BusinessRuleLog;
import com.fibo.ddp.common.model.cignacmb.request.RuleLogParam;
import com.fibo.ddp.common.model.cignacmb.response.EventLogDetailResponse;
import com.fibo.ddp.common.model.cignacmb.response.RuleLogResponse;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 规则执行记录(BusinessRuleLog)表服务接口
 */
public interface BusinessRuleLogService extends IService<BusinessRuleLog> {

    PageInfo<RuleLogResponse> getRuleLogList(RuleLogParam param);

    List<EventLogDetailResponse> queryByIds(List<String> ids);
}
