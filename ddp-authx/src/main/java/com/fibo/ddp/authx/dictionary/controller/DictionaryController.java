package com.fibo.ddp.authx.dictionary.controller;

import com.fibo.ddp.common.model.authx.dictionary.Dictionary;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.service.authx.dictionary.DictionaryService;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageInfo;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * (Dictionary)表控制层
 *
 * @author jgp
 * @since 2021-12-15 15:08:06
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {
    /**
     * 服务对象
     */
    @Resource
    private DictionaryService dictionaryService;

    @PostMapping("/getByKey")
    public ResponseEntityDto<Dictionary> getByKey (@RequestBody Dictionary param){
        Dictionary dictionary = dictionaryService.queryByKey(param.getDictKey());
        return ResponseEntityBuilder.buildNormalResponse(dictionary);
    }
    @PostMapping("/getList")
    public ResponseEntityDto<PageInfo> getList (@RequestBody QueryListParam<Dictionary> param){
        PageInfo pageInfo = dictionaryService.queryList(param);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }
    @PostMapping("/addBatch")
    public ResponseEntityDto addBatch (@RequestBody Collection<Dictionary> param){
        boolean result = dictionaryService.saveBatchDictionary(param);
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
    @PostMapping("/updateById")
    public ResponseEntityDto updateById (Dictionary param){
        boolean result = dictionaryService.update(param);
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
    @PostMapping("/deleteByIds")
    public ResponseEntityDto deleteByIds (@RequestBody List<Long> ids){
        if (CollectionUtils.isEmpty(ids)){
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),"删除字典表id不能为空");
        }
        boolean result = dictionaryService.deleteByIds(ids);
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
    @PostMapping("/refreshCache")
    public ResponseEntityDto refreshCache (){
        Map<String, Dictionary> result = dictionaryService.refreshCache();
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
}
