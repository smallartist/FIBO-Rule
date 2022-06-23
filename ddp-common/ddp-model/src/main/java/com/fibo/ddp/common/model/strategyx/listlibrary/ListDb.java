package com.fibo.ddp.common.model.strategyx.listlibrary;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_list_db")
public class ListDb implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 黑、白名单库编号主键
	 */
	private Long id;
	/**
	 * 名单库code
	 */
	private String listCode;
	/**
	 * 黑、白名单库类型
	 */
	private String listType;
	/**
	 * 黑、白名单库名称
	 */
	private String listName;
	/**
	 * 黑、白名单库数据来源
	 */
	private Integer dataSource;
	/**
	 * 黑、白名单库类型属性
	 */
	private String listAttr;
	/**
	 * 黑、白名单库描述
	 */
	private String listDesc;
	/**
	 * 黑、白名单库表字段
	 */
	private String tableColumn;
	/**
	 * 黑、白名单库检索匹配类型
	 */
	private Integer matchType;
	/**
	 * 黑、白名单库检索查询字段间逻辑
	 */
	private Integer queryType;
	/**
	 * 黑、白名单库检索查询主键
	 */
	private String queryField;
	/**
	 * 黑、白名单库检索查询主键
	 */
	@TableField(exist = false)
	private String queryFieldCn;
	/**
	 * 黑、白名单库归属的组织编号
	 */
	private Long organId;
	/**
	 * 黑、白名单库状态
	 */
	private Integer status;
	/**
	 * 黑、白名单库创建人
	 */
	private Long userId;
	/**
	 * 黑、白名单库创建时间
	 */
	private Date created;
	/**
	 * 黑、白名单库创建人昵称
	 */
	@TableField(exist = false)
	private String nickName;
	/**
	 *是否命中的字段
	 */
	private String resultFieldEn;
	/**
	 * 输出字段
	 */
	@TableField(exist = false)
	private List<StrategyOutput> strategyOutputList;

	/**
	 * 版本列表
	 */
	@TableField(exist = false)
	private List<ListDbVersion> versionList;
	/**
	 * 文件夹id
	 */
	private Long folderId;

	/**
	 * 如果是节点查询此字段不为空为"1"
	 */
	@TableField(exist = false)
	private String nodeQuery;
	private Object snapshot;

	@TableField(exist = false)
	private ListDbVersion executeVersion;
}
