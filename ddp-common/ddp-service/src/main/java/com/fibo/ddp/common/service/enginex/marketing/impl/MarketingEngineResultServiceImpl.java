package com.fibo.ddp.common.service.enginex.marketing.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.enginex.marketing.MarketingEngineResultMapper;
import com.fibo.ddp.common.model.enginex.marketing.entity.MarketingEngineNodeDateResult;
import com.fibo.ddp.common.model.enginex.marketing.entity.MarketingEngineNodeResult;
import com.fibo.ddp.common.model.enginex.marketing.entity.MarketingEngineResult;
import com.fibo.ddp.common.model.enginex.marketing.vo.*;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.enginex.marketing.MarketingEngineNodeDateResultService;
import com.fibo.ddp.common.service.enginex.marketing.MarketingEngineNodeResultService;
import com.fibo.ddp.common.service.enginex.marketing.MarketingEngineResultService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 营销引擎结果表(MarketingEngineResult)表服务实现类
 *
 * @author andy.wang
 * @since 2022-01-07 18:13:24
 */
@Service
public class MarketingEngineResultServiceImpl extends ServiceImpl<MarketingEngineResultMapper, MarketingEngineResult> implements MarketingEngineResultService {

    @Autowired
    private MarketingEngineNodeDateResultService nodeDateResultService;
    @Autowired
    private MarketingEngineNodeResultService nodeResultService;

    @Override
    public PageInfo<MarketingEngineResult> getListByPage(MarketingListResultReqVo param) {
        Long organId = SessionManager.getLoginAccount().getOrganId();
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        LambdaQueryWrapper<MarketingEngineResult> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MarketingEngineResult::getOrganId, organId);
        String searchKey = param.getSearchKey();
        if (StringUtils.isNotBlank(searchKey)) {
            queryWrapper.and(i -> i.like(MarketingEngineResult::getEngineId, searchKey).or()
                    .like(MarketingEngineResult::getEngineName, searchKey));
        }
        List<MarketingEngineResult> list = this.list(queryWrapper);
        PageInfo<MarketingEngineResult> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public MarketingDataResultRspVo getEngineDataByDate(MarketingDataResultReqVo param) {
        MarketingDataResultRspVo marketingDataResultRspVo = null;
        Integer engineVersionId = param.getEngineVersionId();
        Date startDate = param.getStartDate();
        Date endDate = param.getEndDate();
        if (startDate != null && endDate != null) {
            // 根据时间维度查询引擎统计数据
            marketingDataResultRspVo = getEngineResultByDate(engineVersionId, startDate, endDate);
        } else {
            // 从表中查询引擎统计好的数据
            marketingDataResultRspVo = getEngineResultByVersionId(engineVersionId);
        }
        return marketingDataResultRspVo;
    }

