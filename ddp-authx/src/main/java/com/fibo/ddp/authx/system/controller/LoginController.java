
package com.fibo.ddp.authx.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.authx.system.request.LoginInfoParam;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.service.authx.system.SysUserService;
import com.fibo.ddp.common.service.common.AccountSessionWrap;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.utils.common.MD5;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @apiDefine account 1. 账户
 */
@Controller("loginControllerV2")
@RequestMapping("/v2/login/*")
public class LoginController {

	@Autowired
	private RedisManager redisManager;
	@Autowired
	private SysUserService sysUserService;
	/**
	 * @api {POST} /v2/login/login 1.01. 用户登录
	 * @apiGroup account
	 * @apiVersion 1.0.1
	 * @apiParam {String} account 账号
	 * @apiParam {String} password 密码
	 * @apiSuccess {String} token 会话token
	 * @apiParamExample {json} Request:
	 * {"account":"admin","password":"123456"}
	 * @apiSuccessExample {json} Success-Response:
	 * {"status":"1","error":"00000000","msg":null,"data":{"token":"21fd6379df134ea590a462e4de1f6b33"}}
	 */
	@ResponseBody
	@RequestMapping(value = "/login")
	@ArchivesLog(operationType = OpTypeConst.LOGIN)
    public ResponseEntityDto<Object> login(@RequestBody LoginInfoParam param) {
    	Map<String, Object> map = new HashMap<>();
		String account = param.getAccount();
		String password = param.getPassword();
        if(!("".equals(account)) && !("".equals(password))){

			SysUser user = sysUserService.login(account.trim(), MD5.GetMD5Code(password));
			if(null != user && user.getStatus()==1){
				String token = UUID.randomUUID().toString().replaceAll("-", "");
				redisManager.set(token, JSONObject.toJSONString(user), Constants.LOGIN_TOKEN_TIME.intValue());
				map.put("token", token);

				com.fibo.ddp.common.service.common.AccountSessionWrap acsw = new AccountSessionWrap(null, null);
				acsw.setSysUser(user);
				SessionManager.setSession(acsw);
			}else{
				return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.LOGIN_ERROR);
			}
        }
		return ResponseEntityBuilder.buildNormalResponse(map);
    }

	/**
	 * @api {POST} /v2/login/logout 1.02. 用户登出
	 * @apiGroup account
	 * @apiVersion 1.0.1
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} Request:
	 * {}
	 * @apiSuccessExample {json} Success-Response:
	 * {"status":"1","error":"00000000","msg":null,"data":null}
	 */
	@ResponseBody
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	@ArchivesLog(operationType = OpTypeConst.LOGOUT)
	public ResponseEntityDto<Object> logout(HttpServletRequest request) {
		String token = request.getHeader(Constants.SYSTEM_KEY_TOKEN);
		if(StringUtils.isNotBlank(token)){
			redisManager.del(token);
		}
		return ResponseEntityBuilder.buildNormalResponse();
	}

}

