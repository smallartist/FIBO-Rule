package com.fibo.ddp.enginex.dataflow.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import com.fibo.ddp.common.service.enginex.dataflow.EngineVersionServiceV3;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v3/engineVersion")
@Slf4j
public class DataFlowEngineVersionController {

    @Autowired
    private EngineVersionServiceV3 engineVersionServiceV3;

    @PostMapping("/getEngineVersionPage")
    public ResponseEntityDto getEngineVersionPage(@RequestBody QueryListParam<EngineVersion> param){
        PageInfo pageInfo = engineVersionServiceV3.queryList(param);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }
    @PostMapping("/getEngineVersionList")
    public ResponseEntityDto getEngineVersionList(@RequestBody EngineVersion param){
        List<EngineVersion> result = engineVersionServiceV3.queryByEngineId(param.getEngineId());
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
    @PostMapping("/addEngineVersion")
    public ResponseEntityDto addEngineVersion(@RequestBody EngineVersion param){
        boolean result  = engineVersionServiceV3.addEngineVersion(param);
        if (result){
            return ResponseEntityBuilder.buildNormalResponse(result);
        }
        return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),"新增版本失败");
    }
    @PostMapping("/copyEngineVersion")
    public ResponseEntityDto copyEngineVersion(@RequestBody EngineVersion param){
        boolean result  = engineVersionServiceV3.copyEngineVersion(param.getVersionId());
        if (result){
            return ResponseEntityBuilder.buildNormalResponse(result);
        }
        return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),"新增版本失败");
    }
}
