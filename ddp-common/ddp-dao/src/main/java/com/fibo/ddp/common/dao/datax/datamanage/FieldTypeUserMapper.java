package com.fibo.ddp.common.dao.datax.datamanage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.datax.datamanage.FieldTypeUser;
import com.fibo.ddp.common.model.datax.datamanage.request.FieldTreeParam;

import java.util.Map;

public interface FieldTypeUserMapper extends BaseMapper<FieldTypeUser> {

	/**
	 * createFieldTypeUserRel:(新增字段类型). <br/>
	 *
	 * @param paramMap 参数集合
	 * @return 插入成功
	 */
	public boolean createFieldTypeUserRel(Map<String, Object> paramMap);

	/**
	 * batchBindEngineFieldTypeUserRel:(把一批通用字段类型id中不存在的类型id批量绑定到引擎). <br/>
	 *
	 * @param paramMap 参数集合
	 * @return 插入成功
	 */
	public boolean batchBindEngineFieldTypeUserRel(Map<String, Object> paramMap);

	/**
	 * updateFieldTypeUserRel:(更新字段类型名). <br/>
	 *
	 * @param paramMap 参数集合
	 * @return 更新成功
	 */
	public boolean updateFieldTypeUserRel(FieldTreeParam param);

	
}
