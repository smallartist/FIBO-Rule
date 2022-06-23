package com.fibo.ddp.common.dao.strategyx.tag;

import com.fibo.ddp.common.model.strategyx.tag.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * (Tag)表数据库访问层
 *
 * @author jgp
 * @since 2021-12-31 13:30:19
 */
 @Mapper
public interface TagMapper extends BaseMapper<Tag>{

}

