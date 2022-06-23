package com.fibo.ddp.common.service.analyse;

import com.fibo.ddp.common.model.analyse.vo.AnalyseData;
import com.fibo.ddp.common.model.analyse.vo.AnalyseRequestParam;

public interface AnalyseService{

    AnalyseData getAnalyseData(AnalyseRequestParam param);
}
