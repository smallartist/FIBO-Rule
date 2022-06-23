package com.fibo.ddp.common.model.enginex.marketing.vo;

import com.fibo.ddp.common.model.common.BaseParam;
import lombok.Data;

/**
 * 营销引擎结果列表请求VO
 */
@Data
public class MarketingListResultReqVo extends BaseParam {
    private static final long serialVersionUID = -221159747546752923L;
    // 列表页
    /**
     * 搜索关键字（引擎id或者引擎名称）
     */
    private String searchKey;
}
