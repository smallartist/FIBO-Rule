package com.fibo.ddp.common.service.enginex.dataflow.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.enginex.risk.EngineNodeMapper;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.service.enginex.dataflow.EngineNodeServiceV3;
import com.fibo.ddp.common.utils.constant.enginex.NodeTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 存储版本模型的信息包括(使用的参数，节点的位置，节点的执行逻辑)(EngineNode)表服务实现类
 *
 * @author jgp
 * @since 2021-12-22 17:32:09
 */
@Service("engineNodeServiceV3")
public class EngineNodeServiceV3Impl extends ServiceImpl<EngineNodeMapper, EngineNode>  implements EngineNodeServiceV3 {

    @Override
    public List<EngineNode> queryNodeListByVersion(Long versionId) {

        return null;
    }

    @Override
    @Transactional
    public boolean saveStartNode(Long versionId) {
        EngineNode node = new EngineNode();
        node.setNodeX(200D);
        node.setNodeY(200D);
        node.setNodeName("开始");
        node.setNodeOrder(1);
        node.setNodeType(NodeTypeEnum.START.getValue());
        node.setNodeCode("ND_START");
        node.setParams("{\"arr_linkId\":\"\",\"dataId\":\"-1\",\"url\":\"/Riskmanage/resource/images/decision/start.png\",\"type\":\"1\"}");
        node.setVersionId(versionId);
        // 直接插入节点
        return this.save(node);
    }

    @Override
     public boolean updateStatus(List<Long> ids,Integer statu) {
        return true;
    }
}
