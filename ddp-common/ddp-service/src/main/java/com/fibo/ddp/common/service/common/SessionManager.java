package com.fibo.ddp.common.service.common;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.fibo.ddp.common.model.authx.system.SysUser;

/**
 * session管理类
 */
public class SessionManager {

    private static TransmittableThreadLocal<AccountSessionWrap> session = new TransmittableThreadLocal<AccountSessionWrap>() {

	};

	public static AccountSessionWrap getSession() {
		return session.get();
	}

	public static void setSession(AccountSessionWrap conn) {
		session.set(conn);
	}

	public static SysUser getLoginAccount(){
		if(getSession() != null){
			return getSession().getSysUser();
		} else {
			return null;
		}
	}
}