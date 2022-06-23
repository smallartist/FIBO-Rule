package com.fibo.ddp.common.model.strategyx.scorecard.vo;

import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardVersion;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScorecardVersionVo extends ScorecardVersion implements Serializable {
    private static final long serialVersionUID = -213821397529963731L;

    private List<ScorecardDimensionVo> scorecardDimension;  // 维度
    private List<StrategyOutput> strategyOutputList;//自定义输出字段
}
