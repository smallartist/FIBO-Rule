package com.fibo.ddp.common.service.datax.datasource;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.datax.datasource.MqSource;
import com.github.pagehelper.PageInfo;


import java.util.List;

/**
 * (MqSource)表服务接口
 *
 * @author jgp
 * @since 2021-12-20 13:31:51
 */
public interface MqSourceService extends IService<MqSource> {

    MqSource queryById(Long id);

    PageInfo queryList(QueryListParam<MqSource> param);

    MqSource add(MqSource param);

    MqSource update(MqSource param);

    boolean updateStatus(List<Long> ids, Integer status);
}
