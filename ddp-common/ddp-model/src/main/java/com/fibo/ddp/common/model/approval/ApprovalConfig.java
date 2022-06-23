package com.fibo.ddp.common.model.approval;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (ApprovalConfig)实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_approval_config")
public class ApprovalConfig implements Serializable {
    private static final long serialVersionUID = -66040749788313604L;
    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 审批类型
     */
    private String approvalType;
    /**
     * 审批名称
     */
    private String approvalName;
    /**
     * 描述
     */
    private String approvalDesc;
    /**
     * 审批状态（1开启，0关闭，-1删除）
     */
    private Integer approvalStatus;
    /**
     * 创建人id
     */
    private Long createUserId;
    /**
     * 创建人id
     */
    private Long organId;
    /**
     * 修改人id
     */
    private Long updateUserId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

}
