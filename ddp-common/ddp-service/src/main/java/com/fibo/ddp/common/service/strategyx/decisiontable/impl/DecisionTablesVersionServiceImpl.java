package com.fibo.ddp.common.service.strategyx.decisiontable.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.decisiontable.DecisionTablesVersionMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTablesDetail;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTablesDetailCondition;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTablesVersion;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesResultVo;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesVersionVo;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesDetailConditionService;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesDetailService;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesResultService;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesVersionService;
import com.fibo.ddp.common.service.strategyx.strategyout.StrategyOutputService;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.constant.strategyx.DecisionTablesDetailConst;
import com.fibo.ddp.common.utils.constant.strategyx.StrategyType;
import com.fibo.ddp.common.utils.util.strategyx.CustomValueUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * (DecisionTablesVersion)表服务实现类
 */
@Service("tDecisionTablesVersionService")
public class DecisionTablesVersionServiceImpl extends ServiceImpl<DecisionTablesVersionMapper, DecisionTablesVersion> implements DecisionTablesVersionService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DecisionTablesVersionMapper versionMapper;
    @Resource
    private DecisionTablesDetailService detailService;
    @Resource
    private DecisionTablesDetailConditionService conditionService;
    @Resource
    private DecisionTablesResultService resultService;
    @Resource
    private StrategyOutputService outputService;
    @Autowired
    private RedisManager redisManager;
    @Value("${switch.use.cache}")
    private String cacheSwitch;

    @Override
    public List<DecisionTablesVersionVo> queryVersionListByDecisionTablesId(Serializable decisionTablesId) {
        LambdaQueryWrapper<DecisionTablesVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DecisionTablesVersion::getDecisionTablesId,decisionTablesId);
        queryWrapper.eq(DecisionTablesVersion::getStatus,1);
        queryWrapper.orderByDesc(DecisionTablesVersion::getId);
        List<DecisionTablesVersion> ruleVersionList = versionMapper.selectList(queryWrapper);
        List<DecisionTablesVersionVo> DecisionTablesVersionVoList = new ArrayList<>();
        for (DecisionTablesVersion ruleVersion :  ruleVersionList) {
            DecisionTablesVersionVo versionVo = new DecisionTablesVersionVo();
            BeanUtils.copyProperties(ruleVersion,versionVo);
            DecisionTablesVersionVoList.add(versionVo);
        }
        return DecisionTablesVersionVoList;
    }

