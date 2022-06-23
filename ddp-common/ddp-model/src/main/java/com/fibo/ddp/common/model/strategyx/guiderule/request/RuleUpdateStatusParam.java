package com.fibo.ddp.common.model.strategyx.guiderule.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors
public class RuleUpdateStatusParam {
    private String ids;//userId
    private Integer status;//状态
}
