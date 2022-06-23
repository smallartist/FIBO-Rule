package com.fibo.ddp.common.service.enginex.dataflow.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.enginex.dataflow.EngineVersionContentMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.enginex.dataflow.EngineVersionContent;
import com.fibo.ddp.common.model.enginex.dataflow.vo.DataFlowEngineContentVo;
import com.fibo.ddp.common.model.enginex.dataflow.vo.DataFlowNodeVo;
import com.fibo.ddp.common.model.enginex.dataflow.vo.EngineVersionContentVo;
import com.fibo.ddp.common.model.enginex.marketing.dto.AudienceNodeDto;
import com.fibo.ddp.common.model.enginex.marketing.dto.TargetSettingNodeDto;
import com.fibo.ddp.common.model.enginex.marketing.dto.TouchConfigNodeDto;
import com.fibo.ddp.common.model.enginex.marketing.vo.MarketingEngineContentVo;
import com.fibo.ddp.common.model.strategyx.baserule.BaseRule;
import com.fibo.ddp.common.model.strategyx.baserule.BaseRuleTypeEnum;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.enginex.dataflow.EngineVersionContentService;
import com.fibo.ddp.common.service.strategyx.baserule.BaseRuleService;
import com.fibo.ddp.common.utils.constant.enginex.EngineContentConst;
import com.fibo.ddp.common.utils.constant.enginex.EngineTypeConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * (EngineVersionContent)表服务实现类
 *
 * @author jgp
 * @since 2021-12-23 10:21:08
 */
@Service("engineVersionContentService")
public class EngineVersionContentServiceImpl extends ServiceImpl<EngineVersionContentMapper, EngineVersionContent> implements EngineVersionContentService {

    @Resource
    private EngineVersionContentMapper engineVersionContentMapper;
    @Resource
    private BaseRuleService baseRuleService;

    @Override
    public EngineVersionContentVo queryById(Long versionId) {
        if (versionId == null) {
            return null;
        }
        EngineVersionContent engineVersionContent = this.getById(versionId);
        EngineVersionContentVo engineVersionContentVo = queryDetail(engineVersionContent);
        return engineVersionContentVo;
    }

    @Override
    public List<EngineVersionContentVo> queryByIds(Collection<Long> versionIds) {
        if (CollectionUtils.isEmpty(versionIds)) {
            return null;
        }
        List<EngineVersionContent> engineVersionContents = engineVersionContentMapper.selectBatchIds(versionIds);
        List<EngineVersionContentVo> engineVersionContentVos = new ArrayList<>();
        for (EngineVersionContent engineVersionContent : engineVersionContents) {
            EngineVersionContentVo engineVersionContentVo = queryDetail(engineVersionContent);
            engineVersionContentVos.add(engineVersionContentVo);
        }
        return engineVersionContentVos;
    }

    private EngineVersionContentVo queryDetail(EngineVersionContent engineVersionContent) {
        EngineVersionContentVo engineVersionContentVo = new EngineVersionContentVo();
        if (engineVersionContent == null) {
            return engineVersionContentVo;
        }
        BeanUtils.copyProperties(engineVersionContent, engineVersionContentVo);
        String engineContent = engineVersionContent.getEngineContent();
        String engineType = engineVersionContent.getEngineType();
        switch (engineType) {
            case EngineTypeConst
                    .DATA_FLOW_ENGINE:
                if (StringUtils.isNotBlank(engineContent)){
                    engineVersionContentVo.setDataFlowEngineContentVo(JSON.parseObject(engineContent,DataFlowEngineContentVo.class));
                }else {
                    engineVersionContentVo.setDataFlowEngineContentVo(new DataFlowEngineContentVo());
                }
                DataFlowEngineContentVo dataFlowEngineContentVo = engineVersionContentVo.getDataFlowEngineContentVo();
                if (dataFlowEngineContentVo == null) {
                    break;
                }
                List<DataFlowNodeVo> nodeList = dataFlowEngineContentVo.getNodeList();
                if (CollectionUtils.isEmpty(nodeList)) {
                    break;
                }
                for (DataFlowNodeVo dataFlowNodeVo : nodeList) {
                    Long baseRuleId = dataFlowNodeVo.getBaseRuleId();
                    if (baseRuleId != null) {
                        BaseRule baseRule = baseRuleService.queryById(baseRuleId);
                        dataFlowNodeVo.setBaseRule(baseRule);
                    }
                }
                break;
            case EngineTypeConst.MARKETING_ENGINE:
                assemblyMarketingEngineContent(engineContent, engineVersionContentVo);
                break;
        }
        return engineVersionContentVo;
    }

