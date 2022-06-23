package com.fibo.ddp.strategyx.decisiontree.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.decisiontree.vo.DecisionTreeVersionVo;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.service.strategyx.decisiontree.DecisionTreeVersionService;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (DecisionTreeVersion)表控制层
 */
@RestController
@RequestMapping("/v3/decisionTree/version")
public class DecisionTreeVersionController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private DecisionTreeVersionService versionService;

    /**
     * 查询指定版本下的内容
     *
     * @param versionId
     * @return
     */
    @PostMapping("/getVersionInfo/{versionId}")
    public ResponseEntityDto getVersionInfo(@PathVariable Long versionId) {
        DecisionTreeVersionVo version = versionService.queryById(versionId);
        return ResponseEntityBuilder.buildNormalResponse(version);
    }

    /**
     * 新增一个版本
     *
     * @param version
     * @return
     */
    @PostMapping("/addVersion")
    @ArchivesLog(operationType = OpTypeConst.ADD_DECISION_TREE_VERSION)
    public ResponseEntityDto addVersion(@RequestBody DecisionTreeVersionVo version) {
        boolean b = versionService.addVersion(version);
        if (!b) {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION);
        }
        List<DecisionTreeVersionVo> ruleVersionList = versionService.queryVersionListByDecisionTreeId(version.getDecisionTreeId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

    /**
     * 复制版本
     *
     * @param version
     * @return
     */
    @PostMapping("/copyVersion")
    @ArchivesLog(operationType = OpTypeConst.COPY_DECISION_TREE_VERSION)
    public ResponseEntityDto copyVersion(@RequestBody DecisionTreeVersionVo version) {
        boolean b = versionService.copyVersion(version);
        List<DecisionTreeVersionVo> ruleVersionList = versionService.queryVersionListByDecisionTreeId(version.getDecisionTreeId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

    /**
     * 修改版本
     *
     * @param version
     * @return
     */
    @PostMapping("/updateVersion")
    @ArchivesLog(operationType = OpTypeConst.UPDATE_DECISION_TREE_VERSION)
    public ResponseEntityDto updateVersion(@RequestBody DecisionTreeVersionVo version) {
        boolean b = versionService.updateVersion(version);
        List<DecisionTreeVersionVo> ruleVersionList = versionService.queryVersionListByDecisionTreeId(version.getDecisionTreeId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

    /**
     * 修改状态
     *
     * @param statusParam
     * @return
     */
    @RequestMapping(value = "/updateVersionStatus", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_DECISION_TREE_VERSION_STATUS)
    public ResponseEntityDto<Object> updateStatus(@RequestBody StatusParam statusParam) {
        versionService.updateStatus(statusParam);
        List<DecisionTreeVersionVo> ruleVersionList = versionService.queryVersionListByDecisionTreeId(statusParam.getStrategyId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }
}
