package com.fibo.ddp.strategyx.dataclean.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;
import com.fibo.ddp.common.service.strategyx.dataclean.DataCleanVersionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/dataClean/version")
public class DataCleanVersionController {
    /**
     * 服务对象
     */
    @Resource
    private DataCleanVersionService versionService;
    /**
     * 查询指定版本下的内容
     *
     * @param versionId
     * @return
     */
    @PostMapping("/getVersionInfo/{versionId}")
    public ResponseEntityDto getVersionInfo(@PathVariable Long versionId) {
        DataCleanVersion version = versionService.queryById(versionId,false);
        return ResponseEntityBuilder.buildNormalResponse(version);
    }

    /**
     * 新增一个版本
     *
     * @param version
     * @return
     */
    @PostMapping("/addVersion")
    public ResponseEntityDto addVersion(@RequestBody DataCleanVersion version) {
        boolean b = versionService.addVersion(version);
        if (!b) {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION);
        }
        List<DataCleanVersion> ruleVersionList = versionService.queryListByListOpId(version.getDataCleanId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

    /**
     * 复制版本
     *
     * @param version
     * @return
     */
    @PostMapping("/copyVersion")
    public ResponseEntityDto copyVersion(@RequestBody DataCleanVersion version) {
        boolean b = versionService.copyVersion(version);
        List<DataCleanVersion> ruleVersionList = versionService.queryListByListOpId(version.getDataCleanId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

    /**
     * 修改版本
     *
     * @param version
     * @return
     */
    @PostMapping("/updateVersion")
    public ResponseEntityDto updateVersion(@RequestBody DataCleanVersion version) {
        boolean b = versionService.updateVersion(version);
        List<DataCleanVersion> ruleVersionList = versionService.queryListByListOpId(version.getDataCleanId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

    /**
     * 修改状态
     *
     * @param statusParam
     * @return
     */
    @RequestMapping(value = "/updateVersionStatus", method = RequestMethod.POST)
    public ResponseEntityDto<Object> updateStatus(@RequestBody StatusParam statusParam) {
        versionService.updateStatus(statusParam);
        List<DataCleanVersion> ruleVersionList = versionService.queryListByListOpId(statusParam.getStrategyId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }


}
