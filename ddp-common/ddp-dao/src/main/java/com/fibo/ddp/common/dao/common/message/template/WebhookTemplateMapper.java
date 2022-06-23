package com.fibo.ddp.common.dao.common.message.template;

import com.fibo.ddp.common.model.common.message.template.entity.WebhookTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * webhook模板表(WebhookTemplate)表数据库访问层
 *
 * @author andy.wang
 * @since 2022-01-07 18:12:03
 */
@Mapper
public interface WebhookTemplateMapper extends BaseMapper<WebhookTemplate>{

}
