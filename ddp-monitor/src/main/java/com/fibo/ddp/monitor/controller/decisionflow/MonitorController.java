package com.fibo.ddp.monitor.controller.decisionflow;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.enginex.risk.EngineResultSet;
import com.fibo.ddp.common.model.monitor.decisionflow.DecisionFlowRequestDTO;
import com.fibo.ddp.common.utils.constant.monitor.MonitorStorageType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @apiDefine  决策流运行轨迹
 */
@RestController
@RequestMapping("/v2/monitor")
public class MonitorController {

    private static final Logger logger = LoggerFactory.getLogger(MonitorController.class);

    /**
     * @api {POST} /v2/monitor/results 8.10.01. 结果集 页面内容 list
     * @apiVersion 2.0.0
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例  * @apiParam {Number} engineId 引擎id
     * @apiParam {String} [startDate] 开始时间，示例："2021-03-26 23:59:59" 或 "2020-03-26"
     * @apiParam {String} [endDate] 结束时间，示例："2021-03-26 23:59:59" 或 "2020-03-26"
     * @apiParam {String} [businessId] 业务ID
     * @apiParam {Number} [pageNo=1] 第几页
     * @apiParam {Number} [pageSize=10] 每页的条数
     * {"engineId":213,"startDate":"2018-03-26","endDate":"2021-03-26 23:59:59","pageNo":1,"pageSize":10}
     * @apiSuccessExample {json} 成功返回数据示例
     * {"status":"1","error":"00000000","msg":null,"data":{"engineId":213,"startDate":"2018-03-26","endDate":"2021-03-26 23:59:59","pageNo":1,"pageSize":10,"pager":{"pageNum":1,"pageSize":10,"size":10,"startRow":1,"endRow":10,"total":839,"pages":84,"list":[{"id":888,"uid":"15662010322","pid":"10001","input":"{\"queryKey\":\"t0 = '370983'\",\"schemaName\":\"riskmanage\",\"f_mx_contacts_class1_blacklist_cnt\":1,\"f_mx_dial_cnt\":94,\"tableName\":\"organ_46_b_115\",\"f_ms_mac_num\":0,\"f_mx_contact_loan\":1,\"revQueryKey\":\"\",\"ND_10_3020\":\"1\",\"f_ms_imsi_num\":0,\"f_ms_id_card\":\"370983\",\"f_mx_idcard_with_other_names\":0,\"f_mx_phone_with_other_idcards\":0,\"f_mx_score\":578,\"f_mx_roam_day_cnt_3m\":0,\"f_ms_present_address\":\"山东省泰安市岱岳区\",\"f_ms_hit_address_black_list\":0,\"f_mx_second_trans_start_end_time\":180,\"f_mx_phone_with_other_names\":0,\"f_mx_name_match\":1,\"f_ms_contact_abnormal_phone_rate\":0.0,\"f_mx_first_trans_start_end_time\":180,\"f_mx_idcard_match\":1,\"f_ms_contact_abnormal_name_length_rate\":0.13,\"f_ms_imei_num\":0,\"nodeId\":2959,\"f_ms_idfa_num\":0,\"nodeName\":\"黑名单_2\",\"f_ms_uuid_num\":0,\"f_mx_contact_bank\":3,\"f_ms_iswhitelist\":0,\"f_mx_phone_used_time\":76,\"f_ms_isolduser\":0,\"f_mx_contacts_router_ratio\":0.03,\"f_mx_reliability\":1,\"f_mx_is_name_and_idcard_in_court_black\":0,\"f_mx_dialed_cnt\":231,\"f_mx_contact_credit_card\":1,\"f_ms_contact_same_province_rate\":0.0,\"f_ms_contact_abnormal_name_rate\":0.0,\"f_mx_is_name_and_idcard_in_finance_black\":0,\"f_mx_call_cnt_rata\":108.33,\"f_ms_contact_num\":227,\"listDb\":{\"id\":115,\"listDesc\":\"户籍地限制区域\",\"listName\":\"身份证户籍地_黑名单\",\"listType\":\"b\",\"matchType\":1,\"page\":0,\"queryField\":\"698\",\"queryType\":0,\"rows\":0},\"f_ms_age\":29,\"f_ms_hit_black_list\":0,\"f_mx_is_name_and_mobile_in_finance_black\":0,\"f_ms_hit_black_device_list\":0,\"f_ms_present_address_detail\":\"泺亨国际一号楼一单元1801\"}","output":null,"create_datetime":1561023271000,"result":"1","engine_id":213,"engine_version":null,"uuid":null,"engine_name":"准入引擎","engine_code":"8bff0e95-3ba9-4b59-b9a0-8e11b6a830fa","startDate":null,"endDate":null,"type":2,"subVersion":0,"scorecardscore":null,"datilResult":"决策选项_10:1","batchNo":null,"startTime":null,"costTime":null,"resultSetList":[]},{"id":886,"uid":"15878591521","pid":"10001","input":"{\"nodeName\":\"黑名单_2\",\"queryKey\":\"t0 = '450981'\",\"f_ms_uuid_num\":0,\"f_mx_contact_bank\":2,\"f_ms_iswhitelist\":0,\"f_mx_phone_used_time\":63,\"f_ms_isolduser\":0,\"schemaName\":\"riskmanage\",\"f_mx_contacts_class1_blacklist_cnt\":0,\"f_mx_dial_cnt\":79,\"tableName\":\"organ_46_b_115\",\"f_mx_contacts_router_ratio\":0.0,\"f_ms_mac_num\":0,\"f_mx_reliability\":1,\"f_mx_contact_loan\":0,\"revQueryKey\":\"\",\"f_mx_is_name_and_idcard_in_court_black\":0,\"f_mx_dialed_cnt\":367,\"f_mx_contact_credit_card\":0,\"f_ms_contact_same_province_rate\":0.0,\"f_ms_imsi_num\":0,\"f_ms_id_card\":\"450981\",\"f_mx_idcard_with_other_names\":0,\"f_ms_contact_abnormal_name_rate\":0.0,\"f_mx_phone_with_other_idcards\":0,\"f_mx_roam_day_cnt_3m\":0,\"f_ms_present_address\":\"广西玉林市北流市\",\"f_mx_is_name_and_idcard_in_finance_black\":0,\"f_ms_hit_address_black_list\":0,\"f_mx_second_trans_start_end_time\":0,\"f_mx_phone_with_other_names\":0,\"f_mx_name_match\":1,\"f_ms_contact_abnormal_phone_rate\":0.0,\"f_mx_call_cnt_rata\":148.67,\"f_ms_contact_num\":40,\"f_mx_first_trans_start_end_time\":174,\"f_mx_idcard_match\":1,\"listDb\":{\"id\":115,\"listDesc\":\"户籍地限制区域\",\"listName\":\"身份证户籍地_黑名单\",\"listType\":\"b\",\"matchType\":1,\"page\":0,\"queryField\":\"698\",\"queryType\":0,\"rows\":0},\"f_ms_age\":24,\"f_ms_hit_black_list\":0,\"f_mx_is_name_and_mobile_in_finance_black\":0,\"f_ms_contact_abnormal_name_length_rate\":0.05,\"f_ms_imei_num\":0,\"f_ms_hit_black_device_list\":0,\"nodeId\":2959,\"f_ms_idfa_num\":0,\"f_ms_present_address_detail\":\"新城国际69栋二单元502\"}","output":null,"create_datetime":1561023270000,"result":"拒绝","engine_id":213,"engine_version":null,"uuid":null,"engine_name":"准入引擎","engine_code":"8bff0e95-3ba9-4b59-b9a0-8e11b6a830fa","startDate":null,"endDate":null,"type":2,"subVersion":0,"scorecardscore":null,"datilResult":null,"batchNo":null,"startTime":null,"costTime":null,"resultSetList":[]},{"id":887,"uid":"13359659815","pid":"10001","input":"{\"queryKey\":\"t0 = '230521'\",\"schemaName\":\"riskmanage\",\"f_mx_contacts_class1_blacklist_cnt\":0,\"f_mx_dial_cnt\":716,\"tableName\":\"organ_46_b_115\",\"f_ms_mac_num\":0,\"f_mx_contact_loan\":0,\"revQueryKey\":\"\",\"ND_10_3020\":\"3\",\"f_ms_imsi_num\":0,\"f_ms_id_card\":\"230521\",\"f_mx_idcard_with_other_names\":0,\"f_mx_phone_with_other_idcards\":0,\"f_mx_score\":323,\"f_mx_roam_day_cnt_3m\":0,\"f_ms_present_address\":\"黑龙江省双鸭山集贤县\",\"f_ms_hit_address_black_list\":0,\"f_mx_second_trans_start_end_time\":171,\"f_mx_phone_with_other_names\":0,\"f_mx_name_match\":1,\"f_ms_contact_abnormal_phone_rate\":0.0,\"f_mx_first_trans_start_end_time\":133,\"f_mx_idcard_match\":1,\"f_ms_contact_abnormal_name_length_rate\":0.19,\"f_ms_imei_num\":0,\"nodeId\":2959,\"f_ms_idfa_num\":0,\"nodeName\":\"黑名单_2\",\"f_ms_uuid_num\":0,\"f_mx_contact_bank\":0,\"f_ms_iswhitelist\":0,\"f_mx_phone_used_time\":67,\"f_ms_isolduser\":0,\"f_mx_contacts_router_ratio\":0.02,\"f_mx_reliability\":1,\"f_mx_is_name_and_idcard_in_court_black\":0,\"f_mx_dialed_cnt\":426,\"f_mx_contact_credit_card\":0,\"f_ms_contact_same_province_rate\":0.0,\"f_ms_contact_abnormal_name_rate\":0.0,\"f_mx_is_name_and_idcard_in_finance_black\":0,\"f_mx_call_cnt_rata\":380.67,\"f_ms_contact_num\":121,\"listDb\":{\"id\":115,\"listDesc\":\"户籍地限制区域\",\"listName\":\"身份证户籍地_黑名单\",\"listType\":\"b\",\"matchType\":1,\"page\":0,\"queryField\":\"698\",\"queryType\":0,\"rows\":0},\"f_ms_age\":31,\"f_ms_hit_black_list\":0,\"f_mx_is_name_and_mobile_in_finance_black\":0,\"f_ms_hit_black_device_list\":0,\"f_ms_present_address_detail\":\"福利镇繁荣街征港48号\"}","output":null,"create_datetime":1561023270000,"result":"3","engine_id":213,"engine_version":null,"uuid":null,"engine_name":"准入引擎","engine_code":"8bff0e95-3ba9-4b59-b9a0-8e11b6a830fa","startDate":null,"endDate":null,"type":2,"subVersion":0,"scorecardscore":null,"datilResult":"决策选项_10:3","batchNo":null,"startTime":null,"costTime":null,"resultSetList":[]}],"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2,3,4,5,6,7,8],"navigateFirstPage":1,"navigateLastPage":8,"firstPage":1,"lastPage":8}}}
     */
    @RequestMapping(value = "/results", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getResults(@RequestBody Map<String, Object> param) {
        logger.info("/v2/monitor//results inputParam :{}",param);
        Integer pageNo = param.get("pageNo") == null ? 1 : Integer.valueOf(param.get("pageNo").toString());
        Integer pageSize = param.get("pageSize") == null ? 10 : Integer.valueOf(param.get("pageSize").toString());
        PageHelper.startPage(pageNo, pageSize);
        List<EngineResultSet> resultSets = MonitorCenterFactory.getMonitorCenterServiceImp(MonitorStorageType.HBase).getEngineResultSetBySegment(param);
        PageInfo<EngineResultSet> pageInfo = new PageInfo<>(resultSets);
        HashMap<String, Object> modelMap = new HashMap<>();
        modelMap.put("pager", pageInfo);
        modelMap.putAll(param);
        return ResponseEntityBuilder.buildNormalResponse(modelMap);
    }


    /**
     * @api {POST} /v2/monitor/decisionFlow 8.10.01. 查看决策流某条对应的详情（全部节点树以及执行过的节点轨迹）
     * @apiVersion 2.0.0
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例  * @apiParam {hbaseRowKey} hbase行键 行键
     * @apiParam {String} [hbaseRowKey] hbase行键，示例：'09223370413793177785'
     * {"hbaseRowKey":"09223370413793177785"}
     * @apiSuccessExample {json} 成功返回数据示例
     *"status":"1","error":"00000000","msg":null,"data":[{"rowKey":3474863778681796400,"monitorInfo":{"params":{"99":10,"f_hr_age":100,"4_3851_113_score":1,"vip1_user_num":100,"test1":1,"4_3861_114_score":10},"snapshot":[{"nodeId":3483,"parentId":null,"versionId":507,"nodeName":"开始","nodeCode":"ND_START","nodeOrder":1,"nodeType":1,"nodeX":-981,"nodeY":-352,"nodeScript":null,"nodeJson":null,"params":"{\"arr_linkId\":\"\",\"dataId\":\"-1\",\"url\":\"/Riskmanage/resource/images/decision/start.png\",\"type\":\"1\"}","nextNodes":"ND_7","cardId":null,"ruleList":null,"lastNextnode":null},{"nodeId":3851,"parentId":"3483","versionId":507,"nodeName":"评分卡","nodeCode":"ND_7","nodeOrder":7,"nodeType":4,"nodeX":-807.45,"nodeY":-354.3,"nodeScript":null,"nodeJson":"113","params":"{\"dataId\":5,\"url\":\"/Riskmanage/resource/images/decision/createScoreLevel.png\",\"type\":4}","nextNodes":"ND_11","cardId":null,"ruleList":null,"lastNextnode":null},{"nodeId":3861,"parentId":"3851","versionId":507,"nodeName":"评分卡","nodeCode":"ND_11","nodeOrder":11,"nodeType":4,"nodeX":-591.5,"nodeY":-355.32,"nodeScript":null,"nodeJson":"114","params":"{\"dataId\":5,\"url\":\"/Riskmanage/resource/images/decision/createScoreLevel.png\",\"type\":4}","nextNodes":null,"cardId":null,"ruleList":null,"lastNextnode":null}],"process":["3851","3861"]},"baseInfo":{"businessId":"\"113313131\"","engineName":"\"客户经理评分卡\"","engineVersionId":"\"507\"","engineInfo":null}}]
     * */
    @RequestMapping(value = "/decisionFlow", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getResultDecisionFlowDetail(@RequestBody DecisionFlowRequestDTO param) {
        logger.info("/v2/monitor/decisionFlow inputParam :{}",param);
        List<?> resultSets =  MonitorCenterFactory.getMonitorCenterServiceImp(MonitorStorageType.HBase).getResultDecisionFlowDetail(param);
        return ResponseEntityBuilder.buildNormalResponse(resultSets);
    }

    @RequestMapping(value = "/decisionFlowMysql", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getResultDecisionFlowMysqlDetail(@RequestBody DecisionFlowRequestDTO param) {
        logger.info("/v2/monitor/decisionFlowMysql inputParam :{}",param);
        List<?> resultSets =  MonitorCenterFactory.getMonitorCenterServiceImp(MonitorStorageType.Mysql).getResultDecisionFlowDetail(param);
        return ResponseEntityBuilder.buildNormalResponse(resultSets);
    }


    /**
     * @api {POST} /v2/monitor/node 8.10.01. 查看节点配置详情（节点下配置等）
     * @apiVersion 2.0.0
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例  * @apiParam {hbaseRowKey} hbase行键 行键
     * @apiParam {String} [hbaseRowKey] hbase行键，示例：'09223370413793177785'
     * {"hbaseRowKey":"09223370413793177785"}
     * @apiSuccessExample {json} 成功返回数据示例
     * */
    @RequestMapping(value = "/node", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getResultNodeDetail(@RequestBody DecisionFlowRequestDTO param) {
        logger.info("/v2/monitor/node inputParam :{}",param);
        List<?> resultSets =  MonitorCenterFactory.getMonitorCenterServiceImp(MonitorStorageType.HBase).getResultNodeDetail(param);
        return ResponseEntityBuilder.buildNormalResponse(resultSets);
    }

    @RequestMapping(value = "/nodeMysql", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getResultNodeMysqlDetail(@RequestBody DecisionFlowRequestDTO param) {
        logger.info("/v2/monitor/nodeMysql inputParam :{}",param);
        List<?> resultSets  = MonitorCenterFactory.getMonitorCenterServiceImp(MonitorStorageType.Mysql).getResultNodeDetail(param);
        return ResponseEntityBuilder.buildNormalResponse(resultSets);
    }
    /**
     * @api {POST} /v2/monitor/strategy 8.10.01. 查看策略配置详情（具体策略下配置等）
     * @apiVersion 2.0.0
     * @apiSuccess {String} status 状态：1成功，0失败
     * @apiParamExample {json} 请求示例  * @apiParam {hbaseRowKey} hbase行键 行键
     * @apiParam {String} [hbaseRowKey] hbase行键，示例：'09223370413793177785'
     * {"hbaseRowKey":"09223370413793177785"}
     * @apiSuccessExample {json} 成功返回数据示例
     * */
    @RequestMapping(value = "/strategy", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getResultStrategyDetail(@RequestBody DecisionFlowRequestDTO param) {
        logger.info("/v2/monitor/strategy inputParam :{}",param);
        List<?> resultSets  =  MonitorCenterFactory.getMonitorCenterServiceImp(MonitorStorageType.HBase).getResultStrategyDetail(param);
        return ResponseEntityBuilder.buildNormalResponse(resultSets!=null?resultSets.get(0):"");
    }
    @RequestMapping(value = "/strategyMysql", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getResultStrategyMysqlDetail(@RequestBody DecisionFlowRequestDTO param) {
        logger.info("/v2/monitor/strategyMysql inputParam :{}",param);
        List<?> resultSets = MonitorCenterFactory.getMonitorCenterServiceImp(MonitorStorageType.Mysql).getResultStrategyDetail(param);
        return ResponseEntityBuilder.buildNormalResponse(resultSets);
    }
}
