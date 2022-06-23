package com.fibo.ddp.common.model.strategyx.decisiontree;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@TableName("t_decision_tree_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecisionTreeDetail extends Model<DecisionTreeDetail> {
    //决策树详情id
    @TableId(type = IdType.AUTO)
    private Long id;
    //决策表id
    private Long decisionTreeVersionId;
    //字段id
    private Long fieldId;
    //字段en
    private String fieldEn;
    //父节点id
    private Long parentId;
    //逻辑关系，如（&&，||）
    private String logical;
    //节点类型：1-普通节点，2-叶子节点
    private Integer nodeType;
    //叶子节点对应决策结果值
    private String resultValue;
    //结果类型：1常量 2变量 3自定义
    private Integer variableType;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //决策树详情列表
    @TableField(exist = false)
    private List<DecisionTreeDetailCondition> conditionList;
    @TableField(exist = false)
    private List<DecisionTreeDetail> children;
    @TableField(exist = false)
    private String insertTempId;//插入临时id
    @TableField(exist = false)
    private String tempParentId;//插入临时父id
}
