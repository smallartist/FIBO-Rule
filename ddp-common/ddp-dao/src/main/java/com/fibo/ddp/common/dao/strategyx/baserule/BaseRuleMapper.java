package com.fibo.ddp.common.dao.strategyx.baserule;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.strategyx.baserule.BaseRule;
import org.apache.ibatis.annotations.Mapper;


/**
 * (BaseRule)表数据库访问层
 *
 * @author jgp
 * @since 2021-12-27 09:46:39
 */
 @Mapper
public interface BaseRuleMapper extends BaseMapper<BaseRule>{

}

