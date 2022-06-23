package com.fibo.ddp.common.service.analyse;

import com.fibo.ddp.common.model.analyse.vo.AnalyseRequestParam;

import java.util.List;


public interface AnalyseCommonService<T> {

    List<T> getAnalyseData(AnalyseRequestParam param);
}
