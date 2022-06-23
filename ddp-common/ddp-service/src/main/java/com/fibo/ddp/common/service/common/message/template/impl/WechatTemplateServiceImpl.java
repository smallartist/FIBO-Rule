package com.fibo.ddp.common.service.common.message.template.impl;

import com.fibo.ddp.common.model.common.message.template.entity.WechatTemplate;
import com.fibo.ddp.common.dao.common.message.template.WechatTemplateMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.service.common.message.template.WechatTemplateService;
import org.springframework.stereotype.Service;

/**
 * 微信服务号模板表(WechatTemplate)表服务实现类
 *
 * @author andy.wang
 * @since 2022-01-07 18:11:16
 */
@Service
public class WechatTemplateServiceImpl extends ServiceImpl<WechatTemplateMapper, WechatTemplate> implements WechatTemplateService {
    
}
