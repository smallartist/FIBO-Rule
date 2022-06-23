package com.fibo.ddp.common.service.approval;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.approval.Approval;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.github.pagehelper.PageInfo;

/**
 * (Approval)表服务接口
 */
public interface ApprovalService extends IService<Approval> {

    boolean addApproval(Approval entity);

    PageInfo<Approval> queryList(QueryListParam<Approval> param);

    boolean updateApplyStatus(StatusParam<Approval> statusParam);

    boolean updateStatus(StatusParam statusParam);
}
