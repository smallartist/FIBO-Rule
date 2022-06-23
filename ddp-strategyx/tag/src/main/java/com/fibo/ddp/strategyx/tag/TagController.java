package com.fibo.ddp.strategyx.tag;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.common.requestParam.UpdateFolderParam;
import com.fibo.ddp.common.model.common.requestParam.UpdateStatusParam;
import com.fibo.ddp.common.model.strategyx.tag.Tag;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.service.strategyx.tag.TagService;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.ResponseUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/v3/strategyx/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @ResponseBody
    @RequestMapping(value = "getTagInfo/{id}", method = RequestMethod.POST)
    public ResponseEntityDto<Tag> getTagInfo(@PathVariable Long id) {
        if (id == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        Tag tag = tagService.queryById(id);
        ResponseEntityDto<Tag> result = ResponseEntityBuilder.buildNormalResponse(tag);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "getTagList", method = RequestMethod.POST)
    public ResponseEntityDto<Tag> getTagList(@RequestBody QueryListParam<Tag> listParam) {
        PageInfo pageInfo = tagService.queryByEntity(listParam);
        Map<String, Object> responseMap = ResponseUtil.getResponseMap(pageInfo);
        ResponseEntityDto ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(responseMap);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "addTag", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.ADD_TAG)
    public ResponseEntityDto<Tag> addTag(@RequestBody Tag tag) {
        Tag insert = tagService.insertTag(tag);
        ResponseEntityDto<Tag> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(insert);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "updateTag", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_TAG)
    public ResponseEntityDto<Tag> updateTag(@RequestBody Tag tag) {
        Tag update = tagService.updateTag(tag);
        ResponseEntityDto<Tag> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(update);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "updateTagStatus", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_TAG_STATUS)
    public ResponseEntityDto updateStatus(@RequestBody UpdateStatusParam param) {
        UpdateStatusParam.checkParam(param);
        boolean updateResult = tagService.updateStatus(param.getList(), param.getStatus());
        if (updateResult) {
            return ResponseEntityBuilder.buildNormalResponse(updateResult);
        } else {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.TAG_UPDATE_ERROR);
        }

    }

    @ResponseBody
    @RequestMapping(value = "updateTagFolder", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_TAG_FOLDER)
    public ResponseEntityDto updateFolder(@RequestBody UpdateFolderParam param) {
        UpdateFolderParam.checkNotNull(param);
        boolean updateResult = tagService.updateFolder(param.getIds(), param.getFolderId());
        if (updateResult) {
            return ResponseEntityBuilder.buildNormalResponse(updateResult);
        } else {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
        }
    }


}
