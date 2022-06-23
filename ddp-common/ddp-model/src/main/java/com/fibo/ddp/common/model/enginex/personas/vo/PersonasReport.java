package com.fibo.ddp.common.model.enginex.personas.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonasReport {
    private String tagName;
    private String tagVersionCode;
    private Long tagId;
    private Long tagVersionId;
    private String tagValue;
    private int hitCount;
}
