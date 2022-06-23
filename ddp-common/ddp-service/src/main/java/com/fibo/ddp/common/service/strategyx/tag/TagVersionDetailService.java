package com.fibo.ddp.common.service.strategyx.tag;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.tag.TagVersionDetail;
import java.util.List;

/**
 * (TagVersionDetail)表服务接口
 * @since 2021-12-31 13:34:15
 */
public interface TagVersionDetailService extends IService<TagVersionDetail>{

    List<TagVersionDetail> queryByVersionId(Long versionId);

    List<TagVersionDetail> insertList(List<TagVersionDetail> list,Long versionId);
    List<TagVersionDetail> updateList(List<TagVersionDetail> list,Long versionId);
    boolean deleteList(List<TagVersionDetail> list);
    boolean deleteById(Long id);

}
