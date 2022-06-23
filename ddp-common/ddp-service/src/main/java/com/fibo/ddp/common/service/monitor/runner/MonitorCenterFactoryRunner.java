package com.fibo.ddp.common.service.monitor.runner;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MonitorCenterFactoryRunner implements ApplicationContextAware{
    private static Map<String, IMonitorCenterService> monitorCenterDiffStorageBeanMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,IMonitorCenterService> map = applicationContext.getBeansOfType(IMonitorCenterService.class);
        monitorCenterDiffStorageBeanMap = new HashMap<>();
        map.forEach((key,value)->monitorCenterDiffStorageBeanMap.put(value.getStorageType(),value));
    }

    public static <T extends IMonitorCenterService> T getMonitorCenterServiceImp(String storageType){
        return (T)monitorCenterDiffStorageBeanMap.get(storageType);
    }
}
