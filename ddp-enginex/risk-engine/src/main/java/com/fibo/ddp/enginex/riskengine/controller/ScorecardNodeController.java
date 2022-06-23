package com.fibo.ddp.enginex.riskengine.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.strategyx.scorecard.Scorecard;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVersionVo;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVo;
import com.fibo.ddp.common.service.enginex.risk.EngineNodeService;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardService;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardVersionService;
import com.fibo.ddp.common.utils.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("ScorecardNodeControllerV2")
@RequestMapping("/v2/cardNode")
@ResponseBody
public class ScorecardNodeController {

    @Autowired
    private ScorecardVersionService scorecardVersionService;
    @Autowired
    private ScorecardService scorecardService;
    @Autowired
    private EngineNodeService engineNodeService;

    // 决策流
    @RequestMapping("toCardNode")
    public ModelAndView toCardNode() {
        ModelAndView mav = new ModelAndView("decision/decisions");
        return mav;
    }

    /**
     * @api {POST} /v2/cardNode/cardList 7.60. 返回评分卡列表
     * @apiGroup zzzzz01
     * @apiVersion 2.0.0
     * @apiParam {Number} nodeId 节点id，0表示新建的
     * @apiParam {Number} status 1
     * @apiParam {String} [dictKey] 值为"scorecardName"
     * @apiParam {String} [dictValue] 搜索关键字
     * @apiParam {Number} [pageNo=1] 第几页
     * @apiParam {Number} [pageSize=10] 每页的条数
     * @apiParam {Number} [engineId] 引擎id
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例1：
     * {"nodeId":3223,"status":1}
     * @apiParamExample {json} 请求示例2：
     * {"nodeId":3223,"status":1,"dictKey":"scorecardName","dictValue":"客户","pageNo":1,"pageSize":10,"engineId":211}
     * @apiSuccessExample {json} 成功返回数据示例：
     * {"status":"1","error":"00000000","msg":null,"data":{"pager":{"pageNum":1,"pageSize":1,"size":1,"startRow":1,"endRow":1,"total":9,"pages":9,"list":[{"userId":99,"name":"客户评分卡-有盾","versionCode":"SC_test_card2","description":"客户评分卡-有盾值","version":null,"parentId":1076,"userId":135,"author":135,"authorName":"管理员","engineId":211,"type":1,"status":1,"sFieldList":[],"sContentList":[],"created":1520236208000,"updated":1615889385000,"checked":false,"score":"{\"output\":{\"field_id\":0,\"field_code\":\"score\",\"field_name\":\"信用得分\",\"field_type\":1},\"formula\":\"300+#{f_yd_realNameScore}\",\"formula_show\":\"300+@有盾实名认证分值@\",\"fields\":[{\"field_id\":642,\"field_code\":\"f_yd_realNameScore\",\"field_type\":1,\"field_name\":\"有盾实名认证分值\",\"segments\":[{\"segment\":\"(,29]\",\"dictValue\":0},{\"segment\":\"(29,69]\",\"dictValue\":50},{\"segment\":\"(69,)\",\"dictValue\":100}]}]}","pd":null,"odds":null,"engineName":null,"organId":46,"sRuleContentList":[]}],"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2,3,4,5,6,7,8],"navigateFirstPage":1,"navigateLastPage":8,"firstPage":1,"lastPage":8},"keySearch":"客户","list":[{"userId":99,"name":"客户评分卡-有盾","versionCode":"SC_test_card2","description":"客户评分卡-有盾值","version":null,"parentId":1076,"userId":135,"author":135,"authorName":"管理员","engineId":211,"type":1,"status":1,"sFieldList":[],"sContentList":[],"created":1520236208000,"updated":1615889385000,"checked":false,"score":"{\"output\":{\"field_id\":0,\"field_code\":\"score\",\"field_name\":\"信用得分\",\"field_type\":1},\"formula\":\"300+#{f_yd_realNameScore}\",\"formula_show\":\"300+@有盾实名认证分值@\",\"fields\":[{\"field_id\":642,\"field_code\":\"f_yd_realNameScore\",\"field_type\":1,\"field_name\":\"有盾实名认证分值\",\"segments\":[{\"segment\":\"(,29]\",\"dictValue\":0},{\"segment\":\"(29,69]\",\"dictValue\":50},{\"segment\":\"(69,)\",\"dictValue\":100}]}]}","pd":null,"odds":null,"engineName":null,"organId":46,"sRuleContentList":[]}]}}
     */
    @RequestMapping(value = "/cardList", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getCardNode(@RequestBody Map<String, Object> param) {


        Long nodeId = Long.parseLong(param.get("nodeId").toString());
        Integer pageNo = param.get("pageNo") == null ? 1 : Integer.parseInt(param.get("pageNo").toString());
        Integer pageSize = param.get("pageSize") == null ? 10 : Integer.parseInt(param.get("pageSize").toString());

        String keySearch = "1";
        param.put("status", StringUtil.toLongList(param.get("status").toString()));
        PageHelper.startPage(pageNo, pageSize);
        List<Scorecard> list = scorecardService.getScorecardList(param);

        // 回显
        if (nodeId != null && nodeId != 0) {
            EngineNode byId = engineNodeService.findById(nodeId);
            if (byId != null) {
                //获取评分卡id；
                //long knowledgeId = nodeKnowledge.getKnowledgeId();//
                if (StringUtils.isNotBlank(byId.getNodeJson())) {
                    //  && !"".equals(byId.getNodeJson())
//                    long knowledgeId = Long.valueOf(byId.getNodeJson());
                    long knowledgeId = 0;
                    if(byId.getNodeJson()!=null&&!byId.getNodeJson().contains("{")){
                        knowledgeId = StringUtil.getStrToLong(byId.getNodeJson());
                    }
                    //原来存储id现在被转换为存储的是json
                    Map<Long,Long> scorecardIdMap = new HashMap();
                    if (knowledgeId == 0) {
                        try {
                            JSONObject jsonObject = JSON.parseObject(byId.getNodeJson());
                            JSONArray scorecardList = jsonObject.getJSONArray("scorecardList");
                            if (scorecardList!=null&&!scorecardList.isEmpty()){
                                for (int i = 0; i < scorecardList.size(); i++) {
                                    JSONObject scorecard = scorecardList.getJSONObject(i);
                                    scorecardIdMap.put(scorecard.getLong("scorecardId"),scorecard.getLong("versionId"));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        ScorecardVersionVo version = scorecardVersionService.queryById(knowledgeId);
                        if (version!=null){
                            scorecardIdMap.put(version.getScorecardId(),version.getId());
                        }
                    }
                    //判断并回显
                    if (scorecardIdMap != null&& !scorecardIdMap.isEmpty() && list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            Long cardId = list.get(i).getId();
                            if (scorecardIdMap.containsKey(cardId)) {
                                ScorecardVo scorecardInfo = scorecardService.getScorecardInfo(cardId);
                                scorecardInfo.setChecked(true);
                                scorecardInfo.setCheckedId(scorecardIdMap.get(cardId));
                                list.set(i, scorecardInfo);
                            }
                        }
                    }
                }
            }
        }

        PageInfo<Scorecard> pageInfo = new PageInfo<>(list);

        if (param.containsKey("dictValue")) {
            keySearch = param.get("dictValue").toString();
        }

        HashMap<String, Object> modelMap = new HashMap<>();
        modelMap.put("list", list);
        modelMap.put("pager", pageInfo);
        modelMap.put("keySearch", keySearch);
        return ResponseEntityBuilder.buildNormalResponse(modelMap);
    }
}
