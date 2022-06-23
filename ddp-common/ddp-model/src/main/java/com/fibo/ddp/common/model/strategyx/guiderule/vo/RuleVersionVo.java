package com.fibo.ddp.common.model.strategyx.guiderule.vo;

import com.fibo.ddp.common.model.strategyx.guiderule.RuleVersion;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleVersionVo extends RuleVersion {
    private RuleConditionVo ruleConditionVo;//规则对应的结点树

    private List<StrategyOutput> strategyOutputList;//输出字段

    private List<StrategyOutput> failOutputList;//失败输出字段
}