    /**
     * 组装营销引擎content
     * @param engineContent 引擎内容
     * @param engineVersionContentVo 组装后返回对象
     */
    private void assemblyMarketingEngineContent(String engineContent, EngineVersionContentVo engineVersionContentVo) {
        if (StringUtils.isNotBlank(engineContent)) {
            MarketingEngineContentVo marketingEngineContentVo = JSONObject.parseObject(engineContent, MarketingEngineContentVo.class);
            // 受众用户节点
            AudienceNodeDto audienceNodeDto = marketingEngineContentVo.getAudienceNodeDto();
            BaseRule baseRule = baseRuleService.queryById(audienceNodeDto.getBaseRuleId());
            audienceNodeDto.setBaseRule(baseRule);
            // 触达配置节点
            List<TouchConfigNodeDto> touchConfigNodeDtoList = marketingEngineContentVo.getTouchConfigNodeDtoList();
            for (TouchConfigNodeDto touchConfigNodeDto : touchConfigNodeDtoList) {
                BaseRule userSelectRule = baseRuleService.queryById(touchConfigNodeDto.getUserSelectRuleId());
                touchConfigNodeDto.setUserSelectRule(userSelectRule);
            }
            // 目标设置节点
            TargetSettingNodeDto targetSettingNodeDto = marketingEngineContentVo.getTargetSettingNodeDto();
            BaseRule primaryRule = baseRuleService.queryById(targetSettingNodeDto.getPrimaryRuleId());
            targetSettingNodeDto.setPrimaryRule(primaryRule);
            BaseRule secondaryRule = baseRuleService.queryById(targetSettingNodeDto.getSecondaryRuleId());
            targetSettingNodeDto.setSecondaryRule(secondaryRule);

            engineVersionContentVo.setMarketingEngineContentVo(marketingEngineContentVo);
        }
    }

    @Override
    @Transactional
    public boolean addVersionContent(EngineVersionContentVo versionContent) {
        boolean save = false;
        if (checkContent(versionContent)) {
            versionContent.setCreateUserId(SessionManager.getLoginAccount().getUserId());
            save = this.save(versionContent);
        }
        return save;
    }

    @Override
    @Transactional
    public boolean updateVersionContent(EngineVersionContentVo versionContent) {
        boolean update = false;
        if (checkContent(versionContent)) {
            versionContent.setUpdateUserId(SessionManager.getLoginAccount().getUserId());
            //修改时处理基础信息
            updateBaseInfo(versionContent);
            update = engineVersionContentMapper.updateById(versionContent) > 0;
        }
        return update;
    }

    @Override
    public boolean deleteVersionContent(EngineVersionContent versionContent) {
        if (null != versionContent && null != versionContent.getEngineVersionId()) {
            return this.removeById(versionContent.getEngineVersionId());
        }
        return false;
    }

    @Transactional
    public boolean checkContent(EngineVersionContentVo versionContent) {
        if (versionContent == null || versionContent.getEngineVersionId() == null || StringUtils.isBlank(versionContent.getEngineType())) {
            return false;
        }
        if (StringUtils.isBlank(versionContent.getEngineContent())) {
            DataFlowEngineContentVo engineContentVo = new DataFlowEngineContentVo();
            versionContent.setEngineContent(JSON.toJSONString(engineContentVo));
        }
        versionContent.setUpdateUserId(SessionManager.getLoginAccount().getUserId());
        return true;
    }

