package com.fibo.ddp.common.service.strategyx.baserule;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.baserule.BaseRuleCondition;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (BaseRuleCondition)表服务接口
 *
 * @since 2021-12-27 09:46:43
 */
public interface BaseRuleConditionService extends IService<BaseRuleCondition> {

    BaseRuleCondition queryByRuleId(Long ruleId);

    boolean saveConditionTree(BaseRuleCondition condition, Long ruleId);

    @Transactional
    BaseRuleCondition updateCondition(Long ruleId, BaseRuleCondition condition);

    @Transactional
    boolean deleteConditionByRuleId(Long ruleId);

    boolean deleteConditionByRuleIds(List<Long> ruleIds);

    BaseRuleCondition assemble(List<BaseRuleCondition> list);

    List<BaseRuleCondition> disassemble(BaseRuleCondition vo, Long ruleId);

}
