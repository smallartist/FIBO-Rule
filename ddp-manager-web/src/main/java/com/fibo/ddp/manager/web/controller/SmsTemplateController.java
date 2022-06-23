package com.fibo.ddp.manager.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fibo.ddp.common.model.common.BaseParam;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.message.template.entity.SmsTemplate;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.common.message.template.SmsTemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短信模板表(SmsTemplate)表控制层
 *
 * @author andy.wang
 * @since 2022-01-07 18:11:16
 */
@RestController
@RequestMapping("message/smsTemplate")
public class SmsTemplateController {
	
	@Autowired
    private SmsTemplateService smsTemplateService;

    @PostMapping("queryListByPage")
    public ResponseEntityDto<PageInfo<SmsTemplate>> queryListByPage(@RequestBody BaseParam baseParam){
        Long organId = SessionManager.getLoginAccount().getOrganId();
        PageHelper.startPage(baseParam.getPageNo(), baseParam.getPageSize());
        LambdaQueryWrapper<SmsTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SmsTemplate::getOrganId, organId);
        queryWrapper.ne(SmsTemplate::getStatus, -1);
        List<SmsTemplate> list = smsTemplateService.list(queryWrapper);
        PageInfo<SmsTemplate> pageInfo = new PageInfo<>(list);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }
	
	/**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById/{id}")
    public ResponseEntityDto<SmsTemplate> queryById(@PathVariable Integer id) {
        return ResponseEntityBuilder.buildNormalResponse(this.smsTemplateService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param smsTemplate 实体
     * @return 新增是否成功
     */
    @PostMapping("add")
    public ResponseEntityDto<Boolean> add(@RequestBody SmsTemplate smsTemplate) {
        Long organId = SessionManager.getLoginAccount().getOrganId();
        smsTemplate.setOrganId(organId.intValue());
        return ResponseEntityBuilder.buildNormalResponse(this.smsTemplateService.save(smsTemplate));
    }

    /**
     * 编辑数据
     *
     * @param smsTemplate 实体
     * @return 编辑是否成功
     */
    @PostMapping("edit")
    public ResponseEntityDto<Boolean> edit(@RequestBody SmsTemplate smsTemplate) {
        return ResponseEntityBuilder.buildNormalResponse(this.smsTemplateService.updateById(smsTemplate));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById/{id}")
    public ResponseEntityDto<Boolean> deleteById(@PathVariable Integer id) {
        return ResponseEntityBuilder.buildNormalResponse(this.smsTemplateService.removeById(id));
    }
}
