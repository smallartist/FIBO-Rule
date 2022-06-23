package com.fibo.ddp.common.model.enginex.risk;

import lombok.Data;

import java.util.Map;

@Data
public class EngineRule {

	private String refused;
	
	private String code ;
	
	private String policyName;
	
	private String desc;
	
	private String Strtus;
	
	private Map<String, String >fields;
}
