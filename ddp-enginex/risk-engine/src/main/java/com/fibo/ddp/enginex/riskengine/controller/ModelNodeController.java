package com.fibo.ddp.enginex.riskengine.controller;

import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.risk.request.ModelListParam;
import com.fibo.ddp.common.model.strategyx.aimodel.MachineLearningModels;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.enginex.risk.EngineNodeService;
import com.fibo.ddp.common.service.strategyx.aimodel.ModelsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * 模型节点接口
 */
@RestController
@RequestMapping("modelNode")
public class ModelNodeController {

    @Autowired
    private ModelsService modelsService;
    @Autowired
    private EngineNodeService engineNodeService;

    /**
     * @api {POST} /modelNode/getModelList 7.91. 模型节点 获取模型列表
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Integer} pageNo 页数
     * @apiParam {Integer} pageSize 每页的条数
     * @apiParam {Long} nodeId 节点id
     * @apiParam {Long} engineId 引擎id
     * @apiParam {String} [searchString] 搜索关键字
     * @apiSuccess {String} status 状态: 1成功, 0失败
     * @apiParamExample {json} 请求示例：
     * {"pageNo":1,"pageSize":5,"nodeId":3247,"engineId":214,"searchString":"模型"}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {"status":"1","error":"00000000","msg":null,"data":{"pager":{"pageNum":1,"pageSize":5,"size":1,"startRow":1,"endRow":1,"total":1,"pages":1,"list":null,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1,"lastPage":1,"firstPage":1},"modelsList":[{"userId":1,"modelName":"老户模型","description":"老户模型描述","modelType":"pmml","fileName":"1614578067937_model_RF.pmml","filePath":"/usr/local/tomcat_riskmanage/webapps/Riskmanage/upload/models/fieldUpload/1614578067937_model_RF.pmml","modelField":"x16,x17,x8,x19,x6,x10,x5,x1,x13,x2,x3,x15,x21,x18,x11,x7,x14,x12,x20","mappingField":"588,590,591,592,593,594,605,606,611,613,610,612,614,615,616,609,617,620,589","status":1,"creator":135,"modifier":135,"organId":46,"createTime":1614578104000,"updateTime":1614578104000,"modelFieldArr":null,"mappingFieldArr":null,"checked":true}]}}
     */
    @RequestMapping(value = "getModelList", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getModelList(@RequestBody ModelListParam param) {
        String searchString = param.getSearchString();
        Long nodeId = param.getNodeId();
        SysUser sysUser = SessionManager.getLoginAccount();
        Integer organId = Integer.valueOf(sysUser.getOrganId().toString());

        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        List<MachineLearningModels> modelsList = modelsService.getModelsListByOrganId(organId, searchString);
        EngineNode engineNode = engineNodeService.findById(nodeId);
        if (engineNode != null) {
            for (MachineLearningModels models : modelsList) {
                if (models.getId().toString().equals(engineNode.getNodeJson())) {
                    models.setChecked(true);
                    break;
                }
            }
        }

        PageInfo<MachineLearningModels> pageInfo = new PageInfo<MachineLearningModels>(modelsList);
        pageInfo.setList(null);
        HashMap<String, Object> modelMap = new HashMap<>();
        modelMap.put("pager", pageInfo);
        modelMap.put("modelsList", modelsList);
        return ResponseEntityBuilder.buildNormalResponse(modelMap);
    }

}
