package com.fibo.ddp.common.service.strategyx.tag;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.strategyx.tag.Tag;
import com.github.pagehelper.PageInfo;
import java.util.List;

/**
 * (Tag)表服务接口
 * @since 2021-12-31 13:34:00
 */
public interface TagService extends IService<Tag>{

    Tag queryById(Long id);

    PageInfo queryByEntity(QueryListParam<Tag> listParam);

    Tag insertTag(Tag tag);

    Tag updateTag(Tag tag);

    boolean updateStatus(List<Long> ids, Integer status);

    boolean updateFolder(List<Long> ids, Long folderId);

    List<String> queryFieldEnByVersionId(Long versionId);

}