//    @Override
//    public DecisionTablesVersionVo queryById(Long id) {
//        DecisionTablesVersion version = this.getById(id);
//        DecisionTablesVersionVo decisionTablesVersionVo = new DecisionTablesVersionVo();
//        BeanUtils.copyProperties(version,decisionTablesVersionVo);
//
//        decisionTablesVersionVo.setLeftDetailVo(detailService.queryByDecisionTablesVersionId(id, DecisionTablesDetailConst.LEFT_DETAIL_NUM));
//        decisionTablesVersionVo.setTopDetailVo(detailService.queryByDecisionTablesVersionId(id, DecisionTablesDetailConst.TOP_DETAIL_NUM));
//
//        //查询结果集表
//        DecisionTablesResultVo resultList = resultService.queryByDecisionTablesVersionId(id);
//        decisionTablesVersionVo.setResultSet(resultList);
//        //查询策略输出
//        List<StrategyOutput> strategyOutputs = outputService.queryByTactics(new StrategyOutput(id, StrategyType.DECISION_TABLES));
//        decisionTablesVersionVo.setStrategyOutputList(strategyOutputs);
//        return decisionTablesVersionVo;
//    }

    @Override
    public DecisionTablesVersionVo queryById(Long id) {
        DecisionTablesVersion version = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getPrimaryKey(TableEnum.T_DECISION_TABLES_VERSION, id);
            version = redisManager.getByPrimaryKey(key, DecisionTablesVersion.class);
        } else {
            version = this.getById(id);
        }

        DecisionTablesVersionVo decisionTablesVersionVo = new DecisionTablesVersionVo();
        BeanUtils.copyProperties(version,decisionTablesVersionVo);

        decisionTablesVersionVo.setLeftDetailVo(detailService.queryByDecisionTablesVersionId(id, DecisionTablesDetailConst.LEFT_DETAIL_NUM));
        decisionTablesVersionVo.setTopDetailVo(detailService.queryByDecisionTablesVersionId(id, DecisionTablesDetailConst.TOP_DETAIL_NUM));

        //查询结果集表
        DecisionTablesResultVo resultList = resultService.queryByDecisionTablesVersionId(id);
        decisionTablesVersionVo.setResultSet(resultList);
        //查询策略输出
        List<StrategyOutput> strategyOutputs = outputService.queryByTactics(new StrategyOutput(id, StrategyType.DECISION_TABLES));
        decisionTablesVersionVo.setStrategyOutputList(strategyOutputs);
        return decisionTablesVersionVo;
    }

    @Override
    public List<String> queryFieldEnByVersionId(Long versionId) {
        Set<String> fieldEns = new HashSet<>();
        LambdaQueryWrapper<DecisionTablesDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DecisionTablesDetail::getVersionId,versionId);
        List<DecisionTablesDetail> list = detailService.list(queryWrapper);
        Set<Long> detailIds = new HashSet<>();
        for (DecisionTablesDetail detail : list) {
            if (detail.getFieldEn().contains(".") && !detail.getFieldEn().startsWith("%")) {
                fieldEns.add(detail.getFieldEn().split("\\.")[0]);
            } else {
                fieldEns.add(detail.getFieldEn());
            }
        }
        LambdaQueryWrapper<DecisionTablesDetailCondition> conditionWrapper = new LambdaQueryWrapper<>();
        if (detailIds.size()>0){
            conditionWrapper.in(DecisionTablesDetailCondition::getDetailId,detailIds);
            List<DecisionTablesDetailCondition> conditionList = conditionService.list(conditionWrapper);
            for (DecisionTablesDetailCondition condition : conditionList) {
                if (condition.getVariableType()==null||condition.getVariableType()==1){
                    continue;
                }
                if (condition.getVariableType()==2){
                    String fieldValue = condition.getFieldValue();
                    if (fieldValue.contains(".") && !fieldValue.startsWith("%")) {
                        fieldEns.add(fieldValue.split("\\.")[0]);
                    } else {
                        fieldEns.add(fieldValue);
                    }
                }else  if (condition.getVariableType()==3){
                    fieldEns.addAll( CustomValueUtils.getFieldEnSet(condition.getFieldValue()));
                }
            }

        }
        return new ArrayList<>(fieldEns);
    }

    @Override
    @Transactional
    public int addVersionList(List<DecisionTablesVersionVo> versionList) {
        int result = 0;
        for (DecisionTablesVersionVo versionVo : versionList) {
            boolean b = addVersion(versionVo);
            if (b){
                result++;
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean addVersion(DecisionTablesVersionVo version) {
        SysUser loginSysUser = SessionManager.getLoginAccount();
        version.setOrganId(loginSysUser.getOrganId());
        version.setCreateUserId(loginSysUser.getUserId());
        version.setUpdateUserId(loginSysUser.getUserId());
        version.setCreateTime(null);
        version.setUpdateTime(null);
        version.setStatus(1);
        if (version.getVersionCode()==null){
            version.setVersionCode("V:0");
        }
        if (version.getDescription()==null){
            version.setDescription("初始版本");
        }
        int insert = versionMapper.insert(version);
        if (insert>0){
            boolean result = this.addVersionDetail(version);
            if (result){
                saveSnapshot(version.getId());
            }
            return true;
        }else {
            logger.error("新增决策表版本失败{}",version);
        }
        return false;
    }
    @Transactional
    public boolean addVersionDetail(DecisionTablesVersionVo version){
        //插入子表（detail表）数据
        detailService.insertDecisionTablesDetail(version.getId(), version.getLeftDetailVo(), DecisionTablesDetailConst.LEFT_DETAIL_NUM);
        detailService.insertDecisionTablesDetail(version.getId(), version.getTopDetailVo(), DecisionTablesDetailConst.TOP_DETAIL_NUM);
        //插入子表（result表）数据
        resultService.insertDecisionTablesResult(version.getId(), version.getResultSet());
        //插入子表（tactics_output表）数据

        //添加输出字段
        List<StrategyOutput> strategyOutputList = version.getStrategyOutputList();
        if (strategyOutputList !=null&& strategyOutputList.size()>0){
            outputService.insertTacticsOutput(version.getId(), strategyOutputList);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean copyVersion(DecisionTablesVersionVo version) {
        DecisionTablesVersionVo versionVo = this.queryById(version.getId());
        versionVo.setId(null);
        versionVo.setVersionCode(version.getVersionCode());
        versionVo.setDescription(version.getDescription());
        return this.addVersion(versionVo);
    }

    @Override
    @Transactional
    public boolean updateVersion(DecisionTablesVersionVo version) {
        Long versionId = version.getId();
        if (versionId==null){
            return false;
        }
        SysUser loginSysUser = SessionManager.getLoginAccount();
        version.setUpdateUserId(loginSysUser.getUserId());
        //修改版本主表
        versionMapper.updateById(version);
        //修改条件表
        detailService.updateDecisionTablesDetail(version.getId(), version.getLeftDetailVo(), DecisionTablesDetailConst.LEFT_DETAIL_NUM);
        detailService.updateDecisionTablesDetail(version.getId(), version.getTopDetailVo(), DecisionTablesDetailConst.TOP_DETAIL_NUM);
        resultService.updateDecisionTablesResult(version.getId(),version.getResultSet());
        //修改策略输出
        outputService.updateTacticsOutput(versionId,version.getStrategyOutputList(), StrategyType.DECISION_TABLES);
        //存快照
        saveSnapshot(version.getId());
        return true;
    }


    @Override
    @Transactional
    public boolean updateStatus(StatusParam statusParam) {
        LambdaQueryWrapper<DecisionTablesVersion> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.in(DecisionTablesVersion::getId,statusParam.getIds());
        updateWrapper.eq(DecisionTablesVersion::getDecisionTablesId,statusParam.getStrategyId());
        DecisionTablesVersion ruleVersion = new DecisionTablesVersion();
        ruleVersion.setStatus(statusParam.getStatus());
        boolean update = this.update(ruleVersion, updateWrapper);
        return update;
    }

    private boolean saveSnapshot(Long versionId){
//        threadPoolTaskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
                LambdaUpdateWrapper<DecisionTablesVersion> wrapper = new  LambdaUpdateWrapper<>();
                DecisionTablesVersionVo versionVo = queryById(versionId);
                versionVo.setSnapshot(null);
                wrapper.eq(DecisionTablesVersion::getId,versionId).set(DecisionTablesVersion::getSnapshot, JSON.toJSONString(versionVo));
                versionMapper.update(null,wrapper);
//            }
//        });
        return true;
    }
}
