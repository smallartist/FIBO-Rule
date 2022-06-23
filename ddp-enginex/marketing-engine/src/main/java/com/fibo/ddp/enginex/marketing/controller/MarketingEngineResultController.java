package com.fibo.ddp.enginex.marketing.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.enginex.marketing.entity.MarketingEngineResult;
import com.fibo.ddp.common.model.enginex.marketing.vo.MarketingDataResultReqVo;
import com.fibo.ddp.common.model.enginex.marketing.vo.MarketingDataResultRspVo;
import com.fibo.ddp.common.model.enginex.marketing.vo.MarketingListResultReqVo;
import com.fibo.ddp.common.service.enginex.marketing.MarketingEngineResultService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 营销引擎结果表(MarketingEngineResult)表控制层
 *
 * @author andy.wang
 * @since 2022-01-07 18:13:24
 */
@RestController
@RequestMapping("/engineResult/marketing")
public class MarketingEngineResultController {

    @Autowired
    private MarketingEngineResultService marketingEngineResultService;

    /**
     * 获取营销引擎结果列表页
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "queryListByPage", method = RequestMethod.POST)
    public ResponseEntityDto<PageInfo<MarketingEngineResult>> queryListByPage(@RequestBody MarketingListResultReqVo param) {
        PageInfo<MarketingEngineResult> pageInfo = marketingEngineResultService.getListByPage(param);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }

    /**
     * 获取营销引擎数据详情
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "queryEngineDataByDate", method = RequestMethod.POST)
    public ResponseEntityDto<MarketingDataResultRspVo> queryEngineDataByDate(@RequestBody MarketingDataResultReqVo param) {
        MarketingDataResultRspVo marketingDataResultRspVo = marketingEngineResultService.getEngineDataByDate(param);
        return ResponseEntityBuilder.buildNormalResponse(marketingDataResultRspVo);
    }
}
