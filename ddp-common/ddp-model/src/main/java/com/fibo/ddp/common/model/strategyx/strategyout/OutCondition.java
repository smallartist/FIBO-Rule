package com.fibo.ddp.common.model.strategyx.strategyout;

import com.fibo.ddp.common.model.enginex.runner.ExpressionParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutCondition {
    private String logical;
    private List<ExpressionParam> conditionList;
}
