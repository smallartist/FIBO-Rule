package com.fibo.ddp.strategyx.guiderule.controller.approval;

import com.fibo.ddp.common.model.approval.Approval;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.service.approval.ApprovalService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/v3/approval")
public class ApprovalController {
    /**
     * 服务对象
     */
    @Resource
    private ApprovalService approvalService;

    @PostMapping("/getApprovalList")
    public ResponseEntityDto getApprovalList(@RequestBody QueryListParam<Approval> param) {
        PageInfo<Approval> pageInfo = approvalService.queryList(param);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }
    @PostMapping("/updateApplyStatus")
    public ResponseEntityDto updateApplyStatus(@RequestBody StatusParam<Approval> statusParam) {
        boolean result = approvalService.updateApplyStatus(statusParam);
        if (result){
            return ResponseEntityBuilder.buildNormalResponse();
        }
        return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION);
    }
    @PostMapping("/updateStatus")
    public ResponseEntityDto updateStatus(@RequestBody StatusParam statusParam) {
        boolean result = approvalService.updateStatus(statusParam);
        if (result){
            return ResponseEntityBuilder.buildNormalResponse();
        }
        return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION);

    }
}
