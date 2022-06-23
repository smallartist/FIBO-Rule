package com.fibo.ddp.common.model.enginex.marketing.vo;

import com.fibo.ddp.common.model.enginex.marketing.dto.AudienceNodeDto;
import com.fibo.ddp.common.model.enginex.marketing.dto.TargetSettingNodeDto;
import com.fibo.ddp.common.model.enginex.marketing.dto.TouchConfigNodeDto;
import com.fibo.ddp.common.model.enginex.marketing.dto.TriggerSettingNodeDto;
import lombok.Data;

import java.util.List;

/**
 * 营销引擎节点配置VO
 */
@Data
public class MarketingEngineContentVo {

    /**
     * 触发设置节点
     */
    private TriggerSettingNodeDto triggerSettingNodeDto;
    /**
     * 受众用户节点
     */
    private AudienceNodeDto audienceNodeDto;
    /**
     * 触达配置节点（暂时只支持多个触达节点串行方式）
     */
    private List<TouchConfigNodeDto> touchConfigNodeDtoList;
    /**
     * 目标设置节点
     */
    private TargetSettingNodeDto targetSettingNodeDto;
}
