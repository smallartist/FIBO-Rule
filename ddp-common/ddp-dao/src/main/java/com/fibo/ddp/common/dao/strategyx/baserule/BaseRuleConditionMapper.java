package com.fibo.ddp.common.dao.strategyx.baserule;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.strategyx.baserule.BaseRuleCondition;
import org.apache.ibatis.annotations.Mapper;


/**
 * (BaseRuleCondition)表数据库访问层
 *
 * @author jgp
 * @since 2021-12-27 09:46:43
 */
 @Mapper
public interface BaseRuleConditionMapper extends BaseMapper<BaseRuleCondition>{

}

