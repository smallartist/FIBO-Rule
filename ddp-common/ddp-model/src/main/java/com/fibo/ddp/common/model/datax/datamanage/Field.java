package com.fibo.ddp.common.model.datax.datamanage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fibo.ddp.common.model.common.BasePage;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("t_field")
public class Field extends BasePage implements Serializable {
    private static final long serialVersionUID = 7113453776727511877L;

    /**
     * 主键
     * */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 字段英文名
     * */
    private String fieldEn;

    /**
     * 字段中文名
     * */
    private String fieldCn;

    /**
     * 字段类型编号
     * */
    @TableField("field_typeid")
    private Long fieldTypeId;

    /**
     * 字段类型名
     * */
    @TableField(exist = false)
    private String fieldType;

    /**
     * 字段存值类型
     * */
    private Integer valueType;

    /**
     * 字段存值类型中文
     * */
    @TableField(exist = false)
    private String valueTypeName;

    /**
     * 字段约束范围
     * */
    private String valueScope;

    /**
     * 是否衍生字段
     * */
    private Integer isDerivative;

    /**
     * 是否衍生字段
     * */
    @TableField(exist = false)
    private String isDerivativeName;

    /**
     * 是否输出字段
     * */
    private Integer isOutput;

    /**
     * 是否输出字段
     * */
    @TableField(exist = false)
    private String isOutputName;

    /**
     * 是否组织定义的通用字段
     * */
    private Integer isCommon;

    /**
     * 衍生字段公式
     * */
    private String formula;

    /**
     * 衍生字段公式回显信息
     * */
    private String formulaShow;

    /**
     * 衍生字段引用的字段id
     * */
    @TableField("used_fieldid")
    private String usedFieldId;

    /**
     * 衍生字段引用的原生字段id
     * */
    @TableField("orig_fieldid")
    private String origFieldId;

    /**
     * 创建人
     * */
    private Long author;

    /**
     * 创建人昵称
     * */
    @TableField(exist = false)
    private String nickName;

    /**
     * 创建时间
     * */
    private Date created;

    /**
     * 归属的引擎ID
     * */
    @TableField(exist = false)
    private Long engineId;

    /**
     * 归属的引擎名称
     * */
    @TableField(exist = false)
    private String engineName;

    /**
     * 字段状态（启用、停用、删除、未知）
     * */
    @TableField(exist = false)
    private String status;

    /**
     * 字段条件设置集合
     * */
    @TableField(exist = false)
    private List<FieldCond> fieldCondList;

    /**
     * 字段用户关系编号
     * */
    @TableField(exist = false)
    private Long fieldRelId;

    /**
     * 是否使用sql获取指标
     */
    private Boolean isUseSql;

    /**
     * 使用sql获取指标时对应的数据源
     */
    private Integer dataSourceId;

    /**
     * 使用sql获取指标时对应的sql语句
     */
    private String sqlStatement;

    /**
     * sql变量配置
     */
    private String sqlVariable;

    /**
     * 是否使用接口
     */
    private Boolean isInterface;

    /**
     * 接口id
     */
    private Integer interfaceId;

    /**
     * 接口解析指标
     */
    private String interfaceParseField;

    /**
     * json类型对应的json值
     */
    private String jsonValue;
    /**
     * 字典变量例如 日期：date
     */
    private String dictVariable;

    /**
     * 该字段归属的组织编号
     * */
    private Long organId;

    /**
     * 指标类型
     */
    @TableField(exist = false)
    private Integer type;

    /**
     * 指标源类型
     */
    private Integer sourceType;

    /**
     * 消息队列源id
     */
    private Long mqSourceId;
}
