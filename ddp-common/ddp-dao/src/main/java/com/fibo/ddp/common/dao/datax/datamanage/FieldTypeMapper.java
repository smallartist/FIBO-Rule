package com.fibo.ddp.common.dao.datax.datamanage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.datax.datamanage.FieldType;
import com.fibo.ddp.common.model.datax.datamanage.request.FieldTreeParam;

import java.util.List;
import java.util.Map;

public interface FieldTypeMapper extends BaseMapper<FieldType> {

    /**
     * getFieldTypeList:(查找用户的字段类型列表). <br/>
     *
     * @param paramMap 参数集合
     * @return 字段类型列表
     */
    public List<FieldType> getFieldTypeList(Map<String, Object> paramMap);

    /**
     * findTypeIdByParentId:(根据传入的字段类型父ID查找子类型ID). <br/>
     *
     * @param paramMap 参数集合
     * @return 子字段类型ID
     */
    public String findTypeIdByParentId(Map<String, Object> paramMap);

    /**
     * findTypeIdByParentId:(根据传入的字段类型类型ID查找父ID). <br/>
     *
     * @param paramMap 参数集合
     * @return 子字段类型ID
     */
    public String findParentIdByTypeId(Map<String, Object> paramMap);

    /**
     * createFieldType:(新增字段类型). <br/>
     *
     * @param fieldTypeVo 字段类型实体类
     * @return 插入成功
     */
    public boolean createFieldType(FieldType fieldTypeVo);

    /**
     * findIdByFieldType:(新增字段类型). <br/>
     *
     * @param paramMap paramMap
     * @return 字段类型编号
     */
    public long findIdByFieldType(Map<String, Object> paramMap);

    /**
     * updateFieldType:(更新字段类型名). <br/>
     *
     * @param paramMap 参数集合
     * @return 更新成功
     */
    public boolean updateFieldType(FieldTreeParam param);

    /**
     * backFieldTypeByTypeIds:(更新字段类型状态为启用状态(1)). <br/>
     *
     * @param paramMap 参数集合
     * @return 更新成功
     */
    public boolean backFieldTypeByTypeIds(Map<String, Object> paramMap);

    /**
     * isExists:(查找字段名是否存在). <br/>
     *
     * @param paramMap 参数集合
     * @return 存在的记录条数
     */
    public int isExists(Map<String, Object> paramMap);

    List<FieldType> selectFieldTypeList(FieldTreeParam param);
}
