package com.fibo.ddp.strategyx.collectionrule.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationVersion;
import com.fibo.ddp.common.service.strategyx.collectionrule.ListOperationVersionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/v3/listOperation/version")
public class ListOperationVersionController {
    /**
     * 服务对象
     */
    @Resource
    private ListOperationVersionService versionService;
    /**
     * 查询指定版本下的内容
     *
     * @param versionId
     * @return
     */
    @PostMapping("/getVersionInfo/{versionId}")
    public ResponseEntityDto getVersionInfo(@PathVariable Long versionId) {
        ListOperationVersion version = versionService.queryById(versionId);
        return ResponseEntityBuilder.buildNormalResponse(version);
    }

    /**
     * 新增一个版本
     *
     * @param version
     * @return
     */
    @PostMapping("/addVersion")
    public ResponseEntityDto addVersion(@RequestBody ListOperationVersion version) {
        boolean b = versionService.addVersion(version);
        if (!b) {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION);
        }
        List<ListOperationVersion> ruleVersionList = versionService.queryListByListOpId(version.getListOpId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

    /**
     * 复制版本
     *
     * @param version
     * @return
     */
    @PostMapping("/copyVersion")
    public ResponseEntityDto copyVersion(@RequestBody ListOperationVersion version) {
        boolean b = versionService.copyVersion(version);
        List<ListOperationVersion> ruleVersionList = versionService.queryListByListOpId(version.getListOpId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

    /**
     * 修改版本
     *
     * @param version
     * @return
     */
    @PostMapping("/updateVersion")
    public ResponseEntityDto updateVersion(@RequestBody ListOperationVersion version) {
        boolean b = versionService.updateVersion(version);
        List<ListOperationVersion> ruleVersionList = versionService.queryListByListOpId(version.getListOpId());
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
        List<ListOperationVersion> ruleVersionList = versionService.queryListByListOpId(statusParam.getStrategyId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }


}
