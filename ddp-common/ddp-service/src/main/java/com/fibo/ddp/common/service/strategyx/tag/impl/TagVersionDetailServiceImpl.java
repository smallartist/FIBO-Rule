package com.fibo.ddp.common.service.strategyx.tag.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fibo.ddp.common.dao.strategyx.tag.TagVersionDetailMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.strategyx.baserule.BaseRule;
import com.fibo.ddp.common.model.strategyx.baserule.BaseRuleTypeEnum;
import com.fibo.ddp.common.model.strategyx.tag.TagVersionDetail;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.strategyx.baserule.BaseRuleService;
import com.fibo.ddp.common.service.strategyx.tag.TagVersionDetailService;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.SnowFlakUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * (TagVersionDetail)表服务实现类
 *
 * @author jgp
 * @since 2021-12-31 13:34:15
 */
@Service("tagVersionDetailService")
public class TagVersionDetailServiceImpl extends ServiceImpl<TagVersionDetailMapper, TagVersionDetail> implements TagVersionDetailService {
    @Resource
    private TagVersionDetailMapper tagVersionDetailMapper;
    @Autowired
    private BaseRuleService baseRuleService;

    @Override
    public List<TagVersionDetail> queryByVersionId(Long versionId) {
        LambdaQueryWrapper<TagVersionDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TagVersionDetail::getTagVersionId, versionId);
        List<TagVersionDetail> list = this.list(wrapper);
        for (TagVersionDetail detail : list) {
            BaseRule baseRule = baseRuleService.queryById(detail.getTagRuleId());
            detail.setRule(baseRule);
        }
        return list;
    }

    @Override
    @Transactional
    public List<TagVersionDetail> insertList(List<TagVersionDetail> list, Long versionId) {

        SysUser loginAccount = SessionManager.getLoginAccount();
        List<BaseRule> rules = new ArrayList<>();
        for (TagVersionDetail detail : list) {
            long ruleId = detail.getTagRuleId() == null ? SnowFlakUtil.snowflakeId() : detail.getTagRuleId();
            BaseRule rule = detail.getRule();
            rule.setRuleType(BaseRuleTypeEnum.STRATEGY_TAG_CONDITION.getType());
            rule.setOrganId(loginAccount.getOrganId());
            rule.setCreateUserId(loginAccount.getUserId());
            rule.setId(ruleId);
            rules.add(rule);
            detail.setOrganId(loginAccount.getOrganId());
            detail.setCreateUserId(loginAccount.getUserId());
            detail.setTagRuleId(ruleId);
            detail.setTagVersionId(versionId);
        }
        boolean b = baseRuleService.insertBaseRuleList(rules);
        this.saveOrUpdateBatch(list);
        return this.queryByVersionId(versionId);
    }

    @Override
    @Transactional
    public List<TagVersionDetail> updateList(List<TagVersionDetail> list, Long versionId) {
//        Set<Long> ids = list.stream().filter(item -> {
//            return item.getId() != null;
//        }).map(TagVersionDetail::getId).collect(Collectors.toSet());

        List<TagVersionDetail> deleteList = this.queryByVersionId(versionId);
//        List<TagVersionDetail> deleteList = oldList.stream().filter(item -> {
//            return !ids.contains(item.getId());
//        }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(deleteList)) {
            boolean b = this.deleteList(deleteList);
            if (!b) {
                throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "修改标签分层错误");
            }
        }
        return this.insertList(list, versionId);
    }

    @Override
    public boolean deleteList(List<TagVersionDetail> list) {
        List<Long> deleteIds = new ArrayList<>();
        List<Long> deleteRuleIds = new ArrayList<>();
        for (TagVersionDetail detail : list) {
            deleteIds.add(detail.getId());
            if (detail.getTagRuleId() != null) {
                deleteRuleIds.add(detail.getTagRuleId());
            }
        }
        boolean result = true;
        if (!CollectionUtils.isEmpty(deleteIds)) {
            this.removeByIds(deleteIds);
        }
        if (!CollectionUtils.isEmpty(deleteRuleIds)) {
            baseRuleService.deleteBaseRuleByIds(deleteRuleIds);
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        TagVersionDetail detail = this.getById(id);
        if (detail == null) {
            return true;
        }
        Long ruleId = detail.getTagRuleId();
        if (ruleId != null) {
            baseRuleService.deleteBaseRule(id);
        }
        return false;
    }

}
