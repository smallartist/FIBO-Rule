package com.fibo.ddp.common.model.strategyx.dataclean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataCleanConditionAndOutPut {

    //选择集合时的过滤条件
    private DataCleanFilterCondition inputFilterCondition;
    //输出结果过滤条件
    private DataCleanFilterCondition resultFilterCondition;
    //条件
    private DataCleanCondition condition;
    //块信息
    private DataCleanBlock dataCleanBlock;
    //条件成功时的输出
    private List<DataCleanOutput> successOutput = new ArrayList<>();
    private List<DataCleanOutput> defaultOutput= new ArrayList<>();
    //条件执行失败时候的输出
    private List<DataCleanOutput> failOutput= new ArrayList<>();
}
