package com.fibo.ddp.common.service.enginex.dataflow.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.enginex.risk.EngineMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.enginex.risk.Engine;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.enginex.dataflow.EngineServiceV3;
import com.fibo.ddp.common.service.enginex.dataflow.EngineVersionServiceV3;
import com.fibo.ddp.common.utils.constant.StatusConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (Engine)表服务实现类
 *
 * @author jgp
 * @since 2021-12-22 13:28:09
 */
@Service("engineServiceV3")
public class EngineServiceV3Impl extends ServiceImpl<EngineMapper, Engine> implements EngineServiceV3 {

    @Autowired
    private EngineVersionServiceV3 engineVersionServiceV3;

    @Override
    public PageInfo queryList(QueryListParam<Engine> param) {
        if (QueryListParam.checkIsPage(param)) {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
        }
        LambdaQueryWrapper<Engine> wrapper = createWrapper(param.getEntity());
        PageInfo<Engine> pageInfo = new PageInfo<>(this.list(wrapper));
        if(pageInfo.getSize()!=0){
            List<Long> bootEngineIds = engineVersionServiceV3.
                    queryByEngineIds(pageInfo.getList().stream().map(Engine::getId).collect(Collectors.toList()))
                    .stream().filter(item -> {
                        return item.getBootState() == 1;
                    }).map(EngineVersion::getEngineId).distinct().collect(Collectors.toList());
            for (Engine Engine : pageInfo.getList()) {
                if (bootEngineIds.contains(Engine.getId())){
                    Engine.setRunStatus(1);
                }
            }
        }
        return pageInfo;
    }

    @Override
    @Transactional
    public Engine addEngine(Engine engine) {
        initEngine(engine);
        boolean saveEngine = this.save(engine);
        if (saveEngine) {
            //保存成功添加版本
            EngineVersion version = new EngineVersion();
            version.setEngineId(engine.getId());
            version.setVersion(0);
            version.setEngineType(engine.getEngineType());
            boolean saveVersion = engineVersionServiceV3.addEngineVersion(version);
            if (!saveVersion) {
                throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),"新增引擎添加默认版本失败");
            }
            return engine;
        }
        return null;
    }

    @Override
    public Engine updateEngine(Engine engine) {
        if (engine != null && engine.getId() != null){
            boolean result = this.updateById(engine);
            return engine;
        }
        return null;
    }


    @Override
    @Transactional
    public boolean updateStatus(List<Long> ids, Integer status) {
        LambdaUpdateWrapper<Engine> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Engine::getId, ids);
        Engine Engine = new Engine();
        Engine.setStatus(status);
        Engine.setCreator(SessionManager.getLoginAccount().getUserId());
        return this.update(Engine, wrapper);
    }


    private void initEngine(Engine engine) {
        if (engine == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "新增引擎不能为nll");
        }
        SysUser sysUser = SessionManager.getLoginAccount();
        engine.setOrganId(sysUser.getOrganId());
        engine.setCreator(sysUser.getUserId());
        engine.setStatus(StatusConst.STATUS_ENABLED);
    }


    private LambdaQueryWrapper<Engine> createWrapper(Engine entity) {
        LambdaQueryWrapper<Engine> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Engine::getId);
        wrapper.eq(Engine::getOrganId, SessionManager.getLoginAccount().getOrganId());
        if (entity == null) {
            wrapper.ne(Engine::getStatus, StatusConst.STATUS_DELETE);
            return wrapper;
        }
        if (StringUtils.isNotBlank(entity.getCode())) {
            wrapper.eq(Engine::getCode, entity.getCode());
        }
        if (StringUtils.isNotBlank(entity.getEngineType())) {
            wrapper.like(Engine::getEngineType, entity.getEngineType());
        }
        if (entity.getStatus() != null) {
            wrapper.eq(Engine::getStatus, entity.getStatus());
        } else {
            wrapper.ne(Engine::getStatus, StatusConst.STATUS_DELETE);
        }
        if (StringUtils.isNotBlank(entity.getName())) {
            wrapper.like(Engine::getName, entity.getName());
        }
        return wrapper;
    }
}
