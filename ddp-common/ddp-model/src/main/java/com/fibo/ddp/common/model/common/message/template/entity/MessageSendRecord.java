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
 * 消息发送记录表(MessageSendRecord)实体类
 *
 * @author andy.wang
 * @since 2022-01-07 18:13:24
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_message_send_record")
public class MessageSendRecord implements Serializable {
    private static final long serialVersionUID = -61995719748090236L;
    
    @TableId(type = IdType.AUTO)
    /**
     * 主键
     */
    private Integer id;
    /**
     * 触达方式 Sms、App、WebHook、WeChat
     */
    private String touchType;
    /**
     * 触达用户id
     */
    private Integer userId;
    /**
     * 模板名称
     */
    private String templateName;
    /**
     * 模板编码
     */
    private String templateCode;
    /**
     * 发送状态 0：未处理，1：已发送，2：发送成功，-1：发送失败
     */
    private Integer sendStatus;
    /**
     * 发送内容
     */
    private String sendContent;
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

