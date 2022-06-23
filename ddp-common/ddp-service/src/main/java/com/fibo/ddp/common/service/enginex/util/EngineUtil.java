package com.fibo.ddp.common.service.enginex.util;

import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.utils.constant.CommonConst;
import com.fibo.ddp.common.utils.util.CollectionUtil;
import com.fibo.ddp.common.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class EngineUtil {

	public static Map<Long, EngineNode> convertNodeList2Map(List<EngineNode> nodes){
		Map<Long,EngineNode> nodeMap = new HashMap<>();
		if(CollectionUtil.isNotNullOrEmpty(nodes)){
			for (EngineNode engineNode : nodes) {
				nodeMap.put(engineNode.getNodeId(), engineNode);
			}
		}
		return nodeMap;
	}
	
	
	/**
	 * 将nodeList转化为Map，以nodeCode做Key
	 * @param nodes
	 * @return
	 */
	public static Map<String,EngineNode> convertNodeList2MapNodeCodeKey(List<EngineNode> nodes){
		Map<String,EngineNode> nodeMap = new HashMap<String, EngineNode>();
		if(CollectionUtil.isNotNullOrEmpty(nodes)){
			for (EngineNode engineNode : nodes) {
				nodeMap.put(engineNode.getNodeCode(), engineNode);
			}
		}
		return nodeMap;
	}
	
	public static List<EngineNode> getUpdateParentIdNodes(EngineNode engineNode,List<EngineNode> nodeList){
		Map<String,EngineNode> nodeMap = EngineUtil.convertNodeList2MapNodeCodeKey(nodeList);
		//获取当前节点的nextNodes
		String nextNodes = engineNode.getNextNodes();
		List<EngineNode> resultNodes = new ArrayList<EngineNode>();
		if(StringUtil.isValidStr(nextNodes)){
			String[] nextNodeCodes = nextNodes.split(CommonConst.SYMBOL_COMMA);
			EngineNode nextNode = null;
			for (String nextNodeCode : nextNodeCodes) {
				nextNode = nodeMap.get(nextNodeCode);

				String parentId = nextNode.getParentId();
				if(parentId.contains(CommonConst.SYMBOL_COMMA)){
					List<String> parentIdList = Arrays.asList(parentId.split(CommonConst.SYMBOL_COMMA));
					parentIdList = parentIdList.stream().filter(item -> !item.equals(engineNode.getNodeId().toString())).collect(Collectors.toList());
					parentId = StringUtils.join(parentIdList, CommonConst.SYMBOL_COMMA);
				} else {
					parentId = null;
				}
				nextNode.setParentId(parentId);
				resultNodes.add(nextNode);
			}
		}
		return resultNodes;
	}
}
