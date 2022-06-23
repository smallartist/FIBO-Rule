package com.fibo.ddp.common.model.strategyx.knowledge.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleSaveParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;  // 代码
    private String name;  // 名称
    private Integer priority;  // 优先级
    private String description;  // 描述
    private String fieldContent;
    private String outcontent;
    private Long engineId;  // 引擎id
    private String content;  // 规则具体内容
    private Integer score;  // 得分
    private String lastLogical;  //逻辑关系符，存储条件区域最后一个逻辑符号，值有')'、'))'、'-1'
    private Integer ruleAudit;  // 审批规则 5 :通过 ，2 : 拒绝，3：人工审批 4：简化流程
    private Long parentId;   // 父节点id
    private String parentIds;
    private String logical;
    private String fieldId;
    private String operator;
//    private String fieldId        ;
//    private String ruleAudit      ;
//    private String fieldId        ;


}
