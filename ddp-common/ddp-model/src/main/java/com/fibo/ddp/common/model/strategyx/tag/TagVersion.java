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
 * (TagVersion)实体类
 *
 * @author jgp
 * @since 2021-12-31 13:25:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_tag_version")
public class TagVersion implements Serializable {

    private static final long serialVersionUID = -85947920439078454L;
       
    /**
    * 版本主键id
    */
    @TableId(type = IdType.AUTO)
    private Long id;
   
    /**
    * 标签id
    */
    private Long tagId;
   
    /**
    * 版本code
    */
    private String versionCode;
   
    /**
    * 版本名称
    */
    private String versionName;
   
    /**
    * 版本描述
    */
    private String description;
   
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
    private List<TagVersionDetail> detailList;
    /**
     * 快照
     */
    private String snapshot;
}
