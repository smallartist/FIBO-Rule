package com.fibo.ddp.common.service.strategyx.dataclean.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.dataclean.DataCleanMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.strategyx.dataclean.DataClean;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.dataclean.DataCleanService;
import com.fibo.ddp.common.service.strategyx.dataclean.DataCleanVersionService;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.constant.StatusConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("dataCleanService")
@Slf4j
public class DataCleanServiceImpl extends ServiceImpl<DataCleanMapper, DataClean> implements DataCleanService {
    @Resource
    private DataCleanMapper dataCleanMapper;
    @Resource
    private DataCleanVersionService versionService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisManager redisManager;

    // 业务逻辑是否使用缓存
    @Value("${switch.use.cache}")
    private String cacheSwitch;
    private final String dataCleanCachePrefix = Constants.cacheConf.cachePrefix + "dataCleanCache:";

    private static Cache<String, DataClean> dataCleanVersionCache = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .build();


    @Override
    public DataClean queryByCode(String code,String versionCode) {
        DataClean dataClean = null;
        String redisKey = dataCleanCachePrefix + code+":"+versionCode;

        DataClean localCache = dataCleanVersionCache.getIfPresent(redisKey);
        if(localCache != null){
            // 从本地缓存获取
            return localCache;
        } else {
            String dataCleanJson = redisManager.get(redisKey);
            if (StringUtils.isNotBlank(dataCleanJson)) {
                try {
                    dataClean = JSON.parseObject(dataCleanJson, DataClean.class);
                    if (dataClean != null && code.equals(dataClean.getCode())) {
                        // 更新本地缓存
                        dataCleanVersionCache.put(redisKey, dataClean);
                        return dataClean;
                    }
                } catch (Exception e) {
                    log.error("缓存版本信息错误，key:{},value:{}", redisKey, dataCleanJson);
                }
            }
        }

        LambdaQueryWrapper<DataClean> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataClean::getCode,code);
        wrapper.eq(DataClean::getStatus,1);
        dataClean = this.getOne(wrapper);
        //条件与输出
        if (dataClean == null) {
            return null;
        }
        DataCleanVersion dataCleanVersion = versionService.queryByCode(dataClean.getId(), versionCode);
        dataClean.setVersionList(Arrays.asList(dataCleanVersion));
        //查出结果后，放入缓存设置过期时间为120秒
        redisManager.set(redisKey, JSON.toJSONString(dataClean), Constants.cacheConf.cacheSecond);
        return dataClean;
    }


    @Override
    public DataClean queryById(Long id) {
        if (id==null){
            return null;
        }
        DataClean operation = this.getById(id);
        if (operation==null){
            return null;
        }
        operation.setVersionList(versionService.queryListByListOpId(id));
        return operation;
    }

    @Override
    public PageInfo queryByEntity(QueryListParam<DataClean> queryParam) {
        DataClean query = queryParam.getEntity();
        Integer pageNum = queryParam.getPageNum();
        Integer pageSize = queryParam.getPageSize();
        if (pageNum > 0 && pageSize > 0) {
            PageHelper.startPage(pageNum, pageSize);
        }
        LambdaQueryWrapper<DataClean> wrapper = createWrapper(query);
        List<DataClean> operationList = dataCleanMapper.selectList(wrapper);
        PageInfo pageInfo = new PageInfo(operationList);

        //TODO 循环查用户表，待优化
        for (DataClean operation : operationList) {
            if (operation != null && operation.getCreateUserId() != null) {
                operation.setCreateUserName(sysUserMapper.findNickNameById(operation.getCreateUserId()));
                operation.setVersionList(versionService.queryListByListOpId(operation.getId()));
            }
        }
        return pageInfo;
    }

    @Override
    @Transactional
    public DataClean insert(DataClean dataClean) {
        //初始化基本参数
        dataClean = initParam(dataClean);
        //插入并获取insert后实体对象返回id
        boolean save = this.save(dataClean);
        if (!save) {
            throw new ApiException(ErrorCodeEnum.DECISION_TREE_SAVE_ERROR.getCode(), ErrorCodeEnum.DECISION_TREE_SAVE_ERROR.getMessage());
        }
        Long listOpId = dataClean.getId();
        List<DataCleanVersion> versionList = dataClean.getVersionList();
        for (DataCleanVersion versionVo : versionList) {
            versionVo.setDataCleanId(listOpId);
        }
        versionService.addVersionList(versionList);
        return this.queryById(listOpId);
    }


    @Override
    @Transactional
    public DataClean update(DataClean dataClean) {
        if (dataClean.getId() == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        SysUser sysUser = SessionManager.getLoginAccount();
        dataClean.setUpdateUserId(sysUser.getUserId());
        DataClean updateOperation = new DataClean();
        BeanUtils.copyProperties(dataClean, updateOperation);
        //修改主表
        boolean updateResult = this.updateById(updateOperation);
        if (dataClean.getStartTime()==null||dataClean.getEndTime()==null){
            LambdaUpdateWrapper<DataClean> wrapper = new LambdaUpdateWrapper<>();

            wrapper.set(DataClean::getStartTime, dataClean.getStartTime());
            wrapper.set(DataClean::getEndTime, dataClean.getEndTime());
            wrapper.eq(DataClean::getId, dataClean.getId());
            updateResult = this.update(wrapper);
        }
        if (!updateResult) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        Long listOpId = updateOperation.getId();
        List<DataCleanVersion> versionList = dataClean.getVersionList();
        if (versionList != null && versionList.size() > 0) {
            versionService.updateVersion(versionList.get(0));
        }
        return this.queryById(listOpId);
    }

    @Override
    @Transactional
    public boolean updateStatus(List<Long> ids, Integer status) {
        LambdaUpdateWrapper<DataClean> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(DataClean::getId, ids);
        DataClean operation = new DataClean();
        operation.setStatus(status);
        int updateNum = dataCleanMapper.update(operation, wrapper);
        if (updateNum > 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateFolder(List<Long> ids, Long folderId) {
        LambdaUpdateWrapper<DataClean> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(DataClean::getId, ids);
        DataClean operation = new DataClean();
        operation.setFolderId(folderId);
        int updateNum = dataCleanMapper.update(operation, wrapper);
        if (updateNum > 0) {
            return true;
        }
        return false;
    }


    private LambdaQueryWrapper<DataClean> createWrapper(DataClean query) {

        LambdaQueryWrapper<DataClean> wrapper = new LambdaQueryWrapper<>();
        if (query!=null){
            if (query.getOpType()!=null){
                wrapper.eq(DataClean::getOpType,query.getOpType());
            }
            if (query.getFolderId()!=null){
                wrapper.eq(DataClean::getFolderId,query.getFolderId());
            }
            if (query.getName() != null) {
                wrapper.like(DataClean::getName, query.getName());
            }
            if (query.getCode() != null) {
                wrapper.like(DataClean::getCode, query.getCode());
            }
            if (query.getStartTime()!=null){
                wrapper.le(DataClean::getStartTime,query.getStartTime());
                wrapper.ge(DataClean::getEndTime,query.getStartTime());
            }
        }
        wrapper.ne(DataClean::getStatus,-1);
        wrapper.eq(DataClean::getOrganId, SessionManager.getLoginAccount().getOrganId());
        wrapper.orderByDesc( DataClean::getId);
        return wrapper;
    }


    //新插入数据的准备工作
    private DataClean initParam(DataClean dataClean) {
        this.checkUniqueness(dataClean);
        //加入用户信息
        SysUser sysUser = SessionManager.getLoginAccount();
        dataClean.setCreateUserId(sysUser.getUserId());
        dataClean.setOrganId(sysUser.getOrganId());
        dataClean.setUpdateUserId(sysUser.getUserId());
        //加入状态信息
        dataClean.setStatus(StatusConst.STATUS_ENABLED);
        return dataClean;
    }

    //唯一性检查
    private boolean checkUniqueness(DataClean dataClean) {
        DataClean decisionTree = new DataClean();
        decisionTree.setName(dataClean.getName());
        DataClean info = this.getOne(new QueryWrapper<>(decisionTree), false);
        if (info != null) {
            throw new ApiException(ErrorCodeEnum.LIST_OPERATION_NAME_REPEAT.getCode(), ErrorCodeEnum.LIST_OPERATION_NAME_REPEAT.getMessage());
        }
        decisionTree.setName(null);
        decisionTree.setCode(dataClean.getCode());
        info = this.getOne(new QueryWrapper<>(decisionTree), false);
        if (info != null) {
            throw new ApiException(ErrorCodeEnum.LIST_OPERATION_Code_REPEAT.getCode(), ErrorCodeEnum.LIST_OPERATION_Code_REPEAT.getMessage());
        }
        return true;
    }

}
