package com.fibo.ddp.common.model.strategyx.dataclean;

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
@TableName("t_data_clean_block")
public class DataCleanBlock implements Serializable {
    private static final long serialVersionUID = 176027842247885853L;
    /**
    * 主键id
    */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
    * 版本id
    */
    private Long dataCleanVersionId;
    /**
    * 条件区域名称
    */
    private String name;
    /**
     * 选择来源：original原数据，data_op原数据操作，handle_collection选择集合
     */
    private String opType;
    /**
     * 处理集合
     */
    private String handleCollection;
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
