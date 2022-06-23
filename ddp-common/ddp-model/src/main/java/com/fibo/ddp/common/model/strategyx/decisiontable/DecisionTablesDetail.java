package com.fibo.ddp.common.model.strategyx.decisiontable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (DecisionTablesDetail)实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors
@TableName(value = "t_decision_tables_detail")
public class DecisionTablesDetail implements Serializable {

    private static final long serialVersionUID = 6082556987380982292L;
    @TableId(type = IdType.AUTO)
    private Long id;//详情节点id

    private Long decisionTablesId;//决策表id

    private Long versionId;//决策表版本id

    private Integer dimensionality;//条件维度(1-左侧，2-顶部)

    private Long fieldId;//字段id

    private String fieldEn;//字段en

    private Long parentId;//父节点id

    private String logical;//逻辑关系，如（&&，||）

    private Integer type;//节点类型：1-普通节点，2-叶子节点

    private Integer indexValue;//所在维度的值，不能为负数

    private String content;//执行串

    private Date createTime;//创建时间

    private Date updateTime;//修改时间
    @TableField(exist = false)
    private Integer valueType;//值类型
    @TableField(exist = false)
    private String insertTempId;//插入临时id

    @TableField(exist = false)
    private String tempParentId;//插入临时父id

    @TableField(exist = false)
    private List<DecisionTablesDetailCondition> conditionList;//条件列表
}
