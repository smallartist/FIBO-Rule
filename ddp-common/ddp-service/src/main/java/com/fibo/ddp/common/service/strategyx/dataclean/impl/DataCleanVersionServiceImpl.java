package com.fibo.ddp.common.service.strategyx.dataclean.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.dataclean.DataCleanVersionMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanConditionAndOutPut;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanOriginalDataOp;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.dataclean.DataCleanConditionAndOutPutService;
import com.fibo.ddp.common.service.strategyx.dataclean.DataCleanOriginalDataOpService;
import com.fibo.ddp.common.service.strategyx.dataclean.DataCleanVersionService;
import com.fibo.ddp.common.utils.constant.Constants;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("dataCleanVersionService")
@Slf4j
public class DataCleanVersionServiceImpl extends ServiceImpl<DataCleanVersionMapper, DataCleanVersion> implements DataCleanVersionService {
    @Resource
    private DataCleanVersionMapper dataCleanVersionMapper;

    @Resource
    private DataCleanConditionAndOutPutService dataCleanConditionAndOutPutService;

    @Resource
    private DataCleanOriginalDataOpService originalDataOpService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private RedisManager redisManager;

    // 业务逻辑是否使用缓存
    @Value("${switch.use.cache}")
    private String cacheSwitch;
    private final String versionCachePrefix = Constants.cacheConf.cachePrefix + "dataCleanVersionCache:";

    private static Cache<String, DataCleanVersion> dataCleanVersionCache = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .build();

