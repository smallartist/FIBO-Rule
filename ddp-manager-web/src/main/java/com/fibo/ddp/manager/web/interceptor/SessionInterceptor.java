package com.fibo.ddp.manager.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.service.common.AccountSessionWrap;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.constant.ServiceFilterConstant;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.RequestUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 *  会话拦截器
 */
@Component
public class SessionInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private RedisManager redisManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
		String ip = RequestUtil.getClientIP(request);
		AccountSessionWrap acsw = new AccountSessionWrap(ip, uri);
		String reqUuid = UUID.randomUUID().toString().replaceAll("-", "");
		String requestMethod = request.getMethod();
		StringBuffer requestURL = request.getRequestURL();
		acsw.setTraceId(reqUuid);
		String token = request.getHeader(Constants.SYSTEM_KEY_TOKEN);
		logger.debug("===>> 【会话拦截】-BEGIN: {} - {} {}, ### traceId:{},{}, token:{} ### ===>>", ip, requestMethod, requestURL, reqUuid, uri, token);
		SessionManager.setSession(acsw);
		if (ServiceFilterConstant.isSessionFilter(uri)) {
			return true;
		}
		if (StringUtils.isBlank(token)) {
			output(response, ErrorCodeEnum.ERROR_TOKEN_EXPIRE.getCode(),ErrorCodeEnum.ERROR_TOKEN_EXPIRE.getMessage());
			return false;
		}

		try {
			String value = redisManager.get(token);
			if(StringUtils.isBlank(value)){
				output(response, ErrorCodeEnum.ERROR_TOKEN_EXPIRE.getCode(),ErrorCodeEnum.ERROR_TOKEN_EXPIRE.getMessage());
				return false;
			}

			// token更新频率，设置离过期时间还剩n秒以内才更新一次token
			Long time = redisManager.ttl(token);
			if(time.intValue() <= Constants.LOGIN_TOKEN_REFRESH_TIME.intValue()){
				redisManager.set(token, value, Constants.LOGIN_TOKEN_TIME.intValue());
			}

			SysUser sysUser = JSONObject.parseObject(value, SysUser.class);
			acsw.setSysUser(sysUser);
		} catch (ApiException e1) {
			output(response, e1.errCode, e1.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("【会话拦截】调用Token验证服务异常,uri:{}，token:{},IP:{}",uri,token,ip,e);
			output(response, ErrorCodeEnum.SERVER_ERROR.getCode(), ErrorCodeEnum.SERVER_ERROR.getMessage());
			return false;
		}
		
		return true;
	}

	private void output(HttpServletResponse response, String errCode, String errMsg) {
		ResponseEntityDto ret = ResponseEntityBuilder.buildErrorResponse(errCode, errMsg);
		try {
			logger.info("【会话拦截】未通过,{\"errCode\":" + errCode + ",\"errMsg:\":" + errMsg + "}");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Type", "application/json;charset=UTF-8");
			byte[] jsonBytes = JSON.toJSONBytes(ret);
			OutputStream output = response.getOutputStream();
			output.write(jsonBytes);
			output.flush();
		} catch (IOException e) {
			logger.error("【会话拦截】输出响应报文异常！,{},{}",errCode,errMsg, e);
		}
	}
}