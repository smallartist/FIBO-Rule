package com.fibo.ddp.common.model.datax.datasource;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (MqSource)实体类
 *
 * @author jgp
 * @since 2021-12-20 13:31:51
 */
@Data
@TableName("t_field_mq_source")
public class MqSource implements Serializable {
    private static final long serialVersionUID = 775853739970128659L;
    /**
    * 自增主键
    */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
    * 消息队列连接名称
    */
    private String name;
    /**
    * 消息队列类型：kafka
    */
    private String type;
    /**
    * 连接地址
    */
    private String serverAddrs;
    /**
    * 数据库地址
    */
    private String topic;
    /**
    * 用户名
    */
    private String groupId;
    /**
    * offset设置:earliest，latest，none
    */
    private String autoOffsetReset;
    /**
    * 自动提交0:false 1:true
    */
    private Boolean enableAutoCommit;
    /**
    * 超时时间
    */
    private Long timeout;
    /**
    * 自动提交延时
    */
    private Long autoCommitInterval;
    /**
    * 消费线程数
    */
    private Integer concurrency;
    /**
    * key解析器默认：StringDeserializer
    */
    private String keyDeserializer;
    /**
    * value解码器默认：StringDeserializer
    */
    private String valueDeserializer;
    /**
     * 消息体
     */
    private String messageBody;
    /**
    * 状态 0：无效，1：有效，-1删除
    */
    private Integer status;
    /**
    * 创建人
    */
    private Long creator;
    /**
    * 修改人
    */
    private Long modifier;
    /**
    * 企业编号
    */
    private Long organId;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 修改时间
    */
    private Date updateTime;


}
