package com.fibo.ddp.common.model.datax.datamanage.request;

import lombok.Data;

import java.util.Date;

@Data
public class FieldCallParam {
    private Long organId;
    private Date queryTimeStart;
    private Date queryTimeEnd;
    private Integer fieldType;
    private String searchKey;
    private Long fieldId;
}
