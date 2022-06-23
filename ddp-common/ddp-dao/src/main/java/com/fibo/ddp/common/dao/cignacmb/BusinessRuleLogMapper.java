package com.fibo.ddp.common.dao.cignacmb;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.cignacmb.BusinessRuleLog;
import com.fibo.ddp.common.model.cignacmb.request.RuleLogParam;
import com.fibo.ddp.common.model.cignacmb.response.EventLogDetailResponse;
import com.fibo.ddp.common.model.cignacmb.response.RuleLogResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BusinessRuleLogMapper extends BaseMapper<BusinessRuleLog> {

    /**
     * 查询指定行数据
     */
    List<RuleLogResponse> queryAllByLimit(RuleLogParam param);

    /**
     * 根据ids查询规则（事件规则输出详情）
     * @param ruleLogIds
     * @return
     */
    List<EventLogDetailResponse> queryByIds(@Param("ids") List<String> ids);
}
