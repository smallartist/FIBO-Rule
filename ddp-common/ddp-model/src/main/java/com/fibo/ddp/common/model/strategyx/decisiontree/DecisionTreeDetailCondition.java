package com.fibo.ddp.common.model.strategyx.decisiontree;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@SuppressWarnings("serial")
@TableName("t_decision_tree_detail_condition")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecisionTreeDetailCondition extends Model<DecisionTreeDetailCondition> {
    //决策树详情条件id
    @TableId(type = IdType.AUTO)
    private Long id;
    //详情id
    private Long detailId;
    //操作符
    private String operator;
    //变量类型：1常量，2变量
    private Integer variableType;
    //变量值（常量为值，变量为字段en）
    private String fieldValue;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;

}
