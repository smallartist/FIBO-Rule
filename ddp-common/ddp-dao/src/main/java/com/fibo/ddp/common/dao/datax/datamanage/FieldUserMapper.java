package com.fibo.ddp.common.dao.datax.datamanage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.datax.datamanage.FieldUser;

import java.util.List;
import java.util.Map;

public interface FieldUserMapper extends BaseMapper<FieldUser> {

	/**
	 * createFieldUserRel:(绑定字段和用户关系). <br/>
	 *
	 * @param  fieldUser 用户字段实体类
	 * @return 插入成功 
	 * */
	public boolean createFieldUserRel(FieldUser fieldUserVo);
	
	/**
	 * batchCreateFieldUserRel:(批量导入字段信息后批量绑定字段和用户关系). <br/>
	 *
	 * @param  paramMap 参数集合
	 * @return 插入成功 
	 * */
	public boolean batchCreateFieldUserRel(Map<String, Object> paramMap);

	/**
	 * batchBindEngineFieldUserRel:(把一批通用字段id中未绑定的字段id批量绑定到引擎). <br/>
	 *
	 * @param  paramMap 参数集合
	 * @return 插入成功
	 * */
	public boolean batchBindEngineFieldUserRel(Map<String, Object> paramMap);

	/**
	 * updateStatus:(单个或批量更新用户字段关系). <br/>
	 *
	 * @param  paramMap 参数集合
	 * @return 更新成功
	 * */
	public boolean updateStatus(Map<String, Object> paramMap);

	/**
	 * deleteFieldByIds:(批量修改字段删除状态为启用状态(1)). <br/>
	 *
	 * @param paramMap 参数集合
	 * @return 更新是否成功
	 */
	public boolean backFieldByIds(Map<String, Object> paramMap);

	/**
	 * 统计组织内指标数
	 */
	int countFieldByOrganId(Map<String, Object> paramMap);

	/**
	 *
	 * @param paramMap
	 * @return
	 */
	List<Map<String,Object>> countFieldGroupByType(Map<String, Object> paramMap);
}
