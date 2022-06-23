package com.fibo.ddp.common.model.strategyx.listlibrary;

import lombok.Data;

import java.io.Serializable;

@Data
public class TblColumn implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 黑、白名单库匿名字段编号
	 */
	private String colName;
	
	/**
	 * 黑、白名单库字段名
	 */
	private String fieldCn;
	
	/**
	 * 黑、白名单库里存值
	 */
	private String fieldValue;
	
	/**
	 * 黑、白名单库注释（字段编号）
	 */
	private String colComment;
	
	/**
	 * 黑、白名单库匿名字段展示顺序
	 */
	private String colOrder;
}
