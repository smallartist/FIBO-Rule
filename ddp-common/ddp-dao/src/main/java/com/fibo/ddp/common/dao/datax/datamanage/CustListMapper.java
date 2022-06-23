package com.fibo.ddp.common.dao.datax.datamanage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.datax.datamanage.CustList;

import java.util.List;
import java.util.Map;

public interface CustListMapper extends BaseMapper<CustList> {

	/**
	 * findCustList:(查找黑/白名单库的客户名单列表). <br/>
	 * @param paramMap 参数集合
	 * @return 
	 * */
	public List<CustList> findCustList(Map<String, Object> paramMap);

	/**
	 * checkDupCustList:(根据输入的客户信息参数在黑(白)名单库里查找是否存在). <br/>
	 * @param paramMap 参数集合
	 * @return 存在的结果
	 * */
	public int checkDupCustList(Map<String, Object> paramMap);

	/**
	 * importOneCustList:(导入一条黑(白)名单客户信息数据). <br/>
	 * @param paramMap 参数集合
	 * @return 存在的结果
	 * */
	public int importOneCustList(CustList custList);

	/**
	 * findValidColumnData:(导出黑/白名单库的客户名单列表). <br/>
	 * @param paramMap 参数集合
	 * @return
	 * */
	public List<CustList> findValidColumnData(Map<String, Object> paramMap);

	/**
	 * searchTop4Column:(查找黑/白名单库的客户名单前4列数据列表). <br/>
	 * @param paramMap 参数集合
	 * @return
	 * */
	public List<CustList> searchTop4Column(Map<String, Object> paramMap);

	/**
	 * findById:(根据黑/白名单库的客户id查找客户信息). <br/>
	 * @param paramMap 参数集合
	 * @return
	 * */
	public CustList findById(Map<String, Object> paramMap);

	/**
	 * createCustList:(新增黑/白名单的客户信息). <br/>
	 * @param paramMap 参数集合
	 * @return
	 * */
	public boolean createCustList(Map<String, Object> paramMap);


	/**
	 * updateCustList:(修改黑/白名单的客户信息). <br/>
	 * @param paramMap 参数集合
	 * @return
	 * */
	public boolean updateCustList(Map<String, Object> paramMap);

	/**
	 * deleteCustList:(删除黑/白名单的客户信息). <br/>
	 * @param paramMap 参数集合
	 * @return
	 * */
	public boolean deleteCustList(Map<String, Object> paramMap);

	/**
	 * findByQueryKey:(按照黑/白名单的查询key检索命中的客户信息). <br/>
	 * @param paramMap 参数集合
	 * @return
	 * */
	public Integer findByQueryKey(Map<String, Object> paramMap);

	/**
	 * revFindByQueryKey:(黑/白名单模糊搜索时补充按照查询key反向检索命中的客户信息). <br/>
	 * @param paramMap 参数集合
	 * @return
	 * */
	public Integer revFindByQueryKey(Map<String, Object> paramMap);
	
}
