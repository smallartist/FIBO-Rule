package com.fibo.ddp.common.model.enginex.personas;

import java.util.Date;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (PersonasEngineResultDetail)实体类
 *
 * @author jgp
 * @since 2022-01-06 14:24:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_personas_engine_result_detail")
public class PersonasEngineResultDetail implements Serializable {

    private static final long serialVersionUID = 921135339454407581L;
       
    /**
    * 自增主键
    */
    @TableId(type = IdType.AUTO)
    private Long id;
   
    /**
    * 引擎id


    */
    private Long engineId;
   
    /**
    * 引擎版本id

    */
    private Long engineVersionId;
   
    /**
    * 批次号
    */
    private Long batchNo;
   
    /**
    * 用户id
    */
    private Long userId;
   
    /**
    * 标签id
    */
    private Long tagId;
    /**
     * 标签版本id
     *
     */
    private Long tagVersionId;

    /**
    * 标签值
    */
    private String tagValue;
   
    /**
    * 创建时间
    */
    private Date createTime;
    @TableField(exist = false)
    private Date queryStartTime;
    @TableField(exist = false)
    private Date queryEndTime;
}
