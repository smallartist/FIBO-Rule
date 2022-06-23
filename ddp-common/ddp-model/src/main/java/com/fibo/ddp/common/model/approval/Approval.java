package com.fibo.ddp.common.model.approval;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (Approval)实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_approval")
public class Approval implements Serializable {
    private static final long serialVersionUID = 132808868864524091L;
    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    private String applyType;
    /**
     * 创建人id
     */
    private Long createUserId;
    /**
     * 创建人名称
     */
    private String createUserName;
    /**
     * 修改人id
     */
    private Long updateUserId;
    /**
     * 修改人名称
     */
    private String updateUserName;
    /**
     * 组织id
     */
    private Long organId;
    /**
     * 申请单的状态：（-1取消申请。 0 待审批，1 审批通过，2 审批不通过）
     */
    private Integer applyStatus;
    /**
     * 状态：0冻结 -1 删除 1正常
     */
    private Integer status;

    /**
     * 审批人id
     */
    private Long approvalUserId;
    /**
     * 审批人名称
     */
    private String approvalUserName;
    /**
     * 审批时间
     */
    private Date approvalTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 申请详情
     */
    private String applyDetail;
    /**
     * 申请描述
     */
    private String applyDesc;

    @TableField(exist = false)
    private Date queryStartTime;
    @TableField(exist = false)
    private Date queryEndTime;
}
