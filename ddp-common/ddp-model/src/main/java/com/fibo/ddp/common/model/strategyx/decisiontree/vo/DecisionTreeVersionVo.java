package com.fibo.ddp.common.model.strategyx.decisiontree.vo;

import com.fibo.ddp.common.model.strategyx.decisiontree.DecisionTreeVersion;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecisionTreeVersionVo extends DecisionTreeVersion {
    private List<StrategyOutput> strategyOutputList;
}
