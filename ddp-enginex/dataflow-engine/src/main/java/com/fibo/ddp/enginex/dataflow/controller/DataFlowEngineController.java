package com.fibo.ddp.enginex.dataflow.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.common.requestParam.UpdateStatusParam;
import com.fibo.ddp.common.model.enginex.risk.Engine;
import com.fibo.ddp.common.service.enginex.dataflow.EngineServiceV3;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import com.fibo.ddp.common.utils.util.SnowFlakUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Engine)表控制层
 *
 * @author jgp
 * @since 2021-12-22 13:28:09
 */
@RestController
@RequestMapping("/v3/engine")
@Slf4j
public class DataFlowEngineController {
    /**
     * 服务对象
     */
    @Resource
    private EngineServiceV3 engineServiceV3;


    @PostMapping(value = "/getEngineList")
    public ResponseEntityDto getList(@RequestBody QueryListParam<Engine> param) {
        PageInfo pageInfo = engineServiceV3.queryList(param);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }

    /**
     * 生成一个雪花id作为引擎的code
     *
     * @return
     */
    @PostMapping(value = "/getEngineCode")
    public ResponseEntityDto getEngineCode() {
        return ResponseEntityBuilder.buildNormalResponse(SnowFlakUtil.snowflakeIdStr());
    }

    @PostMapping(value = "/addEngine")
    @ArchivesLog(operationType = OpTypeConst.SAVE_ENGINE)
    @ResponseBody
    public ResponseEntityDto addEngine(@RequestBody Engine engine) {

        Engine result = engineServiceV3.addEngine(engine);
        if (result==null){
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),"新增引擎失败");
        }
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
    @PostMapping(value = "/updateEngine")
    @ArchivesLog(operationType = OpTypeConst.UPDATE_ENGINE)
    @ResponseBody
    public ResponseEntityDto updateEngine(@RequestBody Engine engine) {
        Engine result = engineServiceV3.updateEngine(engine);
        if (result==null){
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),"修改引擎失败");
        }
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
    @PostMapping(value = "/updateStatus")
    public ResponseEntityDto updateStatus(@RequestBody UpdateStatusParam param) {
        UpdateStatusParam.checkParam(param);
        boolean result = engineServiceV3.updateStatus(param.getList(), param.getStatus());
        if (!result) {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "修改状态失败");
        }
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
}
