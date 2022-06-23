package com.fibo.ddp.common.model.strategyx.collectionrule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_list_operation_condition")
public class ListOperationCondition implements Serializable {
    private static final long serialVersionUID = 763036874769503399L;
    /**
    * 主键ID
    */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
    * 列表操作的id
    */
    private Long listOpId;
    /**
    * 列表操作版本的id
    */
    private Long listOpVersionId;
    /**
     * 列表操作块id
     */
    private Long listOpBlockId;
    /**
    * 关系节点的逻辑符号：&&（并关系），||（或关系）
    */
    private String logical;
    /**
    * 计算维度：1 count 2count(去重)  3 max  4min  5avg 
    */
    private String opType;
    /**
    * 计算的参数key
    */
    private String opKey;
    /**
    * 表达式节点的操作符
    */
    private String operator;
    /**
    * 变量类型，1常量，2变量
    */
    private Integer variableType;
    /**
    * 表达式节点对应字段的限定值
    */
    private String variableValue;
    /**
    * 父节点的id 此项为0的是根节点
    */
    private Long parentId;
    /**
    * 规则节点的类型：1-关系节点，2-表达式节点
    */
    private Integer conditionType;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 修改时间
    */
    private Date updateTime;
    /**
     * 子条件列表
     */
    @TableField(exist = false)
    private List<ListOperationCondition> children;
//    @TableField(exist = false)
//    private String insertTempId;//插入临时id
//
//    @TableField(exist = false)
//    private String tempParentId;//插入临时父id
}
