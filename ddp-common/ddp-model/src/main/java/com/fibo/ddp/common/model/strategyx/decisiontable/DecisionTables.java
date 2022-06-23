package com.fibo.ddp.common.model.strategyx.decisiontable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesVersionVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (DecisionTables)实体类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors
@TableName(value = "t_decision_tables")
public class DecisionTables implements Serializable {
    private static final long serialVersionUID = -98465498462309886L;

    @TableId(type = IdType.AUTO)
    private Long id;//决策表id

    private String name;//决策表名称

    private String code;//决策表代码(英文)

    private String description;//决策表描述

    private String version;//决策表版本

    private Integer status;//决策表状态：0 停用 1 启用 -1删除（默认启用）

    private Long creator;//决策表创建者

    @TableField(exist = false)
    private String creatorName;//创建者名称

    private Long modifier;//决策表修改者

    private Long organId;//所属组织id

    private Date createTime;//创建时间

    private Date updateTime;//修改时间

    private Long parentId;//文件id

    private String resultFieldEn;//存放执行结果的变量

    @TableField(exist = false)
    private List<Long> parentIds;//文件id集合
    @TableField(exist = false)
    private List<DecisionTablesVersionVo> decisionTablesVersionList;
    @TableField(exist = false)
    private DecisionTablesVersionVo executeVersion;
}
