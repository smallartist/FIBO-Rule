package com.fibo.ddp.common.service.strategyx.scriptrule.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.strategyx.scriptrule.RuleScriptVersionMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.datax.datamanage.Field;
import com.fibo.ddp.common.model.strategyx.scriptrule.RuleScriptVersion;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.strategyx.scriptrule.RuleScriptVersionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * (RuleScriptVersion)表服务实现类
 */
@Service("ruleScriptVersionService")
public class RuleScriptVersionServiceImpl extends ServiceImpl<RuleScriptVersionMapper, RuleScriptVersion> implements RuleScriptVersionService {
    @Resource
    private RuleScriptVersionMapper ruleScriptVersionMapper;

    @Override
    public RuleScriptVersion queryById(Long id) {
        if (id!=null){
            return this.getById(id);
        }
        return null;
    }

    @Override
    public List<RuleScriptVersion> queryVersionListByRuleId(Long ruleId) {
        LambdaQueryWrapper<RuleScriptVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuleScriptVersion::getRuleId,ruleId);
        wrapper.eq(RuleScriptVersion::getStatus,1);
        wrapper.orderByDesc(RuleScriptVersion::getId);
        List<RuleScriptVersion> list = this.list(wrapper);
        return list;
    }

    @Override
    public List<String> queryFieldEnByVersionId(Long versionId) {
        Set<String> fieldEnSet = new HashSet<>();
        RuleScriptVersion ruleScriptVersion = this.queryById(versionId);
        if (ruleScriptVersion==null){
            return new ArrayList<>();
        }
        String scriptContent = ruleScriptVersion.getScriptContent();
        if (StringUtils.isNotBlank(scriptContent)){
            JSONObject jsonObject = JSON.parseObject(scriptContent);
            Object farr = jsonObject.get("farr");
            if (farr!=null&&!"".equals(farr)){
                List<Field> fieldList = JSONArray.parseArray(JSON.toJSONString(farr), Field.class);
                fieldEnSet.addAll(fieldList.stream().map(item->{return item.getFieldEn();}).collect(Collectors.toSet()));
            }
        }
        return new ArrayList<>(fieldEnSet);
    }

    @Override
    @Transactional
    public int addVersionList(List<RuleScriptVersion> versionList) {
        int result = 0;
        for (RuleScriptVersion ruleScriptVersion : versionList) {
            boolean b = this.addVersion(ruleScriptVersion);
            if (b){
                result++;
            }
        }
        return 0;
    }

    @Override
    @Transactional
    public boolean addVersion(RuleScriptVersion version) {
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
        int insert = ruleScriptVersionMapper.insert(version);
        if (insert>0){
            this.saveSnapshot(version.getId());
        }
        return insert>0;
    }

    @Override
    @Transactional
    public boolean copyVersion(RuleScriptVersion version) {
        RuleScriptVersion ruleScriptVersion = this.queryById(version.getId());
        ruleScriptVersion.setId(null);
        ruleScriptVersion.setVersionCode(version.getVersionCode());
        ruleScriptVersion.setDescription(version.getDescription());
        return this.addVersion(ruleScriptVersion);
    }

    @Override
    @Transactional
    public boolean updateVersion(RuleScriptVersion version) {
        Long versionId = version.getId();
        if (versionId==null){
            return false;
        }
        SysUser loginSysUser = SessionManager.getLoginAccount();
        version.setUpdateUserId(loginSysUser.getUserId());
        //修改版本主表
        int updateById = ruleScriptVersionMapper.updateById(version);
        this.saveSnapshot(versionId);
        return updateById>0;
    }

    @Override
    public boolean updateStatus(StatusParam statusParam) {
        LambdaQueryWrapper<RuleScriptVersion> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.in(RuleScriptVersion::getId,statusParam.getIds());
        updateWrapper.eq(RuleScriptVersion::getRuleId,statusParam.getStrategyId());
        RuleScriptVersion ruleVersion = new RuleScriptVersion();
        ruleVersion.setStatus(statusParam.getStatus());
        boolean update = this.update(ruleVersion, updateWrapper);
        return update;
    }
    private boolean saveSnapshot(Long versionId){
//        threadPoolTaskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
                LambdaUpdateWrapper<RuleScriptVersion> wrapper = new  LambdaUpdateWrapper<>();
                RuleScriptVersion versionVo = queryById(versionId);
                versionVo.setSnapshot(null);
                wrapper.eq(RuleScriptVersion::getId,versionId).set(RuleScriptVersion::getSnapshot, JSON.toJSONString(versionVo));
                ruleScriptVersionMapper.update(null,wrapper);
//            }
//        });
        return true;
    }

    @Override
    public List<String> queryFieldEnByVersionIds(List<Long> versionIds) {
        LambdaQueryWrapper<RuleScriptVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RuleScriptVersion::getId,versionIds);
        List<RuleScriptVersion> list = this.list(wrapper);
        Set<String> fieldEnSet = new HashSet<>();
        if (list!=null&&!list.isEmpty()){
            for (RuleScriptVersion ruleScriptVersion : list) {
                collectFieldEn(ruleScriptVersion,fieldEnSet);
            }
        }
        return new ArrayList<>(fieldEnSet);
    }

    private void collectFieldEn(RuleScriptVersion ruleScriptVersion,Set<String> fieldEnSet){
        String scriptContent = ruleScriptVersion.getScriptContent();
        if (StringUtils.isNotBlank(scriptContent)){
            JSONObject jsonObject = JSON.parseObject(scriptContent);
            Object farr = jsonObject.get("farr");
            if (farr!=null&&!"".equals(farr)){
                List<Field> fieldList = JSONArray.parseArray(JSON.toJSONString(farr), Field.class);
                fieldEnSet.addAll(fieldList.stream().map(item->{return item.getFieldEn();}).collect(Collectors.toSet()));
            }
        }
    }
}
