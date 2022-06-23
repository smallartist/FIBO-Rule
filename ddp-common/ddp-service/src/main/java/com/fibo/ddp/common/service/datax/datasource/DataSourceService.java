package com.fibo.ddp.common.service.datax.datasource;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.datax.datasource.DataSource;
import com.fibo.ddp.common.model.datax.datasource.request.DataSourceListParam;
import com.fibo.ddp.common.model.datax.datasource.vo.DataSourceVo;

import java.util.Map;

public interface DataSourceService extends IService<DataSource> {

    Integer saveDataSource(DataSourceVo dataSource);

    Integer updateDataSource(DataSourceVo dataSource);

    DataSourceVo getDataSourceById(Integer id);

    Map<String, Object> getDataSourceList(DataSourceListParam param);

    Integer deleteDataSourceById(Integer id);

    // runner
    DataSource getDataSourceByIdRunner(Integer id);
}
