package com.fibo.ddp.common.dao.strategyx.knowledge;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.enginex.risk.request.KnowledgeTreeListParam;
import com.fibo.ddp.common.model.strategyx.knowledge.KnowledgeTree;

import java.util.List;
import java.util.Map;

/**
 * ClassName:KnowledgeTreeMapper </br>
 * Description:知识库目录mapper
 * @see
 *  
 * */
public interface KnowledgeTreeMapper extends BaseMapper<KnowledgeTree> {
	
	/**
	 * getTreeList：(根据父节点id和组织id,查询其下的所有子节点)
	 *
	 * @param paramMap 参数集合
	 * @return 父节点下的所有子节点
	 * */
	public List<KnowledgeTree> getTreeList(Map<String, Object> paramMap);
	public List<KnowledgeTree> getTreeListV2(Map<String, Object> paramMap);

	/**
	 * getTreeList：(根据父节点id和组织id,查询其下的所有子节点,若节点下规则，则过滤掉)
	 *
	 * @param paramMap 参数集合
	 * @return 父节点下的所有子节点
	 * */
	public List<KnowledgeTree> getTreeDataForEngine(Map<String, Object> paramMap);

	public List<KnowledgeTree> getTreeDataForEngineV2(Map<String, Object> paramMap);

	List<KnowledgeTree> selectFolderList(KnowledgeTreeListParam param);
}
