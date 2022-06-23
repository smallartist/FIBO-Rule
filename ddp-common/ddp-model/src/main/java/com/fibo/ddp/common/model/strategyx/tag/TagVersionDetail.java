package com.fibo.ddp.common.model.strategyx.tag;

import java.util.Date;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fibo.ddp.common.model.strategyx.baserule.BaseRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (TagVersionDetail)实体类
 *
 * @author jgp
 * @since 2021-12-31 13:25:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_tag_version_detail")
public class TagVersionDetail implements Serializable {

    private static final long serialVersionUID = -78415296218468622L;
       
    /**
    * 主键id
    */
    @TableId(type = IdType.AUTO)
    private Long id;
   
    /**
    * 版本主表id
    */
    private Long tagVersionId;
   
    /**
    * 标签值
    */
    private String tagValue;
   
    /**
    * 标签值描述
    */
    private String tagValueDesc;
   
    /**
    * 条件块id
    */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tagRuleId;
   
    /**
    * 组织id
    */
    private Long organId;
   
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

    @TableField(exist = false)
    private BaseRule rule;
}
