package com.fibo.ddp.strategyx.listlibrary.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.listlibrary.ListDbVersion;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.service.strategyx.listlibrary.ListDbVersionService;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ListDbVersion)表控制层
 */
@RestController
@RequestMapping("v3/listDb/version")
public class ListDbVersionController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ListDbVersionService versionService;

    /**
     * 查询指定版本下的内容
     *
     * @param versionId
     * @return
     */
    @PostMapping("/getVersionInfo/{versionId}")
    public ResponseEntityDto getVersionInfo(@PathVariable Long versionId) {
        ListDbVersion version = versionService.queryById(versionId);
        return ResponseEntityBuilder.buildNormalResponse(version);
    }

    /**
     * 新增一个版本
     *
     * @param version
     * @return
     */
    @PostMapping("/addVersion")
    @ArchivesLog(operationType = OpTypeConst.ADD_BLACK_OR_WHITE_LIST_DB_VERSION)
    public ResponseEntityDto addVersion(@RequestBody ListDbVersion version) {
        boolean b = versionService.addVersion(version);
        if (!b) {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION);
        }
        List<ListDbVersion> ruleVersionList = versionService.queryVersionListByListDbId(version.getListDbId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

    /**
     * 复制版本
     *
     * @param version
     * @return
     */
    @PostMapping("/copyVersion")
    @ArchivesLog(operationType = OpTypeConst.COPY_BLACK_OR_WHITE_LIST_DB_VERSION)
    public ResponseEntityDto copyVersion(@RequestBody ListDbVersion version) {
        boolean b = versionService.copyVersion(version);
        List<ListDbVersion> ruleVersionList = versionService.queryVersionListByListDbId(version.getListDbId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

    /**
     * 修改版本
     *
     * @param version
     * @return
     */
    @PostMapping("/updateVersion")
    public ResponseEntityDto updateVersion(@RequestBody ListDbVersion version) {
        boolean b = versionService.updateVersion(version);
        List<ListDbVersion> ruleVersionList = versionService.queryVersionListByListDbId(version.getListDbId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }

    /**
     * 修改状态
     *
     * @param StatusParam
     * @return
     */
    @RequestMapping(value = "/updateVersionStatus", method = RequestMethod.POST)
    public ResponseEntityDto<Object> updateStatus(@RequestBody StatusParam StatusParam) {
        versionService.updateStatus(StatusParam);
        List<ListDbVersion> ruleVersionList = versionService.queryVersionListByListDbId(StatusParam.getStrategyId());
        return ResponseEntityBuilder.buildNormalResponse(ruleVersionList);
    }
}
