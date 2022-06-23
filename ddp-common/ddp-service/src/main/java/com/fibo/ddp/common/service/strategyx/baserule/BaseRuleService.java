package com.fibo.ddp.common.service.strategyx.baserule;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.baserule.BaseRule;

import java.util.List;

/**
 * (BaseRule)表服务接口
 * @since 2021-12-27 09:46:42
 */
public interface BaseRuleService extends IService<BaseRule>{

    BaseRule queryById(Long id);

    boolean insertBaseRule(BaseRule baseRule);

    boolean insertBaseRuleList(List<BaseRule> list);

    boolean deleteBaseRule(Long id);

    boolean deleteBaseRuleByIds(List<Long> ids);
}
