package com.fibo.ddp.common.service.monitor.logger;

import java.lang.annotation.*;

/**
 * ClassName:ArchivesLog <br/>
 * Description: 日志操作类型注解. <br/>
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
public @interface ArchivesLog {

    public String operationType() default "";  

    public String operationName() default ""; 
    
}
