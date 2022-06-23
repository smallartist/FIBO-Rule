
package com.fibo.ddp.common.model.authx.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_role")
public class SysRole implements Serializable {

    private static final long serialVersionUID = -1L;
    @TableId(value = "role_id",type = IdType.AUTO)
    private Long roleId;
    private Long organId;
    private String roleName;
    private String roleCode;//角色代号
    private String roleDesc;
    private String author;//创建者
    private Date birth;//创建时间
    private Integer status;  //状态0禁用1启用
}
