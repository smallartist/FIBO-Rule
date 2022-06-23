package com.fibo.ddp.datax.realtime.controller.datasource;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.common.requestParam.UpdateStatusParam;
import com.fibo.ddp.common.model.datax.consts.DataXCacheConst;
import com.fibo.ddp.common.model.datax.datasource.MqSource;
import com.fibo.ddp.common.service.datax.cache.DataXChange;
import com.fibo.ddp.common.service.datax.datasource.MqSourceService;
import com.github.pagehelper.PageInfo;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (MqSource)表控制层
 *
 * @author jgp
 * @since 2021-12-20 13:31:51
 */
@RestController
@RequestMapping("/mqSource")
public class MqSourceController {
    /**
     * 服务对象
     */
    @Resource
    private MqSourceService mqSourceService;

    @PostMapping("/getMqSourceInfo/{id}")
    public ResponseEntityDto getInfo(@PathVariable("id") Long id) {
        MqSource result =  mqSourceService.queryById(id);
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
    @PostMapping("/getMqSourceList")
    public ResponseEntityDto getMqSourceList(@RequestBody QueryListParam<MqSource> param) {
        PageInfo result =  mqSourceService.queryList(param);
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
    @PostMapping("/addMqSource")
    @DataXChange(changeName = DataXCacheConst.Type.MQ_SOURCE)
    public ResponseEntityDto addMqSource(@RequestBody MqSource param) {
        MqSource result =  mqSourceService.add(param);
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
    @PostMapping("/updateMqSource")
    @DataXChange(changeName = DataXCacheConst.Type.MQ_SOURCE)
    public ResponseEntityDto updateMqSource(@RequestBody MqSource param) {
        MqSource result =  mqSourceService.update(param);
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
    @PostMapping("/updateStatus")
    @DataXChange(changeName = DataXCacheConst.Type.MQ_SOURCE)
    public ResponseEntityDto updateStatus(@RequestBody UpdateStatusParam param) {
        UpdateStatusParam.checkParam(param);
        boolean updateResult =  mqSourceService.updateStatus(param.getList(),param.getStatus());
        if (updateResult){
            return ResponseEntityBuilder.buildNormalResponse(updateResult);
        }else {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.DECISION_TABLES_UPDATE_ERROR);
        }
    }
}
