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
 * APP推送模板表(AppTemplate)实体类
 *
 * @author andy.wang
 * @since 2022-01-07 18:11:16
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_app_template")
public class AppTemplate implements Serializable {
    private static final long serialVersionUID = -45372445008267253L;
    
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
     * 推送标题
     */
    private String pushSubject;
    /**
     * 推送内容
     */
    private String pushContent;
    /**
     * 点击推送后动作 1：唤醒应用，2：打开指定链接，3：自定义跳转
     */
    private Integer clickAction;
    /**
     * 点击推送后跳转地址
     */
    private String jumpUrl;
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

