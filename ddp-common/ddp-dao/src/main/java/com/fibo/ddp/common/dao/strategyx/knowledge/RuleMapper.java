package com.fibo.ddp.common.dao.strategyx.knowledge;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.strategyx.knowledge.Rule;

import java.util.List;
import java.util.Map;

/**
 * ClassName:OrganRuleMapper </br>
 * Description:规则mapper
 * @see
 * */
public interface RuleMapper extends BaseMapper<Rule> {

	/**
	 * getRuleList:(获取规则集合)
	 *
	 * @param  paramMap 参数集合
	 * @return 规则集合
	 * */
	public List<Rule> getRuleList(Map<String, Object> paramMap);

	/**
	 * getRuleList:(查找引用了某些字段的规则集合)
	 *
	 * @param  paramMap 参数集合
	 * @return 规则集合
	 * */
	public List<Rule> checkByField(Map<String, Object> paramMap);

	List<Rule> getAllCodeNameParentId();
}
