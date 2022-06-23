package com.fibo.ddp.common.service.datax.home.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.datax.datainterface.InterfaceInfo;
import com.fibo.ddp.common.model.datax.datasource.DataSource;
import com.fibo.ddp.common.model.monitor.logger.Logger;
import com.fibo.ddp.common.service.cignacmb.BusinessEventLogService;
import com.fibo.ddp.common.service.cignacmb.ITBusinessRuleRelService;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.datax.datamanage.FieldService;
import com.fibo.ddp.common.service.datax.datainterface.InterfaceService;
import com.fibo.ddp.common.service.datax.datasource.DataSourceService;
import com.fibo.ddp.common.service.datax.home.HomeService;
import com.fibo.ddp.common.service.monitor.logger.LogService;
import com.fibo.ddp.common.service.strategyx.collectionrule.ListOperationService;
import com.fibo.ddp.common.utils.util.DataHelp;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("dataXService")
public class DataXHomeServiceImpl implements HomeService {
    @Autowired
    LogService logService;
    @Autowired
    BusinessEventLogService businessEventLogService;
    @Autowired
    ListOperationService listOperationService;
    @Autowired
    ITBusinessRuleRelService itBusinessRuleRelService;
    @Autowired
    FieldService fieldService;
    @Autowired
    DataSourceService dataSourceService;
    @Autowired
    InterfaceService interfaceService;

    @Override
    public Map<String, Object> getIndexInfo() {
        Map<String, Object> map = new HashMap<>();
        SysUser sysUser = SessionManager.getLoginAccount();
        Long organId = sysUser.getOrganId();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("organId", organId);


        // 查询首页上次登录时间
        List<Date> lastLoginTimeList = logService.getLastLoginInfo(sysUser.getUserId());
        Date lastLoginTime = lastLoginTimeList.size() > 1 ? lastLoginTimeList.get(1) : lastLoginTimeList.get(0);

        // 查询首页当天活动日志
        Map<String, Object> param = new HashMap<>();
        param.put("organId", organId);
        param.put("startDate", DataHelp.getDay());
        param.put("endDate", DataHelp.getNowDate());
        PageHelper.startPage(1, 6);
        List<Logger> logList = logService.getLogList(param);
        logList = logList.stream().filter(item -> item.getOpType() != null && item.getOpType().startsWith("log")).collect(Collectors.toList());
        map.put("lastLoginTime", lastLoginTime);
        map.put("logList", logList);
        map.put("databaseCount", getDataBaseCount(organId));
        map.put("interfaceCount", getInterfaceCount(organId));
        map.put("fieldTypeGroup", getFieldTypeGroup(organId));
        map.put("fieldCount", getFieldNum(organId));
        return map;
    }

    private int getInterfaceCount(Long organId) {
        LambdaQueryWrapper<InterfaceInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterfaceInfo::getOrganId, organId);
        wrapper.ne(InterfaceInfo::getStatus, -1);
        return interfaceService.count(wrapper);
    }

    private int getDataBaseCount(Long organId) {
        LambdaQueryWrapper<DataSource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataSource::getOrganId, organId);
        wrapper.ne(DataSource::getStatus, -1);
        return dataSourceService.count(wrapper);
    }

    private int getFieldNum(Long organId) {
        return fieldService.countFieldByOrganId(organId);
    }

    private List<Map<String, Object>> getFieldTypeGroup(Long organId) {
        return fieldService.countFieldGroupByType(organId);
    }
}
