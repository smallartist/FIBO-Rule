package com.fibo.ddp.strategyx.dataclean.execute.business;

import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.strategyx.dataclean.param.RunnerDataCleanParam;

public interface DataCleanBusiness {

    JSONObject runner(RunnerDataCleanParam param);
}
