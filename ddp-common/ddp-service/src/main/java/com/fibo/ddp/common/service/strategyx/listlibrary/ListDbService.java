package com.fibo.ddp.common.service.strategyx.listlibrary;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.listlibrary.ListDb;
import com.fibo.ddp.common.model.strategyx.listlibrary.request.ListDbDataParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ListDbService extends IService<ListDb> {

	/**
	 * findByUser:(查找用户黑/白名单库列表). <br/>
	 *
	 * @param paramMap 参数集合
	 * @return 
	 * */
	public List<ListDb> findByUser(Map<String, Object> paramMap);

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
	public boolean createListDb(ListDb listDb, Map<String, Object> paramMap);

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
	 * isExists:(检索黑(白)名单名称是否存在). <br/>
	 *
	 * @param paramMap 参数集合
	 * @return
	 * */
	public Integer isExists(Map<String, Object> paramMap);

	/**
	 * findFieldsDbByIds:(找出一批黑(白)名单库id所用到一级字段id，有重复、逗号分隔). <br/>
	 *
	 * @param paramMap 参数集合
	 * @return
	 * */
	public String findFieldsByListDbIds(Map<String, Object> paramMap);

	String upload(HttpServletRequest request, Long id) throws Exception;

	Map<String, Object> getListDbData(ListDbDataParam param);

	// runner
	ListDb queryById(Long id);

	List<Long> getNodeFieldIds(Long versionId);

	ListDb queryByVersionId(Long versionId);
}
