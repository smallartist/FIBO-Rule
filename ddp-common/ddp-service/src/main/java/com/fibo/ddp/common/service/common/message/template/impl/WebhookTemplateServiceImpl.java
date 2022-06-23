package com.fibo.ddp.common.service.common.message.template.impl;

import com.fibo.ddp.common.model.common.message.template.entity.WebhookTemplate;
import com.fibo.ddp.common.dao.common.message.template.WebhookTemplateMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.service.common.message.template.WebhookTemplateService;
import org.springframework.stereotype.Service;

/**
 * webhook模板表(WebhookTemplate)表服务实现类
 *
 * @author andy.wang
 * @since 2022-01-07 18:12:03
 */
@Service
public class WebhookTemplateServiceImpl extends ServiceImpl<WebhookTemplateMapper, WebhookTemplate> implements WebhookTemplateService {
    
}
