package com.fibo.ddp.common.model.strategyx.collectionrule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConditionAndOutPut {

    //过滤条件
    private ListOperationFilterCondition filterCondition;
    //条件
    private ListOperationCondition condition;
    //块信息
    private ListOperationBlock listOperationBlock;
    //条件成功时的输出
    private List<ListOperationOutput> successOutput = new ArrayList<>();
    private List<ListOperationOutput> defaultOutput= new ArrayList<>();
    //条件执行失败时候的输出
    private List<ListOperationOutput> failOutput= new ArrayList<>();
}
