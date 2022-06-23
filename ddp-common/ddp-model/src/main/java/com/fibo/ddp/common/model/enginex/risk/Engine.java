package com.fibo.ddp.common.model.enginex.risk;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("t_engine")
public class Engine implements Serializable {
	private static final long serialVersionUID = -6611916471057697499L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 编号
	 */
	private String code;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 引擎类型：
	 */
	private String engineType;
	/**
	 * 状态：0被删除1正常使用
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createDatetime;
	/**
	 * 修改时间
	 */
	private Date updateDatetime;
	/**
	 * 创建者
	 */
	private Long creator;
	/**
	 * 企业编号
	 */
	private Long organId;
	/**
	 * 修改人
	 */
	private Long userId;
	/**
	 * 调用方式 1：同步，2：异步
	 */
	private Integer callbackType;
	/**
	 * 回调地址
	 */
	private String callbackUrl;
	/**
	 * 异常回调地址
	 */
	private String exceptionCallbackUrl;

	@TableField(exist = false)
	private Integer runStatus = 0;

    /**
     * 引擎版本集合
     * */
    @TableField(exist = false)
    private List<EngineVersion> engineVersionList;

    /**
     * 运行状态
     */
    @TableField(exist = false)
    private int runState;

    /**
     * 是否被选中
     */
    @TableField(exist = false)
    private boolean checked;
}
