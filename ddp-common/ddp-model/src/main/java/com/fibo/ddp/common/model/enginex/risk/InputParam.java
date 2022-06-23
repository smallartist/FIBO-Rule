package com.fibo.ddp.common.model.enginex.risk;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class InputParam {

	private Map<String ,Object> inputParam;
	
	private List<Result>  result;
}
