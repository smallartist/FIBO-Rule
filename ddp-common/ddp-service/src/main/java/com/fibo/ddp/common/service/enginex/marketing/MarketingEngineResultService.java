package com.fibo.ddp.common.service.enginex.marketing;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.enginex.marketing.entity.MarketingEngineResult;
import com.fibo.ddp.common.model.enginex.marketing.vo.MarketingDataResultReqVo;
import com.fibo.ddp.common.model.enginex.marketing.vo.MarketingDataResultRspVo;
import com.fibo.ddp.common.model.enginex.marketing.vo.MarketingListResultReqVo;
import com.github.pagehelper.PageInfo;

/**
 * 营销引擎结果表(MarketingEngineResult)表服务接口
 *
 * @author andy.wang
 * @since 2022-01-07 18:13:24
 */
public interface MarketingEngineResultService extends IService<MarketingEngineResult> {

    PageInfo<MarketingEngineResult> getListByPage(MarketingListResultReqVo param);

    MarketingDataResultRspVo getEngineDataByDate(MarketingDataResultReqVo param);
}
