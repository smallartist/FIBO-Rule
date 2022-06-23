package com.fibo.ddp.common.model.datax.datamanage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fibo.ddp.common.model.common.BasePage;
import com.fibo.ddp.common.model.datax.datamanage.vo.FieldSubCondVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("t_field_condition")
public class FieldCond extends BasePage implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 条件编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 字段编号
     */
    private Long fieldId;

    /**
     * 字段条件值
     */
    private String conditionValue;

    /**
     * 字段条件区域设置json格式
     */
    private String content;

    /**
     * 条件字段编号
     */
    private Long condFieldId;

    /**
     * 条件字段的运算符
     */
    private String condFieldOperator;

    /**
     * 条件字段的条件设置值
     */
    private String condFieldValue;

    /**
     * 条件字段间的逻辑符
     */
    private String condFieldLogical;

    /**
     * 创建时间
     */
    private Date created;
    @TableField(exist = false)
    private List<FieldSubCondVo> fieldSubCond;

}
