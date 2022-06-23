package com.fibo.ddp.common.model.datax.datainterface.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors
public class InterfaceUpdateStatusParam {
    private Long[] ids;//userId
    private Integer status;//状态
}
