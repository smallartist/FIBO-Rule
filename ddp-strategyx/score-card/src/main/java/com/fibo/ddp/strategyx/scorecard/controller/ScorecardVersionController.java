package com.fibo.ddp.strategyx.scorecard.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVersionVo;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardVersionService;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Controller("ScorecardVersionController")
@RequestMapping("/v3/scorecardVersion")
@ResponseBody
public class ScorecardVersionController {
    @Resource
    private ScorecardVersionService versionService;

    /**
     * 查询指定版本下的内容
     * @param versionId
     * @return
     */
    @PostMapping("/getScorecardVersionInfo/{versionId}")
    public ResponseEntityDto getScorecardVersionInfo(@PathVariable Long versionId) {
        ScorecardVersionVo version =versionService.queryById(versionId);
        return ResponseEntityBuilder.buildNormalResponse(version);
    }

    /**
     * 新增一个版本
     * @param version
     * @return
     */
    @PostMapping("/addScorecardVersion")
    @ArchivesLog(operationType = OpTypeConst.ADD_SCORECARD_VERSION)
    public ResponseEntityDto addScorecardVersion(@RequestBody ScorecardVersionVo version){
        boolean b = versionService.addVersion(version);
        List<ScorecardVersionVo> scorecardVersionList = versionService.queryVersionListByScorecardId(version.getScorecardId());
        return ResponseEntityBuilder.buildNormalResponse(scorecardVersionList);
    }

    /**
     * 复制版本
     * @param version
     * @return
     */
    @PostMapping("/copyScorecardVersion")
    @ArchivesLog(operationType = OpTypeConst.COPY_SCORECARD_VERSION)
    public ResponseEntityDto copyScorecardVersion(@RequestBody ScorecardVersionVo version){
        boolean b = versionService.copyVersion(version);
        List<ScorecardVersionVo> scorecardVersionList = versionService.queryVersionListByScorecardId(version.getScorecardId());
        return ResponseEntityBuilder.buildNormalResponse(scorecardVersionList);
    }

    /**
     * 修改版本
     * @param version
     * @return
     */
    @PostMapping("/updateScorecardVersion")
    @ArchivesLog(operationType = OpTypeConst.UPDATE_SCORECARD_VERSION)
    public ResponseEntityDto updateScorecardVersion(@RequestBody ScorecardVersionVo version){
        boolean b = versionService.updateVersion(version);
        List<ScorecardVersionVo> scorecardVersionList = versionService.queryVersionListByScorecardId(version.getScorecardId());
        return ResponseEntityBuilder.buildNormalResponse(scorecardVersionList);
    }

    /**
     * 修改状态
     * @param statusParam
     * @return
     */
    @RequestMapping(value = "/updateScorecardVersionStatus", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_SCORECARD_VERSION_STATUS)
    public ResponseEntityDto<Object> updateStatus(@RequestBody StatusParam statusParam) {
        versionService.updateStatus(statusParam);
        List<ScorecardVersionVo> scorecardVersionList = versionService.queryVersionListByScorecardId(statusParam.getStrategyId());
        return ResponseEntityBuilder.buildNormalResponse(scorecardVersionList);
    }


}
