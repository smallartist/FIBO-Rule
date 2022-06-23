package com.fibo.ddp.common.model.datax.datainterface.request;


import com.fibo.ddp.common.model.datax.datainterface.InterfaceInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterfaceListParam {
    protected Integer pageNo = 1;  // 第几页
    protected Integer pageSize = 10;  // 每页的数量


//    protected Boolean search = false;  // 是否搜索

    protected InterfaceInfo interfaceInfo;//查询实体对象

//    protected Integer parentId = 0;  // 文件夹的id
}
