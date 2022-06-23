package com.fibo.ddp.common.model.strategyx.baserule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (BaseRule)实体类
 *
 * @author jgp
 * @since 2021-12-27 09:46:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_base_rule")
public class BaseRule implements Serializable {

    private static final long serialVersionUID = -12797674823526324L;
       
    /**
    * 主键id
    */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.INPUT)
    private Long id;
   
    /**
    * 规则类型：
    */
    private String ruleType;
   
    /**
    * 组织id
    */
    private Long organId;
   
    /**
    * 创建人id
    */
    private Long createUserId;
   
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
    @TableField(exist = false)
    private BaseRuleCondition conditionTree;
}
