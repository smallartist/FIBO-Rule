package com.fibo.ddp.common.service.common.message.template.impl;

import com.fibo.ddp.common.model.common.message.template.entity.SmsTemplate;
import com.fibo.ddp.common.dao.common.message.template.SmsTemplateMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.service.common.message.template.SmsTemplateService;
import org.springframework.stereotype.Service;

/**
 * 短信模板表(SmsTemplate)表服务实现类
 *
 * @author andy.wang
 * @since 2022-01-07 18:11:16
 */
@Service
public class SmsTemplateServiceImpl extends ServiceImpl<SmsTemplateMapper, SmsTemplate> implements SmsTemplateService {
    
}
