package com.fibo.ddp.common.model.analyse.vo;

import com.fibo.ddp.common.model.analyse.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyseData {
    private List<AnalyseEngineCall> callCountList;
    private List<List<AnalyseRule>> ruleList;
    private List<List<List<AnalyseScorecard>>> scorecardList;
    private List<List<List<AnalyseDecisionTables>>> decisionTablesList;
    private List<List<AnalyseDecisionResult>> decisionResultList;
    private List<List<AnalyseEngineNode>> engineNodeList;
    private Map<String,AnalyseEngineSummaryVo> engineSummary;
}
