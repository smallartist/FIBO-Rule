package com.fibo.ddp.common.model.analyse.vo;

import com.fibo.ddp.common.model.authx.system.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyseRequestParam {
    private Date start;
    private Date end;
    private Long engineId;
    private Long versionId;
    private SysUser user;
}
