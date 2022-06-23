package com.fibo.ddp.strategyx.aimodel.controller;

import com.fibo.ddp.common.dao.strategyx.aimodel.MachineLearningModelsMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.datax.datamanage.Field;
import com.fibo.ddp.common.model.strategyx.aimodel.MachineLearningModels;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.datax.datamanage.FieldService;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.service.strategyx.aimodel.ModelsService;
import com.fibo.ddp.common.service.strategyx.aimodel.PMMLExecutor.PMMLExecutor;
import com.fibo.ddp.common.service.strategyx.strategyout.StrategyOutputService;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import com.fibo.ddp.common.utils.constant.strategyx.StrategyType;
import org.apache.commons.lang3.StringUtils;
import org.jpmml.evaluator.Evaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * 模型相关接口
 */
@RestController
@RequestMapping(value = "models")
public class ModelsController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "PMMLExecutorRFImpl")
    private PMMLExecutor pmmlExecutor;
    @Autowired
    private ModelsService modelsService;
    @Resource
    public FieldService fieldService;
    @Resource
    private StrategyOutputService outputService;
    @Autowired
    private MachineLearningModelsMapper machineLearningModelsMapper;

    /**
     * 获取模型列表信息
     * @return
     */
    @RequestMapping(value = "getModelsList", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getModelsList(){
        SysUser sysUser = SessionManager.getLoginAccount();
        Integer organId = Integer.valueOf(sysUser.getOrganId().toString());
        try {
            List<MachineLearningModels> modelsList = modelsService.getModelsListByOrganId(organId, null);
            return ResponseEntityBuilder.buildNormalResponse(modelsList);
        } catch (Exception e) {
            logger.error("获取模型列表信息失败", e);
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
        }
    }

    /**
     * 获取组织下全量指标信息
     * @param
     * @return
     */
    @RequestMapping(value = "getAllFields", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getAllFields(){
        Map<String, Object> paramMap = new HashMap<>();
        SysUser sysUser = SessionManager.getLoginAccount();
        paramMap.put("organId", sysUser.getOrganId());
        paramMap.put("isOutput", 0);
        try {
            List<Map<String, Object>> resultList = new ArrayList<>();
            List<Field> fieldList = fieldService.getFieldList(paramMap);
            for(Field field : fieldList){
                Map<String, Object> map = new HashMap<>();
                map.put("key", field.getId());
                map.put("label", field.getFieldCn());
                resultList.add(map);
            }
            return ResponseEntityBuilder.buildNormalResponse(resultList);
        } catch (Exception e) {
            logger.error("获取全量指标信息失败", e);
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
        }
    }

    /**
     * 上传并解析模型文件
     * @param request
     * @return
     */
    @RequestMapping(value = "uploadAndParseFile", method = RequestMethod.POST)
    public ResponseEntityDto<Object> uploadAndParseFile(HttpServletRequest request){
        String fileName = "";
        String accessUrl = "";
        try {
            // 上传文件
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {
                    String uploadDir = request.getSession().getServletContext().getRealPath("/") + "upload/models/fieldUpload/";
                    if (!new File(uploadDir).exists()) {
                        File dir = new File(uploadDir);
                        dir.mkdirs();
                    }
                    fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    String path = uploadDir + fileName;
                    //上传
                    file.transferTo(new File(path));
                    accessUrl = path;
                }
            }

            // 解析文件
            Evaluator evaluator = pmmlExecutor.loadPmml(accessUrl);
            List<String> fieldList = pmmlExecutor.parseField(evaluator);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("fileName", fileName);
            resultMap.put("filePath", accessUrl);
            resultMap.put("fieldList", fieldList);
            return ResponseEntityBuilder.buildNormalResponse(resultMap);
        } catch (Exception e) {
            logger.error("上次解析模型文件失败", e);
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
        }
    }

    /**
     * 添加模型
     * @param models
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.SAVE_MODELS)
    public ResponseEntityDto<Object> saveModels(@RequestBody MachineLearningModels models){
        modelsService.checkNameNotRepeat(models);
        SysUser sysUser = SessionManager.getLoginAccount();
        models.setCreator(Integer.valueOf(sysUser.getUserId().toString()));
        models.setModifier(Integer.valueOf(sysUser.getUserId().toString()));
        models.setOrganId(Integer.valueOf(sysUser.getOrganId().toString()));
        try {
            models.setModelField(StringUtils.join(models.getModelFieldArr(), ","));
            models.setMappingField(StringUtils.join(models.getMappingFieldArr(), ","));
            int result = machineLearningModelsMapper.insert(models);
            if (result>0){
                outputService.insertTacticsOutput(models.getId().longValue(),models.getStrategyOutputList());
            }
            return ResponseEntityBuilder.buildNormalResponse(result);
        } catch (Exception e) {
            logger.error("保存模型信息失败", e);
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
        }
    }

    /**
     * 删除模型
     * @param id
     * @return
     */
    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ArchivesLog(operationType = OpTypeConst.DELETE_MODELS)
    public ResponseEntityDto<Object> deleteModels(@PathVariable Integer id){
        try {
            MachineLearningModels models = new MachineLearningModels();
            models.setId(id);
            models.setStatus(0);
            int result = machineLearningModelsMapper.updateById(models);
            return ResponseEntityBuilder.buildNormalResponse(result);
        } catch (Exception e) {
            logger.error("删除模型信息失败", e);
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
        }
    }

    /**
     *  修改模型
     * @param models
     * @return
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_MODELS)
    public ResponseEntityDto<Object> updateModels(@PathVariable Integer id , @RequestBody MachineLearningModels models){

        modelsService.checkNameNotRepeat(models);
        SysUser sysUser = SessionManager.getLoginAccount();
        models.setModifier(Integer.valueOf(sysUser.getUserId().toString()));
        models.setId(id);
        try {
            models.setModelField(StringUtils.join(models.getModelFieldArr(), ","));
            models.setMappingField(StringUtils.join(models.getMappingFieldArr(), ","));
            int result = machineLearningModelsMapper.updateById(models);
            outputService.updateTacticsOutput(id.longValue(),models.getStrategyOutputList(), StrategyType.MODELS);
            return ResponseEntityBuilder.buildNormalResponse(result);
        } catch (Exception e) {
            logger.error("修改模型信息失败", e);
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
        }
    }

    /**
     * 获取模型详细信息
     * @param id
     * @return
     */
    @RequestMapping(value = "getDetailInfo/{id}", method = RequestMethod.GET)
    public ResponseEntityDto<Object> getDetailInfo(@PathVariable Integer id){
        try {
            MachineLearningModels models = machineLearningModelsMapper.selectById(id);
            if(models != null){
                String modelField = models.getModelField();
                models.setModelFieldArr(StringUtils.isBlank(modelField) ? new ArrayList<>() : Arrays.asList(modelField.split(",")));

                String mappingField = models.getMappingField();
                List<Integer> mappingFieldArr = new ArrayList<>();
                if(StringUtils.isNotBlank(mappingField)){
                    String[] mappingFields = mappingField.split(",");
                    for(String str : mappingFields){
                        mappingFieldArr.add(Integer.valueOf(str));
                    }
                }
                models.setMappingFieldArr(mappingFieldArr);
                List<StrategyOutput> strategyOutputList = outputService.queryByTactics(new StrategyOutput(id.longValue(), StrategyType.MODELS));
                models.setStrategyOutputList(strategyOutputList);
            }
            return ResponseEntityBuilder.buildNormalResponse(models);
        } catch (Exception e) {
            logger.error("获取模型详细信息失败", e);
            return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
        }
    }

}
