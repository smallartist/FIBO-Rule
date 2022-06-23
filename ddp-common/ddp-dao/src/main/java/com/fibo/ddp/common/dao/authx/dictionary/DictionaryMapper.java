package com.fibo.ddp.common.dao.authx.dictionary;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.authx.dictionary.Dictionary;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;


/**
 * (Dictionary)表数据库访问层
 *
 * @author jgp
 * @since 2021-12-15 15:08:04
 */
public interface DictionaryMapper extends BaseMapper<Dictionary> {

    int insertOrUpdateBatch(@Param("entities") Collection<Dictionary> entities);
}

