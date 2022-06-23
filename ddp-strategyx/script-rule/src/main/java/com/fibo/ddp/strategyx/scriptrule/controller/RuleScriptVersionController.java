package com.fibo.ddp.strategyx.scriptrule.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.scriptrule.RuleScriptVersion;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.service.strategyx.scriptrule.RuleScriptVersionService;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (RuleScriptVersion)表控制层
 */
@RestController
@RequestMapping("/v3/ruleScriptVersion")
public class RuleScriptVersionController {
    /**
     * 服务对象
     */
    @Resource
    private RuleScriptVersionService ruleScriptVersionService;

    /**
     * 查询指定版本下的内容
     * @param versionId
     * @return
     */
    @PostMapping("/getRuleScriptVersion/{versionId}")
    public ResponseEntityDto getRuleVersionInfo(@PathVariable Long versionId) {
        RuleScriptVersion version = ruleScriptVersionService.queryById(versionId);
        return ResponseEntityBuilder.buildNormalResponse(version);
    }

    /**
     * 新增一个版本
     * @param version
     * @return
     */
    @PostMapping("/addRuleScriptVersion")
    @ArchivesLog(operationType = OpTypeConst.SAVE_RULE_SCRIPT_VERSION)
    public ResponseEntityDto addRuleVersion(@RequestBody  RuleScriptVersion version){
        boolean b = ruleScriptVersionService.addVersion(version);
        if (!b){
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION);
        }
        List< RuleScriptVersion> ruleVersionList = ruleScriptVersionService.queryVersionListByRuleId(version.getRuleId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

    /**
     * 复制版本
     * @param version
     * @return
     */
    @PostMapping("/copyRuleScriptVersion")
    @ArchivesLog(operationType = OpTypeConst.COPY_RULE_SCRIPT_VERSION)
    public ResponseEntityDto copyRuleVersion(@RequestBody  RuleScriptVersion version){
        boolean b = ruleScriptVersionService.copyVersion(version);
        List< RuleScriptVersion> ruleVersionList = ruleScriptVersionService.queryVersionListByRuleId(version.getRuleId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

    /**
     * 修改版本
     * @param version
     * @return
     */
    @PostMapping("/updateRuleScriptVersion")
    @ArchivesLog(operationType = OpTypeConst.UPDATE_RULE_SCRIPT_VERSION)
    public ResponseEntityDto updateRuleVersion(@RequestBody RuleScriptVersion version){
        boolean b = ruleScriptVersionService.updateVersion(version);
        List<RuleScriptVersion> ruleVersionList = ruleScriptVersionService.queryVersionListByRuleId(version.getRuleId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

    /**
     * 修改状态
     * @param statusParam
     * @return
     */
    @RequestMapping(value = "/updateRuleScriptVersionStatus", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_RULE_VERSION_SCRIPT_STATUS)
    public ResponseEntityDto<Object> updateStatus(@RequestBody StatusParam statusParam) {
        ruleScriptVersionService.updateStatus(statusParam);
        List<RuleScriptVersion> ruleVersionList = ruleScriptVersionService.queryVersionListByRuleId(statusParam.getStrategyId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

}
