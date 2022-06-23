package com.fibo.ddp.common.dao.enginex.risk;

import com.fibo.ddp.common.model.analyse.AnalyseDecisionResult;
import com.fibo.ddp.common.model.enginex.risk.EngineResultSet;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EngineResultSetMapper {
	/**
	 *
	 * 增加结果集
	 * @param resultSet 结果集对象
	 * @return  返回结果
	 * @see
	 */
	int insertResultSet(EngineResultSet resultSet);
	/**
	 *
	 * 查询结果集列表
	 * @param resultSet 查询对象
	 * @return  返回结果集
	 * @see
	 */
	List<EngineResultSet> getResultSetByList(EngineResultSet resultSet);

	/**
	 * 根据引擎编号和时间段获取结果集数据
	 * @param map
	 * @return
	 */
	List<EngineResultSet> getEngineResultSetBySegment(Map map);

	/**
	 *
	 * 通过主键编号得到
	 * @param resultSet 对象
	 * @return  返回对象
	 * @see
	 */
	EngineResultSet getResultSetById(EngineResultSet resultSet);

	List<EngineResultSet> getResultSetDetailsById(long resultSetId);

	/**
	 * 查找引擎id的批量测试结果
	 * yuanlinfeng
	 * @param resultSetId
	 * @return
	 */
	List<EngineResultSet> getBatchTestResultSetByEngineId(Map<String,Object> paramMap);

	/**
	 * 查找引擎批量测试批次号的所有测试结果
	 * yuanlinfeng
	 * @param resultSetId
	 * @return
	 */
	List<EngineResultSet> getBatchTestResultSetByBatchNo(Map<String,Object> paramMap);

	/**
	 * 更新结果出参
	 * @param resultSet
	 */
	void updateResultOutput(EngineResultSet resultSet);

	List<AnalyseDecisionResult> countDecisionCallNum();

	void updateResultById(@Param("resultId") Integer resultId, @Param("rowKeyStr") String rowKeyStr);
}
