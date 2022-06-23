package com.fibo.ddp.common.model.strategyx.collectionrule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_list_operation_block")
public class ListOperationBlock implements Serializable {
    private static final long serialVersionUID = 176027842247885853L;
    /**
    * 主键id
    */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
    * 版本id
    */
    private Long listOpVersionId;
    /**
    * 条件区域名称
    */
    private String name;
    /**
     * 分组指标列表,逗号分割的en列表
     */
    private String groupFields;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 修改时间
    */
    private Date updateTime;


}
