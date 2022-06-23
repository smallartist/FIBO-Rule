package com.fibo.ddp.common.service.strategyx.tag;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.tag.TagVersion;

import java.util.List;

/**
 * (TagVersion)表服务接口
 *
 * @since 2021-12-31 13:34:09
 */
public interface TagVersionService extends IService<TagVersion> {
    TagVersion queryById(Long id);

    List<TagVersion> queryVersionListByTagId(Long tagId);

    List<String> queryFieldEnByVersionId(Long versionId);

    int addVersionList(List<TagVersion> versionList);

    boolean addVersion(TagVersion version);

    boolean copyVersion(TagVersion version);

    boolean updateVersion(TagVersion version);

    boolean updateStatus(StatusParam statusParam);
}
