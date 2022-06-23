
package com.fibo.ddp.common.model.authx.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_organization")
public class SysOrganization implements Serializable {

    private static final long serialVersionUID = -1L;
    @TableId(value = "organ_id",type = IdType.AUTO)
    private Long organId;//组织编号
    private String name;//组织名称
    private String code;//组织代号
    private String email;
    private String telephone;
    private Integer status;//0禁用1启用
    private String author;//创建者
    private Date birth;//创建时间
    private String token;//唯一标识
}
