package com.fibo.ddp.common.service.enginex.dataflow.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.enginex.risk.EngineVersionMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.enginex.dataflow.vo.EngineVersionContentVo;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.enginex.dataflow.EngineNodeServiceV3;
import com.fibo.ddp.common.service.enginex.dataflow.EngineVersionContentService;
import com.fibo.ddp.common.service.enginex.dataflow.EngineVersionServiceV3;
import com.fibo.ddp.common.utils.constant.StatusConst;
import com.fibo.ddp.common.utils.constant.enginex.EngineTypeConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 不同场景下的多个模型版本(EngineVersion)表服务实现类
 *
 * @author jgp
 * @since 2021-12-22 14:39:33
 */
@Service("engineVersionServiceV3")
public class EngineVersionServiceV3Impl extends ServiceImpl<EngineVersionMapper, EngineVersion> implements EngineVersionServiceV3 {

    @Autowired
    private EngineNodeServiceV3 engineNodeServiceV3;
    @Autowired
    private EngineVersionContentService engineVersionContentService;

    /**
     * 根据主表获取完整信息：包含子表内容
     *
     * @param versionId
     * @return
     */
    @Override
    public EngineVersion queryById(Long versionId) {
        if (versionId == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "参数异常：版本id为空");
        }
        EngineVersion result = this.getById(versionId);
        if (result != null) {
            this.queryDetailInfoSetToParam(result);
        }
        return result;
    }

    /**
     * 查询列表根据入参决定是否分页
     *
     * @param param
     * @return
     */
    @Override
    public PageInfo queryList(QueryListParam<EngineVersion> param) {
        if (QueryListParam.checkIsPage(param)) {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
        }
        LambdaQueryWrapper<EngineVersion> wrapper = this.createWrapper(param.getEntity());
        PageInfo pageInfo = new PageInfo<>(this.list(wrapper));
        return pageInfo;
    }

    @Override
    public List<EngineVersion> queryByEngineIds(Collection<Long> engineIds) {
        LambdaQueryWrapper<EngineVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(EngineVersion::getEngineId,engineIds);
        wrapper.ne(EngineVersion::getStatus, StatusConst.STATUS_DELETE);
        return this.list(wrapper);
    }

    @Override
    public List<EngineVersion> queryByEngineId(Long engineId) {
        LambdaQueryWrapper<EngineVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EngineVersion::getEngineId,engineId);
        wrapper.ne(EngineVersion::getStatus,StatusConst.STATUS_DELETE);
        return this.list(wrapper);
    }

    /**
     * 修改状态，用于假删
     *
     * @param ids
     * @param status
     * @return
     */
    @Override
    public boolean updateStatus(List<Long> ids, Integer status) {
        return true;
    }

    /**
     * 添加一个引擎的版本
     *
     * @return
     */
    @Override
    @Transactional
    public synchronized boolean addEngineVersion(EngineVersion param) {
        if (param == null
                || param.getEngineId() == null
                || StringUtils.isBlank(param.getEngineType())) {
            return false;
        }
        EngineVersion engineVersion = this.getLatestEngineSubVersion(param);

        Integer subVersion = engineVersion == null || engineVersion.getSubVersion() == null ? 0 : engineVersion.getSubVersion() + 1; // 子版本 +1
        //无论引擎是否正在运行，皆在此版本下创建子版本
        param.setBootState(0);
        param.setCreateTime(new Date().toString());
        param.setSubVersion(subVersion);
        this.initEngine(param);
        boolean saveVersionResult = this.save(param);
        boolean result = false;
        if (saveVersionResult) {
            result =this.addDetailInfo(param);
        }
        return result;
    }

    /**
     * 赋值一个引擎版本
     *
     * @param versionId
     * @return
     */
    @Override
    public boolean copyEngineVersion(Long versionId) {

        return false;
    }

    /**
     * 新增修改初始化
     *
     * @param EngineVersion
     */
    private void initEngine(EngineVersion EngineVersion) {
        if (EngineVersion == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "引擎版本不能为nll");
        }
        SysUser loginAccount = SessionManager.getLoginAccount();
        EngineVersion.setLatestTime(new Date().toString());
        EngineVersion.setLayout(0);
        EngineVersion.setLatestUser(loginAccount.getUserId());
        EngineVersion.setUserId(loginAccount.getUserId());
        EngineVersion.setStatus(StatusConst.STATUS_ENABLED);
    }

    /**
     * 创建查询wrapper
     *
     * @param entity
     * @return
     */
    private LambdaQueryWrapper<EngineVersion> createWrapper(EngineVersion entity) {
        LambdaQueryWrapper<EngineVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(EngineVersion::getVersionId);
        if (entity == null || entity.getEngineId() == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "查询引擎版本需要引擎id");
        }
        if (entity.getStatus() != null) {
            wrapper.eq(EngineVersion::getStatus, entity.getStatus());
        } else {
            wrapper.ne(EngineVersion::getStatus, StatusConst.STATUS_DELETE);
        }
        return wrapper;
    }

    /**
     * 获取最后一个子版本用于新版本自增
     *
     * @param entity
     * @return
     */
    private EngineVersion getLatestEngineSubVersion(EngineVersion entity) {
        LambdaQueryWrapper<EngineVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EngineVersion::getEngineId, entity.getEngineId());
        wrapper.eq(EngineVersion::getVersion, entity.getVersion());
        wrapper.orderByDesc(EngineVersion::getSubVersion);
        return this.getOne(wrapper, false);
    }

    /**
     * 根据参数查出子级内容并且装配
     * @param param
     * @return
     */
    private boolean queryDetailInfoSetToParam(EngineVersion param) {
        boolean result = false;
        if (param == null || StringUtils.isBlank(param.getEngineType())) {
            return result;
        }
        result = true;
        switch (param.getEngineType()){
            case EngineTypeConst
                    .RULE_ENGINE:
                param.setEngineNodeList(engineNodeServiceV3.queryNodeListByVersion(param.getVersionId()));
                break;
            case EngineTypeConst
                    .RISK_CONTROL_ENGINE:
                break;
            case EngineTypeConst
                    .DATA_FLOW_ENGINE:
                param.setEngineVersionContent(engineVersionContentService.queryById(param.getVersionId()));
                break;
            case EngineTypeConst
                    .PERSONAS_ENGINE:
                break;
            case EngineTypeConst
                    .MARKETING_ENGINE:
                break;
            case EngineTypeConst
                    .WARNING_ENGINE:
                break;
            case EngineTypeConst
                    .SCORING_ENGINE:
                break;
            case EngineTypeConst
                    .LOGIC_ENGINE:
                break;
            case EngineTypeConst
                    .QUESTIONNAIRE_ENGINE:
                break;
            case EngineTypeConst
                    .PAGE_ENGINE:
                break;
            case EngineTypeConst
                    .MATCHING_ENGINE:
                break;
            default:
                result = false;
        }
        return result;
    }

    /**
     * 添加子级详情表的内容
     * @param param
     * @return
     */
    private boolean addDetailInfo(EngineVersion param) {
        boolean result = false;
        if (param == null || StringUtils.isBlank(param.getEngineType())) {
            return result;
        }
        SysUser loginAccount = SessionManager.getLoginAccount();
        EngineVersionContentVo engineVersionContent;
        switch (param.getEngineType()){
            case EngineTypeConst
                    .RULE_ENGINE:
                result = engineNodeServiceV3.saveStartNode(param.getVersionId());
                break;
            case EngineTypeConst
                    .RISK_CONTROL_ENGINE:
                break;
            case EngineTypeConst
                    .DATA_FLOW_ENGINE:
            case EngineTypeConst
                    .PERSONAS_ENGINE:
            case EngineTypeConst
                    .MARKETING_ENGINE:
                engineVersionContent = new EngineVersionContentVo();
                engineVersionContent.setEngineVersionId(param.getVersionId());
                engineVersionContent.setEngineType(param.getEngineType());
                engineVersionContent.setCreateUserId(loginAccount.getUserId());
                result = engineVersionContentService.addVersionContent(engineVersionContent);
                break;
            case EngineTypeConst
                    .WARNING_ENGINE:
                break;
            case EngineTypeConst
                    .SCORING_ENGINE:
                break;
            case EngineTypeConst
                    .LOGIC_ENGINE:
                break;
            case EngineTypeConst
                    .QUESTIONNAIRE_ENGINE:
                break;
            case EngineTypeConst
                    .PAGE_ENGINE:
                break;
            case EngineTypeConst
                    .MATCHING_ENGINE:
                break;
        }
        return result;
    }
}
