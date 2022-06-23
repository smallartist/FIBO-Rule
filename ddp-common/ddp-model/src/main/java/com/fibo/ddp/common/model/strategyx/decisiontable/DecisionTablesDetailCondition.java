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
 * (DecisionTablesDetailCondition)实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors
@TableName(value = "t_decision_tables_detail_condition")
public class DecisionTablesDetailCondition implements Serializable {
    private static final long serialVersionUID = -90235511839223545L;

    @TableId(type = IdType.AUTO)
    private Long id;//决策表详情条件id

    private Long detailId;//详情id

    private String operator;//操作符

    private Integer variableType;//变量类型：1常量，2变量

    private String fieldValue;//字段值

    private Date createTime;//创建时间

    private Date updateTime;//修改时间
}
