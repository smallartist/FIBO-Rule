package com.fibo.ddp.common.dao.common.message.template;

import com.fibo.ddp.common.model.common.message.template.entity.AppTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * APP推送模板表(AppTemplate)表数据库访问层
 *
 * @author andy.wang
 * @since 2022-01-07 18:11:16
 */
@Mapper
public interface AppTemplateMapper extends BaseMapper<AppTemplate>{

}
