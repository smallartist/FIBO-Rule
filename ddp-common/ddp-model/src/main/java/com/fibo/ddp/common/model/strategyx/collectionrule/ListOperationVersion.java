package com.fibo.ddp.common.model.strategyx.collectionrule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_list_operation_version")
public class ListOperationVersion implements Serializable {
    private static final long serialVersionUID = -32462005941219848L;
    /**
    * 集合操作版本表id
    */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
    * 集合操作id
    */
    private Long listOpId;
    /**
    * 版本code
    */
    private String versionCode;
    /**
    * 版本描述
    */
    private String description;
    /**
    * 数据源数组或者map的en
    */
    private String inputFieldEn;
    /**
    * 数据源类型：map、list
    */
    private String inputFieldType;
    /**
    * 分组指标列表,逗号分割的en列表
    */
    private String groupFields;
    /**
    * 状态：-1删除 ，1启用，0停用
    */
    private Integer status;
    /**
    * 存放执行结果的变量
    */
    private String resultFieldEn;
    /**
    * 所属组织id
    */
    private Long organId;
    /**
    * 创建者id
    */
    private Long createUserId;
    /**
    * 修改者id
    */
    private Long updateUserId;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 修改时间
    */
    private Date updateTime;
    /**
    * 集合操作版本配置快照
    */
    private String snapshot;
    /**
     * 条件和对应输出组合
     */
    @TableField(exist = false)
    private List<ConditionAndOutPut> conditionAndOutPutList;
}
