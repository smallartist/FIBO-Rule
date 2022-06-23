package com.fibo.ddp.common.model.enginex.marketing.vo;

import com.fibo.ddp.common.model.common.BaseParam;
import lombok.Data;

import java.util.Date;

/**
 * 营销引擎数据详情请求VO
 */
@Data
public class MarketingDataResultReqVo extends BaseParam {
    private static final long serialVersionUID = -221159747546752923L;
    // 详情页
    /**
     * 引擎版本id
     */
    private Integer engineVersionId;
    /**
     * 搜索开始时间
     */
    private Date startDate;
    /**
     * 搜索结束时间
     */
    private Date endDate;
}
