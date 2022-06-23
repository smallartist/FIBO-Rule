package com.fibo.ddp.common.model.cignacmb.request;

import com.fibo.ddp.common.model.common.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLogParam extends BaseParam implements Serializable {
    private static final long serialVersionUID = -5236456602152208690L;
    /**
     * 事件流水id
     */
    private String eventRequestId;
    /**
     * 业务类型名称
     */
    private String businessName;
    /**
     * 业务子类型名称
     */
    private String businessChildName;
    /**
     * 模板ID
     */
    private String templateId;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 组织id
     */
    Long organId;
}
