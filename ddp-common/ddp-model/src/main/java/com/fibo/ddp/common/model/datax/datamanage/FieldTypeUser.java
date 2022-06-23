package com.fibo.ddp.common.model.datax.datamanage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fibo.ddp.common.model.common.BasePage;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_field_type_user_rel")
public class FieldTypeUser extends BasePage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 * */
	@TableId(type = IdType.AUTO)
	private Long id;
	
	/**
	 * 字段类型编号（表主键）
	 * */
	private Long fieldTypeId;
	
	/**
	 * 该字段类型归属的组织编号
	 * */
	private Long organId;
	
	/**
	 * 该字段类型归属的引擎id（表主键）
	 * */
	private Long engineId;
	
	/**
	 * 创建或修改该字段的用户编号
	 * */
	private Long userId;
	
	/**
	 * 创建时间
	 * */
	private Date created;


}
