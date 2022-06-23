package com.fibo.ddp.enginex.runner.node.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.datax.datamanage.Field;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardDetail;
import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardDimension;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardDetailVo;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardDimensionVo;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVersionVo;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVo;
import com.fibo.ddp.common.service.datax.datamanage.FieldService;
import com.fibo.ddp.common.service.datax.runner.CommonService;
import com.fibo.ddp.common.service.datax.runner.ExecuteUtils;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardDetailService;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardDimensionService;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardService;
import com.fibo.ddp.common.utils.util.runner.JevalUtil;
import com.fibo.ddp.common.utils.util.runner.StrUtils;
import com.fibo.ddp.common.utils.util.runner.jeval.EvaluationException;
import com.fibo.ddp.enginex.runner.node.EngineRunnerNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScorecardNode implements EngineRunnerNode {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommonService commonService;

    @Resource
    public FieldService fieldService;

    @Resource
    public ScorecardService scorecardService;

    @Autowired
    private ScorecardDimensionService scorecardDimensionService;  // 维度

    @Autowired
    private ScorecardDetailService scorecardDetailService;  // 明细

    private List<Long> getExecuteVersionIdList(EngineNode engineNode) {
        return ExecuteUtils.getExecuteIdList(engineNode,"versionId");
    }

    @Override
    public void getNodeField(EngineNode engineNode, Map<String, Object> inputParam) {
        List<Long> versionIdList = getExecuteVersionIdList(engineNode);
        Set<Long> fieldIdSet = new HashSet<>();
        for (Long versionId : versionIdList) {
            List<ScorecardDimension> scorecardDimensions = scorecardDimensionService.getDimensionListByVersionId(versionId);

            List<Integer> dimensionIds = scorecardDimensions.stream().map(item -> item.getId()).collect(Collectors.toList());

            List<ScorecardDetail> scorecardDetails = scorecardDetailService.getDetailListByDimensionIds(dimensionIds);
            fieldIdSet.addAll(scorecardDetails.stream().map(item -> Long.valueOf(item.getFieldId().toString())).collect(Collectors.toSet()));
        }
        List<Long> ids = new ArrayList<>(fieldIdSet);

        commonService.getFieldByIds(ids, inputParam);
    }

    @Override
    public void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap) {
        //监控中心--节点快照信息
        if (engineNode != null && engineNode.getSnapshot() != null) {
            outMap.put("nodeSnapshot", engineNode.getSnapshot());
        }
        List<Long> versionIdList = getExecuteVersionIdList(engineNode);
        for (Long versionId : versionIdList) {
            ScorecardVo scorecard = scorecardService.queryExecuteScorecard(versionId);
            JSONObject nodeInfo = new JSONObject();
            nodeInfo.put("engineNode", engineNode);
            nodeInfo.put("nodeId", engineNode.getNodeId());
            nodeInfo.put("nodeName", engineNode.getNodeName());
            nodeInfo.put("nodeType", engineNode.getNodeType());
            outMap.put("nodeInfo", nodeInfo);

            List<ScorecardDetail> hitDetailList = new ArrayList<>(); // 命中的评分字段列表
            ScorecardVersionVo versionVo = scorecard.getExecuteVersion();
            List<ScorecardDimensionVo> scorecardDimensions = new ArrayList<>();
            if (versionVo != null) {
                //监控中心 == 策略层面快照信息
                if (versionVo != null && versionVo.getSnapshot() != null) {
                    outMap.put("scorecardStrategy", versionVo.getSnapshot());
                }
                scorecardDimensions = versionVo.getScorecardDimension();
                for (ScorecardDimensionVo scorecardDimensionVo : scorecardDimensions) {
                    List<ScorecardDetailVo> scorecardDetailVoList = scorecardDimensionVo.getChildren();
                    scorecardHit(hitDetailList, 0, scorecardDetailVoList, inputParam);
                }
            }
            Double totalScore = 0d;
            List<JSONObject> scoreDetail = new ArrayList<>();
            for (ScorecardDimension scorecardDimension : scorecardDimensions) {
                Double dimensionScore = 0d;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("dimensionName", scorecardDimension.getDimensionName());
                jsonObject.put("dimensionId", scorecardDimension.getId());
                for (ScorecardDetail scorecardDetail : hitDetailList) {
                    if (!scorecardDetail.getDimensionId().equals(scorecardDimension.getId())) {
                        continue;
                    }
                    String fieldEn = scorecardDetail.getFieldEn();
                    Integer scoreCalculateType = scorecard.getScoreCalculateType();
                    Integer calculateType = scorecardDetail.getCalculateType();
                    // 计算方式为 得分
                    if (calculateType == 1) {
                        if (scoreCalculateType == 1) { // 评分计算方式为 求和
                            Double value = scorecardDetail.getScore();
                            totalScore += value;
                            dimensionScore += value;
                        } else if (scoreCalculateType == 2) { // 评分计算方式为 加权求和
                            Double value = scorecardDetail.getScore() * scorecardDimension.getWeight();
                            totalScore += value;
                            dimensionScore += value;
                        }
                    }
                    // 计算方式为 系数
                    else if (calculateType == 2) {
                        if (scoreCalculateType == 1) { // 评分计算方式为 求和
                            Double value = Double.valueOf(inputParam.get(fieldEn).toString()) * scorecardDetail.getCoefficient();
                            totalScore += value;
                            dimensionScore += value;
                        } else if (scoreCalculateType == 2) { // 评分计算方式为 加权求和
                            Double value = Double.valueOf(inputParam.get(fieldEn).toString()) * scorecardDetail.getCoefficient() * scorecardDimension.getWeight();
                            totalScore += value;
                            dimensionScore += value;
                        }
                    }
                    // 计算方式为 自定义
                    else if (calculateType == 3) {
                        double value = 0d;
                        String custom = scorecardDetail.getCustom();
                        if (custom != null && !"".equals(custom)) {
                            Double result = StrUtils.strToDouble(ExecuteUtils.getObjFromScript(inputParam, custom).toString());
                            if (result != null) {
                                value = result;
                            }
                        }
                        totalScore += value;
                        dimensionScore += value;
                    }
                }
                jsonObject.put("score", dimensionScore);
                scoreDetail.add(jsonObject);
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nodeId", engineNode.getNodeId());
            jsonObject.put("nodeName", engineNode.getNodeName());
            jsonObject.put("cardId", scorecard.getId());
            jsonObject.put("cardName", scorecard.getName());
            jsonObject.put("cardCode", scorecard.getCode());
            String versionCode = "";
            Long cardVersionId = null;
            if (versionVo != null) {
                versionCode = versionVo.getVersionCode();
                cardVersionId = versionVo.getId();
            }

            jsonObject.put("cardVersion", versionCode);
            jsonObject.put("cardVersionId",cardVersionId);
            jsonObject.put("desc", scorecard.getDescription());
            jsonObject.put("score", totalScore);
            jsonObject.put("scoreDetail", scoreDetail);
            //给入参中放入评分卡执行结果
            String resultFieldEn = versionVo.getResultFieldEn();
            if (StringUtils.isBlank(resultFieldEn)) {
                resultFieldEn = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_" + versionVo.getId() + "_score";
            }
            inputParam.put(resultFieldEn, totalScore);
            inputParam.put(scorecard.getCode(), totalScore);
            List<JSONObject> fieldList = new ArrayList<>();
            JSONObject scoreResult = new JSONObject();
            scoreResult.put(resultFieldEn, totalScore);
            fieldList.add(scoreResult);
            if (totalScore != 0) {
                List<JSONObject> jsonObjects = scorecardService.setOutput(scorecard.getId(), inputParam);
                fieldList.addAll(jsonObjects);
            }
            jsonObject.put("fieldList", fieldList);
            //监控中心==》将评分卡的执行结果得分明细放入 输出变量池 用于存入hbase
            outMap.put("nodeResult", jsonObject);
            if (outMap.containsKey("scoreJson")) {
                JSONArray resultJson = (JSONArray) outMap.get("scoreJson");
                resultJson.add(jsonObject);
            } else {
                JSONArray resultJson = new JSONArray();
                resultJson.add(jsonObject);
                outMap.put("scoreJson", resultJson);
            }
            terminalCondition(engineNode,inputParam,outMap,totalScore);
        }
    }

    /**
     * 评分卡命中判断
     *
     * @param hitDetailList
     * @param detailId
     * @param scorecardDetailVoList
     * @param inputParam
     */
    private void scorecardHit(List<ScorecardDetail> hitDetailList, Integer detailId, List<ScorecardDetailVo> scorecardDetailVoList, Map<String, Object> inputParam) {
        List<ScorecardDetailVo> scorecardDetailVos = scorecardDetailVoList.stream().filter(item -> item.getParentId().equals(detailId)).collect(Collectors.toList());
        for (ScorecardDetailVo scorecardDetailVo : scorecardDetailVos) {
            Field field = fieldService.queryById(Long.valueOf(scorecardDetailVo.getFieldId()));
            String fieldEn = field.getFieldEn();
            scorecardDetailVo.setFieldEn(fieldEn);

            String condition = scorecardDetailVo.getCondition();
            Boolean isHit = isScoreFieldValue(inputParam, fieldEn, condition);
            if (isHit) {
                if (scorecardDetailVo.getType() == 1) {
                    hitDetailList.add(scorecardDetailVo);
                } else {
                    this.scorecardHit(hitDetailList, scorecardDetailVo.getId(), scorecardDetailVoList, inputParam);
                }
            }
        }
    }

    /**
     * 评分卡字段是否在区间内
     *
     * @param map
     * @param fieldCode
     * @param condition
     * @return
     */
    private Boolean isScoreFieldValue(Map<String, Object> map, String fieldCode, String condition) {
        if ("(,)".equals(condition)) {
            return true;
        }
        String exp = JevalUtil.getNumericInterval(condition, fieldCode);
        try {
            if (JevalUtil.evaluateBoolean(exp, map)) {
                return true;
            }
        } catch (EvaluationException e) {
            e.printStackTrace();
            logger.error("请求异常", e);
        }
        return false;
    }

    private void terminalCondition(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap,Object executeResult) {
        String resultKey = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_terminal_score";
        Map<String,Object> map = new HashMap<>();
        map.put(resultKey,executeResult);
        ExecuteUtils.terminalCondition(engineNode,inputParam,outMap, map);
    }
}
