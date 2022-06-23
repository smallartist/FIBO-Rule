package com.fibo.ddp.strategyx.collectionrule.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.common.requestParam.UpdateFolderParam;
import com.fibo.ddp.common.model.common.requestParam.UpdateStatusParam;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperation;
import com.fibo.ddp.common.service.strategyx.collectionrule.ListOperationService;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.ResponseUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


@RestController
@RequestMapping("/v3/listOperation")
public class ListOperationController {
    /**
     * 服务对象
     */
    @Resource
    private ListOperationService listOperationService;

    @ResponseBody
    @RequestMapping(value = "/getListOperation/{id}", method = RequestMethod.POST)
    public ResponseEntityDto<ListOperation> getListOperation(@PathVariable Long id) {
        if (id == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        ListOperation listOperation = listOperationService.queryById(id);
        ResponseEntityDto<ListOperation> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(listOperation);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "getListOperationList", method = RequestMethod.POST)
    public ResponseEntityDto<ListOperation> getListOperationList(@RequestBody QueryListParam<ListOperation> listParam) {
        PageInfo pageInfo = listOperationService.queryByEntity(listParam);
        Map<String, Object> responseMap = ResponseUtil.getResponseMap(pageInfo);
        ResponseEntityDto ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(responseMap);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "addListOperation", method = RequestMethod.POST)
    public ResponseEntityDto<ListOperation> addListOperation(@RequestBody ListOperation listOperation) {
        ListOperation insert = listOperationService.insert(listOperation);
        ResponseEntityDto<ListOperation> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(insert);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "updateListOperation", method = RequestMethod.POST)
    public ResponseEntityDto<ListOperation> updateListOperation(@RequestBody ListOperation listOperation) {
        ListOperation update = listOperationService.update(listOperation);
        ResponseEntityDto<ListOperation> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(update);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "updateListOperationStatus", method = RequestMethod.POST)
    public ResponseEntityDto updateStatus(@RequestBody UpdateStatusParam param) {
        UpdateStatusParam.checkParam(param);
        boolean updateResult = listOperationService.updateStatus(param.getList(), param.getStatus());
        if (updateResult) {
            return ResponseEntityBuilder.buildNormalResponse(updateResult);
        } else {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.DECISION_TREE_UPDATE_ERROR);
        }

    }

    @ResponseBody
    @RequestMapping(value = "updateListOperationFolder", method = RequestMethod.POST)
    public ResponseEntityDto updateFolder(@RequestBody UpdateFolderParam param) {
        UpdateFolderParam.checkNotNull(param);
        boolean updateResult = listOperationService.updateFolder(param.getIds(), param.getFolderId());
        if (updateResult) {
            return ResponseEntityBuilder.buildNormalResponse(updateResult);
        } else {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
        }
    }
}
