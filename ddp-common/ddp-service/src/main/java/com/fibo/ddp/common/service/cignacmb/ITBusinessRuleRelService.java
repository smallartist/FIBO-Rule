package com.fibo.ddp.common.service.cignacmb;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.cignacmb.TBusinessRuleRel;
import com.fibo.ddp.common.model.cignacmb.req.BusinessRuleRelReq;
import com.fibo.ddp.common.model.common.ResponseEntityDto;

/**
 * <p>
 * 业务类型与规则关联表 服务类
 * </p>
 *
 * @author oldRose
 * @since 2021-11-10
 */
public interface ITBusinessRuleRelService extends IService<TBusinessRuleRel> {

    ResponseEntityDto<?> addBusinessRuleRel(BusinessRuleRelReq param);

    ResponseEntityDto<?> editBusinessRuleRel(BusinessRuleRelReq param);

    ResponseEntityDto<?> detailBusinessRuleRel(BusinessRuleRelReq param);

    ResponseEntityDto<?> deleteBusinessRuleRel(BusinessRuleRelReq param);

    ResponseEntityDto<?> pageList(BusinessRuleRelReq param);

    ResponseEntityDto<?> queryByBusinessType(BusinessRuleRelReq param);
}
