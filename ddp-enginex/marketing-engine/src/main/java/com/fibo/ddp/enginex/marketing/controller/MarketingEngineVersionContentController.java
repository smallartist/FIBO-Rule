package com.fibo.ddp.enginex.marketing.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.enginex.dataflow.EngineVersionContent;
import com.fibo.ddp.common.model.enginex.dataflow.vo.EngineVersionContentVo;
import com.fibo.ddp.common.service.enginex.dataflow.EngineVersionContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engineVersionContent/marketing")
@Slf4j
public class MarketingEngineVersionContentController {

    @Autowired
    private EngineVersionContentService engineVersionContentService;

    @PostMapping("/getEngineVersionContentInfo")
    public ResponseEntityDto getEngineVersionContentInfo(@RequestBody EngineVersionContent param ){
        EngineVersionContentVo result = engineVersionContentService.queryById(param.getEngineVersionId());
        return ResponseEntityBuilder.buildNormalResponse(result);
    }

    @PostMapping("/updateEngineVersionContentInfo")
    public ResponseEntityDto updateEngineVersionContentInfo(@RequestBody EngineVersionContentVo param){
        boolean result = engineVersionContentService.updateVersionContent(param);
        if (result){
            return ResponseEntityBuilder.buildNormalResponse(result);
        }
        return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),"修改引擎内容失败");
    }
}
