package com.fibo.ddp.manager.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fibo.ddp.common.model.common.BaseParam;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.message.template.entity.WebhookTemplate;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.common.message.template.WebhookTemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * webhook模板表(WebhookTemplate)表控制层
 *
 * @author andy.wang
 * @since 2022-01-07 18:12:03
 */
@RestController
@RequestMapping("message/webhookTemplate")
public class WebhookTemplateController {
	
	@Autowired
    private WebhookTemplateService webhookTemplateService;

    @PostMapping("queryListByPage")
    public ResponseEntityDto<PageInfo<WebhookTemplate>> queryListByPage(@RequestBody BaseParam baseParam){
        Long organId = SessionManager.getLoginAccount().getOrganId();
        PageHelper.startPage(baseParam.getPageNo(), baseParam.getPageSize());
        LambdaQueryWrapper<WebhookTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WebhookTemplate::getOrganId, organId);
        queryWrapper.ne(WebhookTemplate::getStatus, -1);
        List<WebhookTemplate> list = webhookTemplateService.list(queryWrapper);
        PageInfo<WebhookTemplate> pageInfo = new PageInfo<>(list);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }

	/**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById/{id}")
    public ResponseEntityDto<WebhookTemplate> queryById(@PathVariable Integer id) {
        return ResponseEntityBuilder.buildNormalResponse(this.webhookTemplateService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param webhookTemplate 实体
     * @return 新增是否成功
     */
    @PostMapping("add")
    public ResponseEntityDto<Boolean> add(@RequestBody WebhookTemplate webhookTemplate) {
        Long organId = SessionManager.getLoginAccount().getOrganId();
        webhookTemplate.setOrganId(organId.intValue());
        return ResponseEntityBuilder.buildNormalResponse(this.webhookTemplateService.save(webhookTemplate));
    }

    /**
     * 编辑数据
     *
     * @param webhookTemplate 实体
     * @return 编辑是否成功
     */
    @PostMapping("edit")
    public ResponseEntityDto<Boolean> edit(@RequestBody WebhookTemplate webhookTemplate) {
        return ResponseEntityBuilder.buildNormalResponse(this.webhookTemplateService.updateById(webhookTemplate));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById/{id}")
    public ResponseEntityDto<Boolean> deleteById(@PathVariable Integer id) {
        return ResponseEntityBuilder.buildNormalResponse(this.webhookTemplateService.removeById(id));
    }
}
