package com.fibo.ddp.common.service.enginex.personas;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.enginex.personas.PersonasEngineResultDetail;
import com.fibo.ddp.common.model.enginex.personas.vo.PersonasReport;
import com.fibo.ddp.common.model.enginex.personas.vo.PersonasReportParam;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;


/**
 * (PersonasEngineResultDetail)表服务接口
 * @since 2022-01-06 14:24:57
 */
public interface PersonasEngineResultDetailService extends IService<PersonasEngineResultDetail>{

    /**
     * 通过ID查询单条数据
     * @param id 主键
     * @return 实例对象
     */
    PersonasEngineResultDetail queryById(Long id);

    /**
     * 分页查询多条数据
     */
    PageInfo<PersonasEngineResultDetail> queryByEntity(QueryListParam<PersonasEngineResultDetail> param) ;


    Map queryReportList(QueryListParam<PersonasReportParam> param);
}
