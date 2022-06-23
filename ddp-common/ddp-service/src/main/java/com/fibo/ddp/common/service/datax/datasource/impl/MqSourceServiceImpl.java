package com.fibo.ddp.common.service.datax.datasource.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.datax.datasource.MqSourceMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.datax.datasource.MqSource;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.datax.datasource.MqSourceService;
import com.fibo.ddp.common.utils.constant.StatusConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (MqSource)表服务实现类
 *
 * @author jgp
 * @since 2021-12-20 13:31:51
 */
@Service("fieldMqSourceService")
public class MqSourceServiceImpl extends ServiceImpl<MqSourceMapper, MqSource> implements MqSourceService {
    @Resource
    private MqSourceMapper mqSourceMapper;

    @Override
    public MqSource queryById(Long id) {
        if (id == null || id < 1) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "参数异常：id错误");
        }
        MqSource result = this.getById(id);
        if (result == null || result.getStatus() == -1) {
            return null;
        }
        return result;
    }

    @Override
    public PageInfo queryList(QueryListParam<MqSource> param) {
        if (QueryListParam.checkIsPage(param)) {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
        }
        LambdaQueryWrapper<MqSource> wrapper = createWrapper(param.getEntity());
        PageInfo<MqSource> pageInfo = new PageInfo<>(this.list(wrapper));
        return pageInfo;
    }

    @Override
    public MqSource add(MqSource param) {
        if (param == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "参数错误，添加消息队列源失败");
        }
        init(param);
        param.setCreator(param.getModifier());
        boolean save = this.save(param);
        MqSource result = null;
        if (save) {
            result = this.queryById(param.getId());
        }
        return result;
    }

    @Override
    public MqSource update(MqSource param) {
        if (param == null || param.getId() == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "参数错误：修改消息队列源失败");
        }
        init(param);
        boolean save = this.updateById(param);
        MqSource result = null;
        if (save) {
            result = this.queryById(param.getId());
        }
        return result;
    }

    @Override
    public boolean updateStatus(List<Long> ids, Integer status) {
        LambdaUpdateWrapper<MqSource> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(MqSource::getId, ids);
        MqSource mqSource = new MqSource();
        mqSource.setStatus(status);
        mqSource.setModifier(SessionManager.getLoginAccount().getUserId());
        return this.update(mqSource, wrapper);
    }

    private LambdaQueryWrapper<MqSource> createWrapper(MqSource entity) {
        LambdaQueryWrapper<MqSource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MqSource::getOrganId, SessionManager.getLoginAccount().getOrganId());
        wrapper.orderByDesc(MqSource::getId);
        if (entity == null) {
            wrapper.ne(MqSource::getStatus, StatusConst.STATUS_DELETE);
            return wrapper;
        }
        if (StringUtils.isNotBlank(entity.getType())) {
            wrapper.eq(MqSource::getType, entity.getType());
        }
        if (StringUtils.isNotBlank(entity.getName())) {
            wrapper.like(MqSource::getName, entity.getName());
        }
        if (entity.getStatus() != null) {
            wrapper.eq(MqSource::getStatus, entity.getStatus());
        } else {
            wrapper.ne(MqSource::getStatus, StatusConst.STATUS_DELETE);
        }
        return wrapper;
    }

    private void init(MqSource mqSource) {
        SysUser loginAccount = SessionManager.getLoginAccount();
        mqSource.setStatus(1);
        mqSource.setOrganId(loginAccount.getOrganId());
        mqSource.setModifier(loginAccount.getUserId());
    }
}
