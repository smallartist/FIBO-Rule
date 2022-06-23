package com.fibo.ddp.common.model.datax.datamanage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fibo.ddp.common.model.common.BasePage;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_field_user_rel")
public class FieldUser extends BasePage implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 * */
	@TableId(type = IdType.AUTO)
	private Long id;
	
	/**
	 * 字段编号（表主键）
	 * */
	private Long fieldId;
	
	/**
	 * 该字段归属的组织编号
	 * */
	private Long organId;
	
	/**
	 * 该字段归属的引擎id（表主键）
	 * */
	private Long engineId;
	
	/**
	 * 创建或修改该字段的用户编号
	 * */
	private Long userId;
	
	/**
	 * 启用停用删除标志
	 * */
	private Integer status;
	
	/**
	 * 创建时间
	 * */
	private Date created;
	
	/**
	 * 更新时间
	 * */
	private Date updated;
	
}
