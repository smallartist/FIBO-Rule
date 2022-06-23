package com.fibo.ddp.common.dao.strategyx.listlibrary;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.strategyx.listlibrary.TblColumn;

import java.util.List;
import java.util.Map;

public interface TblColumnMapper extends BaseMapper<TblColumn> {

	/**
	 * getColumnList:(查找用户黑/白名单库字段列表). <br/>
	 * @param paramMap 参数集合
	 * @return 
	 * */
	public List<TblColumn> getColumnList(Map<String, Object> paramMap);
}
