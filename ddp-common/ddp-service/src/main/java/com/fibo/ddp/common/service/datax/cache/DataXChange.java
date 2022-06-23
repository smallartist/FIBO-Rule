package com.fibo.ddp.common.service.datax.cache;

import java.lang.annotation.*;

/**
 * ClassName:DataXChange <br/>
 * Description: 日志操作类型注解. <br/>
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
public @interface DataXChange {

    public String changeName() default "";

    public String changeType() default "update";
    
}
