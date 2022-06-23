package com.fibo.ddp.common.model.authx.dictionary;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (Dictionary)实体类
 *
 * @author jgp
 * @since 2021-12-15 15:08:04
 */
@Data
@TableName("t_dictionary")
public class Dictionary implements Serializable {
    private static final long serialVersionUID = 958209037339913842L;
    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 字典key
     */
    private String dictKey;
    /**
     * 字典value
     */
    private String dictValue;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;


}
