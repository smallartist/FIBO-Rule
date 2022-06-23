package com.fibo.ddp.common.model.strategyx.decisiontree.vo;

import com.fibo.ddp.common.model.strategyx.decisiontree.DecisionTree;
import lombok.Data;

@Data
public class DecisionTreeVo extends DecisionTree {
    private DecisionTreeVersionVo executeVersion;
}
