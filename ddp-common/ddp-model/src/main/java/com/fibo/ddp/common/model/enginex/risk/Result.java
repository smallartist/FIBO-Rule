package com.fibo.ddp.common.model.enginex.risk;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Result {
	private String resultType;//规则1代表加减法，2拒绝规则
	private Integer id;//规则编号
	private String code;//规则code
	private String name;
	private String value;
	private Map<String, Object> map;//评分
	private List<EngineRule> list;
}
