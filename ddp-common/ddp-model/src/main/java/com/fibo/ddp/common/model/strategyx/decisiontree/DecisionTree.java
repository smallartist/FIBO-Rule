package com.fibo.ddp.common.model.strategyx.decisiontree;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fibo.ddp.common.model.strategyx.decisiontree.vo.DecisionTreeVersionVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@TableName("t_decision_tree")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecisionTree extends Model<DecisionTree> {
    //决策树主表id
    @TableId(type = IdType.AUTO)
    private Long id;
    //决策树code
    private String code;
    //决策树名称
    private String name;
    //决策树描述
    private String description;
    //文件夹id
    private Long folderId;
    //状态：0 停用 1 启用 -1删除（默认启用）
    private Integer status;
    //创建者id
    private Long createUserId;
    //修改者id
    private Long updateUserId;
    //组织id
    private Long organId;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;

    @TableField(exist = false)
    private String  creatorName;
    //版本列表
    @TableField(exist = false)
    private List<DecisionTreeVersionVo> versionList;
}
