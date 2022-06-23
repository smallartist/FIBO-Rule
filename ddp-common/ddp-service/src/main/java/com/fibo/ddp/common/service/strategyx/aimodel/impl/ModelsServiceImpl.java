package com.fibo.ddp.common.service.strategyx.aimodel.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.aimodel.MachineLearningModelsMapper;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.strategyx.aimodel.MachineLearningModels;
import com.fibo.ddp.common.service.datax.datamanage.FieldService;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.aimodel.ModelsService;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ModelsServiceImpl extends ServiceImpl<MachineLearningModelsMapper, MachineLearningModels> implements ModelsService {

    @Autowired
    private MachineLearningModelsMapper machineLearningModelsMapper;
    @Resource
    private FieldService fieldService;

    @Autowired
    private RedisManager redisManager;

    @Value("${switch.use.cache}")
    private String cacheSwitch;

    @Override
    public List<MachineLearningModels> getModelsListByOrganId(Integer organId, String searchString) {
        return machineLearningModelsMapper.getModelsListByOrganId(organId, searchString);
    }

    @Override
    public boolean checkNameNotRepeat(MachineLearningModels models) {
        if (models == null || "".equals(models) || StringUtils.isBlank(models.getModelName())) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        MachineLearningModels machineLearningModels = new MachineLearningModels();
        machineLearningModels.setModelName(models.getModelName());
        machineLearningModels.setStatus(1);
        LambdaQueryWrapper<MachineLearningModels> wrapper = new LambdaQueryWrapper<>(machineLearningModels);
        if (models.getId() != null) {
            wrapper.ne(MachineLearningModels::getId, models.getId());
        }
        int count = this.count(wrapper);
        if (count > 0) {
            throw new ApiException(ErrorCodeEnum.SERVER_ERROR.getCode(), "模型名称重复");
        }
        return true;
    }

    @Override
    public List<String> queryFieldEnByModelsId(Integer id) {
        Set<String> fieldEns = new HashSet<>();
        MachineLearningModels models = machineLearningModelsMapper.selectById(id);
        if (models != null && models.getMappingField() != null && !"".equals(models.getMappingField())) {
            String[] split = models.getMappingField().split(",");
            for (String s : split) {
                fieldEns.add(fieldService.getFieldEnById(Long.valueOf(s)));
            }
        }
        return new ArrayList<>(fieldEns);
    }

    @Override
    public MachineLearningModels selectById(Integer id) {
        MachineLearningModels machineLearningModels = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getPrimaryKey(TableEnum.T_MACHINE_LEARNING_MODELS, id);
            machineLearningModels = redisManager.getByPrimaryKey(key, MachineLearningModels.class);
        } else {
            machineLearningModels = this.selectById(id);
        }

        return machineLearningModels;
    }

}
