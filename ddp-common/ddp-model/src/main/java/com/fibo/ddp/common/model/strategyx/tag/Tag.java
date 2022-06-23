package com.fibo.ddp.common.model.strategyx.tag;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Tag)实体类
 *
 * @author jgp
 * @since 2021-12-31 13:25:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_tag")
public class Tag implements Serializable {

    private static final long serialVersionUID = -25028602999121820L;
       
    /**
    * 自增主键
    */
    @TableId(type = IdType.AUTO)
    private Long id;
   
    /**
    * 标签代码
    */
    private String tagCode;
   
    /**
    * 标签名称
    */
    private String tagName;
   
    /**
    * 标签描述
    */
    private String tagDesc;
    /**
    * 标签描述
    */
    private Long folderId;
   
    /**
    * 状态：1正常，-1删除 0停用
    */
    private Integer status;
   
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
    private String createUserName;
    @TableField(exist = false)
    private List<TagVersion> versionList;
}
