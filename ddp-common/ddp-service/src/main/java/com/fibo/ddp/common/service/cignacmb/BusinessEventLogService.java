package com.fibo.ddp.common.service.cignacmb;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.cignacmb.BusinessEventLog;
import com.fibo.ddp.common.model.cignacmb.request.EventLogParam;
import com.fibo.ddp.common.model.cignacmb.response.EventLogResponse;
import com.github.pagehelper.PageInfo;

/**
 * 事件调用记录(BusinessEventLog)表服务接口
 */
public interface BusinessEventLogService extends IService<BusinessEventLog> {

    PageInfo<EventLogResponse> getEventLogList(EventLogParam param);
}
