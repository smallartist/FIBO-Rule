package com.fibo.ddp.common.model.enginex.runner;

import lombok.Data;

@Data
public class Sandbox {
	private Integer sandbox;//沙盒组编号
	private Integer proportion;//沙盒占用比例
	private String nextNode;//下个节点序号
	private Integer sum;//分母
	private Integer startNumber;//起始值
	private Integer endNumberl;//终止值
}
