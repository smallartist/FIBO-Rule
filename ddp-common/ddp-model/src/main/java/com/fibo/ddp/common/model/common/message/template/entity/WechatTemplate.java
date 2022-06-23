package com.fibo.ddp.common.model.common.message.template.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.io.Serializable;

/**
 * 微信服务号模板表(WechatTemplate)实体类
 *
 * @author andy.wang
 * @since 2022-01-07 18:11:16
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_wechat_template")
public class WechatTemplate implements Serializable {
    private static final long serialVersionUID = 958482371535782917L;
    
    @TableId(type = IdType.AUTO)
    /**
     * 主键
     */
    private Integer id;
    /**
     * 模板名称
     */
    private String templateName;
    /**
     * 模板编码
     */
    private String templateCode;
    /**
     * 模板内容
     */
    private String templateContent;
    /**
     * 状态 0：无效，1：有效
     */
    private Integer status;
    /**
     * 创建人
     */
    private Integer creator;
    /**
     * 修改人
     */
    private Integer modifier;
    /**
     * 组织编号
     */
    private Integer organId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}

