package com.fibo.ddp.common.model.strategyx.guiderule.vo;

import com.fibo.ddp.common.model.strategyx.guiderule.RuleInfo;
import com.fibo.ddp.common.model.strategyx.scriptrule.RuleScriptVersion;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors
public class RuleVo extends RuleInfo {

    private RuleConditionVo ruleConditionVo;//规则对应的结点树

    private List<StrategyOutput> strategyOutputList;//输出字段

    private List<RuleVersionVo> ruleVersionList;//规则版本列表
//    private List<T> ruleVersionList;//规则版本列表

    private List<RuleScriptVersion> ruleScriptVersionList;//脚本规则集的版本列表
}
