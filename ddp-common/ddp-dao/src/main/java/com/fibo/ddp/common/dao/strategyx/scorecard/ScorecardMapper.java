package com.fibo.ddp.common.dao.strategyx.scorecard;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.strategyx.scorecard.Scorecard;

import java.util.List;
import java.util.Map;

/**
 * ClassName:ScordcardMapper </br>
 * Description:评分卡mapper
 * @see
 * */
public interface ScorecardMapper extends BaseMapper<Scorecard> {

	/**
	 * getScorecardList:(获取评分卡集合)
	 *
	 * @param  paramMap 参数集合
	 * @return 评分卡集合
	 * */
	public List<Scorecard> getScorecardList(Map<String, Object> paramMap);

	/**
	 * checkByField:(查找引用了某些字段的评分卡集合)
	 *
	 * @param  paramMap 参数集合
	 * @return 评分卡集合
	 * */
	public List<Scorecard> checkByField(Map<String, Object> paramMap);
}
