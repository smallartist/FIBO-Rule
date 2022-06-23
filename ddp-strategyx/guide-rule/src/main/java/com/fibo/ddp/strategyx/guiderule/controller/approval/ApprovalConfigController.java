package com.fibo.ddp.strategyx.guiderule.controller.approval;

import com.fibo.ddp.common.model.approval.ApprovalConfig;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.service.approval.ApprovalConfigService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (ApprovalConfig)表控制层
 */
@RestController
@RequestMapping("/v3/approvalConfig")
public class ApprovalConfigController {
    /**
     * 服务对象
     */
    @Resource
    private ApprovalConfigService approvalConfigService;



    @PostMapping("/getApprovalList")
    public ResponseEntityDto getApprovalList(@RequestBody QueryListParam<ApprovalConfig> param) {
        PageInfo<ApprovalConfig> pageInfo = approvalConfigService.queryList(param);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }

    @PostMapping("/updateApprovalStatus")
    public ResponseEntityDto updateApprovalStatus(@RequestBody StatusParam<ApprovalConfig> statusParam) {
        boolean result = approvalConfigService.updateApprovalStatus(statusParam);
        if (result){
            return ResponseEntityBuilder.buildNormalResponse();
        }
        return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION);

    }
}
