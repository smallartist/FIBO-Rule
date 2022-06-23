package com.fibo.ddp.common.model.strategyx.dataclean;

import lombok.Getter;

@Getter
public enum FilterCondEnum {

    INPUT_FILTER("input","策略--标签的条件"),
    RESULT_FILTER("result","策略--标签的条件");

    private String code;
    private String desc;

    FilterCondEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
