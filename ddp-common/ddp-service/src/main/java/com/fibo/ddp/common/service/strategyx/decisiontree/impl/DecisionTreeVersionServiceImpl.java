package com.fibo.ddp.common.service.strategyx.decisiontree.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.decisiontree.DecisionTreeVersionMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.decisiontree.DecisionTreeDetail;
import com.fibo.ddp.common.model.strategyx.decisiontree.DecisionTreeDetailCondition;
import com.fibo.ddp.common.model.strategyx.decisiontree.DecisionTreeVersion;
import com.fibo.ddp.common.model.strategyx.decisiontree.vo.DecisionTreeVersionVo;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.decisiontree.DecisionTreeDetailConditionService;
import com.fibo.ddp.common.service.strategyx.decisiontree.DecisionTreeDetailService;
import com.fibo.ddp.common.service.strategyx.decisiontree.DecisionTreeVersionService;
import com.fibo.ddp.common.service.strategyx.strategyout.StrategyOutputService;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.util.strategyx.CustomValueUtils;
import com.fibo.ddp.common.utils.constant.strategyx.StrategyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("decisionTreeVersionService")
public class DecisionTreeVersionServiceImpl extends ServiceImpl<DecisionTreeVersionMapper, DecisionTreeVersion> implements DecisionTreeVersionService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DecisionTreeVersionMapper versionMapper;
    @Autowired
    private DecisionTreeDetailService detailService;
    @Autowired
    private DecisionTreeDetailConditionService conditionService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private StrategyOutputService outputService;
    @Autowired
    private RedisManager redisManager;
    @Value("${switch.use.cache}")
    private String cacheSwitch;

