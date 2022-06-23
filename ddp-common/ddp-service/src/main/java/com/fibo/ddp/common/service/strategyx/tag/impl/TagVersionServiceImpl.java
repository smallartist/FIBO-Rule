package com.fibo.ddp.common.service.strategyx.tag.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fibo.ddp.common.dao.strategyx.tag.TagVersionMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.baserule.BaseRule;
import com.fibo.ddp.common.model.strategyx.tag.TagVersion;
import com.fibo.ddp.common.model.strategyx.tag.TagVersionDetail;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.strategyx.tag.TagVersionDetailService;
import com.fibo.ddp.common.service.strategyx.tag.TagVersionService;
import com.fibo.ddp.common.utils.constant.StatusConst;
import com.fibo.ddp.common.utils.util.SnowFlakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TagVersion)表服务实现类
 *
 * @author jgp
 * @since 2021-12-31 13:34:11
 */
@Service("tagVersionService")
@Slf4j
public class TagVersionServiceImpl extends ServiceImpl<TagVersionMapper, TagVersion> implements TagVersionService {
    @Resource
    private TagVersionMapper tagVersionMapper;
    @Autowired
    private TagVersionDetailService detailService;

    @Override
    public TagVersion queryById(Long id) {
        TagVersion tagVersion = this.getById(id);
        if (tagVersion != null) {
            List<TagVersionDetail> details = detailService.queryByVersionId(id);
            tagVersion.setDetailList(details);
        }
        return tagVersion;
    }

    @Override
    public List<TagVersion> queryVersionListByTagId(Long tagId) {
        LambdaQueryWrapper<TagVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TagVersion::getTagId, tagId)
                .ne(TagVersion::getStatus, StatusConst.STATUS_DELETE)
                .orderByDesc(TagVersion::getId);
        return this.list(wrapper);
    }

    @Override
    public List<String> queryFieldEnByVersionId(Long versionId) {
        return null;
    }

    @Override
    @Transactional
    public int addVersionList(List<TagVersion> versionList) {
        int result = 0;
        for (TagVersion version : versionList) {
            boolean b = addVersion(version);
            if (b) {
                result++;
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean addVersion(TagVersion version) {
        SysUser loginSysUser = SessionManager.getLoginAccount();
        version.setOrganId(loginSysUser.getOrganId());
        version.setCreateUserId(loginSysUser.getUserId());
        version.setUpdateUserId(loginSysUser.getUserId());
        version.setCreateTime(null);
        version.setUpdateTime(null);
        version.setStatus(1);
        if (version.getVersionCode() == null) {
            version.setVersionCode("V:0");
        }
        if (version.getDescription() == null) {
            version.setDescription("初始版本");
        }
        int insert = tagVersionMapper.insert(version);
        if (insert > 0) {
            boolean result = this.addVersionDetail(version);
            if (result) {
                saveSnapshot(version.getId());
            }
            return true;
        } else {
            log.error("新增标签版本失败{}", version);
        }
        return false;
    }

    @Transactional
    public boolean addVersionDetail(TagVersion version) {
        List<TagVersionDetail> detailList = version.getDetailList();
        for (TagVersionDetail detail : detailList) {
            detail.setId(null);
            detail.setTagVersionId(version.getTagId());
            BaseRule rule = detail.getRule();
            if (rule != null) {
                long snowflakeId = SnowFlakUtil.snowflakeId();
                rule.setId(snowflakeId);
                detail.setTagRuleId(snowflakeId);
            } else {
                detail.setTagRuleId(null);
            }
        }
        detailService.insertList(detailList, version.getId());
        return true;
    }

    @Override
    @Transactional
    public boolean copyVersion(TagVersion version) {
        TagVersion versionVo = this.queryById(version.getId());
        versionVo.setId(null);
        versionVo.setVersionCode(version.getVersionCode());
        versionVo.setDescription(version.getDescription());
        version.setTagId(versionVo.getTagId());
        return this.addVersion(versionVo);
    }

    @Override
    @Transactional
    public boolean updateVersion(TagVersion version) {
        Long versionId = version.getId();
        if (versionId == null) {
            return false;
        }
        SysUser loginSysUser = SessionManager.getLoginAccount();
        version.setUpdateUserId(loginSysUser.getUserId());
        //修改版本主表
        tagVersionMapper.updateById(version);
        //修改条件表
        detailService.updateList(version.getDetailList(), versionId);
        //存快照
        saveSnapshot(version.getId());
        return true;
    }

    @Override
    @Transactional
    public boolean updateStatus(StatusParam statusParam) {
        LambdaQueryWrapper<TagVersion> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.in(TagVersion::getId, statusParam.getIds());
        TagVersion ruleVersion = new TagVersion();
        ruleVersion.setStatus(statusParam.getStatus());
        boolean update = this.update(ruleVersion, updateWrapper);
        return update;
    }

    private boolean saveSnapshot(Long versionId) {
        LambdaUpdateWrapper<TagVersion> wrapper = new LambdaUpdateWrapper<>();
        TagVersion versionVo = queryById(versionId);
        versionVo.setSnapshot(null);
        wrapper.eq(TagVersion::getId, versionId).set(TagVersion::getSnapshot, JSON.toJSONString(versionVo));
        tagVersionMapper.update(null, wrapper);
        return true;
    }
}
