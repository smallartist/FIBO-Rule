package com.fibo.ddp.common.service.strategyx.collectionrule.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.strategyx.collectionrule.ListOperationVersionMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.collectionrule.ConditionAndOutPut;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationVersion;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.strategyx.collectionrule.ConditionAndOutPutService;
import com.fibo.ddp.common.service.strategyx.collectionrule.ListOperationConditionService;
import com.fibo.ddp.common.service.strategyx.collectionrule.ListOperationOutputService;
import com.fibo.ddp.common.service.strategyx.collectionrule.ListOperationVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("listOperationVersionService")
@Slf4j
public class ListOperationVersionServiceImpl extends ServiceImpl<ListOperationVersionMapper, ListOperationVersion> implements ListOperationVersionService {
    @Resource
    private ListOperationVersionMapper listOperationVersionMapper;

    @Resource
    private ListOperationConditionService conditionService;

    @Resource
    private ListOperationOutputService outputService;

    @Resource
    private ConditionAndOutPutService conditionAndOutPutService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Override
    public List<ListOperationVersion> queryListByListOpId(Long listOpId) {
        LambdaQueryWrapper<ListOperationVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ListOperationVersion::getListOpId, listOpId);
        queryWrapper.eq(ListOperationVersion::getStatus, 1);
        queryWrapper.orderByDesc(ListOperationVersion::getId);
        List<ListOperationVersion> versionList = listOperationVersionMapper.selectList(queryWrapper);
        if (versionList==null||versionList.isEmpty()){
            return versionList;
        }
        for (ListOperationVersion version : versionList) {
            //查询版本内部信息
            version.setConditionAndOutPutList(conditionAndOutPutService.queryByListOpVersion(version));
        }
        return versionList;
    }

    @Override
    public ListOperationVersion queryById(Long id) {
        ListOperationVersion version = this.getById(id);
        if (version == null) {
            return null;
        }
        //条件与输出
        version.setConditionAndOutPutList(conditionAndOutPutService.queryByListOpVersion(version));
        return version;
    }

    @Override
    public List<String> queryFieldEnByVersionId(Long versionId) {
        return null;
    }

    @Override
    @Transactional
    public int addVersionList(List<ListOperationVersion> versionList) {
        int result = 0;
        for (ListOperationVersion version : versionList) {
            boolean b = addVersion(version);
            if (b) {
                result++;
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean addVersion(ListOperationVersion version) {
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
        int insert = listOperationVersionMapper.insert(version);
        if (insert > 0) {
            boolean result = this.addVersionDetail(version);
            if (result) {
                saveSnapshot(version.getId());
            }
            return true;
        } else {
            log.error("新增决策树版本失败{}", version);
        }
        return false;
    }

    @Transactional
    public boolean addVersionDetail(ListOperationVersion version) {
        List<ConditionAndOutPut> conditionAndOutPutList = version.getConditionAndOutPutList();
        if (conditionAndOutPutList==null||conditionAndOutPutList.isEmpty()){
            return true;
        }
        return conditionAndOutPutService.addBatch(version,conditionAndOutPutList);
    }

    @Override
    @Transactional
    public boolean copyVersion(ListOperationVersion version) {
        ListOperationVersion versionVo = this.queryById(version.getId());
        versionVo.setId(null);
        versionVo.setVersionCode(version.getVersionCode());
        versionVo.setDescription(version.getDescription());
        return this.addVersion(versionVo);
    }

    @Override
    @Transactional
    public boolean updateVersion(ListOperationVersion version) {
        Long versionId = version.getId();
        if (versionId == null) {
            return false;
        }
        SysUser loginSysUser = SessionManager.getLoginAccount();
        version.setUpdateUserId(loginSysUser.getUserId());
        //修改版本主表
        listOperationVersionMapper.updateById(version);
        //todo 修改版本之下的 信息
        boolean b = conditionAndOutPutService.updateBatch(version, version.getConditionAndOutPutList());
        //存快照
        saveSnapshot(version.getId());
        return true;
    }

    @Override
    @Transactional
    public boolean updateStatus(StatusParam statusParam) {
        LambdaQueryWrapper<ListOperationVersion> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.in(ListOperationVersion::getId, statusParam.getIds());
        updateWrapper.eq(ListOperationVersion::getListOpId, statusParam.getStrategyId());
        ListOperationVersion version = new ListOperationVersion();
        version.setStatus(statusParam.getStatus());
        boolean update = this.update(version, updateWrapper);
        return update;
    }

    private boolean saveSnapshot(Long versionId) {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                LambdaUpdateWrapper<ListOperationVersion> wrapper = new LambdaUpdateWrapper<>();
                ListOperationVersion version = queryById(versionId);
                version.setSnapshot(null);
                wrapper.eq(ListOperationVersion::getId, versionId).set(ListOperationVersion::getSnapshot, JSON.toJSONString(version));
                listOperationVersionMapper.update(null, wrapper);
            }
        });
        return true;
    }
}
