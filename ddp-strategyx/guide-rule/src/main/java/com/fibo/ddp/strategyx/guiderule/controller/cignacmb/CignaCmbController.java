package com.fibo.ddp.strategyx.guiderule.controller.cignacmb;

import com.fibo.ddp.common.model.cignacmb.request.EventLogDetailParam;
import com.fibo.ddp.common.model.cignacmb.request.EventLogParam;
import com.fibo.ddp.common.model.cignacmb.request.RuleLogParam;
import com.fibo.ddp.common.model.cignacmb.response.EventLogDetailResponse;
import com.fibo.ddp.common.model.cignacmb.response.EventLogResponse;
import com.fibo.ddp.common.model.cignacmb.response.RuleLogResponse;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.service.cignacmb.BusinessEventLogService;
import com.fibo.ddp.common.service.cignacmb.BusinessRuleLogService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 招商信诺报表接口
 */

@RestController
@RequestMapping("/cignacmb")
public class CignaCmbController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BusinessRuleLogService ruleLogService;
    @Autowired
    private BusinessEventLogService eventLogService;

    /**
     * 规则报表查询
     * @param param
     * @return
     */
    @RequestMapping(value = "getRuleLogList", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getRuleLogList(@RequestBody RuleLogParam param){
        // “规则编号、规则名称、规则执行时间段” 不能同时为空
        if(StringUtils.isBlank(param.getRuleCode()) && StringUtils.isBlank(param.getRuleName())
                && (param.getStartTime() == null || param.getEndTime() == null)){
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION);
        }
        PageInfo<RuleLogResponse> pageInfo = ruleLogService.getRuleLogList(param);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }

    /**
     * 事件报表查询
     * @param param
     * @return
     */
    @RequestMapping(value = "getEventLogList", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getEventLogList(@RequestBody EventLogParam param){
        // “事件流水号、执行时间段”不能同时为空
        if(StringUtils.isBlank(param.getEventRequestId())
                && (param.getStartTime() == null || param.getEndTime() == null)){
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION);
        }
        PageInfo<EventLogResponse> pageInfo = eventLogService.getEventLogList(param);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }

    /**
     * 事件对应规则输出详情
     * @param param
     * @return
     */
    @RequestMapping(value = "getEventLogDetails", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getEventLogDetails(@RequestBody EventLogDetailParam param){
        String ruleLogIds = param.getRuleLogIds();
        if(StringUtils.isBlank(ruleLogIds)){
            return ResponseEntityBuilder.buildNormalResponse();
        }
        List<String> ids = Arrays.asList(ruleLogIds.split(","));
        List<EventLogDetailResponse> eventLogDetailResponses = ruleLogService.queryByIds(ids);
        return ResponseEntityBuilder.buildNormalResponse(eventLogDetailResponses);
    }

}
