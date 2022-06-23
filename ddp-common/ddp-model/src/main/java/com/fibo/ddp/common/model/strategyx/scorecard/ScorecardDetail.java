package com.fibo.ddp.common.model.strategyx.scorecard;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 评分卡明细表(ScorecardDetail)实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`t_scorecard_detail`")
public class ScorecardDetail implements Serializable {

    private static final long serialVersionUID = -2740621861057988333L;

    @TableId(type = IdType.AUTO)
    private Integer id              ;  // int(11)
    private Integer dimensionId    ;  // int(11)
    private Integer fieldId        ;  // int(11)
    private Integer parentId       ;  // int(11)
    private Integer type            ;  // int(11)
    private Double score           ;  // decimal(10,0)
    private Double coefficient     ;  // decimal(10,0)
    private String custom           ;  //自定义文本
    private Integer calculateType  ;  // int(11)
    private String logical         ;  // varchar(50)
    private Date createTime     ;  // datetime
    private Date updateTime     ;  // datetime

    @TableField(exist = false)
    private String fieldEn;
}
