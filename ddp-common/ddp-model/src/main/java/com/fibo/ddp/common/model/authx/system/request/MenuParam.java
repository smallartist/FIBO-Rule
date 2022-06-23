package com.fibo.ddp.common.model.authx.system.request;

import com.fibo.ddp.common.model.authx.system.SysMenu;
import com.fibo.ddp.common.model.common.BaseParam;
import lombok.Data;

@Data
public class MenuParam extends BaseParam {
    private SysMenu entity = new SysMenu();//查询实体条件
    private Long roleId;//角色id
    private Long parentId;//父id
}
