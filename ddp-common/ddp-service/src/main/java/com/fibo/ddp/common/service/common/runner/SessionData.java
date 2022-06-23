package com.fibo.ddp.common.service.common.runner;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class SessionData {

    private Long organId; // 组织id
    private Long engineId; // 引擎id
    private Integer reqType;//请求类型
}
