package com.fibo.ddp.common.service.enginex.risk;

import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.risk.response.param.NodeTypeResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:EngineNodeServiceV3 <br/>
 * Description: 引擎节点接口. <br/>
 */
public interface EngineNodeService {
	
	/**
	 * getEngineNodeListByEngineVersionId：(根据版本id获取版本下的所有节点)
	 *
	 * @param  engineVersionId 引擎版本id
	 * @return 节点集合
	 * */
	List<EngineNode> getEngineNodeListByEngineVersionId(Long engineVersionId);
	
	/**
	 * findById：(根据节点Id查找节点)
	 *
	 * @param  engineNode 节点信息
	 * @return 
	 * */
    public EngineNode findById(Long id);
    
	/**
	 * updateNode：(修改节点)
	 *
	 * @param  engineNode 节点信息
	 * @return 
	 * */
    public boolean updateNode(EngineNode engineNode, Long targetId);

	/**
	 * 新增引擎节点
	 * @param eNode
	 * @return
	 */
	public boolean saveEngineNode(EngineNode eNode);

	// V2
	public EngineNode saveEngineNodeV2(EngineNode eNode);

	/**
	 * 修改节点参数nextOrder,Params
	 * @param nodeId
	 * @return
	 */
	public boolean updateNodeForNextOrderAndParams(List<EngineNode> eList);

	/**
	 * 获取版下节点的最大顺序号
	 * @param engineId
	 * @return
	 */
	int getMaxNodeOrder(Long versionId);

	/**
	 * updateNodeForMove：(节点移动时，修改节点坐标)
	 *
	 * @param  engineNode 节点信息
	 * @return
	 * */
    public boolean updateNodeForMove(EngineNode engineNode);

	public int renameNode(Map<String, Object> param);

    public void removeNode(Long engineNodeId, String preEngineNodeId);

    public void removeLink(Long engineNodeId, Long preEngineNodeId);

	public List<EngineNode> getEngineTypedNodeListByEngineVersionId(Long versionId, List<Integer> types);

	int updateParentIdByNodeId(Long nodeId, String parentId);

	/**
	 * 获取当前节点前面所有节点的输出信息
	 * @param nodeId
	 * @return
	 */
	List<NodeTypeResponse> getPreviousNodeOutput(Long nodeId);

	/**
	 * 更新节点快照信息
	 * @param paramMap
	 */
    void updateNodeSnapshot(HashMap<String, Object> paramMap);

    // runner
	/**
	 * 根据版本id获取版本下的所有节点
	 * @param versionId
	 * @return
	 */
	List<EngineNode> getEngineNodeListByVersionId(Long versionId);
}