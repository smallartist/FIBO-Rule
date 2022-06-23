package com.fibo.ddp.common.model.strategyx.dataclean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (DataCleanOriginalDataOp)表实体类
 *
 * @author makejava
 * @since 2022-03-21 11:15:01
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_data_clean_original_data_op")
public class DataCleanOriginalDataOp {
    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //数据清洗的版本id
    private Long dataCleanVersionId;
    //操作类型：1.首元素：first_element,2.尾元素：last_element,3.迭代处理：iteration 
    private String opType;
    //操作字段：此字段为数据清洗版本中存储的源数据指标内部字段
    private String opField;
    //父id
    private Long parentId;
    @TableField(exist = false)
    private DataCleanOriginalDataOp child;

    }

