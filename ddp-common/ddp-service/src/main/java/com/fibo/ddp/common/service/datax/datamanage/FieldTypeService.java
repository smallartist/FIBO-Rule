package com.fibo.ddp.common.service.datax.datamanage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.datax.datamanage.FieldType;
import com.fibo.ddp.common.model.datax.datamanage.request.FieldTreeParam;

import java.util.List;
import java.util.Map;

public interface FieldTypeService extends IService<FieldType> {

    /**
     * getFieldTypeList:(查找用户的字段类型列表). 支持查询组织通用字段类型、子类型 支持查询引擎自定义字段类型、子类型 <br/>
     *
     * @param paramMap 参数集合
     * @return 字段类型列表
     */
    public List<FieldType> getFieldTypeList(Map<String, Object> paramMap);

    /**
     * createFieldType:(新增字段类型). <br/>
     *
     * @param paramMap 参数集合
     * @return 插入成功
     */
    public boolean createFieldType(FieldType fieldTypeVo, Map<String, Object> paramMap);

    /**
     * updateFieldType:(更新字段类型名). <br/>
     *
     * @param paramMap 参数集合
     * @return 更新成功
     */
    public boolean updateFieldType(FieldTreeParam param);


    List<FieldType> getTreeList(FieldTreeParam param);
}
