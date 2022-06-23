package com.fibo.ddp.common.service.enginex.risk.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.dao.authx.system.SysMenuMapper;
import com.fibo.ddp.common.dao.authx.system.SysRoleMapper;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.enginex.risk.EngineMapper;
import com.fibo.ddp.common.dao.enginex.risk.EngineNodeMapper;
import com.fibo.ddp.common.dao.enginex.risk.EngineVersionMapper;
import com.fibo.ddp.common.model.authx.system.SysRole;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.ExcelModel;
import com.fibo.ddp.common.model.common.ExcelSheetModel;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.datax.datamanage.Field;
import com.fibo.ddp.common.model.enginex.risk.*;
import com.fibo.ddp.common.model.enginex.risk.response.TestResponse;
import com.fibo.ddp.common.service.common.ExcelUtil;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.datax.datamanage.FieldService;
import com.fibo.ddp.common.service.enginex.risk.EngineService;
import com.fibo.ddp.common.service.enginex.risk.EngineVersionService;
import com.fibo.ddp.common.service.enginex.util.EngineNodeJsonUtil;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.aimodel.ModelsService;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesService;
import com.fibo.ddp.common.service.strategyx.decisiontree.DecisionTreeService;
import com.fibo.ddp.common.service.strategyx.guiderule.RuleService;
import com.fibo.ddp.common.service.strategyx.guiderule.RuleVersionService;
import com.fibo.ddp.common.service.strategyx.listlibrary.ListDbV3Service;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardVersionService;
import com.fibo.ddp.common.service.strategyx.scriptrule.RuleScriptVersionService;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.constant.enginex.NodeTypeEnum;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EngineServiceImpl implements EngineService {
    private static final Logger logger = LoggerFactory.getLogger(EngineServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ListDbV3Service listDbService;//名单库
    @Resource
    private RuleService ruleService;//规则
    @Autowired
    private RuleVersionService ruleVersionService;
    @Autowired
    private RuleScriptVersionService ruleScriptVersionService;
    @Resource
    private DecisionTablesService decisionTablesService;//决策表
    @Resource
    private DecisionTreeService decisionTreeService;//决策表
    @Resource
    private ModelsService modelsService;//机器学习模型
    @Resource
    private ScorecardVersionService scorecardVersionService;//评分卡
    @Resource
    private EngineVersionService versionService;//引擎版本
    @Resource
    private FieldService fieldService;//字段
    @Autowired
    private EngineMapper engineMapper;
    @Autowired
    private EngineVersionMapper engineVersionMapper;
    @Autowired
    private EngineNodeMapper engineNodeMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private RedisManager redisManager;
    @Value("${switch.use.cache}")
    private String cacheSwitch;

    @Override
    public List<Engine> getEngineByList(Engine engineVo) {
        // TODO Auto-generated method stub
        return engineMapper.getEngineByList(engineVo);
    }

    @Override
    public Engine getEngineById(Engine engineVo) {
        // TODO Auto-generated method stub
        return engineMapper.getEngineById(engineVo);
    }

    @Override
    public int updateEngine(Engine engineVo) {
        // TODO Auto-generated method stub
        return engineMapper.updateEngine(engineVo);
    }

    @Override
    public boolean saveEngine(Engine engine) {
        boolean flag = true;
        int engineCount = engineMapper.insertEngineAndReturnId(engine);
        if (engineCount == 1) {
            Long engineId = engine.getId();
            if (engineId != null && engineId > 0) {
                // 开始新增版本
                EngineVersion engineVersion = new EngineVersion();
                engineVersion.setBootState(0);
                engineVersion.setCreateTime(new Date().toString());
                engineVersion.setEngineId(engine.getId());
                engineVersion.setLatestTime(new Date().toString());
                engineVersion.setLatestUser(engine.getCreator());
                // 默认自定义布局
                engineVersion.setLayout(0);
                // 状态正常
                engineVersion.setStatus(1);
                engineVersion.setUserId(engine.getCreator());
                // 默认新增版本0.0
                engineVersion.setVersion(0);
                // 默认子版本为0
                engineVersion.setSubVersion(0);
                EngineNode node = new EngineNode();
                node.setNodeX(200d);
                node.setNodeY(200d);
                node.setNodeName("开始");
                node.setNodeOrder(1);
                node.setNodeType(NodeTypeEnum.START.getValue());
                node.setNodeCode("ND_START");
                node.setParams("{\"arr_linkId\":\"\",\"dataId\":\"-1\",\"url\":\"/Riskmanage/resource/images/decision/start.png\",\"type\":\"1\"}");
                // 保存引擎版本
                int count = engineVersionMapper
                        .insertEngineVersionAndReturnId(engineVersion);
                if (count == 1) {
                    long versionId = engineVersion.getVersionId();
                    node.setVersionId(versionId);
                    // 普通节点就直接插入
                    engineNodeMapper.insert(node);
                }
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }

        return flag;
    }

    public Map<String, Object> getEngineVersionExecute(Map<String, Object> inputParam, String id) {
        // TODO
        return null;
    }

    /**
     * 判断表达式的运算结果是否数值型的公共方法
     */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^(-|\\+)?\\d+(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 查询本公司引擎
     */
    @Override
    public List<Engine> getEngineList(long organId, String searchString,
                                      List<Integer> list) {
        return engineMapper.getEngineList(organId, searchString, list);
    }

    @Override
    public Map<String, Object> getIndexEngineBaseInfo(Map<String, Object> paramMap) {
        return engineMapper.getIndexEngineBaseInfo(paramMap);
    }

    @Override
    public List<IndexEngineReportVo> getIndexRecentDayEngineUseInfo(Map<String, Object> paramMap) {
        return engineMapper.getIndexRecentDayEngineUseInfo(paramMap);
    }

    @Override
    public List<IndexEngineReportVo> getIndexRecentMonthEngineUseInfo(Map<String, Object> paramMap) {
        return engineMapper.getIndexRecentMonthEngineUseInfo(paramMap);
    }

    @Override
    public List<Map<String, Object>> getIndexEngineUseRatio(Map<String, Object> paramMap) {
        return engineMapper.getIndexEngineUseRatio(paramMap);
    }

    @Override
    public List<Field> getFieldByEngineVersion(EngineVersion version) {
        Set<String> fieldEns = getFieldEnByEngineVersion(version);
        List<Field> fields = fieldService.queryByEns(fieldEns);
        if (fields == null || fields.size() == 0) {
            return new ArrayList<>();
        }
        Set<Long> oldIds = new HashSet<>();
        Set<Long> ids = new HashSet<>();
        Iterator<Field> iterator = fields.iterator();
        while (iterator.hasNext()) {
            Field field = iterator.next();
            oldIds.add(field.getId());
            if (field.getIsDerivative() == null || field.getIsDerivative() != 1) {
                continue;
            }

            String origFieldId = field.getOrigFieldId();
            if (origFieldId == null || "".equals(origFieldId)) {
                continue;
            }
            String[] split = origFieldId.split(",");
            for (String s : split) {
                if (isNumeric(s)) {
                    ids.add(Long.valueOf(s));
                }
            }
            iterator.remove();
        }

        if (ids.size() > 0) {
            ids = ids.stream().filter(id -> {
                return !oldIds.contains(id);
            }).collect(Collectors.toSet());
            List<Field> fieldList = fieldService.queryByIds(ids);
            if (fieldList != null && fieldList.size() > 0) {
                fields.addAll(fieldList.stream().filter(field -> {
                    return !fieldEns.contains(field.getFieldEn());
                }).collect(Collectors.toList()));
            }
        }
        return fields;
    }

    @Override
    public String getJsonField(EngineVersion version) {
        version = engineVersionMapper.selectByPrimaryKey(version.getVersionId());

//        JSONObject biz_data = new JSONObject();
        Long organId = SessionManager.getLoginAccount().getOrganId();
//        biz_data.put("organId", organId);
        Long engineId = version.getEngineId();
//        biz_data.put("engineId", engineId);
        String businessId = "";
//        biz_data.put("businessId", businessId);
        String biz_enc = "0";
//        biz_data.put("biz_enc", biz_enc);
        Long timestamp = System.currentTimeMillis();
//        biz_data.put("timestamp", timestamp);

        List<Field> fieldList = getFieldByEngineVersion(version);
        Map fields = new HashMap();
        for (Field field : fieldList) {
            Object result;
            switch (field.getValueType()) {
                case 1:
                    result = 123;
                    break;
                case 2:
                    result = "abc";
                    break;
                case 6:
                    result = JSON.parse(field.getJsonValue());
                    break;
                default:
                    result = "xyz";
            }
            fields.put(field.getFieldEn(), result);
        }
//        biz_data.put("fields", fields);
        JSONObject result = new JSONObject();
        ExecuteParam biz_data = new ExecuteParam(engineId, organId, biz_enc, timestamp, businessId, fields);
        result.put("biz_data", biz_data);
        return JSON.toJSONString(result);
    }

    @Override
    public boolean getFieldExcelTemplate(HttpServletResponse response, EngineVersion version) {
        List<Field> fieldList = getFieldByEngineVersion(version);

        if (fieldList == null || fieldList.size() == 0) {
            return false;
        }
        List<String> headList = new ArrayList<>();
        List<List> data = new ArrayList<>();
        List exampleRow = new ArrayList();
        headList.add("业务id");
        exampleRow.add("业务唯一id，示例：example200");
        data.add(exampleRow);
        for (Field field : fieldList) {
            headList.add(field.getFieldCn());
            switch (field.getValueType()) {
                case 1:
                    exampleRow.add("数值型，示例：123");
                    break;
                case 2:
                    exampleRow.add("字符串，示例：abc");
                    break;
                case 6:
                    exampleRow.add("json型，示例：" + field.getJsonValue());
                    break;
                default:
                    exampleRow.add("其他类型");
            }
        }
        OutputStream out = null;
        try {
            // 设置response的Header

            response.setContentType("application/octet-stream");
            response.setHeader("content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + new String("引擎入参".getBytes("utf-8"), "ISO8859-1") + ".xlsx\"");


//            response.setHeader("Content-disposition", "attachment; filename=\""+ new String("引擎入参.xlsx".getBytes("utf-8"), "ISO8859-1") + "\"");

            ExcelSheetModel sheet = new ExcelSheetModel("引擎入参", headList, data);
            List<ExcelSheetModel> sheets = new ArrayList<>();
            sheets.add(sheet);
            out = response.getOutputStream();
            ExcelUtil.exportExcelTemplate(out, new ExcelModel("引擎入参.xlsx", "xlsx", sheets));
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (out != null) {
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public TestResponse batchTest(HttpServletRequest request){
        //引擎id
        String engineIdStr = request.getParameter("engineId");
        long engineId = StringUtil.getStrTolong(engineIdStr);
        if (engineIdStr==null|| engineId==0){
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),"未找到测试引擎");
        }
        //组织id
        SysUser sysUser = SessionManager.getLoginAccount();
        Long organId = sysUser.getOrganId();
        //加解密标识
        String biz_enc = "0";
        //时间戳
        Long timestamp = System.currentTimeMillis();
        Map fields = new HashMap<>();
        String businessId = "";
        ExecuteParam biz_data = new ExecuteParam(engineId, organId, biz_enc, timestamp, businessId, fields);
        Map<String,Object> param = new HashMap();
        param.put("biz_data", biz_data);
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        Iterator iterator = multiRequest.getFileNames();

        List list = new ArrayList<>();
        while (iterator.hasNext()) {
            MultipartFile file = multiRequest.getFile(iterator.next().toString());
            boolean isXlsx = false;
            String fileName = file.getOriginalFilename();

            if (fileName.endsWith(".xlsx")) {
                isXlsx = true;
            }
            InputStream input = null;
            Workbook wb = null;
            try {
                input = file.getInputStream();
            //根据文件格式(2003或者2007)来初始化
            if (isXlsx)
                wb = new XSSFWorkbook(input);
            else
                wb = new HSSFWorkbook(input);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            Sheet sheet = wb.getSheetAt(0);

            if (sheet != null) {
                try {
                    //处理头信息
                    Row header = sheet.getRow(0);
                    List<String> fieldEns = new ArrayList<>();
                    List<String> fieldCns = new ArrayList<>();
                    int cellNum = header.getLastCellNum();
                    Cell cell = header.getCell(0);
                    for (int i = 1; i < cellNum; i++) {
                        cell = header.getCell(i);
                        Map<String, Object> map = new HashMap<>();
                        fieldCns.add(ExcelUtil.formatCell(cell));
                    }
                    List<Field> fieldList = fieldService.queryByOrganAndCns(fieldCns, sysUser.getOrganId());
                    Map<String, String> collect = fieldList.stream().collect(Collectors.toMap(e -> e.getFieldCn(), e -> e.getFieldEn()));
                    for (String fieldCn : fieldCns) {
                        String en = collect.get(fieldCn);
                        if (StringUtils.isNotBlank(en)){
                            fieldEns.add(en);
                        }else {
                            fieldEns.add(fieldCn);
                        }
                    }
                    //遍历excel,从第二行开始 即 rowNum=1,逐个获取单元格的内容,然后进行格式处理,最后插入数据库
                    for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                        Row hssfRow = sheet.getRow(rowNum);
                        if (hssfRow == null || hssfRow.getCell(0) == null) {
                            continue;
                        }
                        cellNum = hssfRow.getLastCellNum();
                        businessId = ExcelUtil.formatCell(hssfRow.getCell(0));
                        for (int i = 1; i < cellNum; i++) {
                            fields.put(fieldEns.get(i-1), ExcelUtil.formatCell(hssfRow.getCell(i)));
                        }
                        biz_data.setFields(fields);
                        biz_data.setBusinessId(businessId);
                        list.add(JSONObject.parse(JSON.toJSONString(param)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ResponseEntity<List> testResult = getTestResult(list);
        //执行成功
        Map<String, Integer> success = new HashMap<>();
        Integer total = list.size();//总条数
        Integer failNum = 0;//失败条数
        Integer successNum = 0;//成功条数
        if (testResult.getStatusCodeValue()==200){
            List<String> bodyList = testResult.getBody();
            for (String object : bodyList) {
                JSONObject json = JSON.parseObject(object);
                String status = json.getString("status");
                //执行成功
                if ("0x0000".equals(status)){
                    successNum++;
                    String result = "";
                    if (json.containsKey("result")){
                        result = json.getString("result");
                    }
                    int count  = 1;
                    if (success.containsKey(result)){
                        count += success.get(result);
                    }
                    success.put(result,count);
                }else {//失败
                    failNum++;
                }
            }
        }else {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),"链接执行器失败");
        }
        return  new TestResponse(success, successNum, failNum, total);
    }

    private  ResponseEntity<List> getTestResult(List reqJsonStr) {

        return null;

//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<List> entity = new HttpEntity<List>(reqJsonStr,headers);
//            String url = runnerUrl + "/runner/batchExecute";
//            ResponseEntity<List> resp = restTemplate.exchange(url, HttpMethod.POST, entity,List.class);
//            return resp;
//        }catch (Exception e){
//            e.printStackTrace();
//            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),"链接执行器失败");
//        }
    }

    private Set<String> getFieldEnByEngineVersion(EngineVersion version) {
        EngineVersion engineVersion = engineVersionMapper.selectByPrimaryKey(version.getVersionId());
        Engine engine = new Engine();
        engine.setId(engineVersion.getEngineId());
        Set<String> fieldEns = new HashSet<>();
        if (null != engineVersion) {
            List<EngineNode> engineNodeList = engineNodeMapper.getEngineNodeListByEngineVersionId(engineVersion.getVersionId());
            for (int i = 0; i < engineNodeList.size(); i++) {
                EngineNode node = engineNodeList.get(i);
                fieldEns.addAll(getFieldEnList(node));
            }
        }
        return fieldEns;
    }

    private Set<String> getFieldEnList(EngineNode node) {
        Integer nodeType = node.getNodeType();
        Set<String> fieldEns = new HashSet<>();
        if (nodeType == null) {
            return fieldEns;
        }
        switch (nodeType) {
            case 1://开始节点，无逻辑

                break;
            case 2://规则集 从nodeJson.deny_rules.rules[].userId
                fieldEns.addAll(getRuleFieldEnList(node));
                break;
            case 3://分组 nodeJson.fields->{字段对象。fieldcode}
                fieldEns.addAll(getGroupFieldEnList(node));
                break;
            case 4://评分卡 nodeJson直接取
                fieldEns.addAll(getScorecardFieldEnList(node));
                break;
            case 5://黑名单 nodeJson 根据nodeId取关联表查询内外部名单库id，
            case 6://白名单 nodeJson 根据nodeId取关联表查询内外部名单库id，
                fieldEns.addAll(getListDbFieldEnList(node));
                break;
            case 7://分流，无字段

                break;
            case 9://决策选项 nodeJson.input[]字段对象
                fieldEns.addAll(getDecisionOptionsFieldEnList(node));
                break;
            case 14://子引擎 nodeJson中直接为引擎id取运行中版本（t_engine_version中boot_state为1的是运行版本）
                fieldEns.addAll(getChildEngineFieldEnList(node));
                break;
            case 15://模型 nodeJson取模型id
                fieldEns.addAll(getModelsFieldEnList(node));
                break;
            case 16://决策表 nodeJson取决策表id
                fieldEns.addAll(getDecisionTablesFieldEnList(node));
                break;
            case 17://决策树
                fieldEns.addAll(getDecisionTreeFieldEnList(node));
                break;
            case 18://远程调用
                break;
            case 19://聚合节点
                break;
            case 20://冠军挑战节点
                break;
        }
        return fieldEns;
    }

    private Set<String> getRuleFieldEnList(EngineNode node) {
        Set<String> fieldEns = new HashSet<>();
        String nodeJson = node.getNodeJson();
        if (nodeJson == null || "".equals(nodeJson)) {
            return fieldEns;
        }
        JSONObject ruleMap = JSON.parseObject(nodeJson);
        Integer groupType = ruleMap.getInteger("groupType");
        List<JSONObject> maps = new ArrayList<>();
        Object o;
        if (groupType == 1) {
            o = JSON.parseObject(ruleMap.get("mutexGroup").toString(), Map.class).get("rules");
            maps = JSON.parseArray(JSON.toJSONString(o), JSONObject.class);
        } else if (groupType == 2) {
            o = JSON.parseObject(ruleMap.get("executeGroup").toString(), Map.class).get("rules");
            maps = JSON.parseArray(JSON.toJSONString(o), JSONObject.class);
        } else {
            return fieldEns;
        }


        for (JSONObject map : maps) {
            Long id = map.getLong("userId");
            Long ruleVersionId = map.getLong("ruleVersionId");
            int difficulty = map.getIntValue("difficulty");
            switch (difficulty){
                case 2:
                    fieldEns.addAll(ruleVersionService.queryFieldEnByVersionId(ruleVersionId));
                    break;
                case 3:
                    fieldEns.addAll(ruleScriptVersionService.queryFieldEnByVersionId(ruleVersionId));
                    break;
            }
        }
        return fieldEns;
    }

    private Set<String> getGroupFieldEnList(EngineNode node) {
        Set<String> fieldEns = new HashSet<>();
        String nodeJson = node.getNodeJson();
        if (nodeJson == null || "".equals(nodeJson)) {
            return fieldEns;
        }
        Map groupMap = JSON.parseObject(nodeJson, Map.class);
        List<Map> maps = JSON.parseArray(JSON.toJSONString(groupMap.get("fields")), Map.class);
        if (maps == null || maps.size() == 0) {
            return fieldEns;
        }
        for (Map map : maps) {
            Object fieldCode = map.get("fieldCode");
            if (fieldCode != null) {
                fieldEns.add(fieldCode.toString());
            }
        }
        return fieldEns;
    }

    private Set<String> getScorecardFieldEnList(EngineNode node) {
        String nodeJson = node.getNodeJson();
        Set<String> fieldEns = new HashSet<>();
        if (StringUtils.isBlank(nodeJson)) {
            return fieldEns;
        }
        List<Long> versionIds = EngineNodeJsonUtil.getExecuteIdList(node, "versionId");
        if (versionIds==null||versionIds.isEmpty()){
            return fieldEns;
        }
        for (Long versionId : versionIds) {
            if (versionId!=null&&versionId>0){
                fieldEns.addAll(scorecardVersionService.queryFieldEnByVersionId(versionId));
            }
        }
        return fieldEns;
    }

    private Set<String> getListDbFieldEnList(EngineNode node) {
        String nodeJson = node.getNodeJson();
        Set<String> fieldEns = new HashSet<>();
        if (StringUtils.isBlank(nodeJson)) {
            return fieldEns;
        }
        List<Long> listDbIds = EngineNodeJsonUtil.getExecuteIdList(node, "listDbId");
        for (Long listDbId : listDbIds) {
            fieldEns.addAll(listDbService.queryFieldEnsByListDbIds(listDbId));
        }
        return fieldEns;
    }

    private Set<String> getDecisionOptionsFieldEnList(EngineNode node) {
        Set<String> fieldEns = new HashSet<>();
        String nodeJson = node.getNodeJson();
        if (nodeJson == null || "".equals(nodeJson)) {
            return fieldEns;
        }
        Map groupMap = JSON.parseObject(nodeJson, Map.class);
        List<JSONObject> maps = JSON.parseArray(JSON.toJSONString(groupMap.get("input")), JSONObject.class);
        for (JSONObject map : maps) {
            long nodeType = map.getLongValue("nodeType");
            Object fieldCode = map.getString("field_code");
            if (nodeType<0 && fieldCode != null) {
                fieldEns.add(fieldCode.toString());
            }
        }
        return fieldEns;
    }

    private Set<String> getChildEngineFieldEnList(EngineNode node) {
        Set<String> fieldEns = new HashSet<>();
        String nodeJson = node.getNodeJson();
        if (nodeJson == null || "".equals(nodeJson)) {
            return fieldEns;
        }
        String[] split = nodeJson.split(",");
        for (String s : split) {
            List<EngineVersion> versionList = versionService.getEngineVersionListByEngineIdV2(Long.valueOf(s));
            for (EngineVersion version : versionList) {
                Integer bootState = version.getBootState();
                if (bootState == null || bootState == 0) {
                    continue;
                } else if (bootState == 1) {
                    fieldEns.addAll(this.getFieldEnByEngineVersion(version));
                }
            }
        }
        return fieldEns;
    }

    private Set<String> getModelsFieldEnList(EngineNode node) {

        Set<String> fieldEns = new HashSet<>();
        List<Long> versionIds = EngineNodeJsonUtil.getExecuteIdList(node, "modelId");
        if (versionIds==null||versionIds.isEmpty()){
            return fieldEns;
        }
        for (Long versionId : versionIds) {
            fieldEns.addAll(modelsService.queryFieldEnByModelsId(versionId.intValue()));
        }
        return fieldEns;
    }

    private Set<String> getDecisionTablesFieldEnList(EngineNode node) {
        Set<String> fieldEns = new HashSet<>();
        List<Long> versionIds = EngineNodeJsonUtil.getExecuteIdList(node, "versionId");
        if (versionIds==null||versionIds.isEmpty()){
            return fieldEns;
        }
        for (Long versionId : versionIds) {
            fieldEns.addAll(decisionTablesService.queryFieldEnByDecisionTablesVersionId(versionId));
        }
        return fieldEns;
    }

    private Set<String> getDecisionTreeFieldEnList(EngineNode node) {
        Set<String> fieldEns = new HashSet<>();
        List<Long> versionIds = EngineNodeJsonUtil.getExecuteIdList(node, "versionId");
        if (versionIds==null||versionIds.isEmpty()){
            return fieldEns;
        }
        for (Long versionId : versionIds) {
            fieldEns.addAll(decisionTreeService.queryFieldEnByVersionId(versionId));
        }
        return fieldEns;
    }

    @Override
    public Engine getEngineById(Long id) {
        Engine engine = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getPrimaryKey(TableEnum.T_ENGINE, id);
            engine = redisManager.getByPrimaryKey(key, Engine.class);
        } else {
            engine = engineMapper.selectById(id);
        }

        return engine;
    }
}
