package com.fibo.ddp.strategyx.decisiontable.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.UpdateFolderParam;
import com.fibo.ddp.common.model.common.requestParam.UpdateStatusParam;
import com.fibo.ddp.common.model.strategyx.decisiontable.request.DecisionTablesListParam;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesVo;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesService;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.ResponseUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * (DecisionTables)表控制层
 *
 * @author jgp
 * @since 2021-04-12 17:54:48
 */
@RestController
@RequestMapping("v3/decisionTables")
public class DecisionTablesController {

    @Resource
    private DecisionTablesService decisionTablesService;


    @ResponseBody
    @RequestMapping(value = "getDecisionTablesInfo/{id}", method = RequestMethod.POST)
    public ResponseEntityDto<DecisionTablesVo> getDecisionTablesInfo(@PathVariable Long id ) {
        if (id==null){
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        DecisionTablesVo decisionTablesVo = decisionTablesService.queryById(id);
        ResponseEntityDto<DecisionTablesVo> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(decisionTablesVo);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "getDecisionTablesList", method = RequestMethod.POST)
    public ResponseEntityDto<DecisionTablesVo> getDecisionTablesList(@RequestBody DecisionTablesListParam listParam) {
        PageInfo pageInfo = decisionTablesService.queryByEntity(listParam);
        Map<String, Object> responseMap = ResponseUtil.getResponseMap(pageInfo);
        ResponseEntityDto ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(responseMap);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "addDecisionTables", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.ADD_DECISION_TABLES)
    public ResponseEntityDto<DecisionTablesVo> addDecisionTables(@RequestBody DecisionTablesVo decisionTablesVo ) {
        DecisionTablesVo insert = decisionTablesService.insertDecisionTables(decisionTablesVo);
        ResponseEntityDto<DecisionTablesVo> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(insert);
        return ruleResponseEntityDto;
    }
    @ResponseBody
    @ArchivesLog(operationType = OpTypeConst.UPDATE_DECISION_TABLES)
    @RequestMapping(value = "updateDecisionTables", method = RequestMethod.POST)
    public ResponseEntityDto<DecisionTablesVo> updateDecisionTables(@RequestBody DecisionTablesVo decisionTablesVo ) {
        DecisionTablesVo update = decisionTablesService.updateDecisionTables(decisionTablesVo);
        ResponseEntityDto<DecisionTablesVo> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(update);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "updateDecisionTablesStatus", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_DECISION_TABLES_STATUS)
    public ResponseEntityDto updateStatus(@RequestBody UpdateStatusParam param) {
        UpdateStatusParam.checkParam(param);
        boolean updateResult = decisionTablesService.updateStatus(param.getList(), param.getStatus());
        if (updateResult){
            return ResponseEntityBuilder.buildNormalResponse(updateResult);
        }else {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.DECISION_TABLES_UPDATE_ERROR);
        }

    }
    @ResponseBody
    @RequestMapping(value = "updateDecisionTablesParent", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_DECISION_TABLES_FOLDER)
    public ResponseEntityDto updateFolder(@RequestBody UpdateFolderParam param) {
        UpdateFolderParam.checkNotNull(param);
        boolean updateResult = decisionTablesService.updateParent(param.getIds(), param.getFolderId());
        if (updateResult){
            return ResponseEntityBuilder.buildNormalResponse(updateResult);
        }else {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
        }
    }

}
