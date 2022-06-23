package com.fibo.ddp.common.model.strategyx.decisiontable.vo;

import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTablesDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors
public class DecisionTablesDetailVo extends DecisionTablesDetail implements Serializable {

    private static final long serialVersionUID = -8530979508995118093L;
//    private Long userId;//详情节点id
//
//    private Long decisionTablesId;//决策表id
//
//    private Integer dimensionality;//条件维度(1-左侧，2-顶部)
//
//    private Long fieldId;//字段id
//
//    private String fieldEn;//字段en
//
//    private Long parentId;//父节点id
//
//    private String logical;//逻辑关系，如（&&，||）
//
//    private Integer type;//节点类型：1-普通节点，2-叶子节点
//
//    private Integer indexValue;//所在维度的值，不能为负数
//
//    private String content;
//    private Date createTime;//创建时间
//
//    private Date updateTime;//修改时间
//    private Integer valueType;//值类型
//    private String insertTempId;
//    private String tempParentId;
//
//    private List<DecisionTablesDetailCondition> conditionList;//条件列表

    private List<DecisionTablesDetailVo> children;//子节点
}
