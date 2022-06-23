package com.fibo.ddp.common.model.strategyx.scorecard.vo;

import com.fibo.ddp.common.model.strategyx.scorecard.Scorecard;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScorecardVo extends Scorecard implements Serializable {
    private static final long serialVersionUID = 4920595561931964004L;

    private List<ScorecardDimensionVo> scorecardDimension;  // 维度
    private List<StrategyOutput> strategyOutputList;//自定义输出字段
    private List<ScorecardVersionVo> versionList;//版本列表
    private List<Long> versionIdList;//版本id列表
    private Long checkedId;
}
