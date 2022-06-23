package com.fibo.ddp.common.service.strategyx.tag.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.dao.strategyx.tag.TagMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.strategyx.tag.Tag;
import com.fibo.ddp.common.model.strategyx.tag.TagVersion;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.strategyx.tag.TagService;
import com.fibo.ddp.common.service.strategyx.tag.TagVersionService;
import com.fibo.ddp.common.utils.constant.StatusConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Tag)表服务实现类
 *
 * @author jgp
 * @since 2021-12-31 13:34:01
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Resource
    private TagMapper tagMapper;
    @Autowired
    private TagVersionService versionService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Override
    public Tag queryById(Long id) {
        Tag tag = tagMapper.selectById(id);
        if (tag != null) {
            tag.setVersionList(versionService.queryVersionListByTagId(id));
        }
        return tag;
    }

    @Override
    public PageInfo queryByEntity(QueryListParam<Tag> listParam) {
        Tag query = listParam.getEntity();
        Integer pageNum = listParam.getPageNum();
        Integer pageSize = listParam.getPageSize();
        if (pageNum > 0 && pageSize > 0) {
            PageHelper.startPage(pageNum, pageSize);
        }
        LambdaQueryWrapper<Tag> wrapper = createWrapper(query);
        List<Tag> tagList = tagMapper.selectList(wrapper);
        PageInfo pageInfo = new PageInfo(tagList);

        //TODO 循环查用户表，待优化
        for (Tag tag : tagList) {
            if (tag != null && tag.getCreateUserId() != null) {
                tag.setCreateUserName(sysUserMapper.findNickNameById(tag.getCreateUserId()));
                tag.setVersionList(versionService.queryVersionListByTagId(tag.getId()));
            }
        }
        return pageInfo;
    }

    @Override
    @Transactional
    public Tag insertTag(Tag tag) {
        initParam(tag);
        boolean save = this.save(tag);
        if (!save) {
            throw new ApiException(ErrorCodeEnum.DECISION_TREE_SAVE_ERROR.getCode(), ErrorCodeEnum.DECISION_TREE_SAVE_ERROR.getMessage());
        }
        Long tagId = tag.getId();
        List<TagVersion> versionList = tag.getVersionList();
        if (versionList != null && versionList.size() > 0) {
            for (TagVersion versionVo : versionList) {
                versionVo.setTagId(tagId);
            }
            versionService.addVersionList(versionList);
        }
        return this.queryById(tagId);
    }

    @Override
    @Transactional
    public Tag updateTag(Tag tag) {
        if (tag.getId() == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        SysUser sysUser = SessionManager.getLoginAccount();
        tag.setUpdateUserId(sysUser.getUserId());
        //修改主表
        boolean updateResult = this.updateById(tag);
        if (!updateResult) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        Long tagId = tag.getId();
        List<TagVersion> versionList = tag.getVersionList();
        if (versionList != null && versionList.size() > 0) {
            versionService.updateVersion(versionList.get(0));
        }
        return this.queryById(tagId);
    }

    @Override
    @Transactional
    public boolean updateStatus(List<Long> ids, Integer status) {
        LambdaUpdateWrapper<Tag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Tag::getId, ids);
        Tag tag = new Tag();
        tag.setStatus(status);
        int updateNum = tagMapper.update(tag, wrapper);
        if (updateNum > 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateFolder(List<Long> ids, Long folderId) {
        LambdaUpdateWrapper<Tag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Tag::getId, ids);
        Tag tag = new Tag();
        tag.setFolderId(folderId);
        int updateNum = tagMapper.update(tag, wrapper);
        if (updateNum > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> queryFieldEnByVersionId(Long versionId) {
        return versionService.queryFieldEnByVersionId(versionId);
    }

    private LambdaQueryWrapper<Tag> createWrapper(Tag query) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        if (query!=null){
            if (query.getTagName() != null) {
                wrapper.like(Tag::getTagName, query.getTagName());
            }
            if (query.getTagCode() != null) {
                wrapper.like(Tag::getTagCode, query.getTagCode());
            }
            if (query.getFolderId()!=null){
                wrapper.eq(Tag::getFolderId,query.getFolderId());
            }
        }
        wrapper.ne(Tag::getStatus,-1);
        wrapper.eq(Tag::getOrganId, SessionManager.getLoginAccount().getOrganId());
        wrapper.orderByDesc(Tag::getUpdateTime, Tag::getCreateTime, Tag::getId);
        return wrapper;
    }

    //新插入数据的准备工作
    private Tag initParam(Tag vo) {
        this.checkUniqueness(vo);
        //加入用户信息
        SysUser sysUser = SessionManager.getLoginAccount();
        vo.setCreateUserId(sysUser.getUserId());
        vo.setOrganId(sysUser.getOrganId());
        vo.setUpdateUserId(sysUser.getUserId());
        //加入状态信息
        vo.setStatus(StatusConst.STATUS_ENABLED);
        return vo;
    }

    //唯一性检查
    private boolean checkUniqueness(Tag vo) {
        Tag tag = new Tag();
        tag.setTagName(vo.getTagName());
        Tag info = this.getOne(new QueryWrapper<>(tag), false);
        if (info != null) {
            throw new ApiException(ErrorCodeEnum.DECISION_TABLES_NAME_REPEAT.getCode(), ErrorCodeEnum.DECISION_TABLES_NAME_REPEAT.getMessage());
        }
        tag.setTagName(null);
        tag.setTagCode(vo.getTagCode());
        info = this.getOne(new QueryWrapper<>(tag), false);
        if (info != null) {
            throw new ApiException(ErrorCodeEnum.DECISION_TABLES_CODE_REPEAT.getCode(), ErrorCodeEnum.DECISION_TABLES_CODE_REPEAT.getMessage());
        }
        return true;
    }
}
