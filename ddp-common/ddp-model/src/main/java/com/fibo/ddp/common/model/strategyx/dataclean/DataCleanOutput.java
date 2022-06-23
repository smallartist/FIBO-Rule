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
@TableName("t_data_clean_output")
public class DataCleanOutput implements Serializable {
    private static final long serialVersionUID = 360159112010885143L;
    /**
    * 自增主键
    */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
    * 列表操作版本id
    */
    private Long dataCleanVersionId;
    /**
    * 列表操作条件根节点id
    */
    private Long dataCleanConditionId;
    /**
    * 列表操作块id
    */
    private Long dataCleanBlockId;
    /**
    * 输出类型：1 命中输出，2未命中输出3默认输出
    */
    private Integer outputType;
    /**
    * 输出的key
    */
    private String outputKey;
    /**
    * 选择来源：original原数据，data_op原数据操作，handle_collection选择集合
    */
    private String opType;
//    /**
//     * 输出操作对象的key
//     */
//    private String outputOpKey;
    /**
    * 输出的值
    */
    private String outputValue;
    /**
    * 输出类型： 1.常量 2.变量 3.自定义
    */
    private Integer variableType;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 修改时间
    */
    private Date updateTime;


}
