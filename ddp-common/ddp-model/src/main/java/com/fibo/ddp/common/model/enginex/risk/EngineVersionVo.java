package com.fibo.ddp.common.model.enginex.risk;

import lombok.Data;

import java.util.List;

@Data
public class EngineVersionVo implements Comparable<EngineVersionVo>{
	
	private EngineVersion engineVersion;
	
	private List<EngineVersion> subEngineVersionList;

	@Override
	public int compareTo(EngineVersionVo o) {
		if(engineVersion.getVersion()!=o.getEngineVersion().getVersion()){
            return engineVersion.getVersion()-o.getEngineVersion().getVersion();
        }
		return -1;
	}
}
