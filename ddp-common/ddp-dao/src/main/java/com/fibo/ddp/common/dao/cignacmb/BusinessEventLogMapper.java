package com.fibo.ddp.common.dao.cignacmb;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.cignacmb.BusinessEventLog;
import com.fibo.ddp.common.model.cignacmb.request.EventLogParam;
import com.fibo.ddp.common.model.cignacmb.response.EventLogResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BusinessEventLogMapper extends BaseMapper<BusinessEventLog> {

    /**
     * 查询指定行数据
     */
    List<EventLogResponse> queryAllByLimit(EventLogParam param);
}
