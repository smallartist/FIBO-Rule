package com.fibo.ddp.common.model.strategyx.baserule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fibo.ddp.common.model.strategyx.guiderule.RuleLoopGroupAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * (BaseRuleCondition)实体类
 *
 * @author jgp
 * @since 2021-12-27 09:46:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_base_rule_condition")
public class BaseRuleCondition implements Serializable {

    private static final long serialVersionUID = -29775390870188271L;
       
    /**
    * 自增主键
    */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.INPUT)
    private Long id;
   
    /**
    * 规则id
    */
    private Long ruleId;
   
    /**
    * 规则节点的类型：1-关系节点，2-表达式节点 3-for表达式 4-for的结果项 5条件组 6条件组的结果条件
    */
    private Integer condType;
   
    /**
    * 逻辑符号：leaf、&&、||、for、condGroup
    */
    private String logic;
   
    /**
    * 条件左边值的类型
    */
    private Integer leftType;
   
    /**
    * 左边条件引用的指标id
    */
    private Long leftId;
   
    /**
    * 左边的值
    */
    private String leftValue;
   
    /**
    * 操作符
    */
    private String operator;
   
    /**
    * 右边值类型
    */
    private Integer rightType;
   
    /**
    * 右边值引用的指标id
    */
    private Long rightId;
   
    /**
    * 右边的值
    */
    private String rightValue;
   
    /**
    * 父id
    */
    private Long parentId;

    @TableField(exist = false)
    //子条件
    private List<BaseRuleCondition> children;

    @TableField(exist = false)
    private List<RuleLoopGroupAction> loopGroupActions = new ArrayList<>();//循环组对应的条件

    @TableField(exist = false)
    private BaseRuleCondition loopResultCondition;//for对应的结果条件的计算条件树
    @TableField(exist = false)
    private BaseRuleCondition condGroupResultCondition;//条件组对应的结果计算条件树
}
