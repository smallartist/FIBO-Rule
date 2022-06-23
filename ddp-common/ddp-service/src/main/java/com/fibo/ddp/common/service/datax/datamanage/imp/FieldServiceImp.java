package com.fibo.ddp.common.service.datax.datamanage.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.datax.datamanage.*;
import com.fibo.ddp.common.dao.strategyx.knowledge.RuleMapper;
import com.fibo.ddp.common.dao.strategyx.listlibrary.ListDbMapper;
import com.fibo.ddp.common.dao.strategyx.scorecard.ScorecardMapper;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.UpdateFolderParam;
import com.fibo.ddp.common.model.datax.common.ExcelUtil;
import com.fibo.ddp.common.model.datax.common.Status;
import com.fibo.ddp.common.model.datax.datamanage.Field;
import com.fibo.ddp.common.model.datax.datamanage.FieldCond;
import com.fibo.ddp.common.model.datax.datamanage.FieldUser;
import com.fibo.ddp.common.model.strategyx.knowledge.Rule;
import com.fibo.ddp.common.model.strategyx.listlibrary.ListDb;
import com.fibo.ddp.common.model.strategyx.scorecard.Scorecard;
import com.fibo.ddp.common.service.common.runner.RunnerSessionManager;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.datax.datamanage.FieldService;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.StringUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FieldServiceImp extends ServiceImpl<FieldMapper,Field> implements FieldService {

    @Autowired
    private FieldMapper fieldMapper;
    @Autowired
    private FieldUserMapper fieldUserMapper;
    @Autowired
    private FieldCondMapper fieldCondMapper;
    @Autowired
    private ListDbMapper listDbMapper;
    @Autowired
    private RuleMapper ruleMapper;
    @Autowired
    private ScorecardMapper scorecardMapper;
    @Autowired
    private FieldTypeUserMapper fieldTypeUserMapper;
    @Autowired
    private FieldTypeMapper fieldTypeMapper;
    @Autowired
    private RedisManager redisManager;

    // 业务逻辑是否使用缓存
    @Value("${switch.use.cache}")
    private String cacheSwitch;

    protected static final Set<String> KEY_WORDS = new HashSet<String>() {{
        add("DELETE ");
        add("DROP ");
        add("TRUNCATE ");
        add("UPDATE ");
        add("ALTER ");
        add("INSERT ");
        add("CREATE ");
        add("RENAME ");
    }};

    /*
     * 公共方法：去掉id串里重复id
     */
    public StringBuffer getUniqueStr(String usedFieldStr) {

        String arrUsedFieldStr[] = usedFieldStr.split(",");
        Set<String> usedFieldSet = new HashSet<>();
        for (int k = 0; k < arrUsedFieldStr.length; k++) {
            usedFieldSet.add(arrUsedFieldStr[k]);
        }
        String[] arrUsedField = (String[]) usedFieldSet.toArray(new String[usedFieldSet.size()]);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arrUsedField.length; i++) {
            if (i != arrUsedField.length - 1)
                sb.append(arrUsedField[i]).append(",");
            else
                sb.append(arrUsedField[i]);
        }
        return sb;
    }

    @Override
    public boolean createField(Field fieldVo, Map<String, Object> paramMap) {

        String formulaHidden = "";

        //获取衍生字段公式编辑区域引用字段的原生字段
        if (paramMap.containsKey("formulaHidden") && !paramMap.get("formulaHidden").equals("")) {

            formulaHidden = (String) paramMap.get("formulaHidden");
            fieldVo.setFormula(formulaHidden);

            List<Object> formulaList = new ArrayList<>();
            formulaList = JSONObject.parseArray(formulaHidden);

            JSONArray jsonArrayFormula = new JSONArray();

            String origFieldStr = "";
            String usedFieldStr = "";

            for (int i = 0; i < formulaList.size(); i++) {

                JSONObject f = ((JSONArray) formulaList).getJSONObject(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fvalue", f.getString("fvalue"));
                jsonObject.put("formula", f.getString("formula"));
                jsonObject.put("idx", f.getString("idx"));
                jsonArrayFormula.add(jsonObject);

                List<Object> farrList = new ArrayList<>();
                String formula = f.getString("formula");
                Pattern pattern = Pattern.compile("@[a-zA-Z0-9_\u4e00-\u9fa5()（）-]+@");
                Matcher matcher = pattern.matcher(formula);
                while (matcher.find()) {
                    String fieldCN = matcher.group(0).replace("@", "");
                    Map<String, Object> fieldMap = new HashMap<String, Object>();
                    fieldMap.put("userId", paramMap.get("userId"));
                    fieldMap.put("engineId", paramMap.get("engineId"));
                    fieldMap.put("fieldCn", fieldCN);

                    Field field = fieldMapper.findByFieldCn(fieldMap);

                    if (field.getOrigFieldId() == null) {
                        if (origFieldStr.equals("")) {
                            origFieldStr = Long.toString(field.getId());
                        } else {
                            origFieldStr = origFieldStr + "," + field.getId();
                        }
                    } else {
                        if (origFieldStr.equals("")) {
                            origFieldStr = field.getOrigFieldId();
                        } else {
                            origFieldStr = origFieldStr + "," + field.getOrigFieldId();
                        }
                    }
                    usedFieldStr = usedFieldStr + field.getId() + ","; //拼凑该字段引用的字段id
                }
//				}
            }

            fieldVo.setFormulaShow(JSON.toJSONString(jsonArrayFormula));

            //合并原生字段id
            if (!origFieldStr.equals("")) {
                fieldVo.setOrigFieldId(getUniqueStr(origFieldStr).toString());
            }

            //合并引用字段id
            if (!usedFieldStr.equals(",") && !usedFieldStr.equals("")) {
                usedFieldStr = usedFieldStr.substring(0, usedFieldStr.length() - 1);
                fieldVo.setUsedFieldId(getUniqueStr(usedFieldStr).toString());
            }

        } else if (paramMap.containsKey("fieldCondList") && !paramMap.get("fieldCondList").equals("")) {
            //条件区域的使用字段和原生字段获取
			
			/*
			  fieldContent=[{"fieldContent2":"[{\"fieldId\":\"3\",\"operator\":\">\",\"fieldValue\":\"200\",\"logical\":\"&&\"}
				  							  ,{\"fieldId\":\"11\",\"operator\":\"<\",\"fieldValue\":\"50\"}]","conditionValue":"5","fieldValue":"50"}
						   ,{"fieldContent2":"[{\"fieldId\":\"12\",\"operator\":\"in\",\"fieldValue\":\"z\",\"logical\":\"&&\"}
				  							  ,{\"fieldId\":\"11\",\"operator\":\">\",\"fieldValue\":\"200\",\"logical\":\"&&\"}
				  							  ,{\"fieldId\":\"31\",\"operator\":\">\",\"fieldValue\":\"1000\"}]","conditionValue":"8","fieldValue":"1000"}
						   ,{"fieldContent2":"[{\"fieldId\":\"31\",\"operator\":\">\",\"fieldValue\":\"4000\"}]","conditionValue":"9","fieldValue":"4000"}]
			*/
            String fieldContent = (String) paramMap.get("fieldCondList");
            List<Object> fieldContentList = new ArrayList<>();
            fieldContentList = JSONObject.parseArray(fieldContent);

            String origFieldStr = "";
            String usedFieldStr = "";

            for (int i = 0; i < fieldContentList.size(); i++) {
                JSONObject fc = ((JSONArray) fieldContentList).getJSONObject(i);
                List<Object> farrList = new ArrayList<>();
                if (!fc.getString("fieldSubCond").equals("") && fc.getString("fieldSubCond") != null) {
                    farrList = JSONObject.parseArray(fc.getString("fieldSubCond"));
                    for (int j = 0; j < farrList.size(); j++) {
                        JSONObject ObjField = ((JSONArray) farrList).getJSONObject(j);
                        usedFieldStr = usedFieldStr + ObjField.get("fieldId") + ",";

                        Map<String, Object> fieldMap = new HashMap<String, Object>();
                        fieldMap.put("userId", paramMap.get("userId"));
                        fieldMap.put("engineId", paramMap.get("engineId"));
                        fieldMap.put("id", ObjField.get("fieldId"));
                        Field field = fieldMapper.findByFieldId(fieldMap);

                        if (field.getOrigFieldId() == null) {
                            if (origFieldStr.equals("")) {
                                origFieldStr = Long.toString(field.getId());
                            } else {
                                origFieldStr = origFieldStr + "," + field.getId();
                            }
                        } else {
                            if (origFieldStr.equals("")) {
                                origFieldStr = field.getOrigFieldId();
                            } else {
                                origFieldStr = origFieldStr + "," + field.getOrigFieldId();
                            }
                        }


                    }
                }
            }

            //合并引用字段id
            if (!usedFieldStr.equals(",") && !usedFieldStr.equals("")) {
                usedFieldStr = usedFieldStr.substring(0, usedFieldStr.length() - 1);
                fieldVo.setUsedFieldId(getUniqueStr(usedFieldStr).toString());
            }

            //合并原生字段id
            if (!origFieldStr.equals("")) {
                fieldVo.setOrigFieldId(getUniqueStr(origFieldStr).toString());
            }
        }

        if (fieldMapper.isExists(paramMap) == 0) {
            fieldVo.setSourceType((Integer) paramMap.getOrDefault("sourceType", 1));
            fieldMapper.createField(fieldVo);
            FieldUser fieldUserVo = new FieldUser();
            fieldUserVo.setFieldId(fieldVo.getId());
            fieldUserVo.setOrganId((Long) paramMap.get("organId"));
            if (paramMap.get("engineId") != null) {
                fieldUserVo.setEngineId(Long.valueOf((String) paramMap.get("engineId")));
            }
            fieldUserVo.setUserId((Long) paramMap.get("userId"));
            fieldUserVo.setStatus(Status.enable.value);
            fieldUserMapper.createFieldUserRel(fieldUserVo);

            //可能衍生字段只有公式,没有条件设置
            if (paramMap.containsKey("fieldCondList")) {

                String fieldContent = (String) paramMap.get("fieldCondList");
                if (!fieldContent.equals("")) {
                    List<FieldCond> fieldCondVoList = new ArrayList<FieldCond>();
                    List<Object> condList = new ArrayList<>();
                    condList = JSONObject.parseArray(fieldContent);
                    for (int i = 0; i < condList.size(); i++) {
                        JSONObject cond = ((JSONArray) condList).getJSONObject(i);
                        List<Object> subCondList = new ArrayList<>();
                        if (!cond.getString("fieldSubCond").equals("")) {
                            subCondList = JSONObject.parseArray(cond.getString("fieldSubCond"));
                            for (int j = 0; j < subCondList.size(); j++) {
                                JSONObject subCond = ((JSONArray) subCondList).getJSONObject(j);
                                FieldCond fieldCondVo = new FieldCond();
                                fieldCondVo.setFieldId(fieldVo.getId());
                                fieldCondVo.setConditionValue(cond.getString("conditionValue"));
                                fieldCondVo.setContent(cond.getString("fieldSubCond"));
                                fieldCondVo.setCondFieldId(Long.valueOf(subCond.getString("fieldId")));
                                fieldCondVo.setCondFieldOperator(subCond.getString("operator"));
                                fieldCondVo.setCondFieldValue(subCond.getString("fieldValue"));
                                fieldCondVo.setCondFieldLogical(subCond.getString("logical"));
                                fieldCondVoList.add(fieldCondVo);
                            }
                        }
                        fieldCondMapper.createFieldCond(fieldCondVoList);
                    }

                }
            }

            return true;

        } else
            return false;
    }

    @Override
    public boolean updateField(Map<String, Object> paramMap) {

        String formulaHidden = "";

        if (paramMap.containsKey("formulaHidden") && !paramMap.get("formulaHidden").equals("")) {

            formulaHidden = (String) paramMap.get("formulaHidden");
            paramMap.put("formula", formulaHidden);

            List<Object> formulaList = new ArrayList<>();
            formulaList = JSONObject.parseArray(formulaHidden);

            JSONArray jsonArrayFormula = new JSONArray();

            String origFieldStr = "";
            String usedFieldStr = "";

            for (int i = 0; i < formulaList.size(); i++) {

                JSONObject f = ((JSONArray) formulaList).getJSONObject(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fvalue", f.getString("fvalue"));
                jsonObject.put("formula", f.getString("formula"));
                jsonObject.put("idx", f.getString("idx"));
                jsonArrayFormula.add(jsonObject);

                List<Object> farrList = new ArrayList<>();
                String formula = f.getString("formula");
                Pattern pattern = Pattern.compile("@[a-zA-Z0-9_\u4e00-\u9fa5()（）-]+@");
                Matcher matcher = pattern.matcher(formula);
                while (matcher.find()) {
                    String fieldCN = matcher.group(0).replace("@", "");
                    Map<String, Object> fieldMap = new HashMap<String, Object>();
                    fieldMap.put("userId", paramMap.get("userId"));
                    fieldMap.put("engineId", paramMap.get("engineId"));
                    fieldMap.put("fieldCn", fieldCN);

                    Field field = fieldMapper.findByFieldCn(fieldMap);

                    if (field.getOrigFieldId() == null) {
                        if (origFieldStr.equals("")) {
                            origFieldStr = Long.toString(field.getId());
                        } else {
                            origFieldStr = origFieldStr + "," + field.getId();
                        }
                    } else {
                        if (origFieldStr.equals("")) {
                            origFieldStr = field.getOrigFieldId();
                        } else {
                            origFieldStr = origFieldStr + "," + field.getOrigFieldId();
                        }
                    }
                    usedFieldStr = usedFieldStr + field.getId() + ","; //拼凑该字段引用的字段id
                }
//				}
            }

            paramMap.put("formulaShow", JSON.toJSONString(jsonArrayFormula));
            //合并原生字段id
            if (!origFieldStr.equals("")) {
                paramMap.put("origFieldId", getUniqueStr(origFieldStr).toString());
            }

            //合并引用字段id
            if (!usedFieldStr.equals(",") && !usedFieldStr.equals("")) {
                usedFieldStr = usedFieldStr.substring(0, usedFieldStr.length() - 1);
                paramMap.put("usedFieldId", getUniqueStr(usedFieldStr).toString());
            }

        } else if (paramMap.containsKey("fieldCondList") && !paramMap.get("fieldCondList").equals("")) {
            //条件区域的使用字段和原生字段获取
			
			/*
			  fieldContent=[{"fieldContent2":"[{\"fieldId\":\"3\",\"operator\":\">\",\"fieldValue\":\"200\",\"logical\":\"&&\"}
				  							  ,{\"fieldId\":\"11\",\"operator\":\"<\",\"fieldValue\":\"50\"}]","conditionValue":"5","fieldValue":"50"}
						   ,{"fieldContent2":"[{\"fieldId\":\"12\",\"operator\":\"in\",\"fieldValue\":\"z\",\"logical\":\"&&\"}
				  							  ,{\"fieldId\":\"11\",\"operator\":\">\",\"fieldValue\":\"200\",\"logical\":\"&&\"}
				  							  ,{\"fieldId\":\"31\",\"operator\":\">\",\"fieldValue\":\"1000\"}]","conditionValue":"8","fieldValue":"1000"}
						   ,{"fieldContent2":"[{\"fieldId\":\"31\",\"operator\":\">\",\"fieldValue\":\"4000\"}]","conditionValue":"9","fieldValue":"4000"}]
			*/
            String fieldContent = (String) paramMap.get("fieldCondList");
            List<Object> fieldContentList = new ArrayList<>();
            fieldContentList = JSONObject.parseArray(fieldContent);

            String origFieldStr = "";
            String usedFieldStr = "";

            for (int i = 0; i < fieldContentList.size(); i++) {
                JSONObject fc = ((JSONArray) fieldContentList).getJSONObject(i);
                List<Object> farrList = new ArrayList<>();
                if (!fc.getString("fieldSubCond").equals("") && fc.getString("fieldSubCond") != null) {
                    farrList = JSONObject.parseArray(fc.getString("fieldSubCond"));
                    for (int j = 0; j < farrList.size(); j++) {
                        JSONObject ObjField = ((JSONArray) farrList).getJSONObject(j);
                        usedFieldStr = usedFieldStr + ObjField.get("fieldId") + ",";

                        Map<String, Object> fieldMap = new HashMap<String, Object>();
                        fieldMap.put("userId", paramMap.get("userId"));
                        fieldMap.put("engineId", paramMap.get("engineId"));
                        fieldMap.put("id", ObjField.get("fieldId"));
                        Field field = fieldMapper.findByFieldId(fieldMap);

                        if (field.getOrigFieldId() == null) {
                            if (origFieldStr.equals("")) {
                                origFieldStr = Long.toString(field.getId());
                            } else {
                                origFieldStr = origFieldStr + "," + field.getId();
                            }
                        } else {
                            if (origFieldStr.equals("")) {
                                origFieldStr = field.getOrigFieldId();
                            } else {
                                origFieldStr = origFieldStr + "," + field.getOrigFieldId();
                            }
                        }
                    }
                }
            }
            //合并引用字段id
            if (!usedFieldStr.equals(",") && !usedFieldStr.equals("")) {
                usedFieldStr = usedFieldStr.substring(0, usedFieldStr.length() - 1);
                paramMap.put("usedFieldId", getUniqueStr(usedFieldStr).toString());
            }
            //合并原生字段id
            if (!origFieldStr.equals("")) {
                paramMap.put("origFieldId", getUniqueStr(origFieldStr).toString());
            }
        }

        Long id = Long.valueOf(paramMap.get("userId").toString());
        //检查字段id是否归属该用户存在
        Field oldFieldVo = new Field();
        oldFieldVo = fieldMapper.findByFieldId(paramMap);
        if (!oldFieldVo.getId().equals(null)) {
            fieldMapper.updateField(paramMap);

            fieldCondMapper.deleteFieldCondById(id);

            String fieldContent = (String) paramMap.get("fieldCondList");
            List<FieldCond> fieldCondVoList = new ArrayList<FieldCond>();
            List<Object> condList = new ArrayList<>();
            if (!fieldContent.equals("")) {
                condList = JSONObject.parseArray(fieldContent);
                for (int i = 0; i < condList.size(); i++) {
                    JSONObject cond = ((JSONArray) condList).getJSONObject(i);
                    List<Object> subCondList = new ArrayList<>();
                    subCondList = JSONObject.parseArray(cond.getString("fieldSubCond"));
                    for (int j = 0; j < subCondList.size(); j++) {
                        JSONObject subCond = ((JSONArray) subCondList).getJSONObject(j);
                        FieldCond fieldCondVo = new FieldCond();
                        fieldCondVo.setFieldId(id);
                        fieldCondVo.setConditionValue(cond.getString("conditionValue"));
                        fieldCondVo.setContent(cond.getString("fieldSubCond"));
                        fieldCondVo.setCondFieldId(Long.valueOf(subCond.getString("fieldId")));
                        fieldCondVo.setCondFieldOperator(subCond.getString("operator"));
                        fieldCondVo.setCondFieldValue(subCond.getString("fieldValue"));
                        fieldCondVo.setCondFieldLogical(subCond.getString("logical"));
                        fieldCondVoList.add(fieldCondVo);
                    }
                }
                fieldCondMapper.createFieldCond(fieldCondVoList);
            }
            return true;
        } else
            return false;
    }

    /**
     * 查找继承自某字段的所有字段id拼成逗号分隔的字符串返回
     *
     * @return
     */
    public String getField(String fieldIds, String usedFieldId, String engineId) {

        Map<String, Object> param = new HashMap<String, Object>();
        Long userId = SessionManager.getLoginAccount().getUserId();
        param.put("userId", userId);
        param.put("fieldId", usedFieldId);
        param.put("engineId", engineId);

        fieldIds = "";

        String str = fieldMapper.checkField(param);

        if (str != null && str.length() >= 0) {

            String arrIds[] = str.split(",");
            for (int i = 0; i < arrIds.length; i++) {
                if (fieldIds.equals("")) {
                    fieldIds = getField("", arrIds[i], engineId);
                } else {
                    fieldIds = fieldIds + "," + getField("", arrIds[i], engineId);
                }

            }
        } else {
            return usedFieldId;
        }
        return fieldIds;
    }

    /**
     * 查找某字段的所有组成字段id并与该字段一起拼成逗号分隔的字符串返回，用于拷贝字段时检查组成该字段的所有子字段
     *
     * @return
     */
    @Override
    public String getSourceField(String fieldIds, String fieldId) {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        Long userId = SessionManager.getLoginAccount().getUserId();
        paramMap.put("userId", userId);
        paramMap.put("fieldId", fieldId);

        fieldIds = "";

        //String origFieldId = inputParam.get("origFieldId");
        String usedFieldId = fieldMapper.getSourceField(paramMap);

        if (usedFieldId != null && usedFieldId.length() >= 0) {
            //fieldIds = usedFieldId;
            String arrIds[] = usedFieldId.split(",");
            for (int i = 0; i < arrIds.length; i++) {
                if (fieldIds.equals(""))
                    fieldIds = getSourceField("", arrIds[i]);
                else
                    fieldIds = fieldIds + "," + getSourceField("", arrIds[i]);
            }
        } else {
            return fieldId;
        }

        return fieldIds;
    }

    /**
     * 公共检查字段方法 供删除、停用、编辑提交时校验用
     *
     * @return
     */
    @Override
    public Map<String, Object> checkField(Map<String, Object> paramMap) {

        boolean beUsed = false;

        List<Field> fieldList = new ArrayList<Field>();
        List<ListDb> listDbList = new ArrayList<ListDb>();
        List<Rule> ruleList = new ArrayList<Rule>();
        List<Scorecard> scorecardList = new ArrayList<Scorecard>();

        String fieldIds = "";

        String fieldId = (String) paramMap.get("fieldId");
        String s = getField("", fieldId, (String) paramMap.get("engineId"));

        //如果字段没有被引用不做字段有效性校验
        if (!s.equals("") && !s.equals(fieldId)) {
            fieldIds = getUniqueStr(s).toString();
            List<Long> Ids = new ArrayList<Long>();
            Ids = StringUtil.toLongList(fieldIds);
            paramMap.put("Ids", Ids);
            if (!fieldIds.equals("") && fieldIds != null) {
                //校验字段，命中则b改为true
                fieldList = fieldMapper.findFieldByIdsForCheckField(paramMap);
                if (fieldList.size() > 0)
                    beUsed = true;
            }
            s = fieldId + "," + s; //把自身字段加入检查序列，为后续检查做准备
        } else {
            s = fieldId;
        }

        fieldIds = getUniqueStr(s).toString();
        List<Long> Ids = new ArrayList<Long>();
        Ids = StringUtil.toLongList(fieldIds);
        paramMap.put("Ids", Ids);

        //校验数据管理里的黑白名单库，命中则b改为true
        String listDbIdStr = "";
        List<Long> listDbIds = new ArrayList<Long>();
        for (Iterator iterator = Ids.iterator(); iterator.hasNext(); ) {
            Long Id = (Long) iterator.next();
            paramMap.put("fieldId", Id);
            String str = listDbMapper.checkByField(paramMap);
            if (str != null) {
                if (listDbIdStr.equals(""))
                    listDbIdStr = str;
                else
                    listDbIdStr = listDbIdStr + "," + str;
            }

        }
        if (!listDbIdStr.equals("") && !listDbIdStr.equals(",")) {
            String str = getUniqueStr(listDbIdStr).toString();
            listDbIds = StringUtil.toLongList(str);
            paramMap.put("listDbIds", listDbIds);
            listDbList = listDbMapper.findListDbByIds(paramMap);
        }
        if (listDbList.size() > 0)
            beUsed = true;

        //校验规则管理的规则，命中则b改为true
        paramMap.put("fieldIds", Ids);
//        ruleList = ruleMapper.checkByField(paramMap);
//        if (ruleList.size() > 0)
//            beUsed = true;

        //校验引擎管理-知识库-评分卡
//        scorecardList = scorecardMapper.checkByField(paramMap);
//        if (scorecardList.size() > 0)
//            beUsed = true;

        //<待完善>校验引擎管理-决策流节点（决策选项-客户分群）

        paramMap.put("fieldList", fieldList);
        paramMap.put("listDbList", listDbList);
        paramMap.put("ruleList", ruleList);
        paramMap.put("scorecardList", scorecardList);
        paramMap.put("beUsed", beUsed);

        return paramMap;

    }

    @Override
    public Map<String, Object> updateStatus(Map<String, Object> paramMap) {

        boolean result = false;

        List<Long> Ids = (List<Long>) paramMap.get("Ids");
        paramMap.put("Ids", Ids);

        if (paramMap.containsKey("status") && !paramMap.get("status").equals("1")) {//停用、删除特殊处理需要增加校验

            for (Iterator iterator = Ids.iterator(); iterator.hasNext(); ) {
                Long Id = (Long) iterator.next();
                paramMap.put("fieldId", Id.toString());
                checkField(paramMap);
                if ((boolean) paramMap.get("beUsed")) {
                    break; // 遇到第一个被使用的就跳出循环来
                }
            }

            if (!(boolean) paramMap.get("beUsed")) {
                paramMap.put("Ids", Ids);
                result = fieldUserMapper.updateStatus(paramMap);
            }

        } else if (paramMap.containsKey("listType") && paramMap.get("listType").equals("cabage")
                && paramMap.get("status").equals("1")) {//回收站里删除状态变为启用状态

            result = backEngFieldType(paramMap);

        } else {//停用变启用
            result = fieldUserMapper.updateStatus(paramMap);
        }

        paramMap.put("result", result);

        return paramMap;
    }

    @Override
    public List<Field> findByFieldType(Map<String, Object> paramMap) {

        if (!paramMap.containsKey("fType")) {
            return null;
            // throw new ApiException(ErrorCodeEnum.SERVER_ERROR.getVersionCode(), ErrorCodeEnum.SERVER_ERROR.getMessage());
        }
        Integer fType = Integer.valueOf(paramMap.get("fType").toString());

        switch (fType) {
            case 1:
                // paramMap.put("useSql", false);
                // paramMap.put("derivative", false);
                break;
            case 2:
                // paramMap.put("useSql", true);
                break;
            case 3:
                // paramMap.put("derivative", true);
                break;
            case 4:
                // paramMap.put("interface", true);
                break;
            case 5:
                break;
            case 6:
                break;
            default:
                throw new ApiException(ErrorCodeEnum.SERVER_ERROR.getCode(), ErrorCodeEnum.SERVER_ERROR.getMessage());
        }

        return fieldMapper.findByFieldType(paramMap);
    }

    @Override
    public int isExists(Map<String, Object> paramMap) {
        return fieldMapper.isExists(paramMap);
    }

    @Override
    public Field findByFieldId(Map<String, Object> paramMap) {
        return fieldMapper.findByFieldId(paramMap);
    }

    @Override
    public List<Field> findByUser(Map<String, Object> paramMap) {
        return fieldMapper.findByUser(paramMap);
    }

    @Override
    public List<Field> getFieldList(Map<String, Object> paramMap) {
        return fieldMapper.getFieldList(paramMap);
    }

    @Override
    public boolean bindEngineField(Map<String, Object> paramMap) {

        Long userId = SessionManager.getLoginAccount().getUserId();
        Long organId = SessionManager.getLoginAccount().getOrganId();
        paramMap.put("userId", userId);
        paramMap.put("organId", organId);

        //获取所有字段id
        String iFieldIds = (String) paramMap.get("fieldIds");
        String oFieldIds = iFieldIds;
        if (iFieldIds != null && iFieldIds.length() >= 0) {
            String arrIds[] = iFieldIds.split(",");
            for (int i = 0; i < arrIds.length; i++) {
                oFieldIds = oFieldIds + "," + getSourceField("", arrIds[i]);
            }
        }
        String strFieldIds = getUniqueStr(oFieldIds).toString();

        //把不存在字段关系绑定在一起
        if (!strFieldIds.equals("") && strFieldIds != null) {
            //获取所有字段类型id
            List<Long> fieldIds = StringUtil.toLongList(strFieldIds);
            paramMap.put("fieldIds", fieldIds);
            fieldUserMapper.batchBindEngineFieldUserRel(paramMap);
        }


        String strFieldTypeIds = fieldMapper.findOrgFieldTypeIdsByIds(paramMap);
        if (!strFieldTypeIds.equals("") && strFieldTypeIds != null) {

            String parentFieldTypeIds = "";
            //查所有字段类型id的父id
            if (!strFieldTypeIds.equals("")) {
                strFieldTypeIds = getUniqueStr(strFieldTypeIds).toString();
                String arrIds[] = strFieldTypeIds.split(",");

                for (int i = 0; i < arrIds.length; i++) {
                    if (parentFieldTypeIds.equals("")) {
                        parentFieldTypeIds = getAllParentFieldTypeId("", arrIds[i], "");
                    } else {
                        parentFieldTypeIds = parentFieldTypeIds + "," + getAllParentFieldTypeId("", arrIds[i], "");
                    }
                }
            }

            if (!parentFieldTypeIds.equals("")) {
                strFieldTypeIds = strFieldTypeIds + "," + parentFieldTypeIds;
            }
            List<Long> fieldTypeIds = StringUtil.toLongList(strFieldTypeIds);

            paramMap.put("fieldTypeIds", fieldTypeIds);
            fieldTypeUserMapper.batchBindEngineFieldTypeUserRel(paramMap);
        }

        return true;
    }

    @Override
    public Map<String, Object> importExcel(String url, Map<String, Object> paramMap) {
        Map<String, Object> resultMap = new HashMap<>();

        InputStream is = null;
        Workbook Workbook = null;
        Sheet Sheet;
        try {
            is = new FileInputStream(url);
            Workbook = WorkbookFactory.create(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Field> fieldVoList = new ArrayList<Field>();
        List<String> fieldEnList = new ArrayList<>();
        int sucRows = 0; // 导入成功行数
        int failRows = 0; // 导入失败行数
        int repeatRows = 0; // 重复行数
        int existRows = 0; // 已存在的字段

        // 循环工作表 Sheet
        for (int numSheet = 0; numSheet < Workbook.getNumberOfSheets(); numSheet++) {
            Sheet = Workbook.getSheetAt(numSheet);
            if (Sheet == null) {
                continue;
            }
            // 循环行 Row
            for (int rowNum = 1; rowNum <= Sheet.getLastRowNum(); rowNum++) {
                try {
                    Row Row = Sheet.getRow(rowNum);
                    if (Row == null) {
                        continue;
                    }
                    Field fieldVo = new Field();
                    fieldVo.setAuthor(Long.valueOf(paramMap.get("author").toString()));
                    fieldVo.setIsCommon(Integer.valueOf(paramMap.get("isCommon").toString()));

                    // 循环单元格 Cell
                    for (int cellNum = 0; cellNum <= Row.getLastCellNum(); cellNum++) {
                        Cell cell = Row.getCell(cellNum);
                        String cellStr = ExcelUtil.getCellValue(cell).trim();
                        switch (cellNum) { // 逐单元格处理

                            case 0:
                                fieldVo.setFieldEn(cellStr);
                                break;
                            case 1:
                                fieldVo.setFieldCn(cellStr);
                                break;
                            case 2:
                                paramMap.put("fieldType", cellStr);
                                Long fieldTypeId = fieldTypeMapper.findIdByFieldType(paramMap);
                                if (fieldTypeId != 0)
                                    fieldVo.setFieldTypeId(fieldTypeId);
                                else
                                    fieldVo.setFieldTypeId(new Long(0)); //异常1：如果字段类型没法匹配，如何处理？
                                break;
                            case 3:
                                Integer valueType = 0;
                                if (cellStr.equals("数值型")) {
                                    valueType = 1;
                                }
                                if (cellStr.equals("字符型")) {
                                    valueType = 2;
                                }
                                if (cellStr.equals("枚举型")) {
                                    valueType = 3;
                                }
                                if (cellStr.equals("小数型")) {
                                    valueType = 4;
                                }
                                fieldVo.setValueType(valueType);
                                break;
                            case 4:
                                fieldVo.setValueScope(cellStr);
                                break;
                            case 5:
                                if (ExcelUtil.getCellValue(cell).equals("Y")) {
                                    fieldVo.setIsDerivative(1);
                                } else {
                                    fieldVo.setIsDerivative(0);
                                }
                                break;
                            case 6:
                                if (cellStr.equals("Y")) {
                                    fieldVo.setIsOutput(1);
                                } else if (cellStr.equals("N")) {
                                    fieldVo.setIsOutput(0);
                                }
                                break;
                            case 7://这里只处理公式原始值，不确定回显字段及字段绑定
                                fieldVo.setFormula(cellStr);
                                break;
                            default:
                                break;
                        }
                    }
                    if (fieldVo.getFieldEn() != null) {
                        paramMap.put("fieldEn", fieldVo.getFieldEn());
                        Field OldFieldVo = fieldMapper.findByFieldName(paramMap);
                        if (OldFieldVo != null) {
                            existRows++;
                            // fieldVo.setUserId(OldFieldVo.getUserId());
                            // 不能直接更新字段，需先修改已使用到的地方
                            // fieldMapper.updateField(paramMap);
                        } else {
                            // 防止重复字段
                            if (fieldEnList.contains(fieldVo.getFieldEn())) {
                                repeatRows++;
                            } else {
                                sucRows++;
                                // 加入到list，等待批量更新
                                fieldVoList.add(fieldVo);
                                fieldEnList.add(fieldVo.getFieldEn());
                            }
                        }
                    }
                } catch (Exception e) {
                    failRows++;
                    e.printStackTrace();
                }
            }// end for Row
        }// end first sheet
        if (fieldVoList.size() > 0) {
            fieldMapper.batchCreateField(fieldVoList);
            paramMap.put("status", 1);// 导入后字段状态默认启用
            fieldUserMapper.batchCreateFieldUserRel(paramMap);
        }
        resultMap.put("sucRows", sucRows);
        resultMap.put("failRows", failRows);
        resultMap.put("repeatRows", repeatRows);
        resultMap.put("existRows", existRows);
        return resultMap;
    }

    /**
     * 查找某字段类型所有的<子类型>拼成逗号分隔的字符串
     *
     * @return
     */
    public String getAllFieldTypeId(String ids, String pid, String engineId) {

        Map<String, Object> param = new HashMap<String, Object>();
        Long userId = SessionManager.getLoginAccount().getUserId();
        param.put("userId", userId);
        param.put("engineId", engineId);
        param.put("parentId", pid);

        String sid = fieldTypeMapper.findTypeIdByParentId(param);
        if (sid != null && sid.length() > 0) {
            if (ids.equals(""))
                ids = sid;
            else
                ids = ids + "," + sid;

            String arrIds[] = sid.split(",");
            for (int i = 0; i < arrIds.length; i++) {
                String str = getAllFieldTypeId("", arrIds[i], engineId);
                if (!str.equals(""))
                    ids = ids + "," + str;
            }
        }
        return ids;
    }

    /**
     * 查找某字段类型所有的<父类型>拼成逗号分隔的字符串
     *
     * @return
     */
    public String getAllParentFieldTypeId(String ids, String id, String engineId) {

        Map<String, Object> param = new HashMap<String, Object>();
        Long userId = SessionManager.getLoginAccount().getUserId();
        param.put("userId", userId);
        if (engineId == null || engineId.equals("")) {
            engineId = null;
        }
        param.put("engineId", engineId);
        param.put("fieldTypeId", id);

        String pid = fieldTypeMapper.findParentIdByTypeId(param);
        String s = "";
        if (!pid.equals("0")) {
            ids = id + "," + getAllParentFieldTypeId("", pid, engineId);
        } else {
            return id;
        }

        return ids;
    }

    /**
     * 从回收站还原一个或多个同类型或不同类型字段同时还原字段类型
     *
     * @return
     */
    public boolean backEngFieldType(Map<String, Object> paramMap) {

        Long userId = SessionManager.getLoginAccount().getUserId();
        paramMap.put("userId", userId);

        String basicFieldTypeIds = fieldMapper.findFieldTypeIdsByFieldId(paramMap);

        String strFieldTypeIds = basicFieldTypeIds;

        String arrIds[] = basicFieldTypeIds.split(",");
        for (int i = 0; i < arrIds.length; i++) {
            String str = getAllParentFieldTypeId("", arrIds[i], (String) paramMap.get("engineId"));
            if (!str.equals("")) {
                strFieldTypeIds = strFieldTypeIds + "," + str;
            }
        }

        //更新指定字段状态为1
        boolean f = fieldUserMapper.backFieldByIds(paramMap);

        //更新指定类型节点状态为1
        List<Long> fieldTypeIds = StringUtil.toLongList(strFieldTypeIds);
        paramMap.put("fieldTypeIds", fieldTypeIds);
        boolean ft = fieldTypeMapper.backFieldTypeByTypeIds(paramMap);
        //ft 有两种情况：true是通过删除树节点还原会更新，如果通过删除字段还原，执行结果是false.
        boolean result = false;
        if (f)
            result = true;

        return result;

    }

    @Override
    public int isExistsFieldType(Map<String, Object> paramMap) {
        return fieldTypeMapper.isExists(paramMap);
    }

    /**
     * 生成字符型随机数
     *
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成指定范围内的随机整数
     *
     * @return
     */
    public static int getRandomInt(String minS, String maxS) {

        int min = 0, max = 0;

        if (minS.indexOf(".") >= 0) { // 3.90 | .9
            minS = minS.substring(0, minS.indexOf("."));
        }

        if (maxS.indexOf(".") >= 0) { // 3.90
            maxS = maxS.substring(0, maxS.indexOf("."));
        }

        if (maxS.equals("") && !minS.equals("")) { // (4,) 右开区间
            min = Integer.parseInt(minS);
            max = min + 10000;
        } else if (minS.equals("") && !maxS.equals("")) { // (,10) 左开区间
            max = Integer.parseInt(maxS);
            min = max - 10000;
        } else if (!minS.equals("") && !maxS.equals("")) {// (4,10) 左右闭区间
            min = Integer.parseInt(minS);
            max = Integer.parseInt(maxS);
        }

        Random random = new Random();
        int i = random.nextInt(max) % (max - min + 1) + min;

        return i;

    }

    @Override
    public int updateFieldFolder(UpdateFolderParam param) {
        int result = fieldMapper.updateFieldFolder(param);
        return result;
    }

    @Override
    public String getFieldEnById(Long id) {
        return fieldMapper.findFieldNameById(id);
    }

    @Override
    public List<Field> queryByIds(Collection<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return fieldMapper.selectByIds(ids);
    }

    @Override
    public List<Field> queryByEns(Collection<String> ens) {
        if (ens == null || ens.size() == 0) {
            return new ArrayList<>();
        }
        return fieldMapper.selectByEns(ens);
    }

    @Override
    public List<Field> queryByOrganAndCns(Collection<String> cns, Long organId) {
        if (cns == null || cns.size() == 0) {
            return new ArrayList<>();
        }
        return fieldMapper.selectByOrganCns(cns, organId);
    }

    public void sqlFieldCheck(Map map) {
        if (map.containsKey("sqlStatement")) {
            Object sqlStatement = map.get("sqlStatement");
            if (sqlStatement != null && !"".equals(sqlStatement)) {
                String param = sqlStatement.toString().toUpperCase();
                for (String match : KEY_WORDS) {
                    if (param.contains(match)) {
                        throw new ApiException(ErrorCodeEnum.SQL_FIELD_HAVE_RISK.getCode(), ErrorCodeEnum.SQL_FIELD_HAVE_RISK.getMessage() + ":" + match);
                    }
                }
            }
        }
    }

    @Override
    public int countFieldByOrganId(Long organId) {
        Map<String, Object> map = new HashMap<>();
        map.put("organId", organId);
        int result = fieldUserMapper.countFieldByOrganId(map);
        return result;
    }

    @Override
    public List<Map<String, Object>> countFieldGroupByType(Long organId) {
        Map<String, Object> map = new HashMap<>();
        map.put("organId", organId);
        List<Map<String, Object>> result = fieldUserMapper.countFieldGroupByType(map);
        return result;
    }

    @Override
    public Field queryById(Long id) {
        Field field = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getPrimaryKey(TableEnum.T_FIELD, id);
            field = redisManager.getByPrimaryKey(key, Field.class);
        } else {
            field = fieldMapper.selectById(id);
        }
        return field;
    }

    @Override
    public List<Field> findFieldByIdsbyorganId(Long organId, List<Long> ids) {
        List<Field> fieldList = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            List<String> keys = RedisUtils.getPrimaryKey(TableEnum.T_FIELD, ids);
            fieldList = redisManager.hgetAllBatchByPrimaryKeys(keys, Field.class);
        } else {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("organId", organId);
            paramMap.put("Ids", ids);
            fieldList = fieldMapper.findFieldByIdsbyorganId(paramMap);
        }
        return fieldList;
    }

    @Override
    public List<Field> selectFieldListByEns(List<String> fieldEnList) {
        List<Field> fieldList = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            Long organId = RunnerSessionManager.getSession().getOrganId();
            List<String> keys = fieldEnList.stream().map(item -> {
                String fieldEnStr = Constants.fieldName.fieldEn + ":" + organId + ":" + item;
                String fieldEnKey = RedisUtils.getPrimaryKey(TableEnum.T_FIELD, fieldEnStr);
                return fieldEnKey;
            }).collect(Collectors.toList());

            fieldList = redisManager.hgetAllBatchByPrimaryKeys(keys, Field.class);

        } else {
            fieldList = fieldMapper.selectFieldListByEns(fieldEnList);
        }
        return fieldList;
    }

    @Override
    public Field findByFieldEnbyorganId(Long organId, String fieldEn) {
        Field field = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String fieldEnStr = Constants.fieldName.fieldEn + ":" + organId + ":" + fieldEn;
            String fieldEnKey = RedisUtils.getPrimaryKey(TableEnum.T_FIELD, fieldEnStr);
            field = redisManager.getByPrimaryKey(fieldEnKey, Field.class);
            // todo 是否需要status = 1判断
        } else {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("organId", organId);
            paramMap.put("fieldEn", fieldEn);
            field = fieldMapper.findByFieldEnbyorganId(paramMap);
        }
        return field;
    }

    @Override
    public Field findByFieldCnbyorganId(Long organId, String fieldCn) {
        Field field = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String fieldCnStr = Constants.fieldName.fieldCn + ":" + organId + ":" + fieldCn;
            String fieldCnKey = RedisUtils.getPrimaryKey(TableEnum.T_FIELD, fieldCnStr);
            field = redisManager.getByPrimaryKey(fieldCnKey, Field.class);
            // todo 是否需要status = 1判断
        } else {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("organId", organId);
            paramMap.put("fieldCn", fieldCn);
            field = fieldMapper.findByFieldCnbyorganId(paramMap);
        }
        return field;
    }
}
