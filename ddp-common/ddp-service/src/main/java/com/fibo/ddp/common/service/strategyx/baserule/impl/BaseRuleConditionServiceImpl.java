package com.fibo.ddp.common.service.strategyx.baserule.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.strategyx.baserule.BaseRuleConditionMapper;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.strategyx.baserule.BaseRuleCondition;
import com.fibo.ddp.common.model.strategyx.guiderule.RuleLoopGroupAction;
import com.fibo.ddp.common.service.strategyx.baserule.BaseRuleConditionService;
import com.fibo.ddp.common.service.strategyx.guiderule.RuleLoopGroupActionService;
import com.fibo.ddp.common.utils.util.SnowFlakUtil;
import com.fibo.ddp.common.utils.constant.strategyx.CondConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (BaseRuleCondition)表服务实现类
 *
 * @author jgp
 * @since 2021-12-27 09:46:43
 */
@Service("baseRuleConditionService")
public class BaseRuleConditionServiceImpl extends ServiceImpl<BaseRuleConditionMapper, BaseRuleCondition> implements BaseRuleConditionService {
    @Resource
    private BaseRuleConditionMapper baseRuleConditionMapper;
    @Resource
    private RuleLoopGroupActionService loopGroupActionService;

    @Override
    public BaseRuleCondition queryByRuleId(Long ruleId) {
        if (ruleId == null) {
            return null;
        }
        LambdaQueryWrapper<BaseRuleCondition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseRuleCondition::getRuleId, ruleId);
        List<BaseRuleCondition> list = this.list(wrapper);
        BaseRuleCondition result = assemble(list);
        return result;
    }

    @Override
    @Transactional
    public boolean saveConditionTree(BaseRuleCondition condition, Long ruleId) {
        if (condition == null || ruleId == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "存储条件失败：参数错误");
        }
        boolean result = false;
        this.deleteConditionByRuleId(ruleId);
        Long parentId = CondConst.DEFAULT_CONDITION_PARENT_ID;
        condition.setParentId(parentId);
        List<BaseRuleCondition> conditionList = disassemble(condition, ruleId);
        result = this.saveOrUpdateBatch(conditionList);
        if (result) {
            List<RuleLoopGroupAction> actions = new ArrayList<>();
            for (BaseRuleCondition ruleCondition : conditionList) {
                List<RuleLoopGroupAction> loopGroupActions = ruleCondition.getLoopGroupActions();
                if (CollectionUtils.isNotEmpty(loopGroupActions)) {
                    actions.addAll(loopGroupActions);
                }
            }
            if (CollectionUtils.isNotEmpty(actions)) {
                result = loopGroupActionService.saveBatch(actions);
            }
        }
        if (!result) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "条件区域存储失败");
        }
        return result;
    }

    /**
     * 修改规则条件
     *
     * @param condition
     * @return
     */
    @Override
    @Transactional
    public BaseRuleCondition updateCondition(Long ruleId, BaseRuleCondition condition) {
        if (ruleId == null) {
            return null;
        }
        //删除一个规则下的所有条件
        boolean delete = this.deleteConditionByRuleId(ruleId);
        BaseRuleCondition check = null;
        if (!delete) {
            check = this.queryByRuleId(ruleId);
        }
        //插入条件树
        if ((delete || check == null) && condition != null) {
            this.saveConditionTree(condition, ruleId);
        }
        return this.queryByRuleId(ruleId);
    }

    /**
     * 删除根据规则id规则条件
     *
     * @param ruleId
     * @return
     */
    @Override
    @Transactional
    public boolean deleteConditionByRuleId(Long ruleId) {
        if (ruleId == null) {
            return false;
        }
        //删除循环动作子表中的内容
        LambdaQueryWrapper<BaseRuleCondition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseRuleCondition::getRuleId, ruleId);
        wrapper.eq(BaseRuleCondition::getLogic, CondConst.CondLogic.LOOP_RULE_LOGICAL);
        List<BaseRuleCondition> conditionList = baseRuleConditionMapper.selectList(wrapper);
        for (BaseRuleCondition condition : conditionList) {
            loopGroupActionService.deleteLoopGroupByForId(condition.getId());
        }
        //删除条件表内容
        LambdaQueryWrapper<BaseRuleCondition> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseRuleCondition::getRuleId, ruleId);
        int delete = baseRuleConditionMapper.delete(queryWrapper);
        return delete > 0;
    }

    @Override
    public boolean deleteConditionByRuleIds(List<Long> ruleIds) {
        LambdaQueryWrapper<BaseRuleCondition> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BaseRuleCondition::getRuleId, ruleIds);
        wrapper.eq(BaseRuleCondition::getLogic, CondConst.CondLogic.LOOP_RULE_LOGICAL);
        List<BaseRuleCondition> conditionList = baseRuleConditionMapper.selectList(wrapper);
        for (BaseRuleCondition condition : conditionList) {
            loopGroupActionService.deleteLoopGroupByForId(condition.getId());
        }
        LambdaQueryWrapper<BaseRuleCondition> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BaseRuleCondition::getRuleId, ruleIds);
        int delete = baseRuleConditionMapper.delete(queryWrapper);
        return delete > 0;
    }

    @Override
    //装配方法，将规则条件List装配成一个规则树并返回
    public BaseRuleCondition assemble(List<BaseRuleCondition> list) {
        BaseRuleCondition root = null;
        //获取根节点，根节点只有一个的时候进行操作，并且返回拼装好的规则树，否则返回null
        List<BaseRuleCondition> collect = list.stream().filter(item -> {
            return item.getParentId() == CondConst.DEFAULT_CONDITION_PARENT_ID;
        }).collect(Collectors.toList());
        if (collect.size() == 1) {
            root = collect.get(0);
            BaseRuleCondition ruleTree = coupling(list, root);
            return ruleTree;
        }
        return null;
    }

    @Override
    public List<BaseRuleCondition> disassemble(BaseRuleCondition condition, Long ruleId) {
        condition.setParentId(CondConst.DEFAULT_CONDITION_PARENT_ID);
        List<BaseRuleCondition> decoupling = this.decoupling(condition, ruleId);
        return decoupling;
    }


    //耦合方法：将规则节点列表耦合规则树(),循环规则的子节点需要去查循环表获取
    private BaseRuleCondition coupling(List<BaseRuleCondition> list, BaseRuleCondition root) {
        List<BaseRuleCondition> children = new ArrayList<>();
        for (BaseRuleCondition condition : list) {
            //处理root的子节点
            if (!root.getId().equals(condition.getParentId())) {
                continue;
            }
            BaseRuleCondition coupCond = coupling(list, condition);
            String logic = root.getLogic();
            if (StringUtils.isNotBlank(logic)) {
                switch (logic) {
                    //当root为for节点，则此子节点需要拼上循环动作
                    case CondConst.CondLogic.LOOP_RULE_LOGICAL:
                        List<RuleLoopGroupAction> loopList = loopGroupActionService.getRuleLoopList(root.getId(), condition.getId());
                        coupCond.setLoopGroupActions(loopList);
                        if (condition.getCondType() == CondConst.CondType.LOOP_RULE_RESULT_CONDITION) {
                            root.setLoopResultCondition(coupCond);
                            continue;
                        }
                        break;
                    //当root为条件组节点，则此子节点需要拼上条件组结果
                    case CondConst.CondLogic.CONDITION_GROUP_LOGICAL:
                        if (condition.getCondType() == CondConst.CondType.CONDITION_GROUP_RESULT_CONDITION) {
                            root.setCondGroupResultCondition(coupCond);
                            continue;
                        }
                        break;
                    default:
                        break;
                }
            }
            children.add(coupCond);
        }
        root.setChildren(children);
        return root;
    }

    //解耦方法：将规则树解耦为节点列表
    private List<BaseRuleCondition> decoupling(BaseRuleCondition condition, Long ruleId) {
        //设置雪花id
        if (condition.getId() == null || !ruleId.equals(condition.getRuleId())) {
            condition.setId(SnowFlakUtil.snowflakeId());
        }
        //设置ruleId
        condition.setRuleId(ruleId);
        List<BaseRuleCondition> resultList = new ArrayList<>();
        List<BaseRuleCondition> children = condition.getChildren();
        //处理条件树根节点的子条件
        if (children != null && children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {
                BaseRuleCondition child = children.get(i);
                //设置父id
                child.setParentId(condition.getId());
                List<BaseRuleCondition> childList = decoupling(child, ruleId);
                resultList.addAll(childList);
            }
        }
        resultList.add(condition);
        //处理for条件的结果条件
        Integer conditionType = condition.getCondType();
        if (conditionType == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "存储条件失败，条件类型不能为空");
        }
        switch (conditionType) {
            case CondConst.CondType.LOOP_RULE_ROOT:
                BaseRuleCondition loopResult = condition.getLoopResultCondition();
                loopResult.setRuleId(ruleId);
                List<BaseRuleCondition> loopResultList = decoupling(loopResult, ruleId);
                resultList.addAll(loopResultList);
                break;
            case CondConst.CondType.CONDITION_GROUP_ROOT:
                BaseRuleCondition condGroupResult = condition.getCondGroupResultCondition();
                condGroupResult.setRuleId(ruleId);
                List<BaseRuleCondition> condGroupResultList = decoupling(condGroupResult, ruleId);
                resultList.addAll(condGroupResultList);
                break;
            case CondConst.CondType.LOOP_RULE_RESULT_CONDITION:
                List<RuleLoopGroupAction> loopGroupActions = condition.getLoopGroupActions();
                if (CollectionUtils.isNotEmpty(loopGroupActions)) {
                    for (RuleLoopGroupAction action : loopGroupActions) {
                        action.setConditionForId(condition.getParentId());
                        action.setConditionGroupId(condition.getId());
                    }
                }
                break;
            case CondConst.CondType.CONDITION_GROUP_RESULT_CONDITION:

                break;
        }
        return resultList;
    }
}
