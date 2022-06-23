package com.fibo.ddp.common.service.approval.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.approval.ApprovalConfigMapper;
import com.fibo.ddp.common.model.approval.ApprovalConfig;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.service.approval.ApprovalConfigService;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.utils.constant.ApprovalConsts;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (ApprovalConfig)表服务实现类
 */
@Service("approvalConfigService")
public class ApprovalConfigServiceImpl extends ServiceImpl<ApprovalConfigMapper, ApprovalConfig> implements ApprovalConfigService {
    @Resource
    private ApprovalConfigMapper approvalConfigMapper;


    @Override
    public boolean addApprovalConfig(ApprovalConfig entity) {
        if (entity == null) {
            return false;
        }
        this.initEntity(entity, 0);
        boolean insert = this.saveOrUpdate(entity);
        return insert;
    }

    private void initEntity(ApprovalConfig entity, int type) {
        SysUser loginAccount = SessionManager.getLoginAccount();

        switch (type) {
            //新增
            case 0:
                if (entity.getApprovalStatus() == null) {
                    entity.setApprovalStatus(ApprovalConsts.ApprovalStatus.OFF);
                }
                if (loginAccount != null) {
                    if (entity.getCreateUserId() == null) {
                        entity.setCreateUserId(loginAccount.getUserId());
                    }
                    if (entity.getOrganId() == null) {
                        entity.setOrganId(loginAccount.getOrganId());
                    }
                }
                break;
            //修改
            case 1:
                if (loginAccount != null) {
                    if (entity.getCreateUserId() == null) {
                        entity.setCreateUserId(loginAccount.getUserId());
                    }
                    if (entity.getUpdateUserId() == null) {
                        entity.setUpdateUserId(loginAccount.getUserId());
                    }
                }
                break;
        }


    }

    @Override
    public PageInfo<ApprovalConfig> queryList(QueryListParam<ApprovalConfig> param) {
        int pageNum = param.getPageNum();
        int pageSize = param.getPageSize();
        if (pageNum > 0 && pageSize > 0) {
            PageHelper.startPage(pageNum, pageSize);
        }
        LambdaQueryWrapper<ApprovalConfig> wrapper = this.createWrapper(param.getEntity());
        List<ApprovalConfig> list = this.list(wrapper);
        PageInfo<ApprovalConfig> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    private LambdaQueryWrapper<ApprovalConfig> createWrapper(ApprovalConfig entity) {
        LambdaQueryWrapper<ApprovalConfig> wrapper = new LambdaQueryWrapper<>();
        SysUser loginAccount = SessionManager.getLoginAccount();
        if (loginAccount.getOrganId() != 1) {
            wrapper.eq(ApprovalConfig::getOrganId, loginAccount.getOrganId());
        }
        if (entity != null) {
            if (entity.getId() != null) {
                wrapper.eq(ApprovalConfig::getId, entity.getId());
            }
            if (entity.getApprovalStatus() != null) {
                wrapper.eq(ApprovalConfig::getApprovalStatus, entity.getApprovalStatus());
            } else {
                wrapper.ne(ApprovalConfig::getApprovalStatus, ApprovalConsts.ApprovalStatus.DELETE);
            }
            if (entity.getCreateUserId() != null) {
                wrapper.eq(ApprovalConfig::getCreateUserId, entity.getCreateUserId());
            }
            if (entity.getUpdateUserId() != null) {
                wrapper.eq(ApprovalConfig::getUpdateUserId, entity.getUpdateUserId());
            }
        } else {
            wrapper.ne(ApprovalConfig::getApprovalStatus, ApprovalConsts.ApprovalStatus.DELETE);
        }
        return wrapper;
    }

    @Override
    public boolean updateApprovalStatus(StatusParam<ApprovalConfig> statusParam) {
        List<ApprovalConfig> oldList = this.list(createWrapper(null));
        Map<String, ApprovalConfig> map = new HashMap<>();
        if (oldList != null && !oldList.isEmpty()) {
            map = oldList.stream().collect(Collectors.toMap(ApprovalConfig::getApprovalType, item -> {
                return item;
            }));
        }
        List<ApprovalConfig> list = statusParam.getList();
        List<ApprovalConfig> updateList = new ArrayList<>();
        int update = 0;
        if (list != null && !list.isEmpty()) {
            for (ApprovalConfig approvalConfig : list) {
                ApprovalConfig old = map.get(approvalConfig.getApprovalType());
                if (old != null) {
                    old.setApprovalStatus(statusParam.getStatus());
                    updateList.add(old);
                } else {
                    approvalConfig.setApprovalStatus(statusParam.getStatus());
                    this.initEntity(approvalConfig, 0);
                    updateList.add(approvalConfig);
                }
            }
            update = approvalConfigMapper.insertOrUpdateBatch(updateList);
        }

        return update > 0;
    }

    @Override
    public boolean checkApproval(String approvalType) {
        LambdaQueryWrapper<ApprovalConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApprovalConfig::getOrganId, SessionManager.getLoginAccount().getOrganId());
        wrapper.eq(ApprovalConfig::getApprovalType, approvalType);
        ApprovalConfig approvalConfig = this.getOne(wrapper, false);
        if (approvalConfig != null && approvalConfig.getApprovalStatus() == ApprovalConsts.ApprovalStatus.ON) {
            return true;
        }
        return false;
    }
}
