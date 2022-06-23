package com.fibo.ddp.enginex.riskengine.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import com.fibo.ddp.common.model.enginex.risk.request.NodeParam;
import com.fibo.ddp.common.model.enginex.risk.response.param.DecisionFlowOutputResponse;
import com.fibo.ddp.common.model.enginex.risk.response.param.NodeTypeResponse;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.enginex.risk.EngineNodeService;
import com.fibo.ddp.common.service.enginex.risk.EngineVersionService;
import com.fibo.ddp.common.service.enginex.util.EngineNodeUtil;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.utils.constant.CommonConst;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import com.fibo.ddp.common.utils.constant.enginex.NodeTypeEnum;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 决策流
 */
@Controller("DecisionFlowControllerV2")
@RequestMapping("/v2/decision_flow")
@ResponseBody
public class DecisionFlowController {

    @Autowired
    private EngineNodeService engineNodeService;
    @Autowired
    private EngineVersionService engineVersionService;

    /**
     * @api {POST} /v2/decision_flow/getNodeList/{versionId} 7.10. 根据versionId获取所有节点
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} versionId 路径参数versionId
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiSuccess {Number} maxOrder 版下节点的最大顺序号
     * @apiParamExample {json} 请求示例：
     * {无请求体}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {"status":"1","error":"00000000","msg":null,"data":{"versionId":448,"maxOrder":3,"engineNodeList":[{"nodeId":3121,"parentId":null,"versionId":448,"nodeName":"开始","nodeCode":"ND_START","nodeOrder":1,"nodeType":1,"nodeX":86.68,"nodeY":187.81,"nodeScript":null,"nodeJson":null,"params":"{\"arr_linkId\":\"\",\"dataId\":\"-1\",\"url\":\"/Riskmanage/resource/images/decision/start.png\",\"type\":\"1\"}","nextNodes":"ND_2","cardId":null,"ruleList":null,"lastNextnode":null},{"nodeId":3228,"parentId":3121,"versionId":448,"nodeName":"客户分群_2","nodeCode":"ND_2","nodeOrder":2,"nodeType":3,"nodeX":276.98,"nodeY":163.46,"nodeScript":"{\"fields\":[{\"field_id\":\"698\",\"field_code\":\"f_ms_id_card\",\"field_type\":\"2\",\"field_name\":\"身份证号码\"}],\"conditions\":[{\"nextNode\":\"ND_3\",\"formula\":\"(#{f_ms_id_card}contains32)\"}]}","nodeJson":"{\"conditions\":[{\"group_name\":\"分组1\",\"nextNode\":\"ND_3\",\"formulas\":[{\"field_code1\":\"f_ms_id_card\",\"operator1\":\"contains\",\"value1\":\"32\",\"relative_operator\":\"\"}],\"original_name\":\"\"}],\"fields\":[{\"field_code\":\"f_ms_id_card\",\"field_name\":\"身份证号码\",\"field_id\":\"698\",\"field_type\":\"2\"}]}","params":"{\"dataId\":\"3\",\"url\":\"/Riskmanage/resource/images/decision/createUserGroup.png\",\"type\":3,\"html\":\"<div class=\\\"c-swarm-interior\\\"><div class=\\\"c-swarm-interior-left\\\"><div class=\\\"c-swarm-name\\\"><input type=\\\"text\\\" name=\\\"groupName\\\" dictValue=\\\"分组1\\\" data=\\\"分组1\\\"></div><div class=\\\"c-contains-outside\\\"><div class=\\\"c-contained-within\\\"><div class=\\\"c-select-two\\\"><input class=\\\"c-swarm-input datas c-select-input\\\" fieldid=\\\"698\\\" field_type=\\\"2\\\" field_code=\\\"f_ms_id_card\\\" type=\\\"text\\\" dictValue=\\\"身份证号码\\\" valuescope=\\\"[-1,9999999)\\\" title=\\\"身份证号码\\\"><select class=\\\"l_before datas\\\" style=\\\" margin-left:4px;width: 60px;background-position: 40px 0px;\\\"><option data=\\\"contains\\\" dictValue=\\\"包含\\\">包含</option><option data=\\\"notContains\\\" dictValue=\\\"不包含\\\">不包含</option><option data=\\\"equals\\\" dictValue=\\\"等于\\\">等于</option><option data=\\\"notEquals\\\" dictValue=\\\"不等于\\\">不等于</option></select></div><div class=\\\"c-swarm-name\\\"><input type=\\\"text\\\" class=\\\"datas\\\" dictValue=\\\"32\\\"></div><div class=\\\"c-select-one\\\"><select class=\\\"l_before l_relations datas\\\" style=\\\"width:80px;margin-left:5px;background-position: 60px 0px;\\\"><option data=\\\"0\\\" dictValue=\\\"待选\\\">待选</option><option data=\\\"&amp;&amp;\\\" dictValue=\\\"且\\\">且</option><option data=\\\"||\\\" dictValue=\\\"或\\\">或</option></select></div><div class=\\\"c-select-two\\\"><input class=\\\"c-swarm-input datas\\\" fieldid=\\\"\\\" field_type=\\\"\\\" field_code=\\\"\\\" type=\\\"text\\\"><select class=\\\"l_before datas\\\" style=\\\" margin-left:4px;width: 60px;background-position: 40px 0px;\\\"><option data=\\\"0\\\" dictValue=\\\"待选\\\">待选</option></select></div><div class=\\\"c-swarm-name\\\"><input type=\\\"text\\\" class=\\\"datas\\\" dictValue=\\\"\\\"></div></div></div></div><div class=\\\"c-swarm-name\\\"><img src=\\\"/Riskmanage/resource/images/rules/add.png\\\" class=\\\"addCondition\\\"><img src=\\\"/Riskmanage/resource/images/rules/delete.png\\\" style=\\\"margin-left: 14px;\\\" class=\\\"delCondition\\\"></div></div>\",\"groupSelHtml\":\"\\n\\t\\t\\t\\t<!-- <div class=\\\"c-sanbox-lie-input\\\">\\n\\t\\t\\t\\t\\t<input style=\\\"float: left;margin: 0px 5px 0 0;\\\" type=\\\"radio\\\" name=\\\"radios\\\">\\n\\t\\t\\t\\t\\t<div class=\\\"enter-into-proportion\\\">分群1 <span style=\\\"margin-left:3px;\\\"></span></div>\\n\\t\\t\\t\\t</div>\\n\\t\\t\\t\\t<div class=\\\"c-sanbox-lie-input\\\">\\n\\t\\t\\t\\t\\t<input style=\\\"float: left;margin: 0px 5px 0 0;\\\" type=\\\"radio\\\" name=\\\"radios\\\">\\n\\t\\t\\t\\t\\t<div class=\\\"enter-into-proportion\\\">分群2 <span style=\\\"margin-left:3px;\\\"></span></div>\\n\\t\\t\\t\\t</div> -->\\n\\t\\t\\t\"}","nextNodes":"ND_3","cardId":null,"ruleList":null,"lastNextnode":null},{"nodeId":3229,"parentId":3228,"versionId":448,"nodeName":"规则集_3","nodeCode":"ND_3","nodeOrder":3,"nodeType":2,"nodeX":404.29,"nodeY":145.16,"nodeScript":null,"nodeJson":"{\"addOrSubRules\":\"\",\"deny_rules\":\"{\\\"isSerial\\\":0,\\\"rules\\\":[{\\\"userId\\\":508,\\\"parentId\\\":1083,\\\"priority\\\":0,\\\"versionCode\\\":\\\"r_N01027\\\",\\\"name\\\":\\\"逾期总次数\\\"}]}\",\"selectedRule\":\"[{\\\"versionCode\\\":\\\"r_N01027\\\",\\\"name\\\":\\\"逾期总次数\\\",\\\"userId\\\":508,\\\"priority\\\":0,\\\"parentId\\\":1083}]\"}","params":"{\"dataId\":\"4\",\"url\":\"/Riskmanage/resource/images/decision/createRuleGroup.png\",\"type\":\"2\"}","nextNodes":null,"cardId":null,"ruleList":null,"lastNextnode":null}]}}
     */
    @RequestMapping(value = "/getNodeList/{versionId}", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getNodeList(@PathVariable Long versionId) {
        List<EngineNode> nodeList = engineNodeService.getEngineNodeListByEngineVersionId(versionId);

        int maxOrder = 1;
        if (nodeList.size() > 0) {
            maxOrder = engineNodeService.getMaxNodeOrder(versionId);
        }

        HashMap<String, Object> resultMap = new HashMap<>();

        // if (CollectionUtil.isNotNullOrEmpty(nodeList)) {
        //     resultMap.put("maxOrder", maxOrder);  // 版下节点的最大顺序号
        // } else {
        //     resultMap.put("maxOrder", 1);
        // }

        resultMap.put("versionId", versionId);
        resultMap.put("maxOrder", maxOrder);
        resultMap.put("engineNodeList", nodeList);
        return ResponseEntityBuilder.buildNormalResponse(resultMap);
    }

    /**
     * @api {POST} /v2/decision_flow/getNode/{nodeId} 7.11. 获取节点信息（已废除）
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} nodeId 路径参数：节点id
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * {无请求体}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/getNode/{nodeId}", method = RequestMethod.POST)
    public ResponseEntityDto<EngineNode> getNode(@PathVariable Long nodeId) {
        EngineNode engineNode = engineNodeService.findById(nodeId);
        return ResponseEntityBuilder.buildNormalResponse(engineNode);
    }

    /**
     * @api {POST} /v2/decision_flow/getNodeConditionInputParam 7.25. 获取决策流节点条件入参
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} nodeId 节点id
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * {"nodeId":3559}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {"status":"1","error":"00000000","msg":null,"data":{"nodeTypeList":[{"nodeTypeName":"评分卡","nodeInfoList":[{"nodeId":3731,"nodeName":"评分卡","strategyOutputList":[{"fieldEn":"final_scorecard"}]}]}]}}
     */
    @RequestMapping(value = "/getNodeConditionInputParam", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getNodeConditionInputParam(@RequestBody Map<String, Object> paramMap) {
        DecisionFlowOutputResponse decisionFlowOutputResponse = new DecisionFlowOutputResponse();
        Long nodeId = Long.parseLong(paramMap.get("nodeId").toString());
        List<NodeTypeResponse> nodeTypeList = engineNodeService.getPreviousNodeOutput(nodeId);
        decisionFlowOutputResponse.setNodeTypeList(nodeTypeList);
        return ResponseEntityBuilder.buildNormalResponse(decisionFlowOutputResponse);
    }

    /**
     * @api {POST} /v2/decision_flow/save 7.30. save：新增空节点
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} userId 值为0，表示新添加一个空节点
     * @apiParam {Number} versionId versionId
     * @apiParam {Number} nodeType NodeTypeEnum：4评分卡，
     * @apiParam {String} nodeName 节点名称
     * @apiParam {String} nodeCode 节点code
     * @apiParam {Number} nodeOrder 节点顺序
     * @apiParam {Number} nodeX nodeX
     * @apiParam {Number} nodeY nodeY
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * {"userId":0,"nodeType":4,"versionId":457,"nodeName":"测试节点-评分卡","nodeCode":"ND_11","nodeOrder":11,"nodeX":666.66,"nodeY":666.77}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {"status":"1","error":"00000000","msg":null,"data":{"nodeId":3268,"parentId":null,"versionId":457,"nodeName":"测试节点-评分卡","nodeCode":"ND_test","nodeOrder":null,"nodeType":4,"nodeX":666.66,"nodeY":666.77,"nodeScript":null,"nodeJson":null,"params":null,"nextNodes":null,"cardId":null,"ruleList":null,"lastNextnode":null}}
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.SAVE_NODE)
    public ResponseEntityDto<Object> save(@RequestBody Map<String, Object> param) {
        EngineNode engineNode = new EngineNode();
        engineNode.setNodeType(Integer.parseInt(param.get("nodeType").toString()));
        engineNode.setVersionId(Long.parseLong(param.get("versionId").toString()));
        engineNode.setNodeName(param.get("nodeName").toString());
        engineNode.setNodeCode(param.get("nodeCode").toString());
        engineNode.setNodeOrder(Integer.valueOf(param.get("nodeOrder").toString()));
        engineNode.setNodeX(Double.parseDouble(param.get("nodeX").toString()));
        engineNode.setNodeY(Double.parseDouble(param.get("nodeY").toString()));
        EngineNode node = engineNodeService.saveEngineNodeV2(engineNode);
        return ResponseEntityBuilder.buildNormalResponse(node);
    }

    /**
     * @api {POST} /v2/decision_flow/update 7.31. update：修改节点
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} userId 节点id
     * @apiParam {String} initEngineVersionId versionId
     * @apiParam {Number} nodeType 节点类型：4评分卡，
     * @apiParam {Number} [cardId] 评分卡的id
     * @apiParam {Number} [targetId] targetId：设置 target节点的父节点 为 本节点  { 客户分群连线时使用 }
     * @apiParam {String} nodeName 节点名称
     * @apiParam {String} nodeCode 节点code
     * @apiParam {Number} nodeOrder 节点顺序
     * @apiParam {Number} nodeX 节点X轴
     * @apiParam {Number} nodeY 节点Y轴
     * @apiParam {String} params 节点类型，图标等信息（JSON字符串）
     * @apiParam {String} nodeJson 节点json（结构由节点类型决定）（JSON字符串）
     * @apiParam {String} params.dataId 例如："7"
     * @apiParam {String} params.url 例如："/Riskmanage/resource/images/decision/createDcisionOption.png"
     * @apiParam {String} params.type 例如："9"
     * @apiParam {Integer} nodeJson.inputs 例如：1
     * @apiParam {Integer} nodeJson.condition_type 例如：1
     * @apiParam {JSONArray} nodeJson.input 数组，[{"field_id":102,"field_code":"SC_task_card5_score","field_name":"评分卡-申请","field_scope":"","field_type":1}]
     * @apiParam {JSON} nodeJson.output 例如：{"field_id":682,"field_code":"f_decision_limit_rate","field_name":"决策和额度利率","field_type":2,"field_scope":""}
     * @apiParam {JSONArray} nodeJson.conditions 例如：[{"result":"2,0.03,0.67,10,1500,6,三,-1","resultKey":"2,0.03,0.67,10,1500,6,三,-1","formula":[{"field_code":"SC_task_card5_score","operator":">=","result":"485","resultKey":"485"},{"field_code":"SC_task_card5_score","operator":"<","result":"525","resultKey":"525","sign":"and"}]}]
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 评分卡
     * {"userId":3265,"initEngineVersionId":"457","nodeType":4,"cardId":102,"nodeName":"测试节点-评分卡","nodeCode":"ND_22","nodeOrder":22,"nodeX":489,"nodeY":402,"nodeJson":"102","params":{"dataId":5,"url":"/Riskmanage/resource/images/decision/createScoreLevel.png","type":4}}
     * @apiParamExample {json} 规则集
     * {"userId":3265,"initEngineVersionId":"457","nodeType":2,"nodeName":"测试节点-规则集","nodeCode":"ND_5","nodeOrder":5,"nodeX":489,"nodeY":402,"params":{"dataId":"4","url":"/Riskmanage/resource/images/decision/createRuleGroup.png","type":"2"},"deny_rules":{"isSerial":1,"rules":[{"userId":559,"parentId":1088,"priority":0,"versionCode":"r_N04015","name":"近1月实际借款平台数"},{"userId":553,"parentId":1088,"priority":0,"versionCode":"r_N04008","name":"借贷app安装数量"},{"userId":580,"parentId":1084,"priority":0,"versionCode":"r_N0210013","name":"ML-Y"}]},"addOrSubRules":""}
     * @apiParamExample {json} 决策选项
     * {"condition_type":1,"input":[{"field_id":95,"field_code":"SC_test_card2_score","field_name":"客户评分卡-有盾","field_scope":"","field_type":1}],"output":{"field_id":681,"field_code":"f_decision_limit_rate","field_name":"决策和额度利率","field_type":2,"field_scope":""},"conditions":[{"result":"2,0.03,0.67,10,1500,6,-1,-1","resultKey":"2,0.03,0.67,10,1500,6,-1,-1","formula":[{"field_code":"SC_test_card2_score","operator":">","result":"370","resultKey":"370"},{"field_code":"SC_test_card2_score","operator":"<=","result":"400","resultKey":"400","sign":"and"}]},{"result":"2,0.03,0.67,10,1500,6,-1,-1","resultKey":"2,0.03,0.67,10,1500,6,-1,-1","formula":[{"field_code":"SC_test_card2_score","operator":">","result":"330","resultKey":"330"},{"field_code":"SC_test_card2_score","operator":"<=","result":"370","resultKey":"370","sign":"and"}]},{"result":"3,0.03,0.67,10,1500,6,-1,-1","resultKey":"3,0.03,0.67,10,1500,6,-1,-1","formula":[{"field_code":"SC_test_card2_score","operator":">","result":"0","resultKey":"0"},{"field_code":"SC_test_card2_score","operator":"<=","result":"330","resultKey":"330","sign":"and"}]}]}
     * @apiParamExample {json} 沙盒比例
     * {"userId":3311,"initEngineVersionId":"433","nodeType":7,"nodeName":"测试节点-沙盒比例","nodeCode":"ND_5","nodeOrder":5,"nodeX":489,"nodeY":402,"params":{"dataId":"2","url":"/Riskmanage/resource/images/decision/createRiverRate.png","type":"7"},"nodeJson":[{"proportion":"80","sandbox":"1","nextNode":"ND_16"},{"proportion":"20","sandbox":"2","nextNode":"ND_20"}]}
     * @apiParamExample {json} 客户分群
     * {"userId":2770,"initEngineVersionId":"419","nodeType":3,"nodeName":"测试节点-客户分群","nodeCode":"ND_5","nodeOrder":5,"nodeX":489,"nodeY":402,"nodeJson":{"conditions":[{"group_name":"分组1","nextNode":"ND_8","formulas":[{"field_code1":"f_bj_finalScore","operator1":">","value1":"626","relative_operator":""}]},{"group_name":"分组2","nextNode":"ND_12","formulas":[{"field_code1":"f_bj_finalScore","operator1":">","value1":"578","relative_operator":"&&","field_code2":"f_bj_finalScore","operator2":"<=","value2":"626"}]},{"group_name":"分组3","nextNode":"ND_16","formulas":[{"field_code1":"f_bj_finalScore","operator1":"<=","value1":"578","relative_operator":""}]}],"fields":[{"field_code":"f_bj_finalScore","field_name":"第三方分值","field_id":"659","field_type":"1"}]},"params":{"dataId":3,"url":"/Riskmanage/resource/images/decision/createUserGroup.png","type":3,"html":"<div class=\"c-swarm-interior\"><div class=\"c-swarm-interior-left\"><div class=\"c-swarm-name\" style=\"margin-top:20px\">分组<b class=\"datas groupNum\">1</b></div><div class=\"c-contains-outside\"><div class=\"c-contained-within\"><div class=\"c-select-two\"><input class=\"c-swarm-input datas\" fieldid=\"659\" field_type=\"1\" field_code=\"f_bj_finalScore\" type=\"text\" dictValue=\"第三方分值\" valuescope=\"[-1,9999)\" title=\"第三方分值\"><select class=\"l_before datas\" style=\" margin-left:4px;width: 60px;background-position: 40px 0px;\"><option data=\">\" dictValue=\"大于\">大于</option><option data=\">=\" dictValue=\"大于等于\">大于等于</option><option data=\"==\" dictValue=\"等于\">等于</option><option data=\"!=\" dictValue=\"不等于\">不等于</option><option data=\"<\" dictValue=\"小于\">小于</option><option data=\"<=\" dictValue=\"小于等于\">小于等于</option></select></div><div class=\"c-swarm-name\"><input type=\"text\" class=\"datas\" dictValue=\"626\"></div><div class=\"c-select-one\"><select class=\"l_before l_relations datas\" style=\"width:80px;margin-left:5px;background-position: 60px 0px;\"><option data=\"0\" dictValue=\"待选\">待选</option><option data=\"&amp;&amp;\" dictValue=\"且\">且</option><option data=\"||\" dictValue=\"或\">或</option></select></div><div class=\"c-select-two\"><input class=\"c-swarm-input datas\" fieldid=\"\" field_type=\"\" field_code=\"\" type=\"text\"><select class=\"l_before datas\" style=\" margin-left:4px;width: 60px;background-position: 40px 0px;\"><option data=\"0\" dictValue=\"待选\">待选</option></select></div><div class=\"c-swarm-name\"><input type=\"text\" class=\"datas\" dictValue=\"\"></div></div></div></div><div class=\"c-swarm-name\"><img src=\"/Riskmanage/resource/images/rules/add.png\" class=\"addCondition\"><img src=\"/Riskmanage/resource/images/rules/delete.png\" style=\"margin-left: 14px;\" class=\"delCondition\"></div></div><div class=\"c-swarm-interior\"><div class=\"c-swarm-interior-left\"><div class=\"c-swarm-name\" style=\"margin-top:20px\">分组<b class=\"datas groupNum\">2</b></div><div class=\"c-contains-outside\"><div class=\"c-contained-within\"><div class=\"c-select-two\"><input class=\"c-swarm-input datas\" fieldid=\"659\" field_type=\"1\" field_code=\"f_bj_finalScore\" type=\"text\" dictValue=\"第三方分值\" valuescope=\"[-1,9999)\" title=\"第三方分值\"><select class=\"l_before datas\" style=\" margin-left:4px;width: 60px;background-position: 40px 0px;\"><option data=\">\" dictValue=\"大于\">大于</option><option data=\">=\" dictValue=\"大于等于\">大于等于</option><option data=\"==\" dictValue=\"等于\">等于</option><option data=\"!=\" dictValue=\"不等于\">不等于</option><option data=\"<\" dictValue=\"小于\">小于</option><option data=\"<=\" dictValue=\"小于等于\">小于等于</option></select></div><div class=\"c-swarm-name\"><input type=\"text\" class=\"datas\" dictValue=\"578\"></div><div class=\"c-select-one\"><select class=\"l_before l_relations datas\" style=\"width:80px;margin-left:5px;background-position: 60px 0px;\" dictValue=\"且\"><option data=\"0\" dictValue=\"待选\">待选</option><option data=\"&amp;&amp;\" dictValue=\"且\" selected=\"selected\">且</option><option data=\"||\" dictValue=\"或\">或</option></select></div><div class=\"c-select-two\"><input class=\"c-swarm-input datas\" fieldid=\"659\" field_type=\"1\" field_code=\"f_bj_finalScore\" type=\"text\" dictValue=\"第三方分值\" valuescope=\"[-1,9999)\" title=\"第三方分值\"><select class=\"l_before datas\" style=\" margin-left:4px;width: 60px;background-position: 40px 0px;\" dictValue=\"小于等于\"><option data=\">\" dictValue=\"大于\">大于</option><option data=\">=\" dictValue=\"大于等于\">大于等于</option><option data=\"==\" dictValue=\"等于\">等于</option><option data=\"!=\" dictValue=\"不等于\">不等于</option><option data=\"<\" dictValue=\"小于\">小于</option><option data=\"<=\" dictValue=\"小于等于\" selected=\"selected\">小于等于</option></select></div><div class=\"c-swarm-name\"><input type=\"text\" class=\"datas\" dictValue=\"626\"></div></div></div></div><div class=\"c-swarm-name\"><img src=\"/Riskmanage/resource/images/rules/add.png\" class=\"addCondition\"><img src=\"/Riskmanage/resource/images/rules/delete.png\" style=\"margin-left: 14px;\" class=\"delCondition\"></div></div><div class=\"c-swarm-interior\"><div class=\"c-swarm-interior-left\"><div class=\"c-swarm-name\" style=\"margin-top:20px\">分组<b class=\"datas groupNum\">3</b></div><div class=\"c-contains-outside\"><div class=\"c-contained-within\"><div class=\"c-select-two\"><input class=\"c-swarm-input datas c-select-input\" fieldid=\"659\" field_type=\"1\" field_code=\"f_bj_finalScore\" type=\"text\" dictValue=\"第三方分值\" valuescope=\"[-1,9999)\" title=\"第三方分值\"><select class=\"l_before datas\" style=\" margin-left:4px;width: 60px;background-position: 40px 0px;\" dictValue=\"小于等于\"><option data=\">\" dictValue=\"大于\">大于</option><option data=\">=\" dictValue=\"大于等于\">大于等于</option><option data=\"==\" dictValue=\"等于\">等于</option><option data=\"!=\" dictValue=\"不等于\">不等于</option><option data=\"<\" dictValue=\"小于\">小于</option><option data=\"<=\" dictValue=\"小于等于\" selected=\"selected\">小于等于</option></select></div><div class=\"c-swarm-name\"><input type=\"text\" class=\"datas\" dictValue=\"578\"></div><div class=\"c-select-one\"><select class=\"l_before l_relations datas\" style=\"width:80px;margin-left:5px;background-position: 60px 0px;\"><option data=\"0\" dictValue=\"待选\">待选</option><option data=\"&amp;&amp;\" dictValue=\"且\">且</option><option data=\"||\" dictValue=\"1\">或</option></select></div><div class=\"c-select-two\"><input class=\"c-swarm-input datas\" fieldid=\"\" field_type=\"\" field_code=\"\" type=\"text\" dictValue=\"待选\"><select class=\"l_before datas\" style=\" margin-left:4px;width: 60px;background-position: 40px 0px;\"><option data=\"0\" dictValue=\"待选\">待选</option></select></div><div class=\"c-swarm-name\"><input type=\"text\" class=\"datas\" dictValue=\"\"></div></div></div></div><div class=\"c-swarm-name\"><img src=\"/Riskmanage/resource/images/rules/add.png\" class=\"addCondition\"><img src=\"/Riskmanage/resource/images/rules/delete.png\" style=\"margin-left: 14px;\" class=\"delCondition\"></div></div>","groupSelHtml":""}}
     * @apiParamExample {json} (模型)
     * {"userId":3450,"initEngineVersionId":"454","nodeType":15,"nodeName":"测试模型","nodeCode":"ND_11","nodeOrder":11,"nodeX":489,"nodeY":402,"nodeJson":"2","params":{"dataId":12,"url":"/Riskmanage/resource/images/decision/models.png","type":15}}
     * @apiParamExample {json} (子引擎)
     * {"userId":3451,"initEngineVersionId":"454","nodeType":14,"nodeName":"反欺诈引擎","nodeCode":"ND_12","nodeOrder":12,"nodeX":489,"nodeY":402,"nodeJson":"214","params":{"dataId":11,"url":"/Riskmanage/resource/images/decision/childEngine.png","type":14}}
     * @apiParamExample {json} (分组下面连线)
     * {"userId":2781,"targetId":2784,"initEngineVersionId":"419","nodeType":3,"nodeName":"运营商认证距离上次认证时长_4_3_18","nodeCode":"ND_18","nodeOrder":18,"nextNodes":"ND_19,ND_21","nodeX":489,"nodeY":402,"nodeJson":{"conditions":[{"group_name":"分组1","nextNode":"ND_21","formulas":[{"field_code1":"f_mid_operatorValid","operator1":">=","value1":"90","relative_operator":""}]},{"group_name":"分组2","nextNode":"ND_19","formulas":[{"field_code1":"f_mid_operatorValid","operator1":"<","value1":"90","relative_operator":""}]}],"fields":[{"field_code":"f_mid_operatorValid","field_name":"运营商认证距离上次认证时间差","field_id":"593","field_type":"1"}]}}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_NODE)
    public ResponseEntityDto<Object> update(@RequestBody Map<String, Object> param) {
        String idStr = param.get("id").toString();

        String targetIdStr = null;
        if (null != param.get("targetId")) {
            targetIdStr = param.get("targetId").toString();
        }

        Long id = idStr == null ? null : Long.parseLong(idStr);
        Long targetId = targetIdStr == null ? null : Long.parseLong(targetIdStr);

        EngineNode engineNode = EngineNodeUtil.boxEngineNode(param);
        engineNode.setNodeId(id);

        boolean flag = engineNodeService.updateNode(engineNode, targetId);
        if (flag) {
            param.put("result", 1);
            param.put("nodeId", id);
            param.put("EngineNode", engineNode);
        } else {
            param.put("result", -1);
        }
        return ResponseEntityBuilder.buildNormalResponse(param);
    }

    /**
     * @api {POST} /v2/decision_flow/copy 7.33. 复制节点
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} nodeId 节点id
     * @apiParam {Number} [type] 知识库类型（t_node_knowledge_rel.knowledge_type）1规则集，2评分卡，null客户分群决策选项
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * {"nodeId":2808}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.COPY_NODE)
    public ResponseEntityDto<Object> copy(@RequestBody Map<String, Object> param) {

        Long id = Long.parseLong(param.get("nodeId").toString());

        EngineNode eNode = engineNodeService.findById(id);

        // 设置需要更改的属性
        int maxOrder = engineNodeService.getMaxNodeOrder(eNode.getVersionId());
        eNode.setNodeX(eNode.getNodeX() + 50);
        eNode.setNodeY(eNode.getNodeY() + 50);
        eNode.setNodeOrder(maxOrder + 1);
        eNode.setNodeCode("ND_" + (maxOrder + 1));
        eNode.setNodeName(eNode.getNodeName().substring(0, eNode.getNodeName().lastIndexOf("_") + 1) + eNode.getNodeOrder());
        eNode.setNextNodes("");
        eNode.setParentId(null);

        // 设置params属性
        String params = eNode.getParams();
        if (params != null && params.trim().length() > 0) {
            JSONObject obj = JSONObject.parseObject(params);
            obj.put("arr_linkId", "");
            eNode.setParams(obj.toJSONString());
        }

        boolean flag = engineNodeService.saveEngineNode(eNode);

        if (flag) {
            param.put("result", 1);
            param.put("eNode", eNode);
        } else {
            param.put("result", -1);

        }
        return ResponseEntityBuilder.buildNormalResponse(param);
    }

    /**
     * @api {POST} /v2/decision_flow/removeNode 7.34. 删除节点
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} currentNodeId 当前节点id
     * @apiParam {Number} preNodeId 上一节点id，没有则为-1
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * {"currentNodeId":3258,"preNodeId":-1}
     * {"currentNodeId":3431,"preNodeId":3430}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/removeNode", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.DELETE_NODE)
    public ResponseEntityDto<Object> removeNode(@RequestBody NodeParam param) {
        Long currentNodeId = param.getCurrentNodeId();
        String preNodeId = param.getPreNodeId();
        if (currentNodeId != null && preNodeId != null) {
            engineNodeService.removeNode(currentNodeId, preNodeId);
        }
        return ResponseEntityBuilder.buildNormalResponse(null);
    }

    /**
     * @api {POST} /v2/decision_flow/removeNodeList 7.35. 批量删除节点
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} currentNodeId 当前节点id
     * @apiParam {Number} preNodeId 上一节点id，没有则为-1
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * [{"currentNodeId":3258,"preNodeId":-1},
     * {"currentNodeId":3431,"preNodeId":3430}]
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/removeNodeList", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.BATCH_DELETE_NODE)
    public ResponseEntityDto<Object> removeNodeList(@RequestBody List<NodeParam> paramList) {
        for (NodeParam param : paramList) {
            Long currentNodeId = param.getCurrentNodeId();
            String preNodeId = param.getPreNodeId();
            if (currentNodeId != null && preNodeId != null) {
                engineNodeService.removeNode(currentNodeId, preNodeId);
            }
        }
        return ResponseEntityBuilder.buildNormalResponse(null);
    }

    /**
     * @api {POST} /v2/decision_flow/updatePropertyForMove 7.41. 移动节点位置
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} nodeId 节点id
     * @apiParam {Number} nodeX X坐标
     * @apiParam {Number} nodeY Y坐标
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiSuccess {String} data.result 移动结果：1成功，-1失败
     * @apiParamExample {json} 请求示例：
     * {"nodeId":2808,"nodeX":666.66,"nodeY":421}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {"status":"1","error":"00000000","msg":null,"data":{"nodeId":"2808","nodeX":"666.66","nodeY":"421","result":"1"}}
     */
    @RequestMapping(value = "/updatePropertyForMove", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_NODE)
    public ResponseEntityDto<Object> updatePropertyForMove(@RequestBody Map<String, String> param) {
        String s = param.get("nodeId");
        Long nodeId = StringUtil.getStrToLong(s);
        if (nodeId==null||nodeId==0){
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        EngineNode engineNode = this.engineNodeService.findById(nodeId);

        engineNode.setNodeX(Double.parseDouble(param.get("nodeX")));
        engineNode.setNodeY(Double.parseDouble(param.get("nodeY")));
        boolean flag = this.engineNodeService.updateNodeForMove(engineNode);
        if (flag) {
            param.put("result", "1");
        } else {
            param.put("result", "-1");
        }
        return ResponseEntityBuilder.buildNormalResponse(param);
    }

    /**
     * @api {POST} /v2/decision_flow/updateProperty 7.42. 节点连线（客户分群连线,不是调用此接口,应该去调用接口7.31）
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {String} nodeId_1 起点
     * @apiParam {String} nodeId_2 终点
     * @apiParam {String} [nextNodes_1]
     * @apiParam {String} [nextNodes_2]
     * @apiParam {String} [parentId_2]
     * @apiParam {String} [node_json_1] 沙盒比例的信息
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例1：
     * {"nodeId_1":"2923","nodeId_2":"3037"}
     * @apiParamExample {json} 请求示例2(沙盒比例)
     * {"nodeId_1":"3273","nodeId_2":"3274","nextNodes_1":"ND_24,ND_23","nextNodes_2":"ND_15","parentId_2":"","node_json_1":[{"proportion":"20","sandbox":"1","nextNode":"ND_23"},{"proportion":"80","sandbox":"2","nextNode":"ND_24"}]}
     * @apiSuccessExample {json} 成功返回数据示例
     * {待完善}
     */
    @RequestMapping(value = "/updateProperty", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_NODE)
    public ResponseEntityDto<Object> updateProperty(@RequestBody Map<String, Object> param) {
        List<EngineNode> eList = new ArrayList<>();
        boolean flag = false;
        Long sourceNodeId = Long.parseLong((String) param.get("nodeId_1"));
        Long targetNodeId = Long.parseLong((String) param.get("nodeId_2"));
        //获取开始节点和目标节点
        EngineNode sourceNode = engineNodeService.findById(sourceNodeId);
        EngineNode targetNode = engineNodeService.findById(targetNodeId);
        if (sourceNode != null && targetNode != null) {
            String nextNodes = sourceNode.getNextNodes() == null ? "" : sourceNode.getNextNodes().trim();
            if (StringUtil.isValidStr(nextNodes)) {
                nextNodes = nextNodes.concat(CommonConst.SYMBOL_COMMA).concat(targetNode.getNodeCode());
            } else {
                nextNodes = targetNode.getNodeCode();
            }
            if (param.containsKey("node_json_1")) {
                String sourceNodeJson = param.get("node_json_1").toString();
                if (StringUtil.isValidStr(sourceNodeJson)) {
                    sourceNode.setNodeJson(sourceNodeJson);
                }
            }
            sourceNode.setNextNodes(nextNodes);

            //设置目标节点的parentId
            String targetParentId = targetNode.getParentId();
            if(StringUtils.isNotBlank(targetParentId)){
                targetParentId = targetParentId.concat(CommonConst.SYMBOL_COMMA).concat(sourceNodeId.toString());
            } else {
                targetParentId = sourceNodeId.toString();
            }
            targetNode.setParentId(targetParentId);

            eList.add(targetNode);
            eList.add(sourceNode);
            flag = engineNodeService.updateNodeForNextOrderAndParams(eList);
        }

        if (flag) {
            param.put("result", 1);
            return ResponseEntityBuilder.buildNormalResponse(param);
        } else {
            param.put("result", -1);
            return ResponseEntityBuilder.buildUnNormalResponse(param,ErrorCodeEnum.FAIL_IN_LINK);
        }

    }

    /**
     * @api {POST} /v2/decision_flow/removeLink 7.43. 删除连线
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} currentNodeId 当前节点id
     * @apiParam {Number} preNodeId 上一个节点id
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例
     * {"currentNodeId":3259,"preNodeId":2802}
     * @apiSuccessExample {json} 成功返回数据示例
     * {待完善}
     */
    @RequestMapping(value = "/removeLink", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.DELETE_NODE_LINK)
    public ResponseEntityDto<Object> removeLink(@RequestBody Map<String, Object> param) {
        String engineNodeIdStr = param.get("currentNodeId").toString();
        String preEngineNodeIdStr = param.get("preNodeId").toString();
        Long engineNodeId = Long.parseLong(engineNodeIdStr);
        Long preEngineNodeId = Long.parseLong(preEngineNodeIdStr);
        engineNodeService.removeLink(engineNodeId, preEngineNodeId);
        return ResponseEntityBuilder.buildNormalResponse(param);
    }

    /**
     * @api {POST} /v2/decision_flow/renameNode 7.44. 修改节点名称
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} nodeId 节点id
     * @apiParam {String} nodeName 名称
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例
     * {"nodeId":3089,"nodeName":"我是节点001"}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/renameNode", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_NODE)
    public ResponseEntityDto<Object> renameNode(@RequestBody Map<String, Object> param) {
        int count = engineNodeService.renameNode(param);
        if (count == 1) {
            param.put("result", 1);
        } else {
            param.put("result", -1);
        }
        return ResponseEntityBuilder.buildNormalResponse(param);
    }

    /**
     * @api {POST} /v2/decision_flow/saveVersion/{versionId} 7.51. 复制版本
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} versionId 路径参数versionId
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * {无请求体}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/saveVersion/{versionId}", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.SAVE_VERSION)
    public ResponseEntityDto<Object> saveVersion(@PathVariable Long versionId) {

        SysUser sysUser = SessionManager.getLoginAccount();
        EngineVersion engineVersion = engineVersionService.selectByPrimaryKey(versionId);  // 通过versionId获取当前选中的版本
        List<EngineNode> nodeList = engineNodeService.getEngineNodeListByEngineVersionId(versionId);

        Map<String, Object> map = new HashMap<>();
        if (engineVersion != null) {
            //获取当前版本最新子版本
            EngineVersion engineVer = engineVersionService.getLatestEngineSubVersion(engineVersion);
            //组装版本信息
            EngineVersion latestVersion = new EngineVersion();
            latestVersion.setBootState(0);
            latestVersion.setEngineId(engineVersion.getEngineId());
            latestVersion.setLatestUser(sysUser.getUserId());
            latestVersion.setLayout(0);
            latestVersion.setStatus(1);
            latestVersion.setUserId(sysUser.getUserId());
            latestVersion.setVersion(engineVersion.getVersion());
            latestVersion.setSubVersion(engineVer.getSubVersion() + 1);  // 子版本 +1
            Long verId = engineVersionService.saveEngineVersion(latestVersion, nodeList);  // 此处待修改 t_node_knowledge_rel
            // 修复新增版本t_engine_node表parent_id问题
            updateParentId(verId);
            map.put("result", "1");
            map.put("versionId", verId);
            map.put("msg", "保存成功");
            return ResponseEntityBuilder.buildNormalResponse(map);
        }
        map.put("result", "-1");
        map.put("msg", "保存失败");
        return ResponseEntityBuilder.buildNormalResponse(map);
    }

    /**
     * 修复新增版本t_engine_node表parent_id问题
     *
     * @param versionId
     */
    private void updateParentId(Long versionId) {
        if (versionId == null) {
            return;
        }
        List<EngineNode> nodeList = engineNodeService.getEngineNodeListByEngineVersionId(versionId);
        if (nodeList == null || nodeList.isEmpty()) {
            return;
        }

        Map<String, EngineNode> nodeCodeMap = new HashMap<>();
        for (EngineNode engineNode : nodeList) {
            nodeCodeMap.put(engineNode.getNodeCode(), engineNode);
        }

        for (EngineNode sourceEngineNode : nodeList) {
            String nextNodes = sourceEngineNode.getNextNodes();
            if (StringUtils.isNotBlank(nextNodes)) {
                String[] nextNodesArr = nextNodes.split(",");
                for (String nextNode : nextNodesArr) {
                    EngineNode targetEngineNode = nodeCodeMap.get(nextNode);
                    if (targetEngineNode != null) {
                        // 拼接多个父节点
                        String parentId = targetEngineNode.getParentId();
                        if(StringUtils.isNotBlank(parentId)){
                            parentId = parentId + "," + sourceEngineNode.getNodeId();
                        } else {
                            parentId = sourceEngineNode.getNodeId().toString();
                        }
                        engineNodeService.updateParentIdByNodeId(targetEngineNode.getNodeId(), parentId);
                        // 更新修改后的engineNode信息
                        targetEngineNode.setParentId(parentId);
                        nodeCodeMap.put(nextNode, targetEngineNode);
                    }
                }
            }
        }
    }

    /**
     * @api {POST} /v2/decision_flow/validateBranch 7.52. 删除分组
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Integer} engineNodeId XXXXX
     * @apiParam {String} branch XXXXX
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * {"engineNodeId":2787,"branch":"分组3"}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/validateBranch", method = RequestMethod.POST)
    public ResponseEntityDto<Object> validateBranch(@RequestBody Map<String, Object> paramMap) {
        //获取节点编号
        String engineNodeIdStr = paramMap.get("engineNodeId").toString();
        Long engineNodeId = Long.parseLong(engineNodeIdStr);
        EngineNode engineNode = engineNodeService.findById(engineNodeId);
        if (engineNode != null) {
            //从map中获取
            String branch = paramMap.get("branch").toString();
            String jsonStr = engineNode.getNodeJson();
            if (engineNode.getNodeType() == NodeTypeEnum.CLASSIFY.getValue()) {
                if (StringUtil.isValidStr(jsonStr)) {
                    JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                    JSONArray conditions = jsonObject.getJSONArray("conditions");
                    if (conditions != null && !conditions.isEmpty()) {
                        JSONObject object = null;
                        for (int i = 0; i < conditions.size(); i++) {
                            object = conditions.getJSONObject(i);
                            if (object.getString("group_name").equals(branch)) {
                                String nextNode = object.getString("nextNode");
                                if (StringUtil.isValidStr(nextNode)) {
                                    paramMap.put("result", 1);
                                    break;
                                }
                            }
                        }
                    } else {
                        paramMap.put("result", 0);
                    }
                } else {
                    paramMap.put("result", 0);
                }
            } else if (engineNode.getNodeType() == NodeTypeEnum.SANDBOX.getValue()) {
                if (StringUtil.isValidStr(jsonStr)) {
                    JSONArray conditions = JSONArray.parseArray(jsonStr);
                    if (conditions != null && !conditions.isEmpty()) {
                        JSONObject object = null;
                        for (int i = 0; i < conditions.size(); i++) {
                            object = conditions.getJSONObject(i);
                            if (object.getString("sandbox").equals(branch)) {
                                String nextNode = object.getString("nextNode");
                                if (StringUtil.isValidStr(nextNode)) {
                                    paramMap.put("result", 1);
                                    break;
                                }
                            }
                        }
                    } else {
                        paramMap.put("result", 0);
                    }
                } else {
                    paramMap.put("result", 0);
                }
            }
        } else {
            paramMap.put("result", 1);
        }
        return ResponseEntityBuilder.buildNormalResponse(paramMap);
    }

}
