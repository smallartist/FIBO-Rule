package com.fibo.ddp.common.model.monitor.decisionflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 决策流节点层面监控
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TMonitorNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String businessId;

    private String monitorParentId;

    private Long engineId;

    private Long organId;

    private Long engineVersionId;

    private Long nodeId;

    private String nodeName;

    private String nodeType;

    private String snapshot;

    private String input;

    private String output;

    private LocalDateTime createTime;


}
