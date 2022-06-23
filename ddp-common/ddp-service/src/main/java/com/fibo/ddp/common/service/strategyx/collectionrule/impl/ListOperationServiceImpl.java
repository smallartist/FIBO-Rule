package com.fibo.ddp.common.service.strategyx.collectionrule.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.dao.strategyx.collectionrule.ListOperationMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperation;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationVersion;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.strategyx.collectionrule.ListOperationService;
import com.fibo.ddp.common.service.strategyx.collectionrule.ListOperationVersionService;
import com.fibo.ddp.common.utils.constant.StatusConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("listOperationService")
public class ListOperationServiceImpl extends ServiceImpl<ListOperationMapper, ListOperation> implements ListOperationService {
    @Resource
    private ListOperationMapper listOperationMapper;
    @Resource
    private ListOperationVersionService versionService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Override
    public ListOperation queryById(Long id) {
        if (id==null){
            return null;
        }
        ListOperation operation = this.getById(id);
        if (operation==null){
            return null;
        }
        operation.setVersionList(versionService.queryListByListOpId(id));
        return operation;
    }

    @Override
    public PageInfo queryByEntity(QueryListParam<ListOperation> listParam) {
        ListOperation query = listParam.getEntity();
        Integer pageNum = listParam.getPageNum();
        Integer pageSize = listParam.getPageSize();
        if (pageNum > 0 && pageSize > 0) {
            PageHelper.startPage(pageNum, pageSize);
        }
        LambdaQueryWrapper<ListOperation> wrapper = createWrapper(query);
        List<ListOperation> operationList = listOperationMapper.selectList(wrapper);
        PageInfo pageInfo = new PageInfo(operationList);

        //TODO 循环查用户表，待优化
        for (ListOperation operation : operationList) {
            if (operation != null && operation.getCreateUserId() != null) {
                operation.setCreateUserName(sysUserMapper.findNickNameById(operation.getCreateUserId()));
                operation.setVersionList(versionService.queryListByListOpId(operation.getId()));
            }
        }
        return pageInfo;
    }

    @Override
    @Transactional
    public ListOperation insert(ListOperation listOperation) {
        //初始化基本参数
        listOperation = initParam(listOperation);
        //插入并获取insert后实体对象返回id
        boolean save = this.save(listOperation);
        if (!save) {
            throw new ApiException(ErrorCodeEnum.DECISION_TREE_SAVE_ERROR.getCode(), ErrorCodeEnum.DECISION_TREE_SAVE_ERROR.getMessage());
        }
        Long listOpId = listOperation.getId();
        List<ListOperationVersion> versionList = listOperation.getVersionList();
        for (ListOperationVersion versionVo : versionList) {
            versionVo.setListOpId(listOpId);
        }
        versionService.addVersionList(versionList);
        return this.queryById(listOpId);
    }


    @Override
    @Transactional
    public ListOperation update(ListOperation listOperation) {
        if (listOperation.getId() == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        SysUser sysUser = SessionManager.getLoginAccount();
        listOperation.setUpdateUserId(sysUser.getUserId());
        ListOperation updateOperation = new ListOperation();
        BeanUtils.copyProperties(listOperation, updateOperation);
        //修改主表
        boolean updateResult = this.updateById(updateOperation);
        if (listOperation.getStartTime()==null||listOperation.getEndTime()==null){
            LambdaUpdateWrapper<ListOperation> wrapper = new LambdaUpdateWrapper<>();

            wrapper.set(ListOperation::getStartTime, listOperation.getStartTime());
            wrapper.set(ListOperation::getEndTime, listOperation.getEndTime());
            wrapper.eq(ListOperation::getId, listOperation.getId());
            updateResult = this.update(wrapper);
        }
        if (!updateResult) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        Long listOpId = updateOperation.getId();
        List<ListOperationVersion> versionList = listOperation.getVersionList();
        if (versionList != null && versionList.size() > 0) {
            versionService.updateVersion(versionList.get(0));
        }
        return this.queryById(listOpId);
    }

    @Override
    @Transactional
    public boolean updateStatus(List<Long> ids, Integer status) {
        LambdaUpdateWrapper<ListOperation> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(ListOperation::getId, ids);
        ListOperation operation = new ListOperation();
        operation.setStatus(status);
        int updateNum = listOperationMapper.update(operation, wrapper);
        if (updateNum > 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateFolder(List<Long> ids, Long folderId) {
        LambdaUpdateWrapper<ListOperation> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(ListOperation::getId, ids);
        ListOperation operation = new ListOperation();
        operation.setFolderId(folderId);
        int updateNum = listOperationMapper.update(operation, wrapper);
        if (updateNum > 0) {
            return true;
        }
        return false;
    }


    private LambdaQueryWrapper<ListOperation> createWrapper(ListOperation query) {

        LambdaQueryWrapper<ListOperation> wrapper = new LambdaQueryWrapper<>();
        if (query!=null){
            if (query.getOpType()!=null){
                wrapper.eq(ListOperation::getOpType,query.getOpType());
            }
            if (query.getFolderId()!=null){
                wrapper.eq(ListOperation::getFolderId,query.getFolderId());
            }
            if (query.getName() != null) {
                wrapper.like(ListOperation::getName, query.getName());
            }
            if (query.getCode() != null) {
                wrapper.like(ListOperation::getCode, query.getCode());
            }
            if (query.getStartTime()!=null){
                wrapper.le(ListOperation::getStartTime,query.getStartTime());
                wrapper.ge(ListOperation::getEndTime,query.getStartTime());
            }
        }
        wrapper.ne(ListOperation::getStatus,-1);
        wrapper.eq(ListOperation::getOrganId, SessionManager.getLoginAccount().getOrganId());
        wrapper.orderByDesc( ListOperation::getId);
        return wrapper;
    }


    //新插入数据的准备工作
    private ListOperation initParam(ListOperation listOperation) {
        this.checkUniqueness(listOperation);
        //加入用户信息
        SysUser sysUser = SessionManager.getLoginAccount();
        listOperation.setCreateUserId(sysUser.getUserId());
        listOperation.setOrganId(sysUser.getOrganId());
        listOperation.setUpdateUserId(sysUser.getUserId());
        //加入状态信息
        listOperation.setStatus(StatusConst.STATUS_ENABLED);
        return listOperation;
    }

    //唯一性检查
    private boolean checkUniqueness(ListOperation listOperation) {
        ListOperation decisionTree = new ListOperation();
        decisionTree.setName(listOperation.getName());
        ListOperation info = this.getOne(new QueryWrapper<>(decisionTree), false);
        if (info != null) {
            throw new ApiException(ErrorCodeEnum.LIST_OPERATION_NAME_REPEAT.getCode(), ErrorCodeEnum.LIST_OPERATION_NAME_REPEAT.getMessage());
        }
        decisionTree.setName(null);
        decisionTree.setCode(listOperation.getCode());
        info = this.getOne(new QueryWrapper<>(decisionTree), false);
        if (info != null) {
            throw new ApiException(ErrorCodeEnum.LIST_OPERATION_Code_REPEAT.getCode(), ErrorCodeEnum.LIST_OPERATION_Code_REPEAT.getMessage());
        }
        return true;
    }

}