    @Transactional
    public void updateBaseInfo(EngineVersionContentVo versionContent) {
        String engineType = versionContent.getEngineType();
        SysUser loginAccount = SessionManager.getLoginAccount();
        switch (engineType) {
            case EngineTypeConst
                    .DATA_FLOW_ENGINE:
                DataFlowEngineContentVo engineContentVo = versionContent.getDataFlowEngineContentVo();
                EngineVersionContentVo old = this.queryById(versionContent.getEngineVersionId());
                DataFlowEngineContentVo dataFlowEngineContentVo = old.getDataFlowEngineContentVo();
                if (dataFlowEngineContentVo != null) {
                    List<DataFlowNodeVo> nodeList = dataFlowEngineContentVo.getNodeList();
                    if (CollectionUtils.isNotEmpty(nodeList)) {
                        List<Long> baseRuleIds = new ArrayList<>();
                        for (DataFlowNodeVo dataFlowNodeVo : nodeList) {
                            baseRuleIds.add(dataFlowNodeVo.getBaseRuleId());
                        }
                        baseRuleService.deleteBaseRuleByIds(baseRuleIds);
                    }
                }
                if (engineContentVo != null) {
                    List<DataFlowNodeVo> nodeList = engineContentVo.getNodeList();
                    if (CollectionUtils.isNotEmpty(nodeList)) {
                        for (DataFlowNodeVo node : nodeList) {
                            BaseRule baseRule = node.getBaseRule();
                            if (baseRule != null) {
                                baseRule.setRuleType(BaseRuleTypeEnum.DATA_FLOW_ENGINE.getType());
                                baseRule.setCreateUserId(loginAccount.getUserId());
                                baseRule.setOrganId(loginAccount.getOrganId());
                                boolean b = baseRuleService.insertBaseRule(baseRule);
                                if (b) {
                                    node.setBaseRuleId(baseRule.getId());
                                } else {
                                    throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "数据流引擎节点条件块保存失败");
                                }
                            }
                        }
                    }
                    versionContent.setEngineContent(JSON.toJSONString(engineContentVo));
                }
                break;
            case EngineTypeConst.MARKETING_ENGINE:
                handleMarketingEngineContent(versionContent);
                versionContent.setEngineContent(JSON.toJSONString(versionContent.getMarketingEngineContentVo()));
                break;
        }
    }

    /**
     * 处理营销引擎内容修改
     * @param versionContent
     */
    private void handleMarketingEngineContent(EngineVersionContentVo versionContent) {
        // 先删除原条件
        EngineVersionContentVo old = this.queryById(versionContent.getEngineVersionId());
        if (old != null) {
            MarketingEngineContentVo dbContentVo = old.getMarketingEngineContentVo();
            List<Long> baseRuleIds = new ArrayList<>();
            // 受众用户节点
            baseRuleIds.add(dbContentVo.getAudienceNodeDto().getBaseRuleId());
            // 触达配置节点
            for (TouchConfigNodeDto touchConfigNodeDto : dbContentVo.getTouchConfigNodeDtoList()) {
                baseRuleIds.add(touchConfigNodeDto.getUserSelectRuleId());
            }
            // 目标设置节点
            baseRuleIds.add(dbContentVo.getTargetSettingNodeDto().getPrimaryRuleId());
            baseRuleIds.add(dbContentVo.getTargetSettingNodeDto().getSecondaryRuleId());
            baseRuleService.deleteBaseRuleByIds(baseRuleIds);
        }

        // 重新保存新条件
        MarketingEngineContentVo marketingEngineContentVo = versionContent.getMarketingEngineContentVo();
        // 受众用户节点
        AudienceNodeDto audienceNodeDto = marketingEngineContentVo.getAudienceNodeDto();
        BaseRule baseRule = audienceNodeDto.getBaseRule();
        audienceNodeDto.setBaseRuleId(saveMarketingEngineBaseRule(baseRule));
        audienceNodeDto.setBaseRule(null);
        // 触达配置节点
        List<TouchConfigNodeDto> touchConfigNodeDtoList = marketingEngineContentVo.getTouchConfigNodeDtoList();
        for (TouchConfigNodeDto touchConfigNodeDto : touchConfigNodeDtoList) {
            BaseRule userSelectRule = touchConfigNodeDto.getUserSelectRule();
            touchConfigNodeDto.setUserSelectRuleId(saveMarketingEngineBaseRule(userSelectRule));
            touchConfigNodeDto.setUserSelectRule(null);
        }
        // 目标设置节点
        TargetSettingNodeDto targetSettingNodeDto = marketingEngineContentVo.getTargetSettingNodeDto();
        BaseRule primaryRule = targetSettingNodeDto.getPrimaryRule();
        targetSettingNodeDto.setPrimaryRuleId(saveMarketingEngineBaseRule(primaryRule));
        targetSettingNodeDto.setPrimaryRule(null);
        BaseRule secondaryRule = targetSettingNodeDto.getSecondaryRule();
        targetSettingNodeDto.setSecondaryRuleId(saveMarketingEngineBaseRule(secondaryRule));
        targetSettingNodeDto.setSecondaryRule(null);
    }

    /**
     * 保存营销引擎条件
     * @param baseRule
     * @return
     */
    private Long saveMarketingEngineBaseRule(BaseRule baseRule){
        if (baseRule != null) {
            SysUser loginAccount = SessionManager.getLoginAccount();
            baseRule.setRuleType(BaseRuleTypeEnum.MARKETING_ENGINE.getType());
            baseRule.setOrganId(loginAccount.getOrganId());
            baseRule.setCreateUserId(loginAccount.getUserId());
            boolean result = baseRuleService.insertBaseRule(baseRule);
            if (!result) {
                throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "营销引擎节点条件块保存失败");
            }
        }
        return baseRule.getId();
    }

    private void handlerEngineScript(EngineVersionContentVo versionContent) {
        if (versionContent != null && StringUtils.isNotBlank(versionContent.getEngineContent())) {
            //拼装执行条件
            JSONObject engineJson = JSON.parseObject(versionContent.getEngineContent());
            JSONArray nodeList = engineJson.getJSONArray(EngineContentConst.NODE_LIST_KEY);
            for (int i = 0; i < nodeList.size(); i++) {
                JSONObject node = nodeList.getJSONObject(i);
                String nodeType = node.getString(EngineContentConst.DataFlowEngine.NODE_TYPE);
                switch (nodeType) {
                    case EngineContentConst
                            .DataFlowEngine
                            .NodeType.BEGIN:

                        break;
                    case EngineContentConst
                            .DataFlowEngine
                            .NodeType.WHERE:

                        break;
                    case EngineContentConst
                            .DataFlowEngine
                            .NodeType.OR:

                        break;
                    case EngineContentConst
                            .DataFlowEngine
                            .NodeType.WITHIN:

                        break;
                    case EngineContentConst
                            .DataFlowEngine
                            .NodeType.NEXT:

                        break;
                    case EngineContentConst
                            .DataFlowEngine
                            .NodeType.FOLLOWED_BY:

                        break;
                    case EngineContentConst
                            .DataFlowEngine
                            .NodeType.ONE_OR_MORE:

                        break;
                    case EngineContentConst
                            .DataFlowEngine
                            .NodeType.TIMES:

                        break;
                    case EngineContentConst
                            .DataFlowEngine
                            .NodeType.TIMES_OR_MORE:

                        break;
                    default:
                        break;
                }
            }
        }
    }

}
