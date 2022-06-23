package com.fibo.ddp.common.model.monitor.decisionflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 决策流监控
 * </p>
 */
@Data
@EqualsAndHashCode()
public class TMonitorEngine {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String businessId;

    private String monitorParentId;

    private Long engineId;

    private String engineName;

    private Long organId;

    private Long engineVersionId;

    private String process;

    private String snapshot;

    private String input;

    private String output;
    private Date createTime;

}
