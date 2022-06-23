package com.fibo.ddp.manager.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fibo.ddp.common.model.common.BaseParam;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.message.template.entity.WechatTemplate;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.common.message.template.WechatTemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 微信服务号模板表(WechatTemplate)表控制层
 *
 * @author andy.wang
 * @since 2022-01-07 18:11:16
 */
@RestController
@RequestMapping("message/wechatTemplate")
public class WechatTemplateController {
	
	@Autowired
    private WechatTemplateService wechatTemplateService;

    @PostMapping("queryListByPage")
    public ResponseEntityDto<PageInfo<WechatTemplate>> queryListByPage(@RequestBody BaseParam baseParam){
        Long organId = SessionManager.getLoginAccount().getOrganId();
        PageHelper.startPage(baseParam.getPageNo(), baseParam.getPageSize());
        LambdaQueryWrapper<WechatTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WechatTemplate::getOrganId, organId);
        queryWrapper.ne(WechatTemplate::getStatus, -1);
        List<WechatTemplate> list = wechatTemplateService.list(queryWrapper);
        PageInfo<WechatTemplate> pageInfo = new PageInfo<>(list);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }

	/**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById/{id}")
    public ResponseEntityDto<WechatTemplate> queryById(@PathVariable Integer id) {
        return ResponseEntityBuilder.buildNormalResponse(this.wechatTemplateService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param wechatTemplate 实体
     * @return 新增是否成功
     */
    @PostMapping("add")
    public ResponseEntityDto<Boolean> add(@RequestBody WechatTemplate wechatTemplate) {
        Long organId = SessionManager.getLoginAccount().getOrganId();
        wechatTemplate.setOrganId(organId.intValue());
        return ResponseEntityBuilder.buildNormalResponse(this.wechatTemplateService.save(wechatTemplate));
    }

    /**
     * 编辑数据
     *
     * @param wechatTemplate 实体
     * @return 编辑是否成功
     */
    @PostMapping("edit")
    public ResponseEntityDto<Boolean> edit(@RequestBody WechatTemplate wechatTemplate) {
        return ResponseEntityBuilder.buildNormalResponse(this.wechatTemplateService.updateById(wechatTemplate));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById/{id}")
    public ResponseEntityDto<Boolean> deleteById(@PathVariable Integer id) {
        return ResponseEntityBuilder.buildNormalResponse(this.wechatTemplateService.removeById(id));
    }
}
