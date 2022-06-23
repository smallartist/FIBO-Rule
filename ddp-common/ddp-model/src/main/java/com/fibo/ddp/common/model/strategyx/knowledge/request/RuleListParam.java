package com.fibo.ddp.common.model.strategyx.knowledge.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleListParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pageNo = 1;
    private Integer pageSize = 10;

    private String status;
    private Integer engineId;

    private Integer parentId;
    private String parentIds;

    private String key;
    private String value;
    private Boolean isSearch = false;

}
