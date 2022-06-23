package com.fibo.ddp.common.service.datax.runner;

import com.fibo.ddp.common.model.datax.datamanage.Field;

import java.util.List;
import java.util.Map;

public interface CommonService {

    boolean getFieldByIds(List<Long> ids, Map<String, Object> inputParam);

    /**
     * 获取引擎节点所需的指标
     * @param fields
     * @param inputParam
     * @return
     */
    boolean getEngineField(List<Field> fields, Map<String, Object> inputParam);

    Map<String,Object> getFields(List<Field> fields, Map<String, Object> inputParam);

    /**
     * 获取衍生指标
     * @param inputParam
     */
    void getFieldResult(Map<String, Object> inputParam);
}
