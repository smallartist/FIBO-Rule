package com.fibo.ddp.strategyx.guiderule.controller.cignacmb;


import com.fibo.ddp.common.model.cignacmb.req.BusinessRuleRelReq;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.service.cignacmb.ITBusinessRuleRelService;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 业务类型与规则关联表 前端控制器
 * </p>
 *
 * @author oldRose
 * @since 2021-11-10
 */
@RestController
@RequestMapping("/businessRuleRel")
public class TBusinessRuleRelController {

    @Autowired
    private ITBusinessRuleRelService businessRuleRelService;

    /**
     * 新增业务类型和规则关联
     */
    @PostMapping("/add")
    @ArchivesLog(operationType = OpTypeConst.BUSINESS_RULE_REL_ADD)
    public ResponseEntityDto<?> addBusinessRuleRel(@RequestBody BusinessRuleRelReq param) {
         return businessRuleRelService.addBusinessRuleRel(param);
    }

    /**
     * 编辑业务类型和规则关联
     */
    @PostMapping("/edit")
    @ArchivesLog(operationType = OpTypeConst.BUSINESS_RULE_REL_EDIT)
    public ResponseEntityDto<?> editBusinessRuleRel(@RequestBody BusinessRuleRelReq param) {
        return businessRuleRelService.editBusinessRuleRel(param);
    }

    /**
     * 查看详情业务类型和规则关联
     */
    @PostMapping("/detail")
    @ArchivesLog(operationType = OpTypeConst.BUSINESS_RULE_REL_DETAIL)
    public ResponseEntityDto<?> detailBusinessRuleRel(@RequestBody BusinessRuleRelReq param) {
        return businessRuleRelService.detailBusinessRuleRel(param);
    }
    /**
     * 删除详情业务类型和规则关联
     */
    @PostMapping("/delete")
    @ArchivesLog(operationType = OpTypeConst.BUSINESS_RULE_REL_DELETE)
    public ResponseEntityDto<?> deleteBusinessRuleRel(@RequestBody BusinessRuleRelReq param) {
        return businessRuleRelService.deleteBusinessRuleRel(param);
    }

    /**
     * 获取投放计划列表分页(按修改时间，创建时间降序排列)
     */
    @PostMapping("/list")
    @ArchivesLog(operationType = OpTypeConst.BUSINESS_RULE_REL_PAGE)
    public ResponseEntityDto<?> pageList(@RequestBody BusinessRuleRelReq param) {
        return businessRuleRelService.pageList(param);
    }
    /**
     * 根据业务类型查询对应规则
     */
    @PostMapping("/queryByBusinessType")
    @ArchivesLog(operationType = OpTypeConst.QUERY_BY_BUSINESSTYPE)
    public ResponseEntityDto<?> queryByBusinessType(@RequestBody BusinessRuleRelReq param) {
        return businessRuleRelService.queryByBusinessType(param);
    }
}
