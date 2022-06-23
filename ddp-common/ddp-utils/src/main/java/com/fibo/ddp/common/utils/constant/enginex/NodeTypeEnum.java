package com.fibo.ddp.common.utils.constant.enginex;

public enum NodeTypeEnum {
	/**
	 * 开始节点
	 */
	START(1, EnumConst.NODE_START),
	/**
	 * 规则节点
	 */
	POLICY(2,EnumConst.NODE_POLICY),
	/**
	 * 分组节点
	 */
	CLASSIFY(3, EnumConst.NODE_CLASSIFY),
	/**
	 * 评分卡节点
	 */
	SCORECARD(4,EnumConst.NODE_SCORECARD),
	/**
	 * 黑名单节点
	 */
	BLACKLIST(5,EnumConst.NODE_BLACK),
	/**
	 * 白名单节点
	 */
	WHITELIST(6,EnumConst.NODE_WHITE),
	/**
	 * 分流节点
	 */
	SANDBOX(7,EnumConst.NODE_SANDBOX),
	/**
	 * 信用评级节点
	 */
	CREDITLEVEL(8,EnumConst.NODE_CREDIT_LEVEL),
	/**
	 * 决策选项节点
	 */
	DECISION(9,EnumConst.NODE_DECISION),
	/**
	 * 额度计算节点
	 */
	QUOTACALC(10,EnumConst.NODE_QUOTA_CALC),
	/**
	 * 报表分析节点
	 */
	REPORT(11,EnumConst.NODE_REPORT),
	/**
	 * 自定义节点
	 */
	CUSTOMIZE(12,EnumConst.NODE_CUSTOMIZE),
	/**
	 * 复杂规则
	 */
	NODE_COMPLEXRULE(13,EnumConst.NODE_COMPLEXRULE),
	/**
	 * 子引擎
	 */
	CHILD_ENGINE(14,EnumConst.NODE_CHILD_ENGINE),
	/**
	 * 模型
	 */
	MODEL_ENGINE(15,EnumConst.NODE_MODEL_ENGINE),
	/**
	 * 决策表
	 */
	DECISION_TABLES(16,EnumConst.NODE_DECISION_TABLES),
	/**
	 * 决策树
	 */
	DECISION_TREE(17,EnumConst.NODE_DECISION_TREE),
	/**
	 * 远程调用
	 */
	RPC(18, EnumConst.NODE_RPC),
	/**
	 * 并行节点
	 */
	PARALLEL(19, EnumConst.NODE_PARALLEL),
	/**
	 * 聚合节点
	 */
	AGGREGATION(20, EnumConst.NODE_AGGREGATION),
	/**
	 * 冠军挑战节点
	 */
	CHAMPION_CHALLENGE(21, EnumConst.NODE_CHAMPION_CHALLENGE);

	private int value;
	
	private String type;
	 
	private NodeTypeEnum(int value, String type)
	{
		this.value = value;
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static NodeTypeEnum adapad(int value) {
		for (NodeTypeEnum nodeTypeEnum : NodeTypeEnum.values()) {
			if (nodeTypeEnum.getValue() == value) {
				return nodeTypeEnum;
			}
		}
		return null;
	}
}
