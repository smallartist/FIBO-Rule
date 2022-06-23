package com.fibo.ddp.common.model.strategyx.aimodel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("t_machine_learning_models")
public class MachineLearningModels {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String modelName;

    private String description;

    private String modelType;

    private String fileName;

    private String filePath;

    private String modelField;

    private String mappingField;

    private Integer status;

    private Integer creator;

    private Integer modifier;

    private Integer organId;

    private Date createTime;

    private Date updateTime;

    private String resultFieldEn;

    @TableField(exist = false)
    private List<StrategyOutput> strategyOutputList;

    /**
     * 模型编辑页面传值使用
     */
    @TableField(exist = false)
    private List<String> modelFieldArr;

    @TableField(exist = false)
    private List<Integer> mappingFieldArr;

    /**
     * 是否被选中（决策流页面使用）
     */
    @TableField(exist = false)
    private boolean checked;
}