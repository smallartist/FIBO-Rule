package com.fibo.ddp.common.model.authx.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_resource")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = -1L;
    @TableId(value = "resource_id",type = IdType.AUTO)
    private Long resourceId;
    private Long userId;//分配者
    private String name; //资源名称
    private String code;//资源代号
    private String url;//路径
    private Long parentId;//父节点
    private String des;
    private String resourceSystem;
    private Date birth;//创建时间
    private String icon;//图标
	private Integer sort; // 菜单顺序
    private Integer status;//状态
    private Long roleId;//角色id
    private Boolean checked;//菜单默认选中
    private Boolean chkDisabled;//节点是否禁用
    private Boolean hidden;//节点是否隐藏

}
