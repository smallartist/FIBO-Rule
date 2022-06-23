package com.fibo.ddp.common.model.datax.datamanage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fibo.ddp.common.model.common.BasePage;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_field_type")
public class FieldType extends BasePage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 字段类型名
     */
    private String fieldType;

    /**
     * 父节点编号
     */
    private Integer parentId;

    /**
     * 是否组织定义的通用字段类型
     */
    private Integer isCommon;

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 字段类型的子类集合
     */
    private FieldType[] children;

    /**
     * 是否为父类
     */
    private String isParent = "true";

    /**
     * 引擎编号
     */
    @TableField(exist = false)
    private Integer engineId;

    /**
     * 文件夹图片路径
     */
    private String icon;


}
