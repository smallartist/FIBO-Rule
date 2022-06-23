package com.fibo.ddp.common.utils.constant.enginex;

public class EngineMsg {

	/**
	 * 部署成功
	 */
	public static final int STATUS_SUCCESS = 1;
	/**
	 * 部署待审批
	 */
	public static final int STATUS_WAIT = 2;
	public static final String DEPLOY_WAIT = "部署待审批";

	public static final String DEPLOY_SUCCESS = "部署成功!";
	
	public static final String UNDEPLOY_SUCCESS = "当前版本已停用!";
	
	/**
	 * 部署失败
	 */
	public static final int STATUS_FAILED = 0;
	
	public static final String DEPLOY_FAILED = "部署失败!";
	
	public static final String UNDEPLOY_FAILED = "停用当前版本失败!";
	
	public static final String DELETE_RUNNING_FAILED = "当前版本正在运行,不能删除!";
	
	public static final String DELETE_VERSION_SUCCESS = "当前版本删除成功!";
	
	public static final String DELETE_VERSION_FAILED = "未知异常,当前版本删除失败!";
	
}
