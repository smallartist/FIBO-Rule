package com.fibo.ddp.common.model.enginex.risk;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_engine_node")
public class EngineNode implements Serializable{
	
	private static final long serialVersionUID = -1867357850853531748L;
    /**
     * 节点信息编号
     */
    @TableId(type = IdType.AUTO)
    private Long nodeId;

    /**
     * 版本编号
     */
    private Long versionId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点代号
     */
    private String nodeCode;

    /**
     * 节点顺序
     */
    private Integer nodeOrder;

    /**
     * 节点类型
     */
    private Integer nodeType;

    /**
     * 节点信息
     */
    private String nodeJson;

    /**
     * 节点横坐标
     */
    private Double nodeX;

    /**
     * 节点纵坐标
     */
    private Double nodeY;

    /**
     * 节点脚本
     */
    private String nodeScript;

    /**
     * 下个节点(可能是多个)
     */
    private String nextNodes;

    /**
     * 节点用到的参数列表
     */
    private String params;

    /**
     * 上一个节点的id，多个节点逗号分隔
     */
    private String parentId;

    /**
     * 节点配置快照
     */
    private String snapshot;
}