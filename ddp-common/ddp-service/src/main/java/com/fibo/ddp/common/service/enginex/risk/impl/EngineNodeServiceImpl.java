package com.fibo.ddp.common.service.enginex.risk.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.enginex.risk.EngineNodeMapper;
import com.fibo.ddp.common.dao.strategyx.aimodel.MachineLearningModelsMapper;
import com.fibo.ddp.common.dao.strategyx.listlibrary.ListDbMapper;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.risk.consts.EngineNodeConst;
import com.fibo.ddp.common.model.enginex.risk.response.param.*;
import com.fibo.ddp.common.model.strategyx.aimodel.MachineLearningModels;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesVersionVo;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesVo;
import com.fibo.ddp.common.model.strategyx.decisiontree.vo.DecisionTreeVersionVo;
import com.fibo.ddp.common.model.strategyx.decisiontree.vo.DecisionTreeVo;
import com.fibo.ddp.common.model.strategyx.guiderule.RuleInfo;
import com.fibo.ddp.common.model.strategyx.guiderule.RuleVersion;
import com.fibo.ddp.common.model.strategyx.guiderule.param.RuleSetNodeResultParam;
import com.fibo.ddp.common.model.strategyx.listlibrary.ListDb;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVersionVo;
import com.fibo.ddp.common.model.strategyx.scriptrule.RuleScriptVersion;
import com.fibo.ddp.common.service.enginex.risk.EngineNodeService;
import com.fibo.ddp.common.service.enginex.util.EngineNodeUtil;
import com.fibo.ddp.common.service.enginex.util.EngineUtil;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesService;
import com.fibo.ddp.common.service.strategyx.decisiontree.DecisionTreeService;
import com.fibo.ddp.common.service.strategyx.guiderule.RuleService;
import com.fibo.ddp.common.service.strategyx.guiderule.RuleVersionService;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardService;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardVersionService;
import com.fibo.ddp.common.service.strategyx.scriptrule.RuleScriptVersionService;
import com.fibo.ddp.common.utils.constant.CommonConst;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.constant.enginex.NodeTypeEnum;
import com.fibo.ddp.common.utils.util.CollectionUtil;
import com.fibo.ddp.common.utils.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EngineNodeServiceImpl extends ServiceImpl<EngineNodeMapper, EngineNode> implements EngineNodeService {

    @Autowired
    public ScorecardService scorecardService;
    @Autowired
    private DecisionTablesService decisionTablesService;
    @Autowired
    private DecisionTreeService decisionTreeService;
    @Autowired
    private MachineLearningModelsMapper machineLearningModelsMapper;
    @Autowired
    private ScorecardVersionService scorecardVersionService;
    @Autowired
    private RuleVersionService ruleVersionService;
    @Autowired
    private RuleScriptVersionService ruleScriptVersionService;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private EngineNodeMapper engineNodeMapper;
    @Autowired
    private ListDbMapper listDbMapper;
    @Autowired
    private RedisManager redisManager;
    @Value("${switch.use.cache}")
    private String cacheSwitch;

    @Override
    public List<EngineNode> getEngineNodeListByEngineVersionId(
            Long engineVersionId) {
        // TODO Auto-generated method stub
        return engineNodeMapper.getEngineNodeListByEngineVersionId(engineVersionId);
    }

    @Override
    public EngineNode findById(Long id) {
        // TODO Auto-generated method stub
        return engineNodeMapper.selectById(id);
    }

    @Override
    public boolean updateNode(EngineNode engineNode, Long targetId) {
        boolean flag = true;
        //第一步:插入节点
        int count = engineNodeMapper.updateById(engineNode);
        Map<String, Object> hashParam = new HashedMap();
        hashParam.put("snapshot",engineNode.getSnapshot());
        hashParam.put("nodeId",engineNode.getNodeId());
        engineNodeMapper.updateSnapshot(hashParam);
        if (count == 1) {
            //节点编号
            Long nodeId = engineNode.getNodeId();
            // 更新目标节点的parentId
            if (targetId != null) {
                EngineNode targetNode = findById(targetId);
                String targetParentId = targetNode.getParentId();
                if(StringUtils.isNotBlank(targetParentId)){
                    targetParentId = targetParentId.concat(CommonConst.SYMBOL_COMMA).concat(nodeId.toString());
                } else {
                    targetParentId = nodeId.toString();
                }

                Map<String, Object> map = new HashMap<>();
                map.put("nodeId", targetId);
                map.put("parentId", targetParentId);
                engineNodeMapper.updateParentIdByNodeId(map);
            }
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 保存引擎节点
     */
    @Override
    public boolean saveEngineNode(EngineNode eNode) {
        //第一步:插入节点
        int count = engineNodeMapper.insertNodeAndReturnId(eNode);
        return count == 1;
    }

    @Override
    public EngineNode saveEngineNodeV2(EngineNode eNode) {
        engineNodeMapper.insertNodeAndReturnIdV2(eNode);
        return eNode;
    }

    @Override
    public boolean updateNodeForNextOrderAndParams(List<EngineNode> eList) {
        int count = engineNodeMapper.updateNodeForNextOrderAndParams(eList);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int getMaxNodeOrder(Long versionId) {
        // TODO Auto-generated method stub
        return engineNodeMapper.getMaxNodeOrder(versionId);
    }

    @Override
    public boolean updateNodeForMove(EngineNode engineNode) {
        // TODO Auto-generated method stub
        int count = engineNodeMapper.updateNodePosition(engineNode);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int renameNode(Map<String, Object> param) {
        return engineNodeMapper.renameNode(param);
    }

    @Override
    public void removeNode(Long engineNodeId, String preEngineNodeId) {
        //得到此节点
        EngineNode engineNode = engineNodeMapper.selectById(engineNodeId);

        if (engineNode != null) {
            //判断此节点是否有上一节点
            if ("-1".equals(preEngineNodeId)) {
                //没有上一节点
                // if (engineNode.getNodeType() == NodeTypeEnum.SCORECARD.getDictValue() || engineNode.getNodeType() == NodeTypeEnum.POLICY.getDictValue() || NodeTypeEnum.NODE_COMPLEXRULE.getDictValue() == engineNode.getNodeType()) {
                //     deleteKnowledgeRef(engineNode);
                // }
                //不是评分卡或规则,直接删除节点
                engineNodeMapper.deleteById(engineNode.getNodeId());
            } else {
                // 更新当前节点的下一节点的parentId
                List<EngineNode> nodeList = engineNodeMapper.getEngineNodeListByEngineVersionId(engineNode.getVersionId());
                List<EngineNode> nodeListUpdate = EngineUtil.getUpdateParentIdNodes(engineNode, nodeList);
                if (CollectionUtil.isNotNullOrEmpty(nodeListUpdate)) {
                    //批量更新node节点
                    engineNodeMapper.updateNodeForNextOrderAndParams(nodeListUpdate);
                }

                // 更新当前节点的上一节点的nextNodes
                String[] preEngineNodeIdArr = preEngineNodeId.split(",");
                for(String preNodeId : preEngineNodeIdArr){
                    EngineNode preEngineNode = engineNodeMapper.selectById(Long.valueOf(preNodeId));
                    if (preEngineNode != null) {
                        //判断前一节点类型
                        int nodeType = preEngineNode.getNodeType();
                        switch (nodeType) {
                            case 3: // 分组节点
                            case 7: // 分流节点
                            case 19: // 并行节点
                            case 21: // 冠军挑战节点
                                handlePreClassifyOrSandBoxNode(engineNode, preEngineNode);
                                break;
                            default:
                                handlePreCommonNode(engineNode, preEngineNode);
                                break;
                        }
                    }
                }
                engineNodeMapper.deleteById(engineNode.getNodeId());
            }
        }
    }

    /**
     * 处理分群节点(前一个是分群或沙盒)
     *
     * @param engineNode
     * @param preEngineNode
     */
    private void handlePreClassifyOrSandBoxNode(EngineNode engineNode, EngineNode preEngineNode) {
        //第一步,删除节点的nextNode
        String nextNodes = preEngineNode.getNextNodes();
        if (StringUtil.isValidStr(nextNodes)) {
            nextNodes = nextNodes.replace(engineNode.getNodeCode(), "");
            //去掉nextnodes的前后逗号
            if (nextNodes.startsWith(CommonConst.SYMBOL_COMMA)) {
                nextNodes = nextNodes.substring(1);
            }
            if (nextNodes.endsWith(CommonConst.SYMBOL_COMMA)) {
                nextNodes = nextNodes.substring(0, nextNodes.length() - 1);
            }
            //如果删除了中间胡nextnode，则将,,替换为,
            preEngineNode.setNextNodes(nextNodes.replace(CommonConst.SYMBOL_COMMA + CommonConst.SYMBOL_COMMA, CommonConst.SYMBOL_COMMA));
        }
        //第二步,删除nodeJson中的当前节点信息
        String nodeJson = preEngineNode.getNodeJson();
        if (StringUtil.isValidStr(nodeJson)) {
            nodeJson = nodeJson.replace(engineNode.getNodeCode(), "");
            preEngineNode.setNodeJson(nodeJson);
        }
        //第三步,更新节点
        preEngineNode = EngineNodeUtil.boxEngineNodeJson(preEngineNode);
        engineNodeMapper.updateById(preEngineNode);
        //第四步,删除现有节点
//        engineNodeMapper.deleteByExample(engineNode);
    }

    /**
     * 删除节点(节点前一节点是开始节点)
     *
     * @param engineNode
     * @param preEngineNode
     */
    private void handlePreCommonNode(EngineNode engineNode, EngineNode preEngineNode) {
        //第一步,删除开始节点的nextNode
        preEngineNode.setNextNodes("");
        engineNodeMapper.updateById(preEngineNode);
        //第二步,删除当前节点
        // if (engineNode.getNodeType() == NodeTypeEnum.SCORECARD.getDictValue() || engineNode.getNodeType() == NodeTypeEnum.POLICY.getDictValue()) {
        //     deleteKnowledgeRef(engineNode);
        // }
//        engineNodeMapper.deleteByExample(engineNode);
    }

    /**
     * 删除连线
     */
    @Override
    public void removeLink(Long engineNodeId, Long preEngineNodeId) {
        //得到此节点
        EngineNode engineNode = engineNodeMapper.selectById(engineNodeId);
        //得到上一节点
        EngineNode preEngineNode = engineNodeMapper.selectById(preEngineNodeId);
        //删除连线时修改后一个节点的parentId
        String targetParentId = engineNode.getParentId();
        if(targetParentId.contains(CommonConst.SYMBOL_COMMA)){
            List<String> targetParentIdList = Arrays.asList(targetParentId.split(CommonConst.SYMBOL_COMMA));
            targetParentIdList = targetParentIdList.stream().filter(item -> !item.equals(preEngineNodeId.toString())).collect(Collectors.toList());
            targetParentId = StringUtils.join(targetParentIdList, CommonConst.SYMBOL_COMMA);
        } else {
            targetParentId = EngineNodeConst.REMOVE_LINK_PARENT_ID;
        }
        engineNode.setParentId(targetParentId);

        if (preEngineNode != null) {
            //判断前一节点类型
            int nodeType = preEngineNode.getNodeType();
            switch (nodeType) {
                case 3: //分群节点
                case 7: //沙盒节点
                case 19: //并行节点
                case 21: //冠军挑战
                    handlePreClassifyOrSandBoxLink(engineNode, preEngineNode);
                    break;
                default:
                    //其他节点只更新前一节点的nextNode
                    handleCommonLink(engineNode, preEngineNode);
                    break;
            }
        }
    }

    /**
     * 分群和沙盒的连线删除
     *
     * @param engineNode
     * @param preEngineNode
     */
    private void handlePreClassifyOrSandBoxLink(EngineNode engineNode, EngineNode preEngineNode) {
        String nextNodes = preEngineNode.getNextNodes();
        if (StringUtil.isValidStr(nextNodes)) {
            nextNodes = nextNodes.replace(engineNode.getNodeCode(), "");
            //去掉nextnodes的前后逗号
            if (nextNodes.startsWith(CommonConst.SYMBOL_COMMA)) {
                nextNodes = nextNodes.substring(1);
            }
            if (nextNodes.endsWith(CommonConst.SYMBOL_COMMA)) {
                nextNodes = nextNodes.substring(0, nextNodes.length() - 1);
            }
            //更新节点parentId信息
//            engineNode.setParentId(null);
            engineNodeMapper.updateById(engineNode);
            //如果删除了中间的nextnode，则将,,替换为,
            preEngineNode.setNextNodes(nextNodes.replace(CommonConst.SYMBOL_COMMA + CommonConst.SYMBOL_COMMA, CommonConst.SYMBOL_COMMA));
        }
        //第二步,删除nodeJson中的当前节点信息
        String nodeJson = preEngineNode.getNodeJson();
        if (StringUtil.isValidStr(nodeJson)) {
            nodeJson = nodeJson.replace(engineNode.getNodeCode(), "");
            preEngineNode.setNodeJson(nodeJson);
        }
        //第三步,更新节点
        preEngineNode = EngineNodeUtil.boxEngineNodeJson(preEngineNode);
        engineNodeMapper.updateById(preEngineNode);
    }

    /**
     * 删除普通节点连线问题
     *
     * @param engineNode
     * @param preEngineNode
     */
    private void handleCommonLink(EngineNode engineNode, EngineNode preEngineNode) {
        String nextNodes = preEngineNode.getNextNodes();
//        engineNode.setParentId(null);
        engineNodeMapper.updateById(engineNode);
        if (StringUtil.isValidStr(nextNodes)) {
            if (nextNodes.equals(engineNode.getNodeCode())){
                nextNodes = "";
            }else if (nextNodes.endsWith(","+engineNode.getNodeCode())){
                nextNodes=nextNodes.replace(","+engineNode.getNodeCode(),"");
            }else {
                nextNodes=nextNodes.replace(engineNode.getNodeCode()+",","");
            }
            preEngineNode.setNextNodes(nextNodes);
        }
        engineNodeMapper.updateById(preEngineNode);
    }

    @Override
    public List<EngineNode> getEngineTypedNodeListByEngineVersionId(Long versionId, List<Integer> types) {
        return engineNodeMapper.getEngineTypedNodeListByEngineVersionId(versionId, types);
    }

    @Override
    public int updateParentIdByNodeId(Long nodeId, String parentId) {
        if (nodeId != null && parentId != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("nodeId", nodeId);
            map.put("parentId", parentId);
            return engineNodeMapper.updateParentIdByNodeId(map);
        } else {
            return 0;
        }
    }

    @Override
    public List<NodeTypeResponse> getPreviousNodeOutput(Long nodeId) {
        List<NodeTypeResponse> nodeTypeList = new ArrayList<>();
        EngineNode engineNode = findById(nodeId);
        Long versionId = engineNode.getVersionId();
        List<EngineNode> nodeList = getEngineNodeListByEngineVersionId(versionId);
        Map<Long, EngineNode> nodeMap = EngineUtil.convertNodeList2Map(nodeList);
        Set<Integer> nodeTypeSet = nodeList.stream()
                .map(item -> item.getNodeType())
                .filter(item -> item != NodeTypeEnum.START.getValue()
                        && item != NodeTypeEnum.CLASSIFY.getValue()
                        && item != NodeTypeEnum.SANDBOX.getValue())
                .collect(Collectors.toSet());
        for(Integer nodeType : nodeTypeSet){
            NodeTypeEnum nodeTypeEnum = NodeTypeEnum.adapad(nodeType);
            Map<Long, EngineNode> previousNodeMap = new HashMap<>();
            recursivePreviousNode(engineNode, nodeMap, nodeTypeEnum, previousNodeMap);
            List<EngineNode> previousNodes = new ArrayList<>(previousNodeMap.values());
            List<NodeInfoResponse> nodeInfoList = new ArrayList<>();

            switch (nodeTypeEnum) {
                case POLICY:
                    nodeInfoList = getRuleNodeInfo(previousNodes, nodeTypeEnum);
                    break;
                case SCORECARD:
                case DECISION_TABLES:
                case DECISION_TREE:
                case MODEL_ENGINE:
                case CHILD_ENGINE:
                case DECISION:
                    nodeInfoList = getSingleStrategyNodeInfo(previousNodes, nodeTypeEnum);
                    break;
                case BLACKLIST:
                case WHITELIST:
                    nodeInfoList = getListDbNodeInfo(previousNodes, nodeTypeEnum);
                    break;
                default:
                    break;
            }

            NodeTypeResponse nodeTypeResponse = new NodeTypeResponse();
            nodeTypeResponse.setNodeType(nodeTypeEnum.getValue());
            nodeTypeResponse.setNodeTypeName(nodeTypeEnum.getType());
            nodeTypeResponse.setNodeInfoList(nodeInfoList);
            nodeTypeList.add(nodeTypeResponse);
        }
        return nodeTypeList;
    }

    @Override
    public void updateNodeSnapshot(HashMap<String, Object> paramMap) {
        engineNodeMapper.updateSnapshot(paramMap);
    }

    /**
     * 向上递归节点
     *
     * @param currentNode
     */
    private void recursivePreviousNode(EngineNode currentNode, Map<Long, EngineNode> nodeMap, NodeTypeEnum nodeTypeEnum, Map<Long, EngineNode> previousNodeMap) {
        if (StringUtils.isNotBlank(currentNode.getParentId())) {
            List<String> parentIdList = Arrays.asList(currentNode.getParentId().split(CommonConst.SYMBOL_COMMA));
            for(String parentId : parentIdList){
                EngineNode exNode = nodeMap.get(Long.valueOf(parentId));
                if (exNode != null && exNode.getNodeType() == nodeTypeEnum.getValue()) {
                    previousNodeMap.put(exNode.getNodeId(), exNode);
                }
                if (exNode != null) {
                    recursivePreviousNode(exNode, nodeMap, nodeTypeEnum, previousNodeMap);
                }
            }
        }
    }

    /**
     * 获取单个策略的节点信息集合
     * @param previousNodes
     * @param nodeTypeEnum
     * @return
     */
    private List<NodeInfoResponse> getSingleStrategyNodeInfo(List<EngineNode> previousNodes, NodeTypeEnum nodeTypeEnum) {
        List<NodeInfoResponse> nodeInfoList = new ArrayList<>();
        for(EngineNode engineNode : previousNodes){
            if(StringUtils.isBlank(engineNode.getNodeJson())){
                continue;
            }
            String resultFieldEn = "";
            String resultFieldCn = "";
            Integer valueType = 1;
            JSONObject nodeJson = JSON.parseObject(engineNode.getNodeJson());
            JSONObject jsonObject = null;
            switch (nodeTypeEnum) {
                case SCORECARD:
//                    Scorecard scorecard = scorecardService.getById();
                    jsonObject = JSON.parseObject(JSON.toJSONString(nodeJson.getJSONArray("scorecardList").get(0)));
                    ScorecardVersionVo versionVo = scorecardVersionService.queryById(jsonObject.getLong("versionId"));
                    if(StringUtils.isNotBlank(versionVo.getResultFieldEn())){
                        resultFieldEn = versionVo.getResultFieldEn();
                    } else {
                        resultFieldEn = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_" + versionVo.getId() + "_score";
                    }
                    resultFieldCn = "评分";
                    valueType = 1;
                    break;
                case DECISION_TABLES:
                    jsonObject = JSON.parseObject(JSON.toJSONString(nodeJson.getJSONArray("decisionTableList").get(0)));
                    DecisionTablesVo decisionTablesVo = decisionTablesService.queryById(jsonObject.getLong("decisionTableId"));
                    List<DecisionTablesVersionVo> versionList = decisionTablesVo.getDecisionTablesVersionList();
                    for (DecisionTablesVersionVo decisionTablesVersionVo : versionList) {
                        if (decisionTablesVersionVo.getId().equals(jsonObject.getLong("versionId"))){
                            resultFieldEn = decisionTablesVersionVo.getResultFieldEn();
                            if (StringUtils.isBlank(resultFieldEn)){
                                resultFieldEn = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_" + decisionTablesVersionVo.getId() + "_result";
                            }
                            break;
                        }
                    }
//                    DecisionTables decisionTables = decisionTablesService.getById(Long.valueOf(engineNode.getNodeJson()));
//                    resultFieldEn = decisionTables.getResultFieldEn();
                    resultFieldCn = "决策结果";
                    valueType = 2;
                    break;
                case DECISION_TREE:
                    //决策树
                    jsonObject = JSON.parseObject(JSON.toJSONString(nodeJson.getJSONArray("decisionTreeList").get(0)));
                    DecisionTreeVo decisionTree = decisionTreeService.queryById(jsonObject.getLong("decisionTreeId"));
                    List<DecisionTreeVersionVo> decisionTreeVersionList = decisionTree.getVersionList();
                    for (DecisionTreeVersionVo decisionTreeVersionVo : decisionTreeVersionList) {
                        if (decisionTreeVersionVo.getId().equals(jsonObject.getLong("versionId"))){
                            resultFieldEn = decisionTreeVersionVo.getResultFieldEn();
                            if (StringUtils.isBlank(resultFieldEn)){
                                resultFieldEn = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_" + decisionTreeVersionVo.getId() + "_result";
                            }
                            break;
                        }
                    }
                    resultFieldCn = "决策结果";
                    valueType = 2;
                    break;
                case MODEL_ENGINE:
                    MachineLearningModels machineLearningModels = machineLearningModelsMapper.selectById(Integer.valueOf(engineNode.getNodeJson()));
                    resultFieldEn = machineLearningModels.getResultFieldEn();
                    if (StringUtils.isBlank(resultFieldEn)){
                        resultFieldEn = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_" + machineLearningModels.getId() + "_result";
                    }
                    resultFieldCn = "预测结果";
                    valueType = 1;
                    break;
                case CHILD_ENGINE:
                    resultFieldEn = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_" + engineNode.getNodeJson() + "_result";
                    resultFieldCn = "引擎结果";
                    valueType = 2;
                    break;
                case DECISION:
                    resultFieldEn = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_result";
                    resultFieldCn = "决策选项结果";
                    valueType = 2;
                    break;
                default:
                    break;
            }

            NodeStrategyOutputResponse nodeStrategyOutputResponse = new NodeStrategyOutputResponse();
            nodeStrategyOutputResponse.setFieldEn(resultFieldEn);
            nodeStrategyOutputResponse.setFieldCn(resultFieldCn);
            nodeStrategyOutputResponse.setValueType(valueType);
            List<NodeStrategyOutputResponse> strategyOutputList = new ArrayList<>();
            strategyOutputList.add(nodeStrategyOutputResponse);

            NodeInfoResponse nodeInfoResponse = new NodeInfoResponse();
            nodeInfoResponse.setNodeId(engineNode.getNodeId());
            nodeInfoResponse.setNodeName(engineNode.getNodeName());
            nodeInfoResponse.setStrategyOutputList(strategyOutputList);
            nodeInfoList.add(nodeInfoResponse);
        }

        return nodeInfoList;
    }

    private List<NodeInfoResponse> getRuleNodeInfo(List<EngineNode> previousNodes, NodeTypeEnum nodeTypeEnum) {
        List<NodeInfoResponse> nodeInfoList = new ArrayList<>();
        for(EngineNode engineNode : previousNodes){
            List<RuleInfoOutputResponse> ruleInfoList = new ArrayList<>();
            List<Long> ruleIds = new ArrayList<>();
            List<Long> complexRuleVersionIds = new ArrayList<>();
            List<Long> scriptRuleVersionIds = new ArrayList<>();
            JSONObject nodeJson = JSONObject.parseObject(engineNode.getNodeJson());
            if(nodeJson == null){
                return nodeInfoList;
            }

            JSONArray jsonArray = null;
            int groupType = nodeJson.getInteger("groupType") == null ? Constants.ruleNode.EXECUTEGROUP : nodeJson.getInteger("groupType");
            if(groupType == Constants.ruleNode.MUTEXGROUP){
                jsonArray = nodeJson.getJSONObject("mutexGroup").getJSONArray("rules");
            } else {
                jsonArray = nodeJson.getJSONObject("executeGroup").getJSONArray("rules");
            }
            Integer difficulty = null;
            for(int i = 0; i < jsonArray.size(); i++){
                JSONObject ruleObj = jsonArray.getJSONObject(i);
                difficulty = ruleObj.getInteger("difficulty");
                Long  ruleVersionId = ruleObj.getLong("ruleVersionId");
                ruleIds.add(ruleObj.getLong("userId"));
                if (difficulty==null){
//                    ruleIds.add(ruleObj.getLong("userId"));
                    continue;
                }
                switch (difficulty){
                    case 1:
                        ruleIds.add(ruleObj.getLong("userId"));
                        break;
                    case 2:
                        complexRuleVersionIds.add(ruleVersionId);
                        break;
                    case 3:
                        scriptRuleVersionIds.add(ruleVersionId);
                        break;

                }
            }
            List<RuleSetNodeResultParam> ruleSetNodeResultParams = new ArrayList<>();
            List<RuleInfo> ruleList = new ArrayList<>();
            Map<Long,RuleInfo> ruleMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(ruleIds)){
                ruleList.addAll(ruleService.listByIds(ruleIds));
                if (CollectionUtils.isNotEmpty(ruleList)){
                    for (RuleInfo rule : ruleList) {
                        ruleMap.put(rule.getId(),rule);
                        if (rule.getDifficulty()==null||rule.getDifficulty()==1){
                            String resultEn = rule.getResultFieldEn();
                            String scoreEn = rule.getScoreFieldEn();
                            ruleSetNodeResultParams.add(new RuleSetNodeResultParam(rule.getId(),1,null,resultEn,scoreEn,rule.getCode(),rule.getName()));
                        }
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(complexRuleVersionIds)){
                List<RuleVersion> ruleVersionList = ruleVersionService.listByIds(complexRuleVersionIds);
                if (CollectionUtils.isNotEmpty(ruleVersionList)){
                    for (RuleVersion ruleVersion : ruleVersionList) {
                        String resultEn = ruleVersion.getResultFieldEn();
                        String scoreEn = ruleVersion.getScoreFieldEn();
                        RuleInfo rule = ruleMap.get(ruleVersion.getRuleId());
                        ruleSetNodeResultParams.add(new RuleSetNodeResultParam(ruleVersion.getRuleId(),2,ruleVersion.getId(),resultEn,scoreEn,rule.getCode(),rule.getName()));
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(scriptRuleVersionIds)){
                List<RuleScriptVersion> ruleScriptVersionList = ruleScriptVersionService.listByIds(scriptRuleVersionIds);
                if (CollectionUtils.isNotEmpty(ruleScriptVersionList)){
                    for (RuleScriptVersion ruleVersion : ruleScriptVersionList) {
                        String resultEn = "hitResult";
                        String scoreEn = "scoreResult";
                        RuleInfo rule = ruleMap.get(ruleVersion.getRuleId());
                        ruleSetNodeResultParams.add(new RuleSetNodeResultParam(ruleVersion.getRuleId(),3,ruleVersion.getId(),resultEn,scoreEn,rule.getCode(),rule.getName()));
                    }
                }
            }

//            List<Rule> ruleList = ruleMapper.selectnodeByInRoleid(ruleIds);
            for(RuleSetNodeResultParam rule : ruleSetNodeResultParams){
                RuleInfoOutputResponse ruleInfoOutputResponse = new RuleInfoOutputResponse();
                ruleInfoOutputResponse.setId(rule.getId());
                ruleInfoOutputResponse.setCode(rule.getCode());
                ruleInfoOutputResponse.setName(rule.getName());

                List<NodeStrategyOutputResponse> ruleOutputList = new ArrayList<>();
                NodeStrategyOutputResponse hitOutputResponse = new NodeStrategyOutputResponse();
                String resultEn = rule.getResultEn();
                if (StringUtils.isBlank(resultEn)){
                    if (null !=rule.getVersionId()){
                        resultEn = "rule_"+rule.getDifficulty()+"_"+rule.getId()+"_"+rule.getVersionId()+"_hitResult";
                    }else {
                        resultEn = "rule_"+rule.getDifficulty()+"_"+rule.getId()+"_hitResult";
                    }

                }
                hitOutputResponse.setFieldEn(resultEn);
                hitOutputResponse.setFieldCn("是否命中");
                hitOutputResponse.setValueType(2);
                ruleOutputList.add(hitOutputResponse);

                NodeStrategyOutputResponse scoreOutputResponse = new NodeStrategyOutputResponse();
                String scoreEn = rule.getScoreEn();
                if (StringUtils.isBlank(scoreEn)){
                    if (null !=rule.getVersionId()){
                        scoreEn = "rule_"+rule.getDifficulty()+"_"+rule.getId()+"_"+rule.getVersionId()+"_score";
                    }else {
                        scoreEn = "rule_"+rule.getDifficulty()+"_"+rule.getId()+"_score";
                    }
                }
                scoreOutputResponse.setFieldEn(scoreEn);
                scoreOutputResponse.setFieldCn("得分");
                scoreOutputResponse.setValueType(1);
                ruleOutputList.add(scoreOutputResponse);
                ruleInfoOutputResponse.setRuleOutputList(ruleOutputList);

                ruleInfoList.add(ruleInfoOutputResponse);
            }

            List<NodeStrategyOutputResponse> statisticsOutputList = new ArrayList<>();
            NodeStrategyOutputResponse statisticsSizeOutput = new NodeStrategyOutputResponse();
            statisticsSizeOutput.setFieldEn(engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_size");
            statisticsSizeOutput.setFieldCn("规则命中个数");
            statisticsSizeOutput.setValueType(1);
            statisticsOutputList.add(statisticsSizeOutput);

            NodeStrategyOutputResponse statisticsScoreOutput = new NodeStrategyOutputResponse();
            statisticsScoreOutput.setFieldEn(engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_score");
            statisticsScoreOutput.setFieldCn("规则总得分");
            statisticsScoreOutput.setValueType(1);
            statisticsOutputList.add(statisticsScoreOutput);

            RuleOutputResponse ruleOutput = new RuleOutputResponse();
            ruleOutput.setStatisticsOutputList(statisticsOutputList);
            ruleOutput.setRuleInfoList(ruleInfoList);

            NodeInfoResponse nodeInfoResponse = new NodeInfoResponse();
            nodeInfoResponse.setNodeId(engineNode.getNodeId());
            nodeInfoResponse.setNodeName(engineNode.getNodeName());
            nodeInfoResponse.setRuleOutput(ruleOutput);
            nodeInfoList.add(nodeInfoResponse);
        }

        return nodeInfoList;
    }

    private List<NodeInfoResponse> getListDbNodeInfo(List<EngineNode> previousNodes, NodeTypeEnum nodeTypeEnum) {
        List<NodeInfoResponse> nodeInfoList = new ArrayList<>();
        for(EngineNode engineNode : previousNodes){
            List<ListDbInfoOutputResponse> listDbInfoOutput = new ArrayList<>();

            List<Long> listDbIds = new ArrayList<>();
            String nodeJson = engineNode.getNodeJson();
            if (StringUtils.isBlank(nodeJson)){
                continue;
            }
            JSONArray jsonArray = JSON.parseObject(nodeJson).getJSONArray("listDbList");
            if (jsonArray!=null&&jsonArray.size()>0){
                for (Object o : jsonArray) {
                    if (o==null){
                        continue;
                    }
                    Long id = JSON.parseObject(JSON.toJSONString(o)).getLong("listDbId");
                    if (id==null){
                        continue;
                    }
                    listDbIds.add(id);
                }
            }
            if (listDbIds.isEmpty()){
                continue;
            }
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("listDbIds", listDbIds);
            List<ListDb> listDbList = listDbMapper.findListDbByIds(paramMap);
            for(ListDb listDb : listDbList){
                ListDbInfoOutputResponse listDbInfoOutputResponse = new ListDbInfoOutputResponse();
                listDbInfoOutputResponse.setId(listDb.getId());
                listDbInfoOutputResponse.setListName(listDb.getListName());

                List<NodeStrategyOutputResponse> listDbOutputList = new ArrayList<>();
                NodeStrategyOutputResponse hitOutputResponse = new NodeStrategyOutputResponse();
                hitOutputResponse.setFieldEn(listDb.getResultFieldEn());
                hitOutputResponse.setFieldCn("是否命中");
                listDbOutputList.add(hitOutputResponse);
                listDbInfoOutputResponse.setListDbOutputList(listDbOutputList);

                listDbInfoOutput.add(listDbInfoOutputResponse);
            }

            List<NodeStrategyOutputResponse> statisticsOutputList = new ArrayList<>();
            NodeStrategyOutputResponse statisticsSizeOutput = new NodeStrategyOutputResponse();
            statisticsSizeOutput.setFieldEn(engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_size");
            statisticsSizeOutput.setFieldCn("名单库命中个数");
            statisticsOutputList.add(statisticsSizeOutput);

            ListDbOutputResponse listDbOutput = new ListDbOutputResponse();
            listDbOutput.setStatisticsOutputList(statisticsOutputList);
            listDbOutput.setListDbInfoOutput(listDbInfoOutput);

            NodeInfoResponse nodeInfoResponse = new NodeInfoResponse();
            nodeInfoResponse.setNodeId(engineNode.getNodeId());
            nodeInfoResponse.setNodeName(engineNode.getNodeName());
            nodeInfoResponse.setListDbOutput(listDbOutput);
            nodeInfoList.add(nodeInfoResponse);
        }

        return nodeInfoList;
    }

    @Override
    public List<EngineNode> getEngineNodeListByVersionId(Long versionId) {
        List<EngineNode> engineNodeList = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getForeignKey(TableEnum.T_ENGINE_NODE, versionId);
            engineNodeList = redisManager.getByForeignKey(key, EngineNode.class);
            if(engineNodeList != null){
                // 按node_order升序排序
                engineNodeList = engineNodeList.stream().sorted(Comparator.comparing(EngineNode::getNodeOrder)).collect(Collectors.toList());
            }
        } else {
            engineNodeList = engineNodeMapper.getEngineNodeListByVersionId(versionId);
        }

        return engineNodeList;
    }
}
