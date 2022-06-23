package com.fibo.ddp.common.model.cignacmb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 业务类型与规则关联表
 * </p>
 *
 * @author oldRose
 * @since 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TBusinessRuleRel对象", description="业务类型与规则关联表")
public class TBusinessRuleRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
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

    @ApiModelProperty(value = "状态 0：无效，1：有效")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "修改人")
    private Long modifier;

    @ApiModelProperty(value = "企业编号")
    private Long organId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
