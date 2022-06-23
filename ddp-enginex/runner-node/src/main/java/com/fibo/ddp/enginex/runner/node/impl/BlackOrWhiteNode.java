package com.fibo.ddp.enginex.runner.node.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.dao.datax.datamanage.CustListMapper;
import com.fibo.ddp.common.dao.datax.datamanage.FieldMapper;
import com.fibo.ddp.common.dao.strategyx.listlibrary.TblColumnMapper;
import com.fibo.ddp.common.model.datax.datamanage.Field;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.strategyx.listlibrary.ListDb;
import com.fibo.ddp.common.model.strategyx.listlibrary.TblColumn;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import com.fibo.ddp.common.service.common.runner.RunnerSessionManager;
import com.fibo.ddp.common.service.common.runner.SessionData;
import com.fibo.ddp.common.service.datax.runner.CommonService;
import com.fibo.ddp.common.service.datax.runner.ExecuteUtils;
import com.fibo.ddp.common.service.strategyx.listlibrary.ListDbService;
import com.fibo.ddp.common.service.strategyx.strategyout.StrategyOutputService;
import com.fibo.ddp.common.utils.constant.strategyx.StrategyType;
import com.fibo.ddp.enginex.runner.node.EngineRunnerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class BlackOrWhiteNode implements EngineRunnerNode {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommonService commonService;
    @Autowired
    private TblColumnMapper tblColumnMapper;
    @Resource
    private ListDbService listDbService;
    @Resource
    public FieldMapper fieldMapper;
    @Resource
    public CustListMapper custListMapper;
    @Resource
    private StrategyOutputService outputService;

    private List<Long> getExecuteVersionIdList(EngineNode engineNode) {
        return ExecuteUtils.getExecuteIdList(engineNode, "listDbId");
    }

    @Override
    public void getNodeField(EngineNode engineNode, Map<String, Object> inputParam) {
        List<Long> list = getExecuteVersionIdList(engineNode);
        List<Long> fieldIds = new ArrayList<>();
        for (Long l : list) {
            fieldIds.addAll(listDbService.getNodeFieldIds(l));
        }
        commonService.getFieldByIds(fieldIds, inputParam);

    }

    @Override
    public void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap) {
        //监控中心-节点信息快照记录
        if(engineNode!=null && engineNode.getNodeJson()!=null){
            outMap.put("nodeSnapshot",engineNode.getNodeJson());
        }
        inputParam.put("nodeId", engineNode.getNodeId());
        inputParam.put("nodeName", engineNode.getNodeName());
        JSONObject nodeInfo = new JSONObject();
        nodeInfo.put("engineNode", engineNode);
        nodeInfo.put("nodeId", engineNode.getNodeId());
        nodeInfo.put("nodeName", engineNode.getNodeName());
        nodeInfo.put("nodeType", engineNode.getNodeType());
        outMap.put("nodeInfo", nodeInfo);
        //新代码
        String hitKey = "" + engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_size";
        List<Long> list = getExecuteVersionIdList(engineNode);
        List<ListDb> hitListDb = new ArrayList<>();
        //创建内部多选的名单库结果数组
        JSONArray resultJsonArray =  new JSONArray();
        JSONArray strategySnapshot = new JSONArray();
        for (Long id : list) {
            ListDb listDb = listDbService.queryById(id);
            //监控中心 == 策略层面快照信息
            if (listDb != null && listDb.getSnapshot() != null) {
                strategySnapshot.add(listDb.getSnapshot());
            }
            boolean isfalg = this.executeListDb(listDb, inputParam, outMap,resultJsonArray);
            if (isfalg) {
                hitListDb.add(listDb);
            }
        }
        //监控中心==》策略层面快照信息记录
        outMap.put("strategySnapshot",strategySnapshot);
        //构造节点信息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nodeId", engineNode.getNodeId());
        jsonObject.put("nodeName", engineNode.getNodeName());
        jsonObject.put("resultList", resultJsonArray);

        //判断是否出现过同类型的，如果出现过则获取并且增加内容，如果没有则需要构建名单库类型的节点信息
        if (outMap.containsKey("blackJson")) {
            //如果出现过黑名单则从黑名单接送中获取
            JSONObject blackJson = (JSONObject) outMap.get("blackJson");
            JSONArray resultJson = blackJson.getJSONArray("resultJson");
            resultJson.add(jsonObject);
        } else {
            JSONObject nodeResult = new JSONObject();
            nodeResult.put("resultType", 5);
            JSONArray resultJson = new JSONArray();
            resultJson.add(jsonObject);
            nodeResult.put("resultJson",resultJson);
            outMap.put("blackJson", nodeResult);
        }

//        nodeResult.put("resultJson", resultJsonArray);
//        outMap.put("blackJson", nodeResult);
//        outMap.put("nodeResult", nodeResult);
        inputParam.put(hitKey, hitListDb.size());
        //处理终止条件判断中选择的名单库
        long count = handlerResultCount(engineNode, hitListDb);
        this.terminalCondition(engineNode, inputParam, outMap, count);
    }

    private boolean executeListDb(ListDb listDb, Map<String, Object> inputParam, Map<String, Object> outMap,JSONArray resultJsonArray) {
        SessionData sessionData = RunnerSessionManager.getSession();
        Long organId = sessionData.getOrganId();
        JSONObject resultJson = new JSONObject();
        Integer matchs = 0;
        Integer revMatchs = 0; //模糊查询时反向匹配
//        JSONArray strategySnopshot = new JSONArray();
        Long listDbId = listDb.getId();
//        if (listDb.getSnapshot() != null) {
//
//            strategySnopshot.add(listDb.getSnapshot());
//        }
        inputParam.put("listDb", listDb);

        ListDb version = listDb;
        String queryKeyArray[] = listDb.getQueryField().split(",");
        if (queryKeyArray.length > 0) {
            Integer queryType = version.getQueryType();//and（1），or（0）
            Integer matchType = version.getMatchType();//精确匹配（1），模糊匹配（0）
            String queryKey = ""; // t1 like '%t1%'
            String revQueryKey = ""; // 反向模糊匹配 instr('高档洗浴消费',t1) t1行业字段 eg.'洗浴'
            String tableName = "organ" + "_" + organId + "_" + listDb.getListType() + "_" + listDbId;
            inputParam.put("tableName", tableName);
            inputParam.put("schemaName", getDbName());

            //获取名单库的匿名字段与注释的关系
            List<TblColumn> columnList = tblColumnMapper.getColumnList(inputParam);

            //字段id转匿名字段名，准备待查字段条件
            Integer loc = 0;
            List<String> tableColumn = Arrays.asList(version.getTableColumn().split(","));
            int k = 0;
            for (int j = 0; j < queryKeyArray.length; j++) {
                k = tableColumn.indexOf(queryKeyArray[j]);
                if (k < 0) {
                    continue;
                }
                Field field = fieldMapper.selectById(queryKeyArray[j]);
                String fieldEn = field.getFieldEn(); //age
                String columnKey = "t" + k;
                for (TblColumn tblColumn : columnList) {
                    String colName = tblColumn.getColName(); //t5
                    String paramValue = inputParam.get(fieldEn).toString();
                    if (columnKey.equals(colName)) {
                        if (paramValue == null || paramValue.equals("")) {
                            continue ; //数据项缺失导致无法命中，默认返回没命中
                        } else {
                            loc += 1;
                            if (matchType == 1) {
                                if (loc > 1 && queryType == 1) {
                                    queryKey += " and ";
                                } else if (loc > 1 && queryType == 0) {
                                    queryKey += " or ";
                                }
                                queryKey += colName + " = '" + paramValue + "'";
                            } else if (matchType == 0) { //模糊匹配
                                if (loc > 1 && queryType == 1) {
                                    queryKey += " and ";
                                } else if (loc > 1 && queryType == 0) {
                                    queryKey += " or ";
                                    revQueryKey += " + ";
                                }
                                //正向模糊搜索
                                queryKey += colName + " like " + "'%" + paramValue + "%'"; // t5 like '%36岁%'
                                //反向模糊搜索
                                revQueryKey += "max(instr('" + paramValue + "'," + colName + "))";
                            }
                        }
                    }
                }
            }
            inputParam.put("queryKey", queryKey);
            inputParam.put("revQueryKey", revQueryKey);
        }
        matchs += custListMapper.findByQueryKey(inputParam);
        if (!"".equals(inputParam.get("revQueryKey"))) {
            revMatchs = custListMapper.revFindByQueryKey(inputParam);
        }
        if (revMatchs == null){
            revMatchs = 0;
        }
        inputParam.put(listDb.getResultFieldEn(), "未命中");
        List<JSONObject> fieldList = new ArrayList<>();

        JSONObject hitResult = new JSONObject();
        hitResult.put(listDb.getResultFieldEn(), "未命中");

//        resultJson.put("nodeId", inputParam.get("nodeId").toString());
//        resultJson.put("nodeName", inputParam.get("nodeName").toString());
        resultJson.put("listDbId", listDb.getId());
        resultJson.put("listDbName", listDb.getListName());
        resultJson.put("listDbType", listDb.getListType());
        if (null != listDb.getListDesc()) {
            resultJson.put("desc", listDb.getListDesc());
        }
        resultJson.put("fieldList", fieldList);
        resultJsonArray.add(resultJson);
        if (matchs + revMatchs > 0) {
            inputParam.put(listDb.getResultFieldEn(), "命中");
            hitResult.put(listDb.getResultFieldEn(), "命中");
            List<JSONObject> jsonObjects = outputService.setOutput(new StrategyOutput(Long.valueOf(listDbId.toString()), StrategyType.LIST_DB), inputParam);
            fieldList.add(hitResult);
            fieldList.addAll(jsonObjects);
//            outMap.put("black", listDb);
            return true;
        } else {
            fieldList.add(hitResult);
        }

        //监控中心==》策略层面记录策略的快照信息
//        JSONObject strategyObject = new JSONObject();
//        strategyObject.put("strategySnopshot", strategySnopshot);
//        outMap.put("strategySnopshot", strategyObject);
        return false;
    }

    /**
     * 根据传入数据监测是否命中黑名单
     *
     * @param paramMap
     * @return
     * @see
     */
    public boolean findByQueryKey(Map<String, Object> paramMap, Map<String, Object> outmap, Integer type, EngineNode engineNode) {


        return false;
    }


    private String getDbName() {
        return "riskmanage";
    }

    private long handlerResultCount(EngineNode engineNode,List<ListDb> hitListDb){
        JSONObject jsonObject = JSON.parseObject(engineNode.getNodeJson());
        JSONArray selectedRule = jsonObject.getJSONObject("terminationInfo").getJSONArray("selectedRule");
        List<Long> selectedListDbIds = new ArrayList<>();
        if (selectedRule!=null&&selectedRule.size()>0){
            for (Object o : selectedRule) {
                if (o==null){
                    continue;
                }
                Long id = JSON.parseObject(JSON.toJSONString(o)).getLong("id");
                if (id==null){
                    continue;
                }
                selectedListDbIds.add(id);
            }
        }
        long count = hitListDb.stream().filter(item -> selectedListDbIds.contains(item.getId())).count();
        return count;
    }

    private void terminalCondition(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap, Object executeResult) {
        String sizeKey = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_terminal_size";
        Map<String, Object> map = new HashMap<>();
        map.put(sizeKey, executeResult);
        ExecuteUtils.terminalCondition(engineNode, inputParam, outMap, map);
    }
}
