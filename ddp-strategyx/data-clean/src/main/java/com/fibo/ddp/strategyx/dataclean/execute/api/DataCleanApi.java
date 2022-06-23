package com.fibo.ddp.strategyx.dataclean.execute.api;

import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.strategyx.dataclean.param.RunnerDataCleanParam;
import com.fibo.ddp.common.service.common.runner.RunnerSessionManager;
import com.fibo.ddp.common.service.common.runner.SessionData;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.strategyx.dataclean.execute.business.DataCleanBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dataCleanApi")
@Slf4j
public class DataCleanApi {

    @Resource
    private DataCleanBusiness dataCleanBusiness;

    @PostMapping("/runner")
    public Object runnerListOp(@RequestBody RunnerDataCleanParam param){
        long start = System.currentTimeMillis();
        SessionData sessionData = new SessionData();
        sessionData.setOrganId(param.getOrganId());
        RunnerSessionManager.setSession(sessionData);
        JSONObject result = new JSONObject();
        try {
            JSONObject jsonObject = dataCleanBusiness.runner(param);
            result.put("status", ErrorCodeEnum.SUCCESS.getCode());
            result.put("msg", ErrorCodeEnum.SUCCESS.getMessage());
            result.put("data", jsonObject);
            long end = System.currentTimeMillis();
            log.info("=========数据清洗接口调用成功，耗时：{}ms", (end - start));
        } catch (ApiException apiException) {
            result.put("status", apiException.errCode);
            result.put("msg", apiException.message);
            result.put("data", apiException.data);
            log.error("数据清洗接口发生业务异常，errCode: {}, message: {}", apiException.errCode, apiException.message);
        } catch (Exception e) {
            result.put("status", ErrorCodeEnum.SERVER_ERROR.getCode());
            result.put("msg", ErrorCodeEnum.SERVER_ERROR.getMessage());
            result.put("data", null);
            log.error("数据清洗接口发生运行时异常", e);
        }
        return result;
    }
}
