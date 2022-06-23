package com.fibo.ddp.monitor.controller.decisionflow;

import com.fibo.ddp.common.service.monitor.decisionflow.IMonitorService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MonitorCenterFactory implements ApplicationContextAware{
    private static Map<String, IMonitorService> monitorCenterDiffStorageBeanMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,IMonitorService> map = applicationContext.getBeansOfType(IMonitorService.class);
        monitorCenterDiffStorageBeanMap = new HashMap<>();
        map.forEach((key,value)->monitorCenterDiffStorageBeanMap.put(value.getStorageType(),value));
    }

    public static <T extends IMonitorService> T getMonitorCenterServiceImp(String storageType){
        return (T)monitorCenterDiffStorageBeanMap.get(storageType);
    }
}
