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
 * (PersonasEngineResult)实体类
 *
 * @author jgp
 * @since 2022-01-06 14:23:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_personas_engine_result")
public class PersonasEngineResult implements Serializable {

    private static final long serialVersionUID = -31398229059101813L;
       
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
    * 引擎名称
    */
    private String engineName;
   
    /**
    * 批次号
    */
    private Long batchNo;
   
    /**
    * 批次数据量
    */
    private Long batchNum;
   
    /**
    * 创建时间
    */
    private Date createTime;
    @TableField(exist = false)
    private Date queryStartTime;
    @TableField(exist = false)
    private Date queryEndTime;

}
