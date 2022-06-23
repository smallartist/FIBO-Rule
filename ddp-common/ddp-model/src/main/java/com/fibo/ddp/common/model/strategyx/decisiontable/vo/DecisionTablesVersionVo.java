package com.fibo.ddp.common.model.strategyx.decisiontable.vo;

import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTablesVersion;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecisionTablesVersionVo extends DecisionTablesVersion {
    private List<DecisionTablesDetailVo> leftDetailVo;//左侧决策表详情

    private List<DecisionTablesDetailVo> topDetailVo;//顶部决策表详情

    private DecisionTablesResultVo resultSet;//结果集（二维数组表）
    private List<StrategyOutput> strategyOutputList;//输出字段
}
