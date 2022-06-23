package com.fibo.ddp.common.model.strategyx.scorecard;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`t_scorecard_dimension`")
public class ScorecardDimension implements Serializable {

    private static final long serialVersionUID = 8131487634836541557L;

    @TableId(type = IdType.AUTO)
    private Integer id; // int(11)
    private Integer cardId; // int(11)
    private Long versionId;//版本id
    private String dimensionName; // varchar(30)
    private Double weight; // decimal(7,2)
    private String executeType; // varchar(30)
    private Date createTime; // datetime
    private Date updateTime; // datetime
}
