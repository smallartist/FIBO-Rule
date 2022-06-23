package com.fibo.ddp.common.dao.enginex.risk;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.enginex.risk.Engine;
import com.fibo.ddp.common.model.enginex.risk.IndexEngineReportVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EngineMapper extends BaseMapper<Engine> {
	
	List<Engine>  getEngineByList(Engine engineVo);

	Engine getEngineById(Engine engineVo);

	int updateEngine(Engine engineVo);
	
	/**
	 * 创建引擎并返回ID
	 * @param engine
	 * @return
	 */
	int insertEngineAndReturnId(Engine engine);

	/**
	 * 查询本公司引擎
	 */
	public List<Engine> getEngineList(@Param("organId") long organId,
                                      @Param("searchString") String searchString,
                                      @Param("list") List<Integer> list);

	/**
	 * 查询首页引擎基本信息
	 * @param paramMap
	 * @return
	 */
	Map<String,Object> getIndexEngineBaseInfo(Map<String, Object> paramMap);

	/**
	 * 查询首页最近几天引擎使用情况
	 * @param paramMap
	 * @return
	 */
	List<IndexEngineReportVo> getIndexRecentDayEngineUseInfo(Map<String, Object> paramMap);

	/**
	 * 查询首页最近几个月引擎使用情况
	 * @param paramMap
	 * @return
	 */
	List<IndexEngineReportVo> getIndexRecentMonthEngineUseInfo(Map<String, Object> paramMap);

	/**
	 * 查询首页引擎使用占比
	 * @param paramMap
	 * @return
	 */
	List<Map<String,Object>> getIndexEngineUseRatio(Map<String, Object> paramMap);
}
