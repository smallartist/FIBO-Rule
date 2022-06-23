package com.fibo.ddp.manager.web.controller;

import com.fibo.ddp.common.service.common.message.template.MessageSendRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息发送记录表(MessageSendRecord)表控制层
 *
 * @author andy.wang
 * @since 2022-01-07 18:13:24
 */
@RestController
@RequestMapping("message/messageSendRecord")
public class MessageSendRecordController {
	
	@Autowired
    private MessageSendRecordService messageSendRecordService;
	
}