    @Override
    public List<DataCleanVersion> queryListByListOpId(Long listOpId) {
        LambdaQueryWrapper<DataCleanVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataCleanVersion::getDataCleanId, listOpId);
        queryWrapper.eq(DataCleanVersion::getStatus, 1);
        queryWrapper.orderByDesc(DataCleanVersion::getId);
        List<DataCleanVersion> versionList = dataCleanVersionMapper.selectList(queryWrapper);
        if (versionList==null||versionList.isEmpty()){
            return versionList;
        }
        for (DataCleanVersion version : versionList) {
            //查询版本内部信息
            version.setOriginalDataOp(originalDataOpService.queryByDataCleanVersion(version));
            version.setConditionAndOutPutList(dataCleanConditionAndOutPutService.queryByListOpVersion(version));
        }
        return versionList;
    }

    @Override
    public DataCleanVersion queryById(Long id,boolean needCache) {
        DataCleanVersion version = null;
        String redisKey = versionCachePrefix + id;
        if (needCache){
            DataCleanVersion localCache = dataCleanVersionCache.getIfPresent(redisKey);
            if(localCache != null){
                // 从本地缓存获取
                return localCache;
            } else {
                String versionJson = redisManager.get(redisKey);
                if (StringUtils.isNotBlank(versionJson)) {
                    try {
                        version = JSON.parseObject(versionJson, DataCleanVersion.class);
                        if (version != null && id.equals(version.getId())) {
                            // 更新本地缓存
                            dataCleanVersionCache.put(redisKey, version);
                            return version;
                        }
                    } catch (Exception e) {
                        log.error("缓存版本信息错误，key:{},value:{}", redisKey, versionJson);
                    }
                }
            }

            if (Constants.switchFlag.ON.equals(cacheSwitch)) {
                String key = RedisUtils.getPrimaryKey(TableEnum.T_DATA_CLEAN_VERSION, id);
                version = redisManager.getByPrimaryKey(key, DataCleanVersion.class);
            } else {
                version = this.getById(id);
            }
            //条件与输出
            if (version == null) {
                return null;
            }
            //条件与输出
            version.setOriginalDataOp(originalDataOpService.queryByDataCleanVersion(version));
            version.setConditionAndOutPutList(dataCleanConditionAndOutPutService.queryByListOpVersion(version));
            //查出结果后，放入缓存设置过期时间为120秒
            redisManager.set(redisKey, JSON.toJSONString(version), Constants.cacheConf.cacheSecond);
        }else {
            version = this.getById(id);
            version.setOriginalDataOp(originalDataOpService.queryByDataCleanVersion(version));
            version.setConditionAndOutPutList(dataCleanConditionAndOutPutService.queryByListOpVersion(version));
        }
        return version;

    }

    @Override
    public DataCleanVersion queryByCode(Long dataCleanId,String code) {
        LambdaQueryWrapper<DataCleanVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataCleanVersion::getDataCleanId,dataCleanId);
        wrapper.eq(DataCleanVersion::getVersionCode,code);
        wrapper.eq(DataCleanVersion::getStatus,1);
        DataCleanVersion version = this.getOne(wrapper);
        version.setOriginalDataOp(originalDataOpService.queryByDataCleanVersion(version));
        version.setConditionAndOutPutList(dataCleanConditionAndOutPutService.queryByListOpVersion(version));
        return version;
    }

    @Override
    public List<String> queryFieldEnByVersionId(Long versionId) {
        return null;
    }

    @Override
    @Transactional
    public int addVersionList(List<DataCleanVersion> versionList) {
        int result = 0;
        for (DataCleanVersion version : versionList) {
            boolean b = addVersion(version);
            if (b) {
                result++;
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean addVersion(DataCleanVersion version) {
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
        int insert = dataCleanVersionMapper.insert(version);
        if (insert > 0) {
            boolean result = this.addVersionDetail(version);
            if (result) {
                saveSnapshot(version.getId());
            }
            return true;
        } else {
            log.error("新增清洗数据版本失败{}", version);
        }
        return false;
    }

    @Transactional
    public boolean addVersionDetail(DataCleanVersion version) {
        List<DataCleanConditionAndOutPut> conditionAndOutPutList = version.getConditionAndOutPutList();
        DataCleanOriginalDataOp originalDataOp = version.getOriginalDataOp();
        originalDataOp.setParentId(0L);
        originalDataOp.setDataCleanVersionId(version.getId());
        originalDataOpService.addOriginalDataOp(originalDataOp);
        if (CollectionUtils.isNotEmpty(conditionAndOutPutList)){
            dataCleanConditionAndOutPutService.addBatch(version,conditionAndOutPutList);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean copyVersion(DataCleanVersion version) {
        DataCleanVersion versionVo = this.queryById(version.getId(),false);
        versionVo.setId(null);
        versionVo.setVersionCode(version.getVersionCode());
        versionVo.setDescription(version.getDescription());
        return this.addVersion(versionVo);
    }

    @Override
    @Transactional
    public boolean updateVersion(DataCleanVersion version) {
        Long versionId = version.getId();
        if (versionId == null) {
            return false;
        }
        SysUser loginSysUser = SessionManager.getLoginAccount();
        version.setUpdateUserId(loginSysUser.getUserId());
        //修改版本主表
        dataCleanVersionMapper.updateById(version);
        DataCleanOriginalDataOp originalDataOp = version.getOriginalDataOp();
        originalDataOpService.updateDataOp(version,originalDataOp);
        //todo 修改版本之下的 信息
        boolean b = dataCleanConditionAndOutPutService.updateBatch(version, version.getConditionAndOutPutList());
        //存快照
        saveSnapshot(version.getId());
        return true;
    }

    @Override
    @Transactional
    public boolean updateStatus(StatusParam statusParam) {
        LambdaQueryWrapper<DataCleanVersion> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.in(DataCleanVersion::getId, statusParam.getIds());
        updateWrapper.eq(DataCleanVersion::getDataCleanId, statusParam.getStrategyId());
        DataCleanVersion version = new DataCleanVersion();
        version.setStatus(statusParam.getStatus());
        boolean update = this.update(version, updateWrapper);
        return update;
    }

    private boolean saveSnapshot(Long versionId) {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                LambdaUpdateWrapper<DataCleanVersion> wrapper = new LambdaUpdateWrapper<>();
                DataCleanVersion version = queryById(versionId,false);
                version.setSnapshot(null);
                wrapper.eq(DataCleanVersion::getId, versionId).set(DataCleanVersion::getSnapshot, JSON.toJSONString(version));
                dataCleanVersionMapper.update(null, wrapper);
            }
        });
        return true;
    }
}
