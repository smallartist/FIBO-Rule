package com.fibo.ddp.datax.realtime.controller.datasource;


import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.datax.consts.DataXCacheConst;
import com.fibo.ddp.common.model.datax.datasource.request.DataSourceListParam;
import com.fibo.ddp.common.model.datax.datasource.vo.DataSourceVo;
import com.fibo.ddp.common.service.datax.cache.DataXChange;
import com.fibo.ddp.common.service.datax.datasource.DataSourceService;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DataSourceService dataSourceService;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.DATA_SOURCE_SAVE)
    @DataXChange(changeName = DataXCacheConst.Type.DATA_SOURCE)
    public ResponseEntityDto<Object> save(@RequestBody DataSourceVo dataSourceVo) {
        Integer result = dataSourceService.saveDataSource(dataSourceVo);
        return ResponseEntityBuilder.buildNormalResponse(result);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.DATA_SOURCE_MODIFY)
    @DataXChange(changeName = DataXCacheConst.Type.DATA_SOURCE)
    public ResponseEntityDto<Object> update(@RequestBody DataSourceVo dataSourceVo) {
        Integer result = dataSourceService.updateDataSource(dataSourceVo);
        return ResponseEntityBuilder.buildNormalResponse(result);
    }

    @RequestMapping(value = "/getDataSource/{id}", method = RequestMethod.GET)
    public ResponseEntityDto<DataSourceVo> getDataSourceById(@PathVariable Integer id) {
        DataSourceVo dataSourceVo = dataSourceService.getDataSourceById(id);
        return ResponseEntityBuilder.buildNormalResponse(dataSourceVo);
    }

    @RequestMapping(value = "getDataSourceList", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getDataSourceList(@RequestBody DataSourceListParam param) {
        Map<String, Object> result = dataSourceService.getDataSourceList(param);
        return ResponseEntityBuilder.buildNormalResponse(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ArchivesLog(operationType = OpTypeConst.DATA_SOURCE_DELETE)
    @DataXChange(changeName = DataXCacheConst.Type.DATA_SOURCE)
    public ResponseEntityDto<Object> deleteDataSourceById(@PathVariable Integer id) {
        Integer result = dataSourceService.deleteDataSourceById(id);
        return ResponseEntityBuilder.buildNormalResponse(result);
    }

}
