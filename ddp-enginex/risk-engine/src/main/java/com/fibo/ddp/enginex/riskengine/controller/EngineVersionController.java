package com.fibo.ddp.enginex.riskengine.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.enginex.risk.EngineNodeService;
import com.fibo.ddp.common.service.enginex.risk.EngineVersionService;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import com.fibo.ddp.common.utils.constant.enginex.EngineConst;
import com.fibo.ddp.common.utils.constant.enginex.EngineMsg;
import com.fibo.ddp.common.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller("EngineVersionControllerV2")
@RequestMapping("/v2/engineVersion")
@ResponseBody
public class EngineVersionController {

    @Autowired
    private EngineVersionService engineVersionService;
    @Autowired
    private EngineNodeService engineNodeService;

    /**
     * @api {POST} /v2/engineVersion/add/{engineId}/{version} 7.56. 新增版本
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} engineId 路径参数engineId
     * @apiParam {Number} version 路径参数version
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * {无请求体}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/add/{engineId}/{version}", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.SAVE_VERSION)
    public ResponseEntityDto<Object> addEngineVersion(@PathVariable Long engineId, @PathVariable Integer version) {

        Map<String, Object> map = new HashMap<>();
        // 获取当前系统登录用户,即引擎的创建者
        Long userId = SessionManager.getLoginAccount().getUserId();
        //获取当前选中的版本的最大子版本
        EngineVersion engineVer = new EngineVersion();
        engineVer.setEngineId(engineId);
        engineVer.setVersion(version);
        EngineVersion engineVersion = engineVersionService.getLatestEngineSubVersion(engineVer);
        if (engineVersion != null) {
            //无论引擎是否正在运行，皆在此版本下创建子版本
            EngineVersion newEngineVersion = new EngineVersion();
            newEngineVersion.setBootState(0);
            newEngineVersion.setCreateTime(new Date().toString());
            newEngineVersion.setEngineId(engineVersion.getEngineId());
            newEngineVersion.setLatestTime(new Date().toString());
            newEngineVersion.setLatestUser(userId);
            newEngineVersion.setLayout(0);
            newEngineVersion.setStatus(1);
            newEngineVersion.setSubVersion(engineVersion.getSubVersion() + 1);  // 子版本 +1
            newEngineVersion.setUserId(userId);
            newEngineVersion.setVersion(engineVersion.getVersion());
            //新增版本
            Long versionId = saveEngineVersion(newEngineVersion);
            map.put("result", 1);
            map.put("versionId", versionId);
        } else {
            map.put("result", -1);
            map.put("versionId", 0);
        }
        return ResponseEntityBuilder.buildNormalResponse(map);
    }

    /**
     * @api {POST} /v2/engineVersion/clear/{versionId} 7.57. 清空引擎节点（根据versionId清空）
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} versionId 路径参数versionId
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * {无请求体}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/clear/{versionId}", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.CLEAR_NODE)
    public ResponseEntityDto<Object> clear(@PathVariable Long versionId) {
        Map<String, Object> map = new HashMap<>();
        // 正在部署的不能清除
        EngineVersion engineVersion = engineVersionService.selectByPrimaryKey(versionId);
        if (engineVersion != null) {
            if (engineVersion.getBootState() == 1) {
                map.put("result", "-1");
                map.put("msg", "版本正在运行,请先停止部署!");
            } else {
                boolean flag = engineVersionService.clear(versionId);
                if (flag) {
                    map.put("result", "1");
                    map.put("msg", "清除成功!");
                } else {
                    map.put("result", "-1");
                    map.put("msg", "清除失败,请联系管理员!");
                }
            }
        } else {
            map.put("result", "-1");
            map.put("msg", "数据错误,目标版本不存在!");
        }
        return ResponseEntityBuilder.buildNormalResponse(map);
    }

    /**
     * @api {POST} /v2/engineVersion/deploy/{versionId} 7.50.1. 根据versionId部署引擎
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} versionId 路径参数versionId
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiSuccess {Object} data
     * @apiSuccess {Number} data.status 部署结果：1成功，0失败
     * @apiSuccess {String} data.msg 部署结果提示信息
     * @apiParamExample {json} 请求示例：
     * {无请求体}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/deploy/{versionId}", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.ENGINDE_DEPLOY)
    public ResponseEntityDto<Object> deployEngineVersion(@PathVariable Long versionId) {

        int count = engineVersionService.deployEngine(versionId);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("versionId", versionId);
        if (count == 1) {
            resultMap.put("status", EngineMsg.STATUS_SUCCESS);
            resultMap.put("msg", EngineMsg.DEPLOY_SUCCESS);
        } else {
            resultMap.put("status", EngineMsg.STATUS_FAILED);
            resultMap.put("msg", EngineMsg.DEPLOY_FAILED);
        }
        return ResponseEntityBuilder.buildNormalResponse(resultMap);
    }

    @RequestMapping(value = "/applyDeploy/{versionId}", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.ENGINDE_DEPLOY)
    public ResponseEntityDto<Object> applyDeployEngineVersion(@PathVariable Long versionId) {

        Map<String,Object> map = engineVersionService.applyDeployEngine(versionId);
        if (map!=null&&!map.isEmpty()){
            return ResponseEntityBuilder.buildNormalResponse(map);
        }else {
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.PARAMS_EXCEPTION);
        }

    }

    /**
     * @api {POST} /v2/engineVersion/undeploy/{versionId} 7.50.2. 根据versionId停用部署的引擎
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} versionId 路径参数versionId
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiSuccess {Object} data
     * @apiSuccess {Number} data.status 停用部署结果：1已停用，0停用失败
     * @apiSuccess {String} data.msg 停用部署结果提示信息
     * @apiParamExample {json} 请求示例：
     * {无请求体}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/undeploy/{versionId}", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.ENGINDE_UNDEPLOY)
    public ResponseEntityDto<Object> undeployEngineVersion(@PathVariable Long versionId) {

        int count = engineVersionService.undeployEngine(versionId);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("versionId", versionId);
        if (count == 1) {
            resultMap.put("status", EngineMsg.STATUS_SUCCESS);
            resultMap.put("msg", EngineMsg.UNDEPLOY_SUCCESS);
        } else {
            resultMap.put("status", EngineMsg.STATUS_FAILED);
            resultMap.put("msg", EngineMsg.UNDEPLOY_FAILED);
        }
        return ResponseEntityBuilder.buildNormalResponse(resultMap);
    }

    /**
     * @api {POST} /v2/engineVersion/nodeList/{versionId} 7.66.20. 根据版本编号获取节点列表（这个接口没什么用）
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} versionId 路径参数versionId
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * {无请求体}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/nodeList/{versionId}", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getEngineNodeListByVerionId(@PathVariable Long versionId) {

        List<EngineNode> nodeList = engineNodeService.getEngineNodeListByEngineVersionId(versionId);

        // if (CollectionUtil.isNotNullOrEmpty(nodeList)) {
        //     return ResponseEntityBuilder.buildNormalResponse(nodeList);
        // }
        // return ResponseEntityBuilder.buildNormalResponse(null);

        return ResponseEntityBuilder.buildNormalResponse(nodeList);
    }

    /**
     * @api {POST} /v2/engineVersion/getTypedNodes/{versionId} 7.58. 根据类型获取节点
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} versionId 路径参数versionId
     * @apiParam {JSONArray} types 节点类型
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * [3,7]
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/getTypedNodes/{versionId}", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getEngineNodesByType(@PathVariable Long versionId, @RequestBody List<Integer> types) {
        // @RequestParam Map<String, Object> paramMap,
        // @RequestParam("types[]") List<Integer> types

        // Long versionId = Long.parseLong(paramMap.get("versionId").toString());

        HashMap<String, Object> paramMap = new HashMap<>();

        List<EngineNode> nodeList = engineNodeService.getEngineTypedNodeListByEngineVersionId(versionId, types);
        boolean flag = true;
        boolean flag1 = true;
        if (nodeList != null && nodeList.size() > 0) {
            for (EngineNode engineNode : nodeList) {

                if ((int) engineNode.getNodeType() == 7 && flag) {
                    if (!StringUtil.isBlank(engineNode.getNodeJson())) {
                        JSONArray json = JSONArray.parseArray(engineNode.getNodeJson());
                        if (!StringUtil.isBlank(engineNode.getNextNodes())) {
                            String[] nextNodes = engineNode.getNextNodes().split(",");
                            paramMap.put("sanBoxNode", engineNode);
                            if (json.size() == nextNodes.length) {
                                paramMap.put("sanbox", "1");
                            } else {
                                paramMap.put("sanbox", "0");
                                flag = false;
                            }
                        } else {
                            paramMap.put("sanbox", "0");
                            flag = false;
                        }
                    }
                }

                if ((int) engineNode.getNodeType() == 3 && flag1) {
                    if (!StringUtil.isBlank(engineNode.getNodeJson())) {
                        JSONObject jsonObj = JSONObject.parseObject(engineNode.getNodeJson());
                        JSONArray json = jsonObj.getJSONArray("conditions");
                        if (!StringUtil.isBlank(engineNode.getNextNodes())) {
                            String[] nextNodes = engineNode.getNextNodes().split(",");
                            paramMap.put("groupNode", engineNode);
                            if (json.size() == nextNodes.length) {
                                paramMap.put("group", "1");
                            } else {
                                paramMap.put("group", "0");
                                flag1 = false;
                            }
                        } else {
                            paramMap.put("group", "0");
                            flag1 = false;
                        }
                    }
                }
            }
        } else {
            paramMap.put("sanbox", "-1");
            paramMap.put("group", "-1");
        }
        return ResponseEntityBuilder.buildNormalResponse(paramMap);
    }

    /**
     * @api {POST} /v2/engineVersion/delete/{versionId} 7.66.21. 删除当前版本
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} versionId 路径参数versionId
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例：
     * {无请求体}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {待完善}
     */
    @RequestMapping(value = "/delete/{versionId}", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.DELETE_VERSION)
    public ResponseEntityDto<Object> deleteEngineVersion(@PathVariable Long versionId) {

        EngineVersion engineVersion = engineVersionService.selectByPrimaryKey(versionId);

        Map<String, Object> map = new HashMap<>();
        // 当前版本是否正在运行
        int bootState = engineVersion.getBootState();
        if (EngineConst.BOOT_STATE_DEPLOY == bootState) {
            // 如果正在运行,则提示用户不能删除
            map.put("status", EngineMsg.STATUS_FAILED);
            map.put("msg", EngineMsg.DELETE_RUNNING_FAILED);
        } else {
            // 假删除,是否删除 (0:在回收站中,可恢复,1:正常,2彻底删除)
            engineVersion.setStatus(0);  // 状态改为0
            int count = engineVersionService.update(engineVersion);
            if (count == 1) {
                map.put("status", EngineMsg.STATUS_SUCCESS);
                map.put("msg", EngineMsg.DELETE_VERSION_SUCCESS);
            } else {
                map.put("status", EngineMsg.STATUS_FAILED);
                map.put("msg", EngineMsg.DELETE_VERSION_FAILED);
            }
        }
        return ResponseEntityBuilder.buildNormalResponse(map);
    }

    /**
     * 新增版本
     * 新增分为两种情况,一种是自然地新增，
     * 一种是当前版本正在运行修改的新增
     *
     * @param engineVersion
     * @return
     */
    @RequestMapping("save")
    @ArchivesLog(operationType = OpTypeConst.SAVE_VERSION)
    public Long saveEngineVersion(EngineVersion engineVersion) {
        List<EngineNode> nodeList = new ArrayList<>();
        EngineNode engineNode = new EngineNode();
        engineNode.setNodeX(200d);
        engineNode.setNodeY(200d);
        engineNode.setNodeName("开始");
        engineNode.setNodeType(1);
        engineNode.setNodeOrder(1);
        engineNode.setNodeCode("ND_START");
        engineNode.setParams("{\"arr_linkId\":\"\",\"dataId\":\"-1\",\"url\":\"/Riskmanage/resource/images/decision/start.png\",\"type\":\"1\"}");
        nodeList.add(engineNode);
        Long versionId = engineVersionService.saveEngineVersion(engineVersion, nodeList);
        return versionId;
    }

}
