package com.fibo.ddp.common.model.strategyx.scorecard;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 评分卡明细表的condition表(ScorecardDetailCondition)实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`t_scorecard_detail_condition`")
public class ScorecardDetailCondition implements Serializable {

    private static final long serialVersionUID = 821432514853102997L;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 内容id
     */
    private Integer detailId;
    /**
     * 关系运算符
     */
    private String operator;
    /**
     * 指标的值(实参)
     */
    private String fieldValue;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}
