package com.fibo.ddp.common.service.common.runner;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * session管理类
 */
public class RunnerSessionManager {
	private static TransmittableThreadLocal<SessionData> session = new TransmittableThreadLocal<SessionData>() {

	};

	public static SessionData getSession() {
		return session.get();
	}

	public static void setSession(SessionData conn) {
		session.set(conn);
	}
}