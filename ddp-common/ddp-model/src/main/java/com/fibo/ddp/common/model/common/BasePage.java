package com.fibo.ddp.common.model.common;

import lombok.Data;

/**
 * 
 * @ClassName: BasePageVo <br/>
 * @Description: 分页公共基础bean. <br/>
 */
@Data
public class BasePage {
	
	/**
	 * 当前页数
	 */
	private int page;
	
	/**
	 * 每页显示的行数
	 */
	private int rows;

	/**
	 * 开始行数
	 */
	private Integer curRow;

	/**
	 * 结束行数
	 */
	private Integer endRow;
	
	/**
	 * 总行数
	 */
	private Integer total;
}
