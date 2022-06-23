package com.fibo.ddp.enginex.riskengine.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.datax.datamanage.FieldService;
import com.fibo.ddp.common.service.enginex.risk.EngineNodeService;
import com.fibo.ddp.common.service.enginex.risk.EngineVersionService;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.service.strategyx.listlibrary.ListDbService;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 黑白名单节点接口
 */
@Controller("listDbNodeControllerV2")
@RequestMapping("v2/bwListNode")
@ResponseBody
public class ListDbNodeController {

	@Autowired
	private ListDbService listDbService;
	@Autowired
	private EngineNodeService engineNodeService;
	@Autowired
	private EngineVersionService engineVersionService;
	@Autowired
	private FieldService fieldService;

	/**
	 * @api {POST} /v2/bwListNode/update 7.73. 黑白名单节点 修改节点信息
	 * @apiGroup zzzzz01
	 * @apiVersion 2.0.0
	 * @apiParam {Number} nodeId 节点编号
	 * @apiParam {Number} versionId 版本编号
	 * @apiParam {Number} nodeType 节点类型：5黑名单，6白名单
	 * @apiParam {Number} nodeOrder 节点顺序
	 * @apiParam {String} nodeName 节点名称
	 * @apiParam {String} nodeCode 节点code
	 * @apiParam {Number} nodeX 节点X轴
	 * @apiParam {Number} nodeY 节点Y轴
	 * @apiParam {String} innerListdbs 内部黑名单id，逗号分隔
	 * @apiParam {String} outerListdbs 外部黑名单id，逗号分隔
	 * @apiParam {String} [nodeJson] 节点json
	 * @apiParam {String} [nodeScript] 节点脚本
	 * @apiParam {String} [params] 节点类型，图标等信息
	 * @apiSuccess {String} status 状态：1成功，0失败
	 * @apiParamExample {json} 请求示例：
	 * {"nodeId":3361,"versionId":454,"nodeType":5,"nodeOrder":13,"nodeName":"黑名单66","nodeCode":"ND_13","nodeX":666.66,"nodeY":666.77,"innerListdbs":"112,115","outerListdbs":""}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":true}
	 */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_NODE)
    public ResponseEntityDto update(@RequestBody HashMap<String, Object> paramMap) {
    	//增加黑白名单库用到的字段绑定，传递engineId和字段id
		Long userId = SessionManager.getLoginAccount().getUserId();
		paramMap.put("userId", userId); //listDbIds
		
		String innerListdbs = String.valueOf(paramMap.get("innerListdbs"));
		String outerListdbs = String.valueOf(paramMap.get("outerListdbs"));
		Object nodeJson = paramMap.get("nodeJson");
		List<Long> listDbIds = new ArrayList<>();
		if (nodeJson!=null){
			JSONObject jsonObject = JSON.parseObject(nodeJson.toString());
			JSONArray listDbList = jsonObject.getJSONArray("listDbList");
			if (listDbList!=null&&listDbList.size()>0){
				for (Object o : listDbList) {
					JSONObject listDbJson = JSON.parseObject(JSON.toJSONString(o));
					Long listDbId = listDbJson.getLong("listDbId");
					if (listDbId!=null){
						listDbIds.add(listDbId);
					}
				}
			}
		}
		String snapshot = String.valueOf(paramMap.get("snapshot"));
		paramMap.put("listDbIds", listDbIds);
		String strFieldIds = listDbService.findFieldsByListDbIds(paramMap);
		Long versionId = Long.valueOf(String.valueOf(paramMap.get("versionId")));
		EngineVersion engineVesion = engineVersionService.selectByPrimaryKey(versionId);
		Long engineId = engineVesion.getEngineId();
		paramMap.put("engineId", engineId);
		paramMap.put("fieldIds", strFieldIds);
		fieldService.bindEngineField(paramMap);
		paramMap.put("snapshot",snapshot);
		engineNodeService.updateNodeSnapshot(paramMap);
		return ResponseEntityBuilder.buildNormalResponse();
    }

}
