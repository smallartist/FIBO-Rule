package com.fibo.ddp.common.model.authx.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@TableName("t_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = -1L;
    @TableId(value = "user_id",type = IdType.AUTO)
    private Long userId;//用户（主键）
    private Long organId;//组织编号
    private String employeeId;//员工编号
    private String account;//账户
    private String password;
    private String nickName;//昵称
    private String email;
    private String cellphone;
    private String qq;
    private String latestTime;
    private String latestIp;
    private String remark;
    private Integer status;
    private Date birth;//创建时间
    private String author;//创建人
    @TableField(exist = false)
    private SysRole sysRole;//角色对象
    @TableField(exist = false)
    private SysOrganization sysOrgan;//公司对象
}
