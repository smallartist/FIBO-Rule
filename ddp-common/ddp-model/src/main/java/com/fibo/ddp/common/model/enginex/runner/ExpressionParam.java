package com.fibo.ddp.common.model.enginex.runner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//表达式的参数实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpressionParam {
    private String fieldEn;//表达式中key字段en
    private String operator;//表达式的操作符
    private Integer variableType;//表达式中value类型，1常量 2变量,3自定义
    private String fieldValue;//表达式中对应常量value值或者变量key
    private String executionLogic;//执行逻辑
    private Integer conditionType;//规则节点的类型：1-关系节点，2-表达式节点
}
