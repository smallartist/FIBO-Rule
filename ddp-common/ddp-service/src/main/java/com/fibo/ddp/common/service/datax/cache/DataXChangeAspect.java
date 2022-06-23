package com.fibo.ddp.common.service.datax.cache;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;


@Aspect
@Component
public class DataXChangeAspect {
    @Autowired
    private DataXCacheService dataXCacheService;

    private HttpServletRequest request = null;

    @Pointcut("@annotation(com.fibo.ddp.common.service.datax.cache.DataXChange))")
    public void controllerAspect() {
    }
    @AfterReturning(pointcut = "controllerAspect() && @annotation(dataXChange)", returning = "returnValue")
    public void doAfter(JoinPoint joinPoint,DataXChange dataXChange, Object returnValue) {
        String changeName = dataXChange.changeName();
        System.out.println(changeName);
        dataXCacheService.refreshCacheInfo(changeName);
        dataXCacheService.pushRedisSub(null,dataXChange.changeName());
    }


}