//    @Override
//    public DecisionTreeVersionVo queryById(Long id) {
//        DecisionTreeVersion version = this.getById(id);
//        if (version == null) {
//            return null;
//        }
//        DecisionTreeVersionVo decisionTreeVersionVo = new DecisionTreeVersionVo();
//        BeanUtils.copyProperties(version, decisionTreeVersionVo);
//        //查询详情
//        decisionTreeVersionVo.setDetailList(detailService.queryByVersionId(id));
//        //查询策略输出
//        List<StrategyOutput> strategyOutputs = outputService.queryByTactics(new StrategyOutput(id, StrategyType.DECISION_TREE));
//        decisionTreeVersionVo.setStrategyOutputList(strategyOutputs);
//        return decisionTreeVersionVo;
//    }

    @Override
    public DecisionTreeVersionVo queryById(Long id) {
        DecisionTreeVersion version ;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getPrimaryKey(TableEnum.T_DECISION_TREE_VERSION, id);
            version = redisManager.getByPrimaryKey(key, DecisionTreeVersion.class);
        } else {
            version = this.getById(id);
        }
        if (version == null) {
            return null;
        }
        DecisionTreeVersionVo decisionTreeVersionVo = new DecisionTreeVersionVo();
        BeanUtils.copyProperties(version, decisionTreeVersionVo);
        //查询详情
        decisionTreeVersionVo.setDetailList(detailService.queryByVersionId(id));
        //查询策略输出
        List<StrategyOutput> strategyOutputs = outputService.queryByTactics(new StrategyOutput(id, StrategyType.DECISION_TREE));
        decisionTreeVersionVo.setStrategyOutputList(strategyOutputs);
        return decisionTreeVersionVo;
    }

    @Override
    public List<DecisionTreeVersionVo> queryVersionListByDecisionTreeId(Long decisionTreeId) {
        LambdaQueryWrapper<DecisionTreeVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DecisionTreeVersion::getDecisionTreeId, decisionTreeId);
        queryWrapper.eq(DecisionTreeVersion::getStatus, 1);
        queryWrapper.orderByDesc(DecisionTreeVersion::getId);
        List<DecisionTreeVersion> ruleVersionList = versionMapper.selectList(queryWrapper);
        List<DecisionTreeVersionVo> DecisionTreeVersionVoList = new ArrayList<>();
        for (DecisionTreeVersion ruleVersion : ruleVersionList) {
            DecisionTreeVersionVo versionVo = new DecisionTreeVersionVo();
            BeanUtils.copyProperties(ruleVersion, versionVo);
            DecisionTreeVersionVoList.add(versionVo);
        }
        return DecisionTreeVersionVoList;
    }

    @Override
    public List<String> queryFieldEnByVersionId(Long versionId) {
        Set<String> fieldEns = new HashSet<>();
        LambdaQueryWrapper<DecisionTreeDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DecisionTreeDetail::getDecisionTreeVersionId, versionId);
        List<DecisionTreeDetail> list = detailService.list(queryWrapper);
        Set<Long> detailIds = new HashSet<>();
        for (DecisionTreeDetail detail : list) {
            if (detail.getFieldEn().contains(".") && !detail.getFieldEn().startsWith("%")) {
                fieldEns.add(detail.getFieldEn().split("\\.")[0]);
            } else {
                fieldEns.add(detail.getFieldEn());
            }
//            fieldEns.add(detail.getFieldEn());
//            detailIds.add(detail.getUserId());
        }
        LambdaQueryWrapper<DecisionTreeDetailCondition> conditionWrapper = new LambdaQueryWrapper<>();
        if (detailIds.size() > 0) {
            conditionWrapper.in(DecisionTreeDetailCondition::getDetailId, detailIds);
            List<DecisionTreeDetailCondition> conditionList = conditionService.list(conditionWrapper);
            for (DecisionTreeDetailCondition condition : conditionList) {
                if (condition.getVariableType() == null || condition.getVariableType() == 1) {
                    continue;
                }
                if (condition.getVariableType() == 2) {
//                    fieldEns.add(condition.getFieldValue());
                    String fieldValue = condition.getFieldValue();
                    if (fieldValue.contains(".") && !fieldValue.startsWith("%")) {
                        fieldEns.add(fieldValue.split("\\.")[0]);
                    } else {
                        fieldEns.add(fieldValue);
                    }
                } else if (condition.getVariableType() == 3) {
                    fieldEns.addAll(CustomValueUtils.getFieldEnSet(condition.getFieldValue()));
                }
            }
        }
        return new ArrayList<>(fieldEns);
    }

    @Override
    @Transactional
    public int addVersionList(List<DecisionTreeVersionVo> versionList) {
        int result = 0;
        for (DecisionTreeVersionVo versionVo : versionList) {
            boolean b = addVersion(versionVo);
            if (b) {
                result++;
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean addVersion(DecisionTreeVersionVo version) {
        SysUser loginSysUser = SessionManager.getLoginAccount();
        version.setOrganId(loginSysUser.getOrganId());
        version.setCreateUserId(loginSysUser.getUserId());
        version.setUpdateUserId(loginSysUser.getUserId());
        version.setCreateTime(null);
        version.setUpdateTime(null);
        version.setStatus(1);
        if (version.getVersionCode() == null) {
            version.setVersionCode("V:0");
        }
        if (version.getDescription() == null) {
            version.setDescription("初始版本");
        }
        int insert = versionMapper.insert(version);
        if (insert > 0) {
            boolean result = this.addVersionDetail(version);
            if (result) {
                saveSnapshot(version.getId());
            }
            return true;
        } else {
            logger.error("新增决策树版本失败{}", version);
        }
        return false;
    }

    @Transactional
    public boolean addVersionDetail(DecisionTreeVersionVo version) {
        //插入子表（detail表）数据
        boolean b = detailService.addDecisionTreeDetailList(version.getId(), version.getDetailList());
        //添加输出字段
        List<StrategyOutput> strategyOutputList = version.getStrategyOutputList();
        if (strategyOutputList != null && strategyOutputList.size() > 0) {
            outputService.insertTacticsOutput(version.getId(), strategyOutputList);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean copyVersion(DecisionTreeVersionVo version) {
        DecisionTreeVersionVo versionVo = this.queryById(version.getId());
        versionVo.setId(null);
        versionVo.setVersionCode(version.getVersionCode());
        versionVo.setDescription(version.getDescription());
        return this.addVersion(versionVo);
    }

    @Override
    @Transactional
    public boolean updateVersion(DecisionTreeVersionVo version) {
        Long versionId = version.getId();
        if (versionId == null) {
            return false;
        }
        SysUser loginSysUser = SessionManager.getLoginAccount();
        version.setUpdateUserId(loginSysUser.getUserId());
        //修改版本主表
        versionMapper.updateById(version);
        //修改条件表
        detailService.updateByVersionId(versionId, version.getDetailList());
        //修改策略输出
        outputService.updateTacticsOutput(versionId, version.getStrategyOutputList(), StrategyType.DECISION_TREE);
        //存快照
        saveSnapshot(version.getId());
        return true;
    }

    @Override
    @Transactional
    public boolean updateStatus(StatusParam statusParam) {
        LambdaQueryWrapper<DecisionTreeVersion> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.in(DecisionTreeVersion::getId, statusParam.getIds());
        updateWrapper.eq(DecisionTreeVersion::getDecisionTreeId, statusParam.getStrategyId());
        DecisionTreeVersion ruleVersion = new DecisionTreeVersion();
        ruleVersion.setStatus(statusParam.getStatus());
        boolean update = this.update(ruleVersion, updateWrapper);
        return update;
    }

    private boolean saveSnapshot(Long versionId) {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                LambdaUpdateWrapper<DecisionTreeVersion> wrapper = new LambdaUpdateWrapper<>();
                DecisionTreeVersionVo versionVo = queryById(versionId);
                versionVo.setSnapshot(null);
                wrapper.eq(DecisionTreeVersion::getId, versionId).set(DecisionTreeVersion::getSnapshot, JSON.toJSONString(versionVo));
                versionMapper.update(null, wrapper);
            }
        });
        return true;
    }

    @Override
    public List<String> queryFieldEnByVersionIdRunner(Long versionId) {
        Set<String> fieldEns = new HashSet<>();

        List<DecisionTreeDetail> list = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getForeignKey(TableEnum.T_DECISION_TREE_DETAIL, versionId);
            list = redisManager.getByForeignKey(key, DecisionTreeDetail.class);
        } else {
            LambdaQueryWrapper<DecisionTreeDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DecisionTreeDetail::getDecisionTreeVersionId, versionId);
            list = detailService.list(queryWrapper);
        }
        if(list != null&&!list.isEmpty()){
            Set<Long> detailIds = new HashSet<>();
            for (DecisionTreeDetail detail : list) {
                fieldEns.add(detail.getFieldEn());
                detailIds.add(detail.getId());
            }
            fieldEns.addAll(conditionService.queryFieldEnByDetailIds(detailIds));
        }
        return new ArrayList<>(fieldEns);
    }
}
