package com.fibo.ddp.common.service.enginex.personas;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.enginex.personas.PersonasEngineResult;
import com.github.pagehelper.PageInfo;


/**
 * (PersonasEngineResult)表服务接口
 * @since 2022-01-06 14:23:10
 */
public interface PersonasEngineResultService extends IService<PersonasEngineResult>{

    PersonasEngineResult queryById(Long id);
    /**
     * 分页查询多条数据
     */
    PageInfo<PersonasEngineResult> queryByEntity(QueryListParam<PersonasEngineResult> param);


}
