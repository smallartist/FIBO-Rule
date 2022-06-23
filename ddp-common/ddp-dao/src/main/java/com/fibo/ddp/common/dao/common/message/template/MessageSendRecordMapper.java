package com.fibo.ddp.common.dao.common.message.template;

import com.fibo.ddp.common.model.common.message.template.entity.MessageSendRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息发送记录表(MessageSendRecord)表数据库访问层
 *
 * @author andy.wang
 * @since 2022-01-07 18:13:24
 */
@Mapper
public interface MessageSendRecordMapper extends BaseMapper<MessageSendRecord>{

}
