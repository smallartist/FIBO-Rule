package com.fibo.ddp.common.service.enginex.risk;

import com.fibo.ddp.common.model.datax.datamanage.Field;
import com.fibo.ddp.common.model.enginex.risk.Engine;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import com.fibo.ddp.common.model.enginex.risk.IndexEngineReportVo;
import com.fibo.ddp.common.model.enginex.risk.response.TestResponse;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface EngineService {

	List<Engine> getEngineByList(Engine engineVo);

	Engine getEngineById(Engine engineVo) ;
	
	int updateEngine(Engine engineVo);

	boolean saveEngine(Engine engine);

	/**
	 * 查询本公司引擎
	 */
	public List<Engine> getEngineList(@Param("organId") long organId,
                                      @Param("searchString") String searchString,
                                      @Param("list") List<Integer> list);

	/**
	 * 页面填写
	 * @param inputParam 入参
	 * @param id 版本号
	 * @return  返回id
	 * @see
	 */
	 Map<String,Object> getEngineVersionExecute(Map<String, Object> inputParam, String id);

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

	List<Field> getFieldByEngineVersion(EngineVersion version) ;

	boolean getFieldExcelTemplate(HttpServletResponse response, EngineVersion version);

	String getJsonField(EngineVersion version);

	TestResponse batchTest(HttpServletRequest request);

	// runner
	Engine getEngineById(Long id);
}
