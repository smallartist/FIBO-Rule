package com.fibo.ddp.enginex.runner.node.impl;

import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.service.datax.runner.CommonService;
import com.fibo.ddp.common.service.datax.runner.ExecuteUtils;
import com.fibo.ddp.common.service.strategyx.listlibrary.ListDbService;
import com.fibo.ddp.common.utils.constant.enginex.NodeTypeEnum;
import com.fibo.ddp.enginex.runner.node.EngineRunnerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDbNode implements EngineRunnerNode {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommonService commonService;

    @Resource
    private ListDbService listDbService;

    private List<Long> getExecuteVersionIdList(EngineNode engineNode){
        return ExecuteUtils.getExecuteIdList(engineNode,"versionId");
    }

    @Override
    public void getNodeField(EngineNode engineNode, Map<String, Object> inputParam) {
        List<Long> list = getExecuteVersionIdList(engineNode);
        List<Long> fieldIds = new ArrayList<>();
        for (Long l : list) {
            fieldIds.addAll(listDbService.getNodeFieldIds(l));
        }
        commonService.getFieldByIds(fieldIds,inputParam);
    }

    @Override
    public void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap) {
        //监控中心--节点信息快照记录，主要用于监控中心节点信息配置页面显示
        JSONObject snapshot = new JSONObject();
        snapshot.put("snapshot",engineNode.getSnapshot());
        outMap.put("nodeSnapshot",snapshot);
        inputParam.put("nodeId", engineNode.getNodeId());
        inputParam.put("nodeName", engineNode.getNodeName());
        JSONObject nodeInfo = new JSONObject();
        nodeInfo.put("engineNode",engineNode);
        nodeInfo.put("nodeId",engineNode.getNodeId());
        nodeInfo.put("nodeName",engineNode.getNodeName());
        nodeInfo.put("nodeType",engineNode.getNodeType());
        outMap.put("nodeInfo",nodeInfo);
        int type = 0;
        if(engineNode.getNodeType() == NodeTypeEnum.BLACKLIST.getValue()){
            type = 1;
        } else if(engineNode.getNodeType() == NodeTypeEnum.WHITELIST.getValue()){
            type = 2;
        }
        boolean isfalg = this.findByQueryKey(inputParam, outMap, type,engineNode);
        if (isfalg) {
            if (type == 1) {
                outMap.put("isBlack", "true");
                outMap.put("enginefalg", "true");
//                engineNode.setNextNodes(null);
            } else {
                outMap.put("isWhite", "true");
                outMap.put("engineWhite", "true");
//                engineNode.setNextNodes(null);
            }
        }
    }

    /**
     * 根据传入数据监测是否命中黑名单
     *
     * @param paramMap
     * @return
     * @see
     */
    public boolean findByQueryKey(Map<String, Object> paramMap, Map<String, Object> outmap, Integer type,EngineNode engineNode) {
//        SessionData sessionData = SessionManager.getSession();
//        Long organId = sessionData.getOrganId();
//        JSONObject blackandWhite = new JSONObject();
//        JSONObject resultJson = new JSONObject();
//        //传递nodeId
//        String strlistDbIds = null;
//        if (!paramMap.get("nodeId").equals("0")) {
//            NodeListDb nodeListDb = nodeListDbMapper.findByNodeId(paramMap);
//            strlistDbIds = nodeListDb.getInnerListdbs();
//            //节点配置信息快照，hbase入库用
////            JSONObject bwNodeSnopshot = new JSONObject();
////            String strListDbIdsOut = nodeListDb.getOuterListdbs();
////            if(strListDbIdsOut!=null && strListDbIdsOut.length()>0){
////                bwNodeSnopshot.put("outerIdList",strListDbIdsOut.split(","));
////            }
//            //循环处理所有内部黑/白名单库
//            String[] arraylistDBIds = null;
//            Integer matchs = 0;
//            Integer revMatchs = 0; //模糊查询时反向匹配
//            if (strlistDbIds.length() > 0) {
//                arraylistDBIds = strlistDbIds.split(",");
////                bwNodeSnopshot.put("innerIdList",arraylistDBIds);
////                outmap.put("nodeSnapshot",bwNodeSnopshot);
//                String hitKey = ""+engineNode.getNodeType()+"_"+engineNode.getNodeId()+"_size";
//                int hitSize = 0;
//                JSONArray strategySnopshot = new JSONArray();
//                for (int i = 0; i < arraylistDBIds.length; i++) {
//                    HashMap<String, Object> param1 = new HashMap<String, Object>();
//                    param1.put("organId", organId);
//                    Integer listDbId = Integer.valueOf(arraylistDBIds[i]).intValue();
//                    param1.put("listDbId", listDbId);
//                    ListDb listDb = new ListDb();
////                    listDb = listDbService.findListDbByIdandByorganId(param1);
//                    //监控中心--策略层面记录名单库快照信息
//                    if(listDb.getSnapshot()!=null){
//                        strategySnopshot.add(listDb.getSnapshot());
//                    }
//                    paramMap.put("listDb", listDb);
//                    String listType = listDb.getListType();
//                    String queryKeyArray[] = listDb.getQueryField().split(",");
//                    if (queryKeyArray.length > 0) {
//                        Integer queryType = listDb.getQueryType();//and（1），or（0）
//                        Integer matchType = listDb.getMatchType();//精确匹配（1），模糊匹配（0）
//
//                        String queryKey = ""; // t1 like '%t1%'
//                        String revQueryKey = ""; // 反向模糊匹配 instr('高档洗浴消费',t1) t1行业字段 eg.'洗浴'
//                        String tableName = "organ" + "_" + organId + "_" + listType + "_" + listDbId;
//                        paramMap.put("tableName", tableName);
//                        paramMap.put("schemaName", getDbName());
//
//                        //获取名单库的匿名字段与注释的关系
//                        List<TblColumn> columnList = tblColumnMapper.getColumnList(paramMap);
//
//                        //字段id转匿名字段名，准备待查字段条件
//                        Integer loc = 0;
//
//                        for (int j = 0; j < queryKeyArray.length; j++) {
//
//                            HashMap<String, Object> inputParam = new HashMap<String, Object>();
//                            inputParam.put("id", queryKeyArray[j]);
//                            inputParam.put("organId", organId);
//                            inputParam.put("engineId", null);
//                            //id(3)-field(age)
//                            Field field = fieldMapper.findByFieldIdbyorganId(inputParam);
//                            String fieldEn = field.getFieldEn(); //age
//
//                            for (TblColumn tblColumn : columnList) {
//
//                                String colComment = tblColumn.getColComment(); //age
//                                String colName = tblColumn.getColName(); //t5
//                                String paramValue =paramMap.get(fieldEn).toString();
//
//                                if (colName.startsWith("t") && queryKeyArray[j].equals(colComment)) {
//
//                                    if (paramValue == null || paramValue.equals("")) {
//                                        return false; //数据项缺失导致无法命中，默认返回没命中
//                                    } else {
//                                        loc += 1;
//                                        if (matchType == 1) {
//                                            if (loc > 1 && queryType == 1) {
//                                                queryKey += " and ";
//                                            } else if (loc > 1 && queryType == 0) {
//                                                queryKey += " or ";
//                                            }
//                                            queryKey += colName + " = '" + paramValue + "'";
//                                        } else if (matchType == 0) { //模糊匹配
//                                            if (loc > 1 && queryType == 1) {
//                                                queryKey += " and ";
//                                            } else if (loc > 1 && queryType == 0) {
//                                                queryKey += " or ";
//                                                revQueryKey += " + ";
//                                            }
//                                            //正向模糊搜索
//                                            queryKey += colName + " like " + "'%" + paramValue + "%'"; // t5 like '%36岁%'
//                                            //反向模糊搜索
//                                            revQueryKey += "max(instr('" + paramValue + "'," + colName + "))";
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        paramMap.put("queryKey", queryKey);
//                        paramMap.put("revQueryKey", revQueryKey);
//                    }
//                    matchs += custListMapper.findByQueryKey(paramMap);
//                    if (!paramMap.get("revQueryKey").equals("")) {
//                        revMatchs = custListMapper.revFindByQueryKey(paramMap);
//                    }
//                    if (revMatchs == null)
//                        revMatchs = 0;
//
//                    paramMap.put(listDb.getResultFieldEn(),"未命中");
//                    List<JSONObject> fieldList =  new ArrayList<>();
//
//                    JSONObject hitResult = new JSONObject();
//                    hitResult.put(listDb.getResultFieldEn(),"未命中");
//                    if (matchs + revMatchs > 0) {
//                        resultJson.put("nodeId", paramMap.get("nodeId").toString());
//                        resultJson.put("nodeName", paramMap.get("nodeName").toString());
//                        resultJson.put("status", "0x0000");
//                        paramMap.put(listDb.getResultFieldEn(),"命中");
//                        hitResult.put(listDb.getResultFieldEn(),"命中");
//                        hitSize++;
//                        List<JSONObject> jsonObjects = outputService.setOutput(new StrategyOutput(Long.valueOf(listDbId.toString()), StrategyType.LIST_DB), paramMap);
//                        fieldList.add(hitResult);
//                        fieldList.addAll(jsonObjects);
//                        resultJson.put("fieldList",fieldList);
//                        if (type == 1) {
//                            blackandWhite.put("resultType", 5);
//                            outmap.put("black", listDb);
//                            resultJson.put("blackId", listDb.getId());
//                            resultJson.put("blackName", listDb.getListName());
//                            resultJson.put("blackType", listDb.getListType());
//                            if (null != listDb.getListDesc()) {
//                                resultJson.put("desc", listDb.getListDesc());
//                            }
//
//                            blackandWhite.put("resultJson", resultJson);
//                            //黑名单api返回
//                            outmap.put("blackJson", blackandWhite);
//                        } else {
//                            blackandWhite.put("resultType", 6);
//                            resultJson.put("status", "0x0000");
//                            resultJson.put("whiteId", listDb.getId());
//                            resultJson.put("whiteName", listDb.getListName());
//                            resultJson.put("whiteType", listDb.getListType());
//                            if (null != listDb.getListDesc()) {
//                                resultJson.put("desc", listDb.getListDesc());
//                            }
//                            blackandWhite.put("resultJson", resultJson);
//                            //白名单api返回
//                            outmap.put("whiteJson", blackandWhite);
//                            outmap.put("white", listDb);
//                        }
//                        paramMap.put(hitKey,hitSize);
//                        return true;
//                    }else {
//                        //未命中
//                        blackandWhite.put("resultJson","未命中");
//                        fieldList.add( hitResult);
//                    }
//                    //监控中心==》将评分卡的执行结果得分明细放入 输出变量池 用于存入hbase
//                    outmap.put("nodeResult",blackandWhite);
//                }
//                paramMap.put(hitKey,hitSize);
//                //监控中心==》策略层面记录策略的快照信息
//                JSONObject strategyObject =  new JSONObject();
//                strategyObject.put("strategySnopshot",strategySnopshot);
//                outmap.put("strategySnopshot",strategyObject);
//            } else
//                return false;
//        }
        return false;
    }

    /**
     * Description: 获取jdbc.properties里配置的数据库名
     *
     * @return
     * @see
     */
    private String getDbName() {

//        ResourceBundle resource = ResourceBundle.getBundle("conf/jdbc");
//        String mysqlUrl = resource.getString("mysql.url");
//
//        String aArray[] = mysqlUrl.split("/");
//        String bArray[] = aArray[3].split("\\?");
//        String dbName = bArray[0];
//
//        return dbName;
        return "riskmanage";
    }
    private void terminalCondition(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap,Object executeResult) {
        String sizeKey = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_terminal_size";
        Map<String,Object> map = new HashMap<>();
        map.put(sizeKey,executeResult);
        ExecuteUtils.terminalCondition(engineNode,inputParam,outMap, map);
    }
}
