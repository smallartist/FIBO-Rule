package com.fibo.ddp.common.service.common.message.template.impl;

import com.fibo.ddp.common.model.common.message.template.entity.AppTemplate;
import com.fibo.ddp.common.dao.common.message.template.AppTemplateMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.service.common.message.template.AppTemplateService;
import org.springframework.stereotype.Service;

/**
 * APP推送模板表(AppTemplate)表服务实现类
 *
 * @author andy.wang
 * @since 2022-01-07 18:11:16
 */
@Service
public class AppTemplateServiceImpl extends ServiceImpl<AppTemplateMapper, AppTemplate> implements AppTemplateService {
    
}
