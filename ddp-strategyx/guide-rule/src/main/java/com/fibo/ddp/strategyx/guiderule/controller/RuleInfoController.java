package com.fibo.ddp.strategyx.guiderule.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.UpdateFolderParam;
import com.fibo.ddp.common.model.common.requestParam.UpdateStatusParam;
import com.fibo.ddp.common.model.strategyx.guiderule.request.RuleListParamV2;
import com.fibo.ddp.common.model.strategyx.guiderule.vo.RuleVo;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.service.strategyx.guiderule.RuleService;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller("ruleControllerV3")
@RequestMapping("v3/rule")
public class RuleInfoController {

    @Autowired
    private RuleService ruleService;

    /**
     * @api {POST} /v3/rule/getRuleInfo/{userId} 3.19. V3获取rule信息
     * @apiGroup knowledge
     * @apiVersion 2.0.0
     * @apiParam {Long} userId :  规则id
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * {}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {"status":"1","error":"00000000","msg":null,"data":{"userId":666,"name":"2021_4_6测试接口","versionCode":"test2021_4_6第二次输入","description":"接口测试","priority":0,"parentId":0,"author":135,"userId":135,"organId":46,"engineId":null,"status":1,"type":1,"isNon":null,"content":"test","created":"2021-04-06T08:38:17.000+00:00","updated":"2021-04-07T05:12:52.000+00:00","ruleType":0,"ruleAudit":2,"score":0,"lastLogical":"","ruleConditionVo":{"userId":14,"logical":"&&","fieldId":null,"operator":null,"fieldValue":null,"ruleId":666,"parentId":0,"conditionType":1,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[{"userId":15,"logical":null,"fieldId":null,"operator":">","fieldValue":"1000","ruleId":666,"parentId":14,"conditionType":1,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null},{"userId":16,"logical":null,"fieldId":null,"operator":"<","fieldValue":"10000","ruleId":666,"parentId":14,"conditionType":1,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null}],"tempParentId":null},"ruleContentInfoList":[{"userId":16,"fieldId":10000,"fieldValue":"test01","ruleId":666},{"userId":17,"fieldId":10000,"fieldValue":"test02","ruleId":666}]}}
     */
    @ResponseBody
    @RequestMapping(value = "getRuleInfo/{id}", method = RequestMethod.POST)
    public ResponseEntityDto<RuleVo> getRuleInfo(@PathVariable Long id) {
        if (id == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        RuleVo ruleVo = ruleService.queryById(id,2);
        ResponseEntityDto<RuleVo> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(ruleVo);
        return ruleResponseEntityDto;
    }


    /**
     * @api {POST} /v3/rule/getRuleList 3.20. V3规则 列表 list
     * @apiGroup knowledge
     * @apiVersion 2.0.0
     * @apiParam {Integer} [pageNum] 第几页，有默认值1
     * @apiParam {Integer} [pageSize] 每页的数量，有默认值10
     * @apiParam {object} [ruleInfo] 查询条件rule对象（不加查询条件则返回全部，以下参数未此对象的字段）
     * @apiParam (ruleInfo) {Long} [userId] rule的id
     * @apiParam (ruleInfo) {String} [name] rule的名字
     * @apiParam (ruleInfo) {String} [versionCode] rule的代码
     * @apiParam (ruleInfo) {String} [description] rule的描述信息
     * @apiParam (ruleInfo) {Integer} [priority] 优先级
     * @apiParam (ruleInfo) {String} [parentId] 父节点id（文件夹id）
     * @apiParam (ruleInfo) {Integer} [priority] 优先级
     * @apiParam (ruleInfo) {Long} [author] 用户id
     * @apiParam (ruleInfo) {Long} [userId] 用户id
     * @apiParam (ruleInfo) {Long} [organId] 组织id
     * @apiParam (ruleInfo) {Integer} [engineId] 引擎id（原表内容未用到）
     * @apiParam (ruleInfo) {Integer} [status] 状态id（0 :停用 ，1 : 启用，-1：删除）
     * @apiParam (ruleInfo) {Integer} [type] 规则类型（0 : 系统的规则  1：组织的规则 2： 引擎的规则）
     * @apiParam (ruleInfo) {Integer} [isNon] 逻辑关系“非”，0：否 ，1：是
     * @apiParam (ruleInfo) {String} [content] 规则执行内容
     * @apiParam (ruleInfo) {Integer} [ruleType] 规则类型（0硬性拒绝规则，1加减分规则）
     * @apiParam (ruleInfo) {Integer} [ruleAudit] 规则审核（2 终止，5 继续）
     * @apiParam (ruleInfo) {Integer} [score] 规则得分
     * @apiParam (ruleInfo) {String} [lastLogical] 最后逻辑（原表内容未用到）
     * @apiSuccess {String} status 状态: 1成功, 0失败
     * @apiParamExample {json} 请求示例：
     * {"pageNum":1,"pageSize":10,"ruleInfo":{"userId":666,"name":"2021_4_6测试接口","versionCode":"test2021_4_6第二次输入","description":"接口测试","priority":0,"parentId":0,"author":135,"userId":135,"organId":46,"engineId":null,"status":1,"type":1,"isNon":null,"content":"test","created":"2021-04-06T08:38:17.000+00:00","updated":"2021-04-06T09:07:27.000+00:00","ruleType":0,"ruleAudit":2,"score":0,"lastLogical":""}}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {"status":"1","error":"00000000","msg":null,"data":{"userId":666,"name":"2021_4_6测试接口","versionCode":"test2021_4_6第二次输入","description":"接口测试","priority":0,"parentId":0,"author":135,"userId":135,"organId":46,"engineId":null,"status":1,"type":1,"isNon":null,"content":"test","created":"2021-04-06T08:38:17.000+00:00","updated":"2021-04-07T05:12:52.000+00:00","ruleType":0,"ruleAudit":2,"score":0,"lastLogical":"","ruleConditionVo":{"userId":14,"logical":"&&","fieldId":null,"operator":null,"fieldValue":null,"ruleId":666,"parentId":0,"conditionType":1,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[{"userId":15,"logical":null,"fieldId":null,"operator":">","fieldValue":"1000","ruleId":666,"parentId":14,"conditionType":0,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null},{"userId":16,"logical":null,"fieldId":null,"operator":"<","fieldValue":"10000","ruleId":666,"parentId":14,"conditionType":0,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null},{"userId":14,"logical":"&&","fieldId":null,"operator":null,"fieldValue":null,"ruleId":666,"parentId":0,"conditionType":1,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[{"userId":14,"logical":"&&","fieldId":null,"operator":null,"fieldValue":null,"ruleId":666,"parentId":0,"conditionType":1,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[{"userId":15,"logical":null,"fieldId":null,"operator":">","fieldValue":"1000","ruleId":666,"parentId":14,"conditionType":0,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null},{"userId":16,"logical":null,"fieldId":null,"operator":"<","fieldValue":"10000","ruleId":666,"parentId":14,"conditionType":0,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null}],"tempParentId":null},{"userId":15,"logical":null,"fieldId":null,"operator":">","fieldValue":"1000","ruleId":666,"parentId":14,"conditionType":0,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null},{"userId":16,"logical":null,"fieldId":null,"operator":"<","fieldValue":"10000","ruleId":666,"parentId":14,"conditionType":0,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null}],"tempParentId":null},{"userId":14,"logical":"&&","fieldId":null,"operator":null,"fieldValue":null,"ruleId":666,"parentId":0,"conditionType":1,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null}],"tempParentId":null},"ruleContentInfoList":[{"userId":16,"fieldId":10000,"fieldValue":"test01","ruleId":666},{"userId":17,"fieldId":10000,"fieldValue":"test02","ruleId":666}]}}
     */
    @ResponseBody
    @RequestMapping(value = "getRuleList", method = RequestMethod.POST)
    public ResponseEntityDto getRuleList(@RequestBody RuleListParamV2 ruleListParam) {
        PageInfo pageInfo = ruleService.queryByEntity(ruleListParam);
        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("pageInfo", pageInfo);
        responseMap.put("klist", pageInfo.getList());
        pageInfo.setList(null);
        ResponseEntityDto ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(responseMap);
        return ruleResponseEntityDto;
    }

    /**
     * @api {POST} /v3/rule/addRule 3.21. V3添加规则
     * @apiGroup knowledge
     * @apiVersion 2.0.0
     * @apiParam {String} name rule的名字
     * @apiParam {String} versionCode rule的代码
     * @apiParam {String} description rule的描述信息
     * @apiParam {Integer} priority 优先级
     * @apiParam {String} parentId 父节点id（文件夹id）
     * @apiParam {Integer} [status] 状态id（0 :停用 ，1 : 启用，-1：删除）默认启用
     * @apiParam {Integer} [type] 规则类型（0 : 系统的规则  1：组织的规则 2： 引擎的规则）默认组织规则
     * @apiParam {Integer} [isNon] 逻辑关系“非”，0：否 ，1：是
     * @apiParam {String} [content] 规则执行内容
     * @apiParam {Integer} ruleType 规则类型（0硬性拒绝规则，1加减分规则）
     * @apiParam {Integer} [ruleAudit] 规则审核（2 终止，5 继续）根据ruleType推定拒绝规则为终止
     * @apiParam {Integer} [score] 规则得分
     * @apiParam {String} [lastLogical] 最后逻辑（原表内容未用到）
     * @apiParam {object} [ruleConditionVo] 规则对应的一颗规则条件的树形对象
     * @apiParam (ruleConditionVo) {String} logical 逻辑符号&&、||（仅关系表达式）
     * @apiParam (ruleConditionVo) {Long} fieldId 字段id（仅条件表达式）
     * @apiParam (ruleConditionVo) {String} operator 操作符（仅条件表达式）
     * @apiParam (ruleConditionVo) {String} fieldValue 字段值（仅条件表达式）
     * @apiParam (ruleConditionVo) {Integer} conditionType 条件类型1.关系表达式，2.条件表达式
     * @apiParam (ruleConditionVo) {List} [children] 子对象列表，和所属对象相同
     * @apiParam {List} [ruleContentInfoList] 同一规则下的content对象list
     * @apiParam (ruleContentInfoList) {Long} [fieldId] 字段id
     * @apiParam (ruleContentInfoList) {String} [fieldValue] 字段值
     * @apiSuccess {String} status 状态: 1成功, 0失败
     * @apiParamExample {json} 请求示例：
     * {"name":"2021_4_6测试接口02","versionCode":"test2021_4_7","description":"接口测试","priority":0,"parentId":0,"author":135,"userId":135,"organId":46,"status":1,"type":1,"isNon":null,"content":"test","ruleType":0,"ruleAudit":2,"score":0,"lastLogical":"","ruleConditionVo":{"logical":"&&","conditionType":"1","children":[{"fieldId":"0","operator":">","fieldValue":"10","conditionType":"1"},{"fieldId":"0","operator":"<","fieldValue":"100","conditionType":"1"}]},"ruleContentInfoList":[{"fieldId":"10000","fieldValue":"test1"},{"fieldId":"10000","fieldValue":"test2"}]}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {"status":"1","error":"00000000","msg":null,"data":{"userId":668,"name":"2021_4_6测试接口02","versionCode":"test2021_4_7","description":"接口测试","priority":0,"parentId":0,"author":135,"userId":135,"organId":46,"engineId":null,"status":1,"type":1,"isNon":null,"content":"test","created":null,"updated":null,"ruleType":0,"ruleAudit":2,"score":0,"lastLogical":"","ruleConditionVo":{"userId":17,"logical":"&&","fieldId":null,"operator":null,"fieldValue":null,"ruleId":668,"parentId":0,"conditionType":1,"createTime":"2021-04-07T08:09:17.000+00:00","updateTime":null,"insertTempId":null,"children":[{"userId":18,"logical":null,"fieldId":null,"operator":">","fieldValue":"10","ruleId":668,"parentId":17,"conditionType":1,"createTime":"2021-04-07T08:09:17.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null},{"userId":19,"logical":null,"fieldId":null,"operator":"<","fieldValue":"100","ruleId":668,"parentId":17,"conditionType":1,"createTime":"2021-04-07T08:09:17.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null}],"tempParentId":null},"ruleContentInfoList":[{"userId":18,"fieldId":10000,"fieldValue":"test1","ruleId":668},{"userId":19,"fieldId":10000,"fieldValue":"test2","ruleId":668}]}}
     */
    @ResponseBody
    @RequestMapping(value = "addRule", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.SAVE_RULE)
    public ResponseEntityDto<RuleVo> addRule(@RequestBody RuleVo ruleVo) {
        RuleVo insert = ruleService.insertRuleInfo(ruleVo);
        ResponseEntityDto<RuleVo> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(insert);
        return ruleResponseEntityDto;
    }

    /**
     * @api {POST} /v3/rule/updateRule 3.22. V3修改规则
     * @apiGroup knowledge
     * @apiVersion 2.0.0
     * @apiParam {Long} userId rule的id
     * @apiParam {String} name rule的名字
     * @apiParam {String} versionCode rule的代码
     * @apiParam {String} description rule的描述信息
     * @apiParam {Integer} priority 优先级
     * @apiParam {String} parentId 父节点id（文件夹id）
     * @apiParam {Long} author 用户id
     * @apiParam {Long} userId 用户id
     * @apiParam {Long} organId 组织id
     * @apiParam {Integer} [engineId] 引擎id（原表内容未用到）
     * @apiParam {Integer} status 状态id（0 :停用 ，1 : 启用，-1：删除）
     * @apiParam {Integer} type 规则类型（0 : 系统的规则  1：组织的规则 2： 引擎的规则）
     * @apiParam {Integer} [isNon] 逻辑关系“非”，0：否 ，1：是
     * @apiParam {String} [content] 规则执行内容
     * @apiParam {Integer} ruleType 规则类型（0硬性拒绝规则，1加减分规则）
     * @apiParam {Integer} [ruleAudit] 规则审核（2 终止，5 继续）根据ruleType推定拒绝规则为终止
     * @apiParam {Integer} [score] 规则得分
     * @apiParam {String} [lastLogical] 最后逻辑（原表内容未用到）
     * @apiParam {object} [ruleConditionVo] 规则对应的一颗规则条件的树形对象
     * @apiParam (ruleConditionVo) {Long} [userId] 条件id
     * @apiParam (ruleConditionVo) {String} logical 逻辑符号&&、||（仅关系表达式）
     * @apiParam (ruleConditionVo) {Long} fieldId 字段id（仅条件表达式）
     * @apiParam (ruleConditionVo) {String} operator 操作符（仅条件表达式）
     * @apiParam (ruleConditionVo) {String} fieldValue 字段值（仅条件表达式）
     * @apiParam (ruleConditionVo) {Long} [ruleId] 规则id
     * @apiParam (ruleConditionVo) {Long} [parentId] 条件父节点id
     * @apiParam (ruleConditionVo) {Integer} conditionType 条件类型1.关系表达式，2.条件表达式
     * @apiParam (ruleConditionVo) {List} [children] 子对象列表，和所属对象相同
     * @apiParam {List} [ruleContentInfoList] 同一规则下的content对象list
     * @apiParam (ruleContentInfoList) {Long} [userId] content主键id
     * @apiParam (ruleContentInfoList) {Long} fieldId 字段id
     * @apiParam (ruleContentInfoList) {String} fieldValue 字段值
     * @apiParam (ruleContentInfoList) {Long} [ruleId] 规则id
     * @apiSuccess {String} status 状态: 1成功, 0失败
     * @apiParamExample {json} 请求示例：
     * {"userId":667,"name":"2021_4_7测试接口","versionCode":"test2021_4_7第二次输入","description":"接口测试","priority":0,"parentId":0,"author":135,"userId":135,"organId":46,"engineId":null,"status":1,"type":1,"isNon":null,"content":"test","created":"2021-04-06T08:38:17.000+00:00","updated":null,"ruleType":0,"ruleAudit":2,"score":0,"lastLogical":"","ruleConditionVo":{"userId":5,"logical":"&&","fieldId":null,"operator":null,"fieldValue":null,"ruleId":666,"parentId":0,"conditionType":1,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[{"userId":6,"logical":null,"fieldId":0,"operator":">","fieldValue":"1000","ruleId":666,"parentId":5,"conditionType":1,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null},{"userId":7,"logical":null,"fieldId":0,"operator":"<","fieldValue":"10000","ruleId":666,"parentId":5,"conditionType":1,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null}],"tempParentId":null},"ruleContentInfoList":[{"userId":10,"fieldId":"10000","fieldValue":"test01","ruleId":666},{"userId":11,"fieldId":"10000","fieldValue":"test02","ruleId":666}]}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {"status":"1","error":"00000000","msg":null,"data":{"userId":667,"name":"2021_4_7测试接口","versionCode":"test2021_4_7第二次输入","description":"接口测试","priority":0,"parentId":0,"author":135,"userId":135,"organId":46,"engineId":null,"status":1,"type":1,"isNon":null,"content":"test","created":"2021-04-06T08:38:17.000+00:00","updated":null,"ruleType":0,"ruleAudit":2,"score":0,"lastLogical":"","ruleConditionVo":{"userId":20,"logical":"&&","fieldId":null,"operator":null,"fieldValue":null,"ruleId":667,"parentId":0,"conditionType":1,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[{"userId":21,"logical":null,"fieldId":null,"operator":">","fieldValue":"1000","ruleId":667,"parentId":20,"conditionType":1,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null},{"userId":22,"logical":null,"fieldId":null,"operator":"<","fieldValue":"10000","ruleId":667,"parentId":20,"conditionType":1,"createTime":"2021-04-06T08:38:26.000+00:00","updateTime":null,"insertTempId":null,"children":[],"tempParentId":null}],"tempParentId":null},"ruleContentInfoList":[{"userId":20,"fieldId":10000,"fieldValue":"test01","ruleId":667},{"userId":21,"fieldId":10000,"fieldValue":"test02","ruleId":667}]}}
     */
    @ResponseBody
    @RequestMapping(value = "updateRule", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_RULE)
    public ResponseEntityDto<RuleVo> updateRule(@RequestBody RuleVo rule) {
        RuleVo update = ruleService.updateRuleInfo(rule);
        ResponseEntityDto<RuleVo> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(update);
        return ruleResponseEntityDto;
    }

    /**
     * @api {POST} /v3/rule/updateRuleStatus 3.23. V3修改状态
     * @apiGroup knowledge
     * @apiVersion 2.0.0
     * @apiParam {LongArray} ids  规则id数组
     * @apiParam {Integer} status  目标状态：0未启用，1启用，2删除
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * {"ids":[666,667],"status":"1"}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {"status":"1","error":"00000000","msg":null,"data":true}
     */
    @ResponseBody
    @RequestMapping(value = "updateRuleStatus", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_RULE_STATUS)
    public ResponseEntityDto updateStatus(@RequestBody UpdateStatusParam param) {
        UpdateStatusParam.checkParam(param);
        boolean updateResult = ruleService.updateStatus(param.getList(), param.getStatus());
        if (updateResult) {
            return ResponseEntityBuilder.buildNormalResponse(updateResult);
        } else {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
        }

    }

    @ResponseBody
    @RequestMapping(value = "updateRuleParent", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_RULE_STATUS)
    public ResponseEntityDto updateParent(@RequestBody UpdateFolderParam param) {
        UpdateFolderParam.checkNotNull(param);
        boolean updateResult = ruleService.updateParent(param.getIds(), param.getFolderId());
        if (updateResult) {
            return ResponseEntityBuilder.buildNormalResponse(updateResult);
        } else {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
        }
    }

    // 查询简单规则
    @ResponseBody
    @RequestMapping(value = "getSimpleRule/{id}", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.SELECT_SAMPLE_RULE)
    public ResponseEntityDto<RuleVo> getSimpleRule(@PathVariable Long id) {
        if (id == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        RuleVo ruleVo = ruleService.queryById(id,1);
        ResponseEntityDto<RuleVo> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(ruleVo);
        return ruleResponseEntityDto;
    }

    @ResponseBody
    @RequestMapping(value = "getScriptRule/{id}", method = RequestMethod.POST)
    public ResponseEntityDto<RuleVo> getScriptRule(@PathVariable Long id) {
        if (id == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        RuleVo ruleVo = ruleService.queryById(id,3);
        ResponseEntityDto<RuleVo> ruleResponseEntityDto = ResponseEntityBuilder.buildNormalResponse(ruleVo);
        return ruleResponseEntityDto;
    }
}
