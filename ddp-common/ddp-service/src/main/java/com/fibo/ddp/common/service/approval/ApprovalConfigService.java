package com.fibo.ddp.common.service.approval;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.approval.ApprovalConfig;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.github.pagehelper.PageInfo;

/**
 * (ApprovalConfig)表服务接口
 */
public interface ApprovalConfigService extends IService<ApprovalConfig> {

    boolean addApprovalConfig(ApprovalConfig entity);

    PageInfo<ApprovalConfig> queryList(QueryListParam<ApprovalConfig> param);

    boolean updateApprovalStatus(StatusParam<ApprovalConfig> statusParam);
    //校验是否需要审批
    boolean checkApproval(String approvalType);
}
