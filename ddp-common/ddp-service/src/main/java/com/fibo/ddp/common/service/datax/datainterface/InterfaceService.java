package com.fibo.ddp.common.service.datax.datainterface;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.datax.datainterface.InterfaceInfo;
import com.fibo.ddp.common.model.datax.datainterface.vo.InterfaceVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface InterfaceService extends IService<InterfaceInfo> {
    InterfaceVo getInterfaceById(Long id);

    PageInfo queryByEntity(QueryListParam<InterfaceInfo> param);

    InterfaceVo inserInterfaceInfo(InterfaceVo interfaceVo);

    InterfaceVo updateInterfaceInfo(InterfaceVo interfaceVo);

    //更新接口
    boolean updateStatus(Long[] ids, Integer status);

    //删除接口
    Boolean deleteInterfaceInfo(InterfaceVo interfaceVo);

    //http请求
    String getHttpResponse(InterfaceInfo interfaceInfo);

    // runner
    InterfaceInfo getInterfaceById(Integer id);

    /**
     * 接口请求
     * @param interfaceInfo
     * @param inputParam
     * @param callType
     * @return
     */
    String getHttpResponse(InterfaceInfo interfaceInfo, Map<String, Object> inputParam, Integer callType);

    /**
     * 解析接口指标
     * @param parseStr
     * @param jsonStr
     * @return
     */
    String interfaceParseField(String parseStr, String jsonStr);
}
