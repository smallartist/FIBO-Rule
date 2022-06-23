package com.fibo.ddp.common.model.cignacmb;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 事件调用记录(BusinessEventLog)实体类
 */
@Data
@TableName("t_business_event_log")
public class BusinessEventLog implements Serializable {
    private static final long serialVersionUID = 766848314346971098L;
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
     * 业务类型编码
     */
    private String businessCode;
    /**
     * 业务子类型名称
     */
    private String businessChildName;
    /**
     * 业务子类型编码
     */
    private String businessChildCode;
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
     * 规则执行结束时间
     */
    private Date callEndTime;
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
    /**
     * 企业编号
     */
    private Integer organId;
    /**
     * 创建时间
     */
    private Date createTime;
}

