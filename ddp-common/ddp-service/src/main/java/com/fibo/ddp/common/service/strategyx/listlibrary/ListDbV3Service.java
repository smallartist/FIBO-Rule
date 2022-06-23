package com.fibo.ddp.common.service.strategyx.listlibrary;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.strategyx.listlibrary.ListDb;
import com.fibo.ddp.common.model.strategyx.listlibrary.request.ListDbDataParam;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ListDbV3Service extends IService<ListDb> {

	boolean updateStatus(List<Long> ids, Integer status);

	 boolean addListDb(ListDb listDb);
	/**
	 * findById:(按编号查找名单库实体对象). <br/>
	 *
	 * @param paramMap 参数集合
	 * @return
	 * */
	public ListDb findById(Map<String, Object> paramMap);

	public boolean updateListDb(ListDb listDb);

	/**
	 * isExists:(检索黑(白)名单名称是否存在). <br/>
	 *
	 * @return
	 * */
	public boolean isExists(ListDb listDb);


	String upload(HttpServletRequest request, Long id) throws Exception;

	Map<String, Object> getListDbData(ListDbDataParam param);

	ListDb queryById(Long id);

	PageInfo queryByEntity(QueryListParam<ListDb> listParam);

	List<String> queryFieldEnsByListDbIds(Long id);

	boolean updateFolder(List<Long> ids, Long folderId);
}
