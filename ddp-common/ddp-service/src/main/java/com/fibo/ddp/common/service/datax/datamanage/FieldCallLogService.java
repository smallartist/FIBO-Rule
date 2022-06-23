package com.fibo.ddp.common.service.datax.datamanage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.datax.datamanage.FieldCall;
import com.fibo.ddp.common.model.datax.datamanage.FieldCallLog;
import com.fibo.ddp.common.model.datax.datamanage.request.FieldCallParam;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * (FieldCallLog)表服务接口
 *
 * @author jgp
 * @since 2021-12-08 14:18:29
 */
public interface FieldCallLogService extends IService<FieldCallLog> {

    PageInfo queryFieldCallList(QueryListParam<FieldCallParam> param);

    PageInfo queryFieldCallLogList(QueryListParam<FieldCallParam> param);

    List<FieldCall> queryFieldCallCountList(FieldCallParam param);

}
