package com.fibo.ddp.common.model.strategyx.decisiontable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (DecisionTablesResult)实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors
@TableName(value = "t_decision_tables_result")
public class DecisionTablesResult implements Serializable {
    private static final long serialVersionUID = -37103405087760345L;

    @TableId(type = IdType.AUTO)
    private Long id;//决策表结果集id

    private Long decisionTablesId;//决策表id

    private Long versionId;//决策表版本id

    private Integer rows;//行数

    private Integer columns;//列数

    private String resultValue;//结果集二维数组

    private Date createTime;//创建时间

    private Date updateTime;//修改时间
}
