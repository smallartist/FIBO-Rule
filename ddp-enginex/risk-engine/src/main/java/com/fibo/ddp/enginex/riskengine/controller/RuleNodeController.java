package com.fibo.ddp.enginex.riskengine.controller;

import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.enginex.risk.request.KnowledgeTreeListParam;
import com.fibo.ddp.common.model.strategyx.guiderule.RuleInfo;
import com.fibo.ddp.common.model.strategyx.guiderule.request.RuleListParamV2;
import com.fibo.ddp.common.model.strategyx.knowledge.KnowledgeTree;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.strategyx.guiderule.RuleService;
import com.fibo.ddp.common.service.strategyx.guiderule.RuleVersionService;
import com.fibo.ddp.common.service.strategyx.knowledge.KnowledgeTreeService;
import com.fibo.ddp.common.service.strategyx.scriptrule.RuleScriptVersionService;
import com.fibo.ddp.common.utils.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("RuleNodeController")
@RequestMapping("/v2/ruleNode")
@ResponseBody
public class RuleNodeController {

    @Autowired
    private RuleService ruleService;
    @Autowired
    private RuleVersionService versionService;
    @Autowired
    private RuleScriptVersionService ruleScriptVersionService;
    @Autowired
    private KnowledgeTreeService knowledgeTreeService;

    /**
     * @api {POST} /v2/ruleNode/getTreeDataForEngine 7.61. 获取树形节点集合
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Integer} status 值为1
     * @apiParam {Integer} type 1规则集，2
     * @apiParam {Integer} [pageNo=1] 第几页
     * @apiParam {Integer} [pageSize=5] 每页的条数
     * @apiParam {Integer} [engineId] 引擎id
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * {"status":1,"type":1,"pageNo":1,"pageSize":5,"engineId":211}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/getTreeDataForEngine", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getTreeDataForEngine(@RequestBody Map<String, Object> param) {

        // @RequestParam Map<String,Object> inputParam,
        // @RequestParam(defaultValue = "1") Integer pageNo,
        // @RequestParam(defaultValue = "5") Integer pageSize

        Integer pageNo = param.get("pageNo") == null ? 1 : Integer.valueOf(param.get("pageNo").toString());
        Integer pageSize = param.get("pageSize") == null ? 5 : Integer.valueOf(param.get("pageSize").toString());

        param.putAll(getParam(param));
        param.put("sort", false);
        param.put("tree_type", StringUtil.toLongList("0"));
        param.put("status", StringUtil.toLongList(param.get("status").toString()));
        PageHelper.startPage(pageNo, pageSize);
        List<KnowledgeTree> klist = knowledgeTreeService.getTreeDataForEngine(param);
        PageInfo<KnowledgeTree> pageInfo = new PageInfo<>(klist);
        param.put("klist", klist);
        param.put("pager", pageInfo);
        return ResponseEntityBuilder.buildNormalResponse(param);
    }

    /**
     * @api {POST} /v2/ruleNode/getRuleDataForEngine 7.62. 获取规则数据为引擎节点
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Integer} status 值为1
     * @apiParam {Integer} parentIds XXXXX
     * @apiParam {Integer} [is_select] 值为1
     * @apiParam {Integer} [dictKey] 值为ruleName
     * @apiParam {Integer} [dictValue] 搜索关键字
     * @apiParam {Integer} [engineId] 引擎id
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例1：
     * {"status":1,"parentIds":1038,"engineId":211}
     * @apiParamExample {json} 请求示例2：
     * {"status":1,"is_select":1,"dictKey":"ruleName","dictValue":"欺诈","engineId":211}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/getRuleDataForEngine", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getRuleDataForEngine(@RequestBody Map<String, Object> param) {
        param.putAll(getParam(param));
        param.put("status", StringUtil.toLongList(param.get("status").toString()));
        RuleInfo rule = new RuleInfo();
        rule.setStatus(1);
        rule.setOrganId(SessionManager.getLoginAccount().getOrganId());
        if (param.containsKey("parentIds")) {
            List<Long> parentIds = StringUtil.toLongList(param.get("parentIds").toString());
            param.put("parentIds",parentIds );
            rule.setParentIds(parentIds);
        }
//        List<Rule> rlist = s.ruleService.getRuleList(inputParam);
        RuleListParamV2 ruleListParam = new RuleListParamV2();
        ruleListParam.setPageNum(0);
        ruleListParam.setPageSize(0);
        ruleListParam.setRuleInfo(rule);
        List<RuleInfo> list = ruleService.queryByEntity(ruleListParam).getList();
        if (list != null && list.size() > 0) {
            for (RuleInfo info : list) {
                if (info.getDifficulty() == 2) {
                    info.setRuleVersionList(versionService.queryVersionListByRuleId(info.getId()));
                }else
                if (info.getDifficulty() == 3){
                    info.setRuleScriptVersionList(ruleScriptVersionService.queryVersionListByRuleId(info.getId()));
                }
            }
        }
        param.put("rlist", list);
        return ResponseEntityBuilder.buildNormalResponse(param);
    }

    // 已选规则预览（待完善）（待讨论）
    @RequestMapping(value = "/previewRule", method = RequestMethod.POST)
    public ModelAndView previewRule(@RequestParam Map<String, Object> paramMap) {
        ModelAndView mav = new ModelAndView("decision/rulesDetails");
        mav.addAllObjects(paramMap);
        return mav;
    }

    // 根据用户角色或权限，获取所需参数集合
    private Map<String, Object> getParam(Map<String, Object> paramMap) {
        SysUser sysUser = SessionManager.getLoginAccount();
        paramMap.put("userId", sysUser.getUserId());
        paramMap.put("organId", sysUser.getOrganId());
        return paramMap;
    }

    @RequestMapping(value = "/getFolderList", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getFolderList(@RequestBody KnowledgeTreeListParam param) {
        List<KnowledgeTree> list = knowledgeTreeService.getFolderList(param);
        Map<String, Object> map = new HashMap<>();
        map.put("klist", list);
        return ResponseEntityBuilder.buildNormalResponse(map);
    }

}
