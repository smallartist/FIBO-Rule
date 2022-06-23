package com.fibo.ddp.common.dao.enginex.risk;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EngineNodeMapper extends BaseMapper<EngineNode> {

    /**
     * getEngineNodeListByEngineVersionId：(根据版本id获取版本下的所有节点)
     *
     * @param engineVersionId 引擎版本id
     * @return 节点集合
     *
     */
    List<EngineNode> getEngineNodeListByEngineVersionId(@Param("engineVersionId") Long engineVersionId);

    int insertNodeAndReturnId(EngineNode engineNode);

    // V2
    int insertNodeAndReturnIdV2(EngineNode engineNode);

    int updateNodeForNextOrderAndParams(List<EngineNode> eList);

    int getMaxNodeOrder(@Param("versionId") Long versionId);

    int renameNode(Map<String, Object> param);

    int updateSnapshot(Map<String, Object> param);

    int updateNextNodes(EngineNode engineNode);

    int deleteNodesByNodeIds(@Param("commons") List<Long> commons);

    List<EngineNode> getEngineTypedNodeListByEngineVersionId(@Param("versionId") Long versionId, @Param("types") List<Integer> types);

    int updateParentIdByNodeId(Map<String, Object> map);

    int updateNodePosition(EngineNode engineNode);

    // runner
    /**
     * 根据版本id获取版本下的所有节点
     * @param engineVersionId
     * @return
     */
    List<EngineNode> getEngineNodeListByVersionId(@Param("engineVersionId") Long engineVersionId);
}