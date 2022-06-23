package com.fibo.ddp.common.dao.datax.datamanage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.datax.datamanage.FieldCond;

import java.util.List;

public interface FieldCondMapper extends BaseMapper<FieldCond> {

    /**
     * createFieldCond:(生成条件关系). <br/>
     *
     * @param
     * @return 字段列表
     */
    public boolean createFieldCond(List<FieldCond> fieldCondVoList);

    /**
     * deleteFieldCondById:(删除字段的条件设置). <br/>
     *
     * @param
     * @return 是否删除成功
     */
    public boolean deleteFieldCondById(Long id);


    /**
     * getFieldCondList:(找出字段条件设置(去重)). <br/>
     *
     * @param
     * @return 字段列表
     */
    public List<FieldCond> getFieldCondList(Long fieldId);
}
