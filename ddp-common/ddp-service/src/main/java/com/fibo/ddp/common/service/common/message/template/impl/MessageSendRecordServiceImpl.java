package com.fibo.ddp.common.service.common.message.template.impl;

import com.fibo.ddp.common.model.common.message.template.entity.MessageSendRecord;
import com.fibo.ddp.common.dao.common.message.template.MessageSendRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.service.common.message.template.MessageSendRecordService;
import org.springframework.stereotype.Service;

/**
 * 消息发送记录表(MessageSendRecord)表服务实现类
 *
 * @author andy.wang
 * @since 2022-01-07 18:13:24
 */
@Service
public class MessageSendRecordServiceImpl extends ServiceImpl<MessageSendRecordMapper, MessageSendRecord> implements MessageSendRecordService {
    
}
