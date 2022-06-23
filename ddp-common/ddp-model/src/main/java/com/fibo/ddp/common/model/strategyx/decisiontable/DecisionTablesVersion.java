package com.fibo.ddp.common.model.strategyx.decisiontable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (DecisionTablesVersion)表实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
@TableName("t_decision_tables_version")
public class DecisionTablesVersion extends Model<DecisionTablesVersion> {
    //版本主鍵id
    @TableId( type = IdType.AUTO)
    private Long id;
    //决策表id
    private Long decisionTablesId;
    //版本号
    private String versionCode;
    //版本描述
    private String description;
    //状态：-1删除 ，1启用，0停用
    private Integer status;
    //存放执行结果的变量
    private String resultFieldEn;
    //所属组织id
    private Long organId;
    //创建者id
    private Long createUserId;
    //修改者id
    private Long updateUserId;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //决策表版本配置快照
    private String snapshot;

}
