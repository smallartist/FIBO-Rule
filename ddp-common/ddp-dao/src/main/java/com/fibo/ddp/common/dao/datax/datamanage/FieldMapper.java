package com.fibo.ddp.common.dao.datax.datamanage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.common.requestParam.UpdateFolderParam;
import com.fibo.ddp.common.model.datax.datamanage.Field;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FieldMapper extends BaseMapper<Field> {

    /**
     * findByFieldType:(根据字段类型名找出该用户可用的字段列表). <br/>
     *
     * @param paramMap 参数集合
     * @return 字段列表
     */
    public List<Field> findByFieldType(Map<String, Object> paramMap);

    /**
     * checkField:(查找引用该字段的所有字段). <br/>
     *
     * @param paramMap 参数集合
     * @return 字段id逗号分隔字符串
     */
    public String checkField(Map<String, Object> paramMap);

    /**
     * getSourceField:(查找构成该字段的子字段及原生字段). <br/>
     *
     * @param paramMap 参数集合
     * @return
     */
    public String getSourceField(Map<String, Object> paramMap);

    /**
     * findFieldTypeIdsByFieldId:(在引擎里找出一批字段id对应的唯一字段类型id串). <br/>
     *
     * @param paramMap 参数集合
     * @return 字段类型id逗号分隔字符串
     */
    public String findFieldTypeIdsByFieldId(Map<String, Object> paramMap);

    /**
     * findOrgFieldTypeIdsByIds:(在通用字段里找出一批字段id对应的唯一字段类型id串). <br/>
     *
     * @param paramMap 参数集合
     * @return 字段类型id逗号分隔字符串
     */
    public String findOrgFieldTypeIdsByIds(Map<String, Object> paramMap);

    /**
     * findFieldByIdsForCheckField:(找出一批字段id的字段列表（包含引擎引用的通用字段，engineId为空是不加engineId=null条件）). <br/>
     *
     * @param paramMap 参数集合
     * @return 字段列表
     */
    public List<Field> findFieldByIdsForCheckField(Map<String, Object> paramMap);

    /**
     * findByFieldName:(根据字段英文或中文名找出字段对象). <br/>
     *
     * @param paramMap 参数集合
     * @return 字段对象
     */
    public Field findByFieldName(Map<String, Object> paramMap);

    /**
     * findByFieldCn:(根据字段中文名找出字段对象). <br/>
     *
     * @param paramMap 参数集合
     * @return 字段对象
     */
    public Field findByFieldCn(Map<String, Object> paramMap);

    /**
     * findByUser:(找出该用户可用字段列表). <br/>
     *
     * @param paramMap 参数集合
     * @return 字段类型列表
     */
    public List<Field> findByUser(Map<String, Object> paramMap);

    /**
     * findByFieldId:(根据字段Id查找字段对象). <br/>
     *
     * @param paramMap 参数集合
     * @return 字段对象
     */
    public Field findByFieldId(Map<String, Object> paramMap);

    /**
     * createField:(添加字段). <br/>
     *
     * @param fieldVo 字段实体对象
     * @return 插入是否成功
     */
    public boolean createField(Field fieldVo);

    /**
     * batchCreateField:(批量添加字段). <br/>
     *
     * @param fieldVoList 字段实体对象list集合
     * @return 字段类型列表
     */
    public boolean batchCreateField(List<Field> fieldVoList);

    /**
     * updateField:(修改字段). <br/>
     *
     * @param paramMap 参数集合
     * @return 更新是否成功
     */

    public boolean updateField(Map<String, Object> paramMap);

    /**
     * isExists:(根据字段英文或中文名查找字段是否存在). <br/>
     *
     * @param paramMap 参数集合
     * @return 存在的记录条数
     */
    public int isExists(Map<String, Object> paramMap);

    /**
     * getFieldList:(获取组织的所有字段). <br/>
     *
     * @param paramMap 参数集合
     * @return
     */
    public List<Field> getFieldList(Map<String, Object> paramMap);



    public String findFieldNameById(Long fieldId);

    int updateFieldFolder(UpdateFolderParam param);

    List<Field> selectByIds(@Param("ids") Collection<Long> ids);

    List<Field> selectByEns(@Param("ens") Collection<String> ens);

    List<Field> selectByOrganCns(@Param("cns") Collection<String> cns, @Param("organId") Long organId);

    // runner
    /**
     * findFieldByIds:(找出一批字段id对应的字段列表). <br/>
     * @param paramMap 参数集合
     * @return 字段列表
     */
    public List<Field> findFieldByIdsbyorganId(Map<String, Object> paramMap);

    /**
     * findByFieldEn:(根据引擎和字段英文名找出引擎所用字段对象). <br/>
     * @param paramMap 参数集合
     * @return 字段对象
     */
    public Field findByFieldEnbyorganId(Map<String, Object> paramMap);

    /**
     * findByFieldCn:(根据字段中文名找出字段对象). <br/>
     * @param paramMap 参数集合
     * @return 字段对象
     */
    public Field findByFieldCnbyorganId(Map<String, Object> paramMap);

    List<Field> selectFieldListByEns(@Param("ens")List<String> ens);
}
