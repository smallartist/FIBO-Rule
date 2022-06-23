package com.fibo.ddp.common.service.common;

import com.fibo.ddp.common.model.authx.system.SysUser;
import lombok.Data;

/**
 * session 包装类
 */
@Data
public class AccountSessionWrap {
	public AccountSessionWrap(String ip, String requestUri) {
		init(null, ip, requestUri);
	}

	public AccountSessionWrap(SysUser sysUser, String ip, String requestUri) {
		init(sysUser, ip, requestUri);
	}

	private void init(SysUser sysUser, String ip, String requestUri) {
		setSysUser(sysUser);
		setIp(ip);
		setRequestUri(requestUri);
	}

	/** session */
	private SysUser sysUser;
	private String ip;
	private String requestUri;
	/** 请求唯一标识 */
	private String traceId;

}