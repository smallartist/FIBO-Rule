package com.fibo.ddp.common.dao.strategyx.scorecard;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评分卡明细表(ScorecardDetail)表数据库访问层
 */
@Mapper
public interface ScorecardDetailMapper extends BaseMapper<ScorecardDetail> {
}

