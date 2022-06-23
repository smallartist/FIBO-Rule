package com.fibo.ddp.common.model.analyse;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_analyse_engine_summary
 */
@Data
@TableName("t_analyse_engine_summary")
public class AnalyseEngineSummary implements Serializable {
    /**
     * 引擎概况主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 引擎版本id
     */
    private Long engineVersionId;

    /**
     * 引擎名称
     */
    private String engineName;

    /**
     * 统计维度（1.调用次数 engine_call,2.决策结果 decision_result,3规则命中 rule_hit 4.评分卡 scorecard 5.决策表 decision_tables6.名单库 list_db）
     */
    private String statisticsDimension;

    /**
     * 统计数量（截至创建日期的当天的总数）
     */
    private Long statisticsCount;

    /**
     * 组织id
     */
    private Long organId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}