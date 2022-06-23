package com.fibo.ddp.datax.realtime.controller.datamanage;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.datax.datamanage.FieldCall;
import com.fibo.ddp.common.model.datax.datamanage.request.FieldCallParam;
import com.fibo.ddp.common.service.datax.datamanage.FieldCallLogService;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/DataX/statistics")
public class FieldCallController {
    @Autowired
    private FieldCallLogService fieldCallLogService;

    @PostMapping("/getFieldCallList")
    public ResponseEntityDto getFieldCallList(@RequestBody QueryListParam<FieldCallParam> param){
        PageInfo pageInfo = fieldCallLogService.queryFieldCallList(param);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }
    @PostMapping("/getFieldCallLogList")
    public ResponseEntityDto getFieldCallLogList(@RequestBody QueryListParam<FieldCallParam> param){
        PageInfo pageInfo = fieldCallLogService.queryFieldCallLogList(param);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }
    @PostMapping("/getFieldCallCountList")
    public ResponseEntityDto getFieldCallLogList(@RequestBody FieldCallParam param){
        List<FieldCall> fieldCalls = fieldCallLogService.queryFieldCallCountList(param);
        return ResponseEntityBuilder.buildNormalResponse(fieldCalls);
    }
}
