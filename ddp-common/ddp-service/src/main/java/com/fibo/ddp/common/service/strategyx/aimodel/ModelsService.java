package com.fibo.ddp.common.service.strategyx.aimodel;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.aimodel.MachineLearningModels;

import java.util.List;

public interface ModelsService extends IService<MachineLearningModels> {

    /**
     * 获取组织下所有模型信息
     * @param organId
     * @return
     */
    List<MachineLearningModels> getModelsListByOrganId(Integer organId, String searchString);

    boolean checkNameNotRepeat(MachineLearningModels models);

    List<String> queryFieldEnByModelsId(Integer id);

    // runner
    MachineLearningModels selectById(Integer id);
}
