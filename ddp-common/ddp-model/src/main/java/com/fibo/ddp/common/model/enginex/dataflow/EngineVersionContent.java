package com.fibo.ddp.common.model.enginex.dataflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (EngineVersionContent)实体类
 *
 * @author jgp
 * @since 2021-12-23 10:21:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_engine_version_content")
public class EngineVersionContent implements Serializable {

    private static final long serialVersionUID = 501475813219875839L;
       
    /**
    * 引擎版本id
    */
    @TableId(type = IdType.INPUT)
    private Long engineVersionId;
    /**
     * 引擎类型
     */
    private String engineType;
    /**
    * 引擎内容
    */
    private String engineContent;
    /**
     * 引擎执行内容
     */
    private String engineScript;

    /**
    * 创建人id
    */
    private Long createUserId;
   
    /**
    * 修改人id
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
}
