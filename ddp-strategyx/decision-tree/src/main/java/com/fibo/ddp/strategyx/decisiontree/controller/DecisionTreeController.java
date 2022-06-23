package com.fibo.ddp.strategyx.decisiontree.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.common.requestParam.UpdateFolderParam;
import com.fibo.ddp.common.model.common.requestParam.UpdateStatusParam;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesVo;
import com.fibo.ddp.common.model.strategyx.decisiontree.vo.DecisionTreeVo;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.service.strategyx.decisiontree.DecisionTreeService;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.ResponseUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * (DecisionTree)表控制层
 */
@RestController
@RequestMapping("v3/decisionTree")
public class DecisionTreeController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private DecisionTreeService decisionTreeService;


    @ResponseBody
    @RequestMapping(value = "getDecisionTreeInfo/{id}", method = RequestMethod.POST)
    public ResponseEntityDto<DecisionTreeVo> getDecisionTreeInfo(@PathVariable Long id) {
        if (id == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        DecisionTreeVo decisionTablesVo = decisionTreeService.queryById(id);
        ResponseEntityDto<DecisionTreeVo> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(decisionTablesVo);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "getDecisionTreeList", method = RequestMethod.POST)
    public ResponseEntityDto<DecisionTablesVo> getDecisionTreeList(@RequestBody QueryListParam<DecisionTreeVo> listParam) {
        PageInfo pageInfo = decisionTreeService.queryByEntity(listParam);
        Map<String, Object> responseMap = ResponseUtil.getResponseMap(pageInfo);
        ResponseEntityDto ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(responseMap);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "addDecisionTree", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.ADD_DECISION_TREE)
    public ResponseEntityDto<DecisionTreeVo> addDecisionTree(@RequestBody DecisionTreeVo decisionTreeVo) {
        DecisionTreeVo insert = decisionTreeService.insertDecisionTree(decisionTreeVo);
        ResponseEntityDto<DecisionTreeVo> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(insert);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "updateDecisionTree", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_DECISION_TREE)
    public ResponseEntityDto<DecisionTreeVo> updateDecisionTree(@RequestBody DecisionTreeVo decisionTreeVo) {
        DecisionTreeVo update = decisionTreeService.updateDecisionTree(decisionTreeVo);
        ResponseEntityDto<DecisionTreeVo> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(update);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "updateDecisionTreeStatus", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_DECISION_TREE_STATUS)
    public ResponseEntityDto updateStatus(@RequestBody UpdateStatusParam param) {
        UpdateStatusParam.checkParam(param);
        boolean updateResult = decisionTreeService.updateStatus(param.getList(), param.getStatus());
        if (updateResult) {
            return ResponseEntityBuilder.buildNormalResponse(updateResult);
        } else {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.DECISION_TREE_UPDATE_ERROR);
        }

    }

    @ResponseBody
    @RequestMapping(value = "updateDecisionTreeFolder", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_DECISION_TREE_FOLDER)
    public ResponseEntityDto updateFolder(@RequestBody UpdateFolderParam param) {
        UpdateFolderParam.checkNotNull(param);
        boolean updateResult = decisionTreeService.updateFolder(param.getIds(), param.getFolderId());
        if (updateResult) {
            return ResponseEntityBuilder.buildNormalResponse(updateResult);
        } else {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
        }
    }

}
