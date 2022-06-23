package com.fibo.ddp.common.service.approval.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.approval.ApprovalMapper;
import com.fibo.ddp.common.model.approval.Approval;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.service.approval.ApprovalService;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.enginex.risk.EngineVersionService;
import com.fibo.ddp.common.utils.constant.ApprovalConsts;
import com.fibo.ddp.common.utils.constant.StatusConst;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * (Approval)表服务实现类
 */
@Service("approvalService")
public class ApprovalServiceImpl extends ServiceImpl<ApprovalMapper, Approval> implements ApprovalService {
    @Resource
    private ApprovalMapper approvalMapper;
    @Resource
    private EngineVersionService engineVersionService;

    @Override
    @Transactional
    public boolean addApproval(Approval entity) {
        if (entity == null) {
            return false;
        }
        this.initEntity(entity, 0);
        boolean insert = this.saveOrUpdate(entity);
        return insert;
    }

    private void initEntity(Approval entity, int type) {
        SysUser loginAccount = SessionManager.getLoginAccount();
        if (entity==null){
            entity = new Approval();
        }
        switch (type) {
            //新增
            case 0:
                if (entity.getApplyStatus() == null) {
                    entity.setApplyStatus(ApprovalConsts.ApplyStatus.WAIT);
                }
                if (entity.getStatus() == null) {
                    entity.setStatus(StatusConst.STATUS_ENABLED);
                }
                if (entity.getCreateTime() != null) {
                    entity.setCreateTime(new Date());
                }
                if (loginAccount!=null){
                    if (entity.getCreateUserId() == null) {
                        entity.setCreateUserId(loginAccount.getUserId());
                    }
                    if (StringUtils.isBlank(entity.getCreateUserName())) {
                        entity.setCreateUserName(loginAccount.getNickName());
                    }
                    if (entity.getOrganId()==null){
                        entity.setOrganId(loginAccount.getOrganId());
                    }
                }
                break;
            //修改审批状态,需要一并执行修改该的内容
            case 1:
               if (entity.getApplyStatus()!= ApprovalConsts.ApplyStatus.CANCEL){
                   if (entity.getApprovalTime() == null) {
                       entity.setApprovalTime(new Date());
                   }
                   if (loginAccount!=null){
                       if (entity.getApprovalUserId() == null) {
                           entity.setApprovalUserId(loginAccount.getUserId());
                       }
                       if (StringUtils.isBlank(entity.getApprovalUserName())) {
                           entity.setApprovalUserName(loginAccount.getNickName());
                       }
                   }
               }
            //修改
            case 2:
                if (entity.getUpdateTime() == null) {
                    entity.setUpdateTime(new Date());
                }
                if (loginAccount!=null){
                    if (entity.getUpdateUserId() == null) {
                        entity.setUpdateUserId(loginAccount.getUserId());
                    }
                    if (StringUtils.isBlank(entity.getUpdateUserName())) {
                        entity.setUpdateUserName(loginAccount.getNickName());
                    }
                }
                break;
        }


    }

    @Override
    public PageInfo<Approval> queryList(QueryListParam<Approval> param) {
        int pageNum = param.getPageNum();
        int pageSize = param.getPageSize();
        if (pageNum > 0 && pageSize > 0) {
            PageHelper.startPage(pageNum, pageSize);
        }
        LambdaQueryWrapper<Approval> wrapper = this.createWrapper(param.getEntity());
        wrapper.orderByDesc(Approval::getId);
        List<Approval> list = this.list(wrapper);
        PageInfo<Approval> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    @Override
    @Transactional
    public boolean updateApplyStatus(StatusParam<Approval> statusParam) {
        LambdaQueryWrapper<Approval> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.in(Approval::getId, statusParam.getIds());
        Approval approval = new Approval();
        approval.setApplyStatus(statusParam.getStatus());
        initEntity(approval,1);
        //修改状态
        boolean update = this.update(approval, updateWrapper);
        if (update) {
            //处理申请相关的逻辑
            handlerApplyByType(statusParam.getList(), statusParam.getStatus());
        }
        return update;
    }

    private boolean handlerApplyByType(List<Approval> list, int status) {
        for (Approval approval : list) {
            try {
                String applyDetail = approval.getApplyDetail();
                JSONObject jsonObject = JSON.parseObject(applyDetail);
                switch (approval.getApplyType()) {
                    //决策流版本发布
                    case ApprovalConsts.ApprovalType.DECISION_FLOW_VERSION_DEPLOY:
                        long engineVersionId = jsonObject.getLongValue("engineVersionId");
                        engineVersionService.approvalCallBack(engineVersionId, status);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updateStatus(StatusParam statusParam) {
        LambdaQueryWrapper<Approval> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Approval::getId,statusParam.getIds());
        Approval approval = new Approval();
        approval.setStatus(statusParam.getStatus());
        boolean update = this.update(approval, wrapper);
        return update;
    }

    private LambdaQueryWrapper<Approval> createWrapper(Approval entity) {
        LambdaQueryWrapper<Approval> wrapper = new LambdaQueryWrapper<>();
        SysUser loginAccount = SessionManager.getLoginAccount();
        if (loginAccount.getOrganId()!=1){
            wrapper.eq(Approval::getOrganId, loginAccount.getOrganId());
        }
        if (entity != null) {
            if (entity.getId() != null) {
                wrapper.eq(Approval::getId, entity.getId());
            }
            if (StringUtils.isNotBlank(entity.getApplyType())) {
                wrapper.eq(Approval::getApplyType, entity.getApplyType());
            }
            if (StringUtils.isNotBlank(entity.getCreateUserName())) {
                wrapper.eq(Approval::getCreateUserName, entity.getCreateUserName());
            }
            if (StringUtils.isNotBlank(entity.getApprovalUserName())) {
                wrapper.eq(Approval::getApprovalUserName, entity.getApprovalUserName());
            }
            if (entity.getApplyStatus()!=null){
                wrapper.eq(Approval::getApplyStatus,entity.getApplyStatus());
            }
            if (entity.getStatus() != null) {
                wrapper.eq(Approval::getStatus, entity.getStatus());
            } else {
                wrapper.eq(Approval::getStatus, StatusConst.STATUS_ENABLED);
            }
            if (entity.getCreateUserId()!=null) {
                if (entity.getCreateUserId()==0L){
                    wrapper.eq(Approval::getCreateUserId, loginAccount.getUserId());
                }else {
                    wrapper.eq(Approval::getCreateUserId,entity.getCreateUserId());
                }

            }
            if (entity.getQueryStartTime()!=null){
                wrapper.ge(Approval::getCreateTime,entity.getQueryStartTime());
            }
            if (entity.getQueryEndTime()!=null){
                wrapper.le(Approval::getCreateTime,entity.getQueryEndTime());
            }
        } else {
            wrapper.eq(Approval::getStatus, StatusConst.STATUS_ENABLED);
        }
        return wrapper;
    }

    private boolean checkAuthority(SysUser sysUser) {

        return false;
    }
}
