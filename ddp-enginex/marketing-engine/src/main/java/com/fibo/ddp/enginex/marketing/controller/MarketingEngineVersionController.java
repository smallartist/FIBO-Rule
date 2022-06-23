package com.fibo.ddp.enginex.marketing.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import com.fibo.ddp.common.service.enginex.dataflow.EngineVersionServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/engineVersion/marketing")
@Slf4j
public class MarketingEngineVersionController {

    @Autowired
    private EngineVersionServiceV3 engineVersionServiceV3;

    @PostMapping("/getEngineVersionList")
    public ResponseEntityDto getEngineVersionList(@RequestBody EngineVersion param){
        List<EngineVersion> result = engineVersionServiceV3.queryByEngineId(param.getEngineId());
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
}
