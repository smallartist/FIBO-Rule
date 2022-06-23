package com.fibo.ddp.common.dao.datax.datamanage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.datax.datamanage.FieldCall;
import com.fibo.ddp.common.model.datax.datamanage.FieldCallLog;
import com.fibo.ddp.common.model.datax.datamanage.request.FieldCallParam;

import java.util.List;

/**
 * (FieldCallLog)表数据库访问层
 *
 * @author jgp
 * @since 2021-12-08 14:18:29
 */
public interface FieldCallLogMapper extends BaseMapper<FieldCallLog> {

    List<FieldCall> findFieldCallList(FieldCallParam param);

    List<FieldCallLog> findFieldCallLogList(FieldCallParam param);

    List<FieldCall> findFieldCallCountList(FieldCallParam param);

}

