package com.fibo.ddp.common.dao.strategyx.listlibrary;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.strategyx.listlibrary.ListDb;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ListDbMapper extends BaseMapper<ListDb> {

	/**
	 * findByUser:(查找用户黑/白名单库列表). <br/>
	 *
	 * @param paramMap 参数集合
	 * @return 
	 * */
	public List<ListDb> findByUser(Map<String, Object> paramMap);

	/**
	 * checkByField:(检索字段被哪个黑/白名单库使用). <br/>
	 *
	 * @param paramMap 参数集合
	 * @return
	 * */
	public String checkByField(Map<String, Object> paramMap);

	/**
	 * isExists:(检索黑(白)名单名称是否存在). <br/>
	 *
	 * @param paramMap 参数集合
	 * @return
	 * */
	public Integer isExists(Map<String, Object> paramMap);

	/**
	 * updateStatus:(单个或批量更新名单状态). <br/>
	 *
	 * @param  paramMap 参数集合
	 * @return 更新成功
	 * */
	public boolean updateStatus(Map<String, Object> paramMap);

	/**
	 * createListDb:(建黑、白名单库). <br/>
	 *
	 * @param sqlStr 建表sql
	 * @return 是否成功
	 */
	public boolean createListDb(ListDb listDb);

	/**
	 * createTable:(建黑、白名单库存放的表). <br/>
	 *
	 * @param sqlStr 建表sql
	 * @return 是否成功
	 */
	public boolean createTable(Map<String, Object> paramMap);

	/**
	 * createIndex:(建黑、白名单库存放的表索引). <br/>
	 *
	 * @param sqlStr 建表sql
	 * @return 是否成功
	 */
	public boolean createIndex(Map<String, Object> paramMap);

	/**
	 * findById:(按编号查找名单库实体对象). <br/>
	 *
	 * @param paramMap 参数集合
	 * @return
	 * */
	public ListDb findById(Map<String, Object> paramMap);

	/**
	 * updateListDb:(修改黑白名单). <br/>
	 *
	 * @param sqlStr 建表sql
	 * @return 是否成功
	 */
	public boolean updateListDb(Map<String, Object> paramMap);

	/**
	 * findListDbById:(按编号查找名单类型\查询主键). <br/>
	 *
	 * @param paramMap 参数集合
	 * @return
	 * */
	public List<ListDb> findListDbByIds(Map<String, Object> paramMap);

	/**
	 * findFieldsDbByIds:(找出一批黑(白)名单库id所用到一级字段id，有重复、逗号分隔). <br/>
	 *
	 * @param paramMap 参数集合
	 * @return
	 * */
	public String findFieldsByListDbIds(Map<String, Object> paramMap);

	String selectFieldIdsByListDbId(@Param("userId") Long id);

	int customInsert(Map<String, Object> paramMap);

	List<LinkedHashMap<String, Object>> customSelect(Map<String, Object> paramMap);
}
