package com.fibo.ddp.common.model.enginex.risk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EngineResultSet {

	private Integer id;
	private String uid;
	private String pid;
	private String input;
	private String output;
	private Date createDatetime;
	
	private String result;
	
	private Long engineId;
	
	private Integer engineVersion;
	
	private String uuid;
	
	private String engineName;
	
	private String engineCode;
	
	private Date startDate;
	
	private Date endDate;
	
	private Integer type;
	
	private Integer subVersion;
	
	private String scorecardscore;
	
	private String datilResult;

	/**
	 *决策表结果
	 */
	private String decisionTablesResult;

	/**
	 *决策树结果
	 */
	private String decisionTreeResult;
	
	/**
	 * 批量测试批次号
	 */
	private String batchNo;
	
	/**
	 * 批量测试每批测试开始时间
	 */
	private Date startTime;
	
	/**
	 * 批量测试每批次花费时间
	 */
	private String costTime;

	/**
	 * hbase行键
	 */
	private String hbaseRowKey;
}
