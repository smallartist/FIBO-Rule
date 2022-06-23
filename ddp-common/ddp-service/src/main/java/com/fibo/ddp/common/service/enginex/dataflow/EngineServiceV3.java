package com.fibo.ddp.common.service.enginex.dataflow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.enginex.risk.Engine;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * (Engine)表服务接口
 *
 * @author jgp
 * @since 2021-12-22 13:28:08
 */
public interface EngineServiceV3 extends IService<Engine> {

    PageInfo queryList(QueryListParam<Engine> param);

    boolean updateStatus(List<Long> ids, Integer status);

    Engine addEngine(Engine engine);

    Engine updateEngine(Engine engine);
}
