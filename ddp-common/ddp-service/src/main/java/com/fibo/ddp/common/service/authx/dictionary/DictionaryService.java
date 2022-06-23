package com.fibo.ddp.common.service.authx.dictionary;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.authx.dictionary.Dictionary;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * (Dictionary)表服务接口
 *
 * @author jgp
 * @since 2021-12-15 15:08:04
 */
public interface DictionaryService extends IService<Dictionary> {

    Dictionary queryByKey(String key);

    PageInfo<Dictionary> queryList(QueryListParam<Dictionary> param);

    boolean saveBatchDictionary(Collection<Dictionary> dictionaries);

    boolean update(Dictionary param);

    Map<String, Dictionary> refreshCache();

    boolean deleteByIds(List<Long> ids);
}
