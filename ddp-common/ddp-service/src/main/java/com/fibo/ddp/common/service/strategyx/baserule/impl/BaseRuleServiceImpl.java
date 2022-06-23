package com.fibo.ddp.common.service.strategyx.baserule.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.strategyx.baserule.BaseRuleMapper;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.strategyx.baserule.BaseRule;
import com.fibo.ddp.common.model.strategyx.baserule.BaseRuleCondition;
import com.fibo.ddp.common.service.strategyx.baserule.BaseRuleConditionService;
import com.fibo.ddp.common.service.strategyx.baserule.BaseRuleService;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.SnowFlakUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * (BaseRule)表服务实现类
 *
 * @author jgp
 * @since 2021-12-27 09:46:43
 */
@Service("baseRuleService")
public class BaseRuleServiceImpl extends ServiceImpl<BaseRuleMapper, BaseRule> implements BaseRuleService {
    @Resource
    private BaseRuleMapper baseRuleMapper;
    @Autowired
    private BaseRuleConditionService conditionService;

    @Override
    public BaseRule queryById(Long id) {
        if (id == null) {
            return null;
        }
        BaseRule baseRule = baseRuleMapper.selectById(id);
        BaseRuleCondition baseRuleCondition = conditionService.queryByRuleId(id);
        baseRule.setConditionTree(baseRuleCondition);
        return baseRule;

    }

    @Override
    @Transactional
    public boolean insertBaseRule(BaseRule baseRule) {
        if (baseRule == null
                || StringUtils.isBlank(baseRule.getRuleType())
                || baseRule.getConditionTree() == null) {
            return false;
        }
        boolean save = false;
        if (baseRule.getId() == null) {
            //如果没有分配id
            long newId = SnowFlakUtil.snowflakeId();
            baseRule.setId(newId);
        }
        save = this.save(baseRule);
        if (save) {
            save = conditionService.saveConditionTree(baseRule.getConditionTree(), baseRule.getId());
        }
        return save;
    }

    @Override
    public boolean insertBaseRuleList(List<BaseRule> list) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        for (BaseRule baseRule : list) {
            if (!this.insertBaseRule(baseRule)) {
                throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "插入规则块失败");
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deleteBaseRule(Long id) {
        if (id == null) {
            return true;
        }
        boolean result = this.removeById(id);
        if (result) {
            if (!conditionService.deleteConditionByRuleId(id)) {
                throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "删除规则条件表失败");
            }
        }
        return result;
    }

    @Override
    public boolean deleteBaseRuleByIds(List<Long> ids) {
        boolean result = this.removeByIds(ids);
        if (result) {
            if (!conditionService.deleteConditionByRuleIds(ids)) {
                throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "删除规则条件表失败");
            }
        }
        return result;
    }

}
