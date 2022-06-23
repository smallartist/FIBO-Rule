package com.fibo.ddp.common.model.strategyx.knowledge;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * ClassName:OrganRuleVo <br/>
 * Description: 规则实体类. <br/>
 * @see
 */
@Data
public class Rule implements Serializable,Cloneable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 * */
	private Long id;
	
	/**
	 * 名称
	 * */
	private String name;
	
	/**
	 * 代码
	 * */
	private String code;
	
	/**
	 * 描述
	 * */
	private String description;

	/**
	 * 优先级
	 * */
	private Integer priority;
	
	/**
	 * 父节点id
	 * */
	private Long parentId;
	
	/**
	 *修改人id
	 * */
	private Long userId;
	
	/**
	 *创建人id
	 * */
	private Long author;
	
	/**
	 *创建人名称
	 * */
	private String authorName;
	
	/**
	 * 组织id
	 * */
	private Long organId;
	
	/**
	 * 引擎id
	 * */
	private Long engineId;
	
	/**
	 * 规则类型  0 : 系统的规则  1：组织的规则 2： 引擎的规则
	 * */
	private Integer type;
	
	/**
	 * 逻辑关系"非" 0:不是非 1：是非
	 * */
	private Integer isNon;
	
	/**
	 * 状态   0 :停用 ，1 : 启用，-1：删除
	 * */
	private Integer status;
	/**
	 * 审批规则 5 :通过 ，2 : 拒绝，3：人工审批 4：简化流程  
	 */
	public int ruleAudit;

	/**
	 * 创建日期
	 * */
	private Date created;
	
	/**
	 * 修改日期
	 * */
	private Date updated;
	
	/**
	 * 规则具体内容
	 * */
	public String content;
	
	/**
	 * 0硬性拒绝规则1加减分规则
	 */
	private Integer ruleType;
	
	/**
	 *得分
	 */
	private Integer score;
	
	/**
	 *逻辑关系符，存储条件区域最后一个逻辑符号，值有')'、'))'、'-1'
	 */
	private String lastLogical;
	
	/**
	 * 引擎名
	 * */
	private String engineName;
	
	/**
	 * 规则节点名称
	 * */
	private String engineNodeName;
	
	/**
	 * 规则节点名称
	 * */
	private Long engineNodeId;
	
	/**
	 * 区分规则集和规则
	 * */
	private int  showType = 0;

	private Integer difficulty;//规则难度：1-简单规则，2复杂规则

	private String resultFieldEn;//存放是否命中的字段

	private String scoreFieldEn;//存放得分的字段en

	public Integer getRuleType() {
		if(ruleAudit == 2) {
			ruleType = 0;
		}else{
			ruleType = 1;
		}
		return ruleType;
	}
}