    /**
     * 从表中查询引擎统计好的数据
     *
     * @param engineVersionId
     * @return
     */
    public MarketingDataResultRspVo getEngineResultByVersionId(Integer engineVersionId) {
        MarketingDataResultRspVo dataResultRspVo = new MarketingDataResultRspVo();
        LambdaQueryWrapper<MarketingEngineResult> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MarketingEngineResult::getEngineVersionId, engineVersionId);
        List<MarketingEngineResult> list = this.list(queryWrapper);
        if (list != null && !list.isEmpty()) {
            MarketingEngineResult engineResult = list.get(0);
            dataResultRspVo = JSONObject.parseObject(JSONObject.toJSONString(engineResult), MarketingDataResultRspVo.class);
            List<MarketingEngineNodeResult> nodeResultList = nodeResultService.getNodeResultByVersionId(engineVersionId);
            List<MarketingEngineNodeDateResult> nodeDateResultList = nodeDateResultService.getNodeDateResultByDate(engineVersionId, null, null);
            Map<Integer, List<MarketingEngineNodeDateResult>> nodeDateResultMap = nodeDateResultList.stream().collect(Collectors.groupingBy(item -> item.getNodeId()));
            List<MarketingNodeResultRspVo> nodeResultRspVoList = new ArrayList<>();
            // 组装引擎节点中按天的统计信息
            for (MarketingEngineNodeResult nodeResult : nodeResultList) {
                MarketingNodeResultRspVo nodeResultRspVo = JSONObject.parseObject(JSONObject.toJSONString(nodeResult), MarketingNodeResultRspVo.class);
                Integer nodeId = nodeResult.getNodeId();
                if (nodeDateResultMap != null && nodeDateResultMap.containsKey(nodeId)) {
                    List<MarketingEngineNodeDateResult> engineNodeDateResults = nodeDateResultMap.get(nodeId);
                    List<MarketingNodeDateResultRspVo> nodeDateResultRspVoList = JSONObject.parseArray(JSONObject.toJSONString(engineNodeDateResults), MarketingNodeDateResultRspVo.class);
                    nodeResultRspVo.setNodeDateResultRspVoList(nodeDateResultRspVoList);
                }
                nodeResultRspVoList.add(nodeResultRspVo);
            }
            dataResultRspVo.setNodeResultRspVoList(nodeResultRspVoList);
        }
        return dataResultRspVo;
    }

    /**
     * 根据时间维度查询引擎统计数据
     *
     * @param engineVersionId
     * @param startDate
     * @param endDate
     * @return
     */
    public MarketingDataResultRspVo getEngineResultByDate(Integer engineVersionId, Date startDate, Date endDate) {
        MarketingDataResultRspVo dataResultRspVo = new MarketingDataResultRspVo();
        List<MarketingEngineNodeDateResult> nodeDateResultList = nodeDateResultService.getNodeDateResultByDate(engineVersionId, startDate, endDate);
        if (nodeDateResultList != null && nodeDateResultList.isEmpty()) {
            List<MarketingNodeResultRspVo> nodeResultRspVoList = new ArrayList<>();
            int engineEnterNum = 0;
            int engineTouchNum = 0;
            int engineCompleteNum = 0;
            Map<Integer, List<MarketingEngineNodeDateResult>> nodeDateResultMap = nodeDateResultList.stream().collect(Collectors.groupingBy(item -> item.getNodeId()));
            for (Integer nodeId : nodeDateResultMap.keySet()) {
                MarketingNodeResultRspVo nodeResultRspVo = new MarketingNodeResultRspVo();
                List<MarketingEngineNodeDateResult> engineNodeDateResults = nodeDateResultMap.get(nodeId);
                int enterNum = 0;
                int touchNum = 0;
                int completeNum = 0;
                for (MarketingEngineNodeDateResult nodeDateResult : engineNodeDateResults) {
                    enterNum += nodeDateResult.getEnterNum();
                    touchNum += nodeDateResult.getTouchNum();
                    completeNum += nodeDateResult.getCompleteNum();
                }
                float completeRate = completeNum / enterNum;
                nodeResultRspVo.setNodeId(nodeId);
                nodeResultRspVo.setEnterNum(enterNum);
                nodeResultRspVo.setTouchNum(touchNum);
                nodeResultRspVo.setCompleteNum(completeNum);
                nodeResultRspVo.setCompleteRate(completeRate);
                List<MarketingNodeDateResultRspVo> nodeDateResultRspVoList = JSONObject.parseArray(JSONObject.toJSONString(engineNodeDateResults), MarketingNodeDateResultRspVo.class);
                nodeResultRspVo.setNodeDateResultRspVoList(nodeDateResultRspVoList);
                nodeResultRspVoList.add(nodeResultRspVo);

                engineEnterNum += enterNum;
                engineTouchNum += touchNum;
                engineCompleteNum += completeNum;
            }
            float engineCompleteRate = engineCompleteNum / engineEnterNum;
            dataResultRspVo.setEngineVersionId(engineVersionId);
            dataResultRspVo.setEnterNum(engineEnterNum);
            dataResultRspVo.setTouchNum(engineTouchNum);
            dataResultRspVo.setCompleteNum(engineCompleteNum);
            dataResultRspVo.setCompleteRate(engineCompleteRate);
            dataResultRspVo.setNodeResultRspVoList(nodeResultRspVoList);
        }
        return dataResultRspVo;
    }
}
