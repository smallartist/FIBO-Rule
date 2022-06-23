package com.fibo.ddp.common.model.cignacmb.req;

import com.alibaba.fastjson.JSONArray;
import com.fibo.ddp.common.model.common.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by niuge on 2021/11/10.
 */
@Data
public class BusinessRuleRelReq extends BaseParam {
    //业务类型和规则关系
    private Long id;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务子类型名称")
    private String businessChildName;

    @ApiModelProperty(value = "业务子类型编码")
    private String businessChildCode;

    @ApiModelProperty(value = "发送方式：自动/手动")
    private String sendType;

    @ApiModelProperty(value = "是否取消订阅")
    private String isUnsubscribe;

    @ApiModelProperty(value = "事件类型：通知/待办/系统类")
    private String eventType;

    @ApiModelProperty(value = "待办任务")
    private String backlog;

    @ApiModelProperty(value = "是否需人工干预")
    private String isManualIntervention;

    @ApiModelProperty(value = "5大类规则信息")
    private String ruleInfo;
    /**
     * {"blacklist":[1,2],"threshold":[5,6],"mutex":[7,8],"merge":[10,20],"intercept":[30,40]}
     */
    /**
     * 黑名单
     */
    private JSONArray blacklist;
    /**
     * 阈值规则
     */
    private JSONArray threshold;
    /**
     * 互斥规则
     */
    private JSONArray mutex;
    /**
     * 合并规则
     */
    private JSONArray merge;
    /**
     * 拦截规则
     */
    private JSONArray intercept;

}
