package com.fibo.ddp.enginex.persons.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.enginex.personas.PersonasEngineResult;
import com.fibo.ddp.common.model.enginex.personas.PersonasEngineResultDetail;
import com.fibo.ddp.common.model.enginex.personas.vo.PersonasReport;
import com.fibo.ddp.common.model.enginex.personas.vo.PersonasReportParam;
import com.fibo.ddp.common.service.enginex.personas.PersonasEngineResultDetailService;
import com.fibo.ddp.common.service.enginex.personas.PersonasEngineResultService;
import com.fibo.ddp.common.utils.util.ResponseUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v3/engineResult/personas")
public class PersonasEngineResultController {
    @Autowired
    private PersonasEngineResultService personasEngineResultService;
    @Autowired
    private PersonasEngineResultDetailService personasEngineResultDetailService;

    @PostMapping("/getResultList")
    public ResponseEntityDto getResultList(QueryListParam<PersonasEngineResult> param){
        PageInfo<PersonasEngineResult> resultPageInfo = personasEngineResultService.queryByEntity(param);
        Map<String, Object> responseMap = ResponseUtil.getResponseMap(resultPageInfo);
        return ResponseEntityBuilder.buildNormalResponse(responseMap);
    }
    @PostMapping("/getResultDetailList")
    public ResponseEntityDto getResultDetailList(QueryListParam<PersonasEngineResultDetail> param){
        PageInfo<PersonasEngineResultDetail> resultPageInfo = personasEngineResultDetailService.queryByEntity(param);
        Map<String, Object> responseMap = ResponseUtil.getResponseMap(resultPageInfo);
        return ResponseEntityBuilder.buildNormalResponse(responseMap);
    }
    @PostMapping("/getReportList")
    public ResponseEntityDto getReportList(QueryListParam<PersonasReportParam> param){
        Map map = personasEngineResultDetailService.queryReportList(param);
        return ResponseEntityBuilder.buildNormalResponse(map);
    }
}
