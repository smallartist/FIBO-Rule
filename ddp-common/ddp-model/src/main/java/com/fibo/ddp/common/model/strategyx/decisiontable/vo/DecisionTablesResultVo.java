package com.fibo.ddp.common.model.strategyx.decisiontable.vo;

import com.alibaba.fastjson.JSONArray;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTablesResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors
public class DecisionTablesResultVo extends DecisionTablesResult implements Serializable {
    private static final long serialVersionUID = -37103405087760345L;

//    private Long userId;//决策表结果集id
//
//    private Long decisionTablesId;//决策表id
//
//    private Integer rows;//行数
//
//    private Integer columns;//列数



//    private String resultValue;//结果集
//
//    private Date createTime;//创建时间
//
//    private Date updateTime;//修改时间

    private JSONArray resultList;//结果集二维数组
}