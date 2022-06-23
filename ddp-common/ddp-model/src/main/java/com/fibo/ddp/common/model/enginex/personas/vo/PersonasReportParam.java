package com.fibo.ddp.common.model.enginex.personas.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonasReportParam {

    /**
     * 引擎id
     */
    private Long engineId;

    /**
     * 引擎版本id
     */
    private Long engineVersionId;

    /**
     * 批次号
     */
    private Long batchNo;
    /**
     * 查询起点时间
     */
    private Date queryStartTime;
    /**
     * 查询终点时间
     */
    private Date queryEndTime;
}
