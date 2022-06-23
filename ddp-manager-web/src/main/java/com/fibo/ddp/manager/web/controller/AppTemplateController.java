package com.fibo.ddp.manager.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.message.template.entity.AppTemplate;
import com.fibo.ddp.common.model.common.message.template.vo.AppTemplateReqVo;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.common.message.template.AppTemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * APP推送模板表(AppTemplate)表控制层
 *
 * @author andy.wang
 * @since 2022-01-07 18:11:16
 */
@RestController
@RequestMapping("message/appTemplate")
public class AppTemplateController {
	
	@Autowired
    private AppTemplateService appTemplateService;

    @PostMapping("queryListByPage")
    public ResponseEntityDto<PageInfo<AppTemplate>> queryListByPage(@RequestBody AppTemplateReqVo appTemplateReqVo){
        Long organId = SessionManager.getLoginAccount().getOrganId();
        PageHelper.startPage(appTemplateReqVo.getPageNo(), appTemplateReqVo.getPageSize());
        LambdaQueryWrapper<AppTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AppTemplate::getOrganId, organId);
        queryWrapper.ne(AppTemplate::getStatus, -1);
        List<AppTemplate> list = appTemplateService.list(queryWrapper);
        PageInfo<AppTemplate> pageInfo = new PageInfo<>(list);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }

	/**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById/{id}")
    public ResponseEntityDto<AppTemplate> queryById(@PathVariable Integer id) {
        return ResponseEntityBuilder.buildNormalResponse(this.appTemplateService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param appTemplate 实体
     * @return 新增是否成功
     */
    @PostMapping("add")
    public ResponseEntityDto<Boolean> add(@RequestBody AppTemplate appTemplate) {
        Long organId = SessionManager.getLoginAccount().getOrganId();
        appTemplate.setOrganId(organId.intValue());
        return ResponseEntityBuilder.buildNormalResponse(this.appTemplateService.save(appTemplate));
    }

    /**
     * 编辑数据
     *
     * @param appTemplate 实体
     * @return 编辑是否成功
     */
    @PostMapping("edit")
    public ResponseEntityDto<Boolean> edit(@RequestBody AppTemplate appTemplate) {
        return ResponseEntityBuilder.buildNormalResponse(this.appTemplateService.updateById(appTemplate));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById/{id}")
    public ResponseEntityDto<Boolean> deleteById(@PathVariable Integer id) {
        return ResponseEntityBuilder.buildNormalResponse(this.appTemplateService.removeById(id));
    }
}
