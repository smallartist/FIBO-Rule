package com.fibo.ddp.common.dao.strategyx.aimodel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.strategyx.aimodel.MachineLearningModels;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MachineLearningModelsMapper extends BaseMapper<MachineLearningModels> {

    /**
     * 获取组织下所有模型信息
     * @param organId
     * @return
     */
    List<MachineLearningModels> getModelsListByOrganId(@Param("organId") Integer organId, @Param("searchString") String searchString);
}