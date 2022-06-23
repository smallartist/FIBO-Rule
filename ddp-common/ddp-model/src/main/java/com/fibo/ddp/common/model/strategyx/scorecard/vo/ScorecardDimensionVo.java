package com.fibo.ddp.common.model.strategyx.scorecard.vo;

import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardDimension;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScorecardDimensionVo extends ScorecardDimension implements Serializable {
    private static final long serialVersionUID = 4920595561931964004L;

    private List<ScorecardDetailVo> children;  // 明细
}
