package com.fibo.ddp.common.model.cignacmb.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLogDetailParam implements Serializable {
    private static final long serialVersionUID = 6175292951569720477L;
    /**
     * 规则执行记录表ids
     */
    private String ruleLogIds;
}
