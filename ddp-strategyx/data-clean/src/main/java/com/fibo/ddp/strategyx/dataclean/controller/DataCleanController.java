package com.fibo.ddp.strategyx.dataclean.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.common.requestParam.UpdateFolderParam;
import com.fibo.ddp.common.model.common.requestParam.UpdateStatusParam;
import com.fibo.ddp.common.model.strategyx.dataclean.DataClean;
import com.fibo.ddp.common.service.strategyx.dataclean.DataCleanService;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.ResponseUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


@RestController
@RequestMapping("/dataClean")
public class DataCleanController {
    /**
     * 服务对象
     */
    @Resource
    private DataCleanService dataCleanService;

    @ResponseBody
    @RequestMapping(value = "/getDataClean/{id}", method = RequestMethod.POST)
    public ResponseEntityDto<DataClean> getDataClean(@PathVariable Long id) {
        if (id == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        DataClean dataClean = dataCleanService.queryById(id);
        ResponseEntityDto<DataClean> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(dataClean);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "getDataCleanList", method = RequestMethod.POST)
    public ResponseEntityDto<DataClean> getDataCleanList(@RequestBody QueryListParam<DataClean> listParam) {
        PageInfo pageInfo = dataCleanService.queryByEntity(listParam);
        Map<String, Object> responseMap = ResponseUtil.getResponseMap(pageInfo);
        ResponseEntityDto ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(responseMap);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "addDataClean", method = RequestMethod.POST)
    public ResponseEntityDto<DataClean> addDataClean(@RequestBody DataClean dataClean) {
        DataClean insert = dataCleanService.insert(dataClean);
        ResponseEntityDto<DataClean> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(insert);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "updateDataClean", method = RequestMethod.POST)
    public ResponseEntityDto<DataClean> updateDataClean(@RequestBody DataClean dataClean) {
        DataClean update = dataCleanService.update(dataClean);
        ResponseEntityDto<DataClean> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(update);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "updateDataCleanStatus", method = RequestMethod.POST)
    public ResponseEntityDto updateStatus(@RequestBody UpdateStatusParam param) {
        UpdateStatusParam.checkParam(param);
        boolean updateResult = dataCleanService.updateStatus(param.getList(), param.getStatus());
        if (updateResult) {
            return ResponseEntityBuilder.buildNormalResponse(updateResult);
        } else {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.DECISION_TREE_UPDATE_ERROR);
        }

    }

    @ResponseBody
    @RequestMapping(value = "updateDataCleanFolder", method = RequestMethod.POST)
    public ResponseEntityDto updateFolder(@RequestBody UpdateFolderParam param) {
        UpdateFolderParam.checkNotNull(param);
        boolean updateResult = dataCleanService.updateFolder(param.getIds(), param.getFolderId());
        if (updateResult) {
            return ResponseEntityBuilder.buildNormalResponse(updateResult);
        } else {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
        }
    }
}
