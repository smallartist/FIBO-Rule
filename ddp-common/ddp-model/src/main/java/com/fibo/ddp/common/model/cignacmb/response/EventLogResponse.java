package com.fibo.ddp.common.model.cignacmb.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLogResponse {
    /**
     * 主键
     */
    private Long id;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 事件流水id
     */
    private String eventRequestId;
    /**
     * 事件ID
     */
    private String eventId;
    /**
     * 事件名称
     */
    private String eventName;
    /**
     * 业务类型名称
     */
    private String businessName;
    /**
     * 业务子类型名称
     */
    private String businessChildName;
    /**
     * 模板ID
     */
    private String templateId;
    /**
     * 模板名称
     */
    private String templateName;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 客户手机号
     */
    private String customerMobile;
    /**
     * 保单号
     */
    private String policyNo;
    /**
     * 发送平台
     */
    private String sendPlatform;
    /**
     * 规则执行开始时间
     */
    private Date callStartTime;
    /**
     * 耗时（批次耗时），单位毫秒
     */
    private Integer callTime;
    /**
     * 执行结果 1:成功，0:失败
     */
    private Integer callStatus;
    /**
     * 规则执行记录表ids
     */
    private String ruleLogIds;
}
