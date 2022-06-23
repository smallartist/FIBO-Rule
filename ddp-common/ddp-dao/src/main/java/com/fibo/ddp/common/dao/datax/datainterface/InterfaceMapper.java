package com.fibo.ddp.common.dao.datax.datainterface;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.datax.datainterface.InterfaceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InterfaceMapper extends BaseMapper<InterfaceInfo> {
    int updateStatus(@Param(value = "ids") Long[] ids, @Param(value = "status") Integer status);

    List<InterfaceInfo> queryInterfaceList(InterfaceInfo interfaceInfo);

    List<InterfaceInfo> queryLimit(@Param(value = "start") int start, @Param(value = "size") int size);

}
