package com.fibo.ddp.common.dao.enginex.risk;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.enginex.risk.EngineResultSetDTO;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EngineVersionMapper extends BaseMapper<EngineVersion> {

    int insertOne(EngineVersion record);

    EngineVersion selectByPrimaryKey(long versionId);

    int updateByPrimaryKeySelective(EngineVersion record);

    int updateByPrimaryKey(EngineVersion record);

	int undeployVersion(long engineId);

	int updateBootState(@Param("versionId") long versionId, @Param("bootState") int bootState);

	/**
	 * getEngineVersionListByEngineId：(根据引擎id获取引擎下的所有版本)
	 *
	 * @param engineId 引擎id
	 * @return 版本集合
	 * */
	List<EngineVersion> getEngineVersionListByEngineId(long engineId);

	// 新增的V2
	List<EngineVersion> getEngineVersionListByEngineIdV2(long engineId);

	int insertEngineVersionAndReturnId(EngineVersion engineVersion);

	EngineVersion getLatestEngineVersion(EngineVersion engineVersion);

	EngineVersion getLatestEngineSubVersion(EngineVersion engineVersion);

	/**
	 * 清除所有此版本下的小版本(除过0版本)
	 * @param map
	 * @return
	 */
	int cleanSubVersionByVersion(Map map);

	/**
	 * 获取引擎对应的版本及引擎名信息
	 * yuanlinfeng
	 * @param paramMap
	 * @return
	 */
	public List<EngineVersion> getEngineVersionByEngineId(Map<String, Object> paramMap);

    List<EngineVersion> selectAll();

	List<EngineResultSetDTO> countDecisionResult();

	/**
	 * 获取引擎正在运行中的版本
	 * @param engineId
	 * @return
	 */
	EngineVersion getRunningVersion(@Param("engineId") Long engineId);
}