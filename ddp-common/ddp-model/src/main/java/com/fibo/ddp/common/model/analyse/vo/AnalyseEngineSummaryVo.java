package com.fibo.ddp.common.model.analyse.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@Data
@NoArgsConstructor
public class AnalyseEngineSummaryVo {
    //昨天数据
    private Long yesterdayData;
    //今天数据
    private Long todayData;
    //今天预测数据
    private Long predictData;
    //增长率
    private Float growthRate;

    public AnalyseEngineSummaryVo( long todayData, long lastData,long yesterdayData) {
        this.yesterdayData = yesterdayData;
        this.todayData = todayData;
        if (lastData!=0L){
            DecimalFormat df = new DecimalFormat("0.00");
            this.growthRate = Float.valueOf(df.format((float)todayData/lastData));
            this.predictData = new Float(yesterdayData*growthRate).longValue();
        }else {
            this.growthRate = Float.valueOf(0);
            this.predictData = 0L;
        }

    }
}
