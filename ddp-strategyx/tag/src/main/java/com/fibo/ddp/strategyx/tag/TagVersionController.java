package com.fibo.ddp.strategyx.tag;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.tag.TagVersion;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.service.strategyx.tag.TagVersionService;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/v3/strategyx/tagVersion")
public class TagVersionController {
    /**
     * 服务对象
     */
    @Resource
    private TagVersionService versionService;

    /**
     * 查询指定版本下的内容
     *
     * @param versionId
     * @return
     */
    @PostMapping("/getVersionInfo/{versionId}")
    public ResponseEntityDto getVersionInfo(@PathVariable Long versionId) {
        TagVersion version = versionService.queryById(versionId);
        return ResponseEntityBuilder.buildNormalResponse(version);
    }

    /**
     * 新增一个版本
     *
     * @param version
     * @return
     */
    @PostMapping("/addVersion")
    @ArchivesLog(operationType = OpTypeConst.ADD_TAG_VERSION)
    public ResponseEntityDto addVersion(@RequestBody TagVersion version) {
        boolean b = versionService.addVersion(version);
        if (!b) {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION);
        }
        List<TagVersion> versionList = versionService.queryVersionListByTagId(version.getTagId());
        return ResponseEntityBuilder.buildNormalResponse(versionList);
    }

    /**
     * 复制版本
     *
     * @param version
     * @return
     */
    @PostMapping("/copyVersion")
    @ArchivesLog(operationType = OpTypeConst.COPY_TAG_VERSION)
    public ResponseEntityDto copyVersion(@RequestBody TagVersion version) {
        boolean b = versionService.copyVersion(version);
        List<TagVersion> versionList = versionService.queryVersionListByTagId(version.getTagId());
        return ResponseEntityBuilder.buildNormalResponse(versionList);
    }

    /**
     * 修改版本
     *
     * @param version
     * @return
     */
    @PostMapping("/updateVersion")
    @ArchivesLog(operationType = OpTypeConst.UPDATE_TAG_VERSION)
    public ResponseEntityDto updateVersion(@RequestBody TagVersion version) {
        boolean b = versionService.updateVersion(version);
        List<TagVersion> versionList = versionService.queryVersionListByTagId(version.getTagId());
        return ResponseEntityBuilder.buildNormalResponse(versionList);
    }

    /**
     * 修改状态
     *
     * @param statusParam
     * @return
     */
    @RequestMapping(value = "/updateVersionStatus", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_TAG_VERSION_STATUS)
    public ResponseEntityDto<Object> updateStatus(@RequestBody StatusParam statusParam) {
        versionService.updateStatus(statusParam);
        List<TagVersion> versionList = versionService.queryVersionListByTagId(statusParam.getStrategyId());
        return ResponseEntityBuilder.buildNormalResponse(versionList);
    }
}
