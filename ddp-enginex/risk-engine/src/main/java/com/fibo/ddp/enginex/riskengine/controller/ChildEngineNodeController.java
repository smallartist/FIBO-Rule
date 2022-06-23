package com.fibo.ddp.enginex.riskengine.controller;

import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.enginex.risk.Engine;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.risk.request.EngineListParam;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.enginex.risk.EngineNodeService;
import com.fibo.ddp.common.service.enginex.risk.EngineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 子引擎节点接口
 */
@RestController
@RequestMapping("childEngineNode")
public class ChildEngineNodeController {

    @Autowired
    private EngineService engineService;
    @Autowired
    private EngineNodeService engineNodeService;

    /**
     * @api {POST} /childEngineNode/getEngineList 7.81. 子引擎节点 获取引擎列表
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Integer} pageNo 页数
     * @apiParam {Integer} pageSize 每页的条数
     * @apiParam {Long} nodeId 节点id
     * @apiParam {Long} engineId 引擎id
     * @apiParam {String} [searchString] 搜索关键字
     * @apiSuccess {String} status 状态: 1成功, 0失败
     * @apiParamExample {json} 请求示例：
     * {"pageNo":1,"pageSize":5,"nodeId":0,"engineId":220,"searchString":"信用卡"}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {"status":"1","error":"00000000","msg":null,"data":{"pager":{"pageNum":1,"pageSize":5,"size":2,"startRow":1,"endRow":2,"total":2,"pages":1,"list":null,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1,"lastPage":1,"firstPage":1},"engineList":[{"userId":220,"versionCode":"7eb07302-cbf3-4f6e-95b5-075e89b59f4c","name":"信用卡引擎2","description":"信用卡引擎2","status":1,"createDatetime":1610505340000,"updateDatetime":1610505340000,"creator":142,"userId":null,"organId":46,"searchString":null,"engineVersionList":null,"runState":0,"checked":false,"callbackType":1,"callbackUrl":null},{"userId":219,"versionCode":"5bc79716-8c62-4248-bf91-de07f654668a","name":"信用卡引擎1","description":"信用卡引擎1","status":1,"createDatetime":1610505333000,"updateDatetime":1610505333000,"creator":142,"userId":null,"organId":46,"searchString":null,"engineVersionList":null,"runState":0,"checked":false,"callbackType":1,"callbackUrl":null}]}}
     */
    @RequestMapping(value = "getEngineList", method = RequestMethod.POST)
    public ResponseEntityDto getEngineList(@RequestBody EngineListParam param) {
        String searchString = param.getSearchString();
        Long nodeId = param.getNodeId();
        List<Integer> list = new ArrayList<Integer>();
        SysUser user = SessionManager.getLoginAccount();
        Long organId = user.getOrganId();

        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        //查询对应的引擎
        List<Engine> engineList = engineService.getEngineList(organId, searchString, list);

        EngineNode engineNode = engineNodeService.findById(nodeId);
        if (engineNode != null) {
            for (Engine engine : engineList) {
                if (engine.getId().toString().equals(engineNode.getNodeJson())) {
                    engine.setChecked(true);
                    break;
                }
            }
        }

        PageInfo<Engine> pageInfo = new PageInfo<Engine>(engineList);
        pageInfo.setList(null);
        HashMap<String, Object> resultMap = new HashMap();
        resultMap.put("pager", pageInfo);
        resultMap.put("engineList", engineList);
        return ResponseEntityBuilder.buildNormalResponse(resultMap);
    }

}
