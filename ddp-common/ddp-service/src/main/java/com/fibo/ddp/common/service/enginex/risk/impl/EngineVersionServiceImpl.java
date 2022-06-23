package com.fibo.ddp.common.service.enginex.risk.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.enginex.risk.EngineNodeMapper;
import com.fibo.ddp.common.dao.enginex.risk.EngineVersionMapper;
import com.fibo.ddp.common.model.approval.Approval;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import com.fibo.ddp.common.service.approval.ApprovalConfigService;
import com.fibo.ddp.common.service.approval.ApprovalService;
import com.fibo.ddp.common.service.enginex.risk.EngineVersionService;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.utils.constant.ApprovalConsts;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.constant.enginex.EngineConst;
import com.fibo.ddp.common.utils.constant.enginex.EngineMsg;
import com.fibo.ddp.common.utils.constant.enginex.NodeTypeEnum;
import com.fibo.ddp.common.utils.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class EngineVersionServiceImpl implements EngineVersionService {

    @Autowired
    private EngineVersionMapper engineVersionMapper;
    @Autowired
    private EngineNodeMapper engineNodeMapper;
    @Resource
    private ApprovalService approvalService;
    @Resource
    private ApprovalConfigService approvalConfigService;
    @Autowired
    private RedisManager redisManager;
    @Value("${switch.use.cache}")
    private String cacheSwitch;

    @Override
    @Transactional
    public Map<String,Object> applyDeployEngine(Long versionId) {
        boolean isApproval = approvalConfigService.checkApproval(ApprovalConsts.ApprovalType.DECISION_FLOW_VERSION_DEPLOY);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("versionId", versionId);
        if (!isApproval){
            int count = this.deployEngine(versionId);
            if (count == 1) {
                resultMap.put("status", EngineMsg.STATUS_SUCCESS);
                resultMap.put("msg", EngineMsg.DEPLOY_SUCCESS);
            } else {
                resultMap.put("status", EngineMsg.STATUS_FAILED);
                resultMap.put("msg", EngineMsg.DEPLOY_FAILED);
            }
        }else {
            int b = engineVersionMapper.updateBootState(versionId, EngineConst.BOOT_STATE_DEPLOY_APPLY);
            //TODO需要存储审批表内容
            Approval approval = new Approval();
            approval.setApplyType(ApprovalConsts.ApprovalType.DECISION_FLOW_VERSION_DEPLOY);
            JSONObject detail = new JSONObject();
            detail.put("engineVersionId",versionId);
            approval.setApplyDetail(JSON.toJSONString(detail));
            JSONObject desc = new JSONObject();
            desc.put("remark","决策流版本发布申请");
            approval.setApplyDesc(JSON.toJSONString(desc));
            approvalService.addApproval(approval);
            resultMap.put("status", EngineMsg.STATUS_WAIT);
            resultMap.put("msg", EngineMsg.DEPLOY_WAIT);
        }
        return resultMap;
    }

    @Override
    @Transactional
    public boolean applyDeployFail(Long versionId) {
        engineVersionMapper.updateBootState(versionId,EngineConst.BOOT_STATE_UNDEPLOY);
        return true;
    }

    @Override
    @Transactional
    public boolean approvalCallBack(Long versionId, int result) {
        switch (result){
            //通过
            case ApprovalConsts.ApplyStatus
                    .PASS:
                this.deployEngine(versionId);
                break;
            //拒绝
            case ApprovalConsts.ApplyStatus
                    .DENY:
                this.applyDeployFail(versionId);
                break;
            //取消
            case ApprovalConsts.ApplyStatus
                    .CANCEL:

                break;
            case ApprovalConsts.ApplyStatus
                    .WAIT:

                break;
        }

        return true;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Transactional
    @Override
    public int deployEngine(Long versionId) {
        EngineVersion engineVersion = engineVersionMapper.selectByPrimaryKey(versionId);
        int count = 0;
        if (engineVersion != null) {
            //获取当前引擎编号
            long engineId = engineVersion.getEngineId();
            //查看是否有正在运行的版本,如果有,将其模型设置为未部署
            engineVersionMapper.undeployVersion(engineId);
            //当前版本
            int version = engineVersion.getVersion();
            //获取当前子版本
            int subVersion = engineVersion.getSubVersion();
            //插入完之后清除之前版本下的所有小版本
            Map map = new HashMap();
            map.put("engineId", engineId);
            map.put("version", version);
            if (subVersion != 0) {
                //获取当前引擎的最大版本
                EngineVersion latestEngineVersion = engineVersionMapper.getLatestEngineVersion(engineVersion);
                //说明此版本为小版本,则需要生成大版本
                engineVersion.setVersion(latestEngineVersion.getVersion() + 1);
                engineVersion.setSubVersion(0);
                engineVersion.setBootState(EngineConst.BOOT_STATE_DEPLOY);
                count = engineVersionMapper.updateByPrimaryKeySelective(engineVersion);
                if (count == 1) {
                    engineVersionMapper.cleanSubVersionByVersion(map);
                }
            } else {
                //说明此版本为大版本,将boot_state值置为1:正在运行
                engineVersion.setBootState(EngineConst.BOOT_STATE_DEPLOY);
                count = engineVersionMapper.updateByPrimaryKeySelective(engineVersion);
            }
        }
        return count;
    }

    @Override
    public int undeployEngine(Long versionId) {
        EngineVersion engineVersion = engineVersionMapper.selectByPrimaryKey(versionId);
        int count = 0;
        if (engineVersion != null) {
            engineVersion.setBootState(EngineConst.BOOT_STATE_UNDEPLOY);
            count = engineVersionMapper.updateByPrimaryKeySelective(engineVersion);
        }
        return count;
    }

    // V2
    @Override
    public List<EngineVersion> getEngineVersionListByEngineIdV2(Long engineId) {
        return engineVersionMapper.getEngineVersionListByEngineIdV2(engineId);
    }

    @Override
    public int update(EngineVersion engineVersion) {
        return engineVersionMapper.updateByPrimaryKey(engineVersion);
    }

    @Override
    public EngineVersion getLatestEngineSubVersion(EngineVersion engineVersion) {
        return engineVersionMapper.getLatestEngineSubVersion(engineVersion);
    }

    @Override
    public Long saveEngineVersion(EngineVersion engineVersion, List<EngineNode> nodeList) {
        int count = engineVersionMapper.insertEngineVersionAndReturnId(engineVersion);
        if (count == 1) {
            long versionId = engineVersion.getVersionId();
            //开始插入节点信息及知识库映射信息
            for (EngineNode engineNode : nodeList) {
                // 清空原有父节点id
                engineNode.setParentId(null);
                engineNode.setVersionId(versionId);
                if (engineNode.getNodeType().intValue() == NodeTypeEnum.POLICY.getValue() || engineNode.getNodeType().intValue() == NodeTypeEnum.NODE_COMPLEXRULE.getValue()) {
                    engineNodeMapper.insertNodeAndReturnId(engineNode);
                } else if (engineNode.getNodeType().intValue() == NodeTypeEnum.SCORECARD.getValue()) {
                    engineNodeMapper.insertNodeAndReturnId(engineNode);
                } else if (engineNode.getNodeType().intValue() == NodeTypeEnum.BLACKLIST.getValue() || engineNode.getNodeType().intValue() == NodeTypeEnum.WHITELIST.getValue()) {
                    //插入节点并且返回节点编号
                    engineNodeMapper.insertNodeAndReturnId(engineNode);
                } else {
                    //普通节点就直接插入
                    engineNodeMapper.insert(engineNode);
                }
            }
            return versionId;
        }
        return new Long(0);
    }

    @Override
    public EngineVersion selectByPrimaryKey(Long versionId) {
        return engineVersionMapper.selectByPrimaryKey(versionId);
    }

    @Override
    public boolean clear(Long versionId) {
        // 所有节点分为四类：
        // 第一类开始节点，只需要更新nextNode,
        // 第二类评分卡和规则节点,需要删除知识库映射关系,
        // 第三类,黑白名单需要删除字段映射关系,
        // 第四类,普通节点直接删除
        EngineNode startNode = null;
        List<Long> knowledges = new ArrayList<>();
        List<Long> blackWhites = new ArrayList<>();
        List<Long> commons = new ArrayList<>();

        // 第一步,获取版本所有节点
        List<EngineNode> engineNodes = engineNodeMapper.getEngineNodeListByEngineVersionId(versionId);
        if (CollectionUtil.isNotNullOrEmpty(engineNodes)) {
            for (EngineNode engineNode : engineNodes) {
                switch (engineNode.getNodeType()) {
                    case 1:
                        //开始节点
                        startNode = engineNode;
                        startNode.setNextNodes("");
                        break;
                    case 2:
                    case 4:
                    case 13:
                        //规则，评分卡
                        knowledges.add(engineNode.getNodeId());
                        commons.add(engineNode.getNodeId());
                        break;
                    case 5:
                    case 6:
                        //黑白名单
                        blackWhites.add(engineNode.getNodeId());
                        commons.add(engineNode.getNodeId());
                        break;
                    default:
                        commons.add(engineNode.getNodeId());
                        break;
                }
            }

            //第二步,清除评分卡，规则节点映射信息
            if (CollectionUtil.isNotNullOrEmpty(knowledges)) {
                // nodeKnowledgeMapper.deleteKnowledgesBatchByNodeIds(knowledges);
            }
            //第四步,删除节点
            if (CollectionUtil.isNotNullOrEmpty(commons)) {
                engineNodeMapper.deleteNodesByNodeIds(commons);
            }
            //第五步,将start节点的nextNode置空
            if (startNode != null) {
                engineNodeMapper.updateNextNodes(startNode);
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<EngineVersion> getEngineVersionByEngineId(Map<String, Object> paramMap) {
        return engineVersionMapper.getEngineVersionByEngineId(paramMap);
    }

    @Override
    public EngineVersion getRunningVersion(Long engineId) {
        EngineVersion engineVersion = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getForeignKey(TableEnum.T_ENGINE_VERSION, engineId);
            List<EngineVersion> list = redisManager.getByForeignKey(key, EngineVersion.class);
            Optional<EngineVersion> optional = list.stream().filter(item -> item.getBootState() == 1).findFirst();
            if(optional.isPresent()){
                engineVersion = optional.get();
            }
        } else {
            engineVersion = engineVersionMapper.getRunningVersion(engineId);
        }

        return engineVersion;
    }
}
