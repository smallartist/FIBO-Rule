package com.fibo.ddp.authx.system.controller;

import com.fibo.ddp.common.model.authx.system.SysRole;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.BaseParam;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.service.authx.system.SysRoleService;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("sysRoleControllerV2")
@RequestMapping("v2/sysRole")
@ResponseBody
public class SysRoleController {
	@Autowired
	private SysRoleService sysRoleService;
	/**
	 * @api {POST} /v2/sysRole/getRoleList 6.21. 获取角色列表
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Integer} pageNo 页数
	 * @apiParam {Integer} pageSize 每页的条数
	 * @apiSuccess {JSON} pager 分页信息
	 * @apiSuccess {JSONArray} listRole 角色列表
	 * @apiSuccess (listRole) {Integer} userId 角色编号
	 * @apiSuccess (listRole) {Integer} organId 组织编号
	 * @apiSuccess (listRole) {String} roleName 角色名称
	 * @apiSuccess (listRole) {String} author 创建者
	 * @apiSuccess (listRole) {Long} birth 创建时间
	 * @apiSuccess (listRole) {Integer} status 状态：0禁用，1启用
	 * @apiParamExample {json} 请求示例：
	 * {"pageNo":1,"pageSize":2}
	 * @apiSuccessExample {json} Success-Response:
	 * {"status":"1","error":"00000000","msg":null,"data":{"listRole":[{"userId":72,"organId":46,"roleName":"业务管理员","roleCode":null,"roleDesc":null,"author":"超级管理员","birth":1498725111000,"status":1},{"userId":71,"organId":46,"roleName":"引擎管理员","roleCode":null,"roleDesc":null,"author":"超级管理员","birth":1498725103000,"status":1}],"pager":{"pageNum":1,"pageSize":2,"size":2,"startRow":1,"endRow":2,"total":3,"pages":2,"list":null,"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2],"navigateFirstPage":1,"navigateLastPage":2,"firstPage":1,"lastPage":2}}}
	 */
	@RequestMapping(value = "/getRoleList", method = RequestMethod.POST)
	public ResponseEntityDto getRoleList(@RequestBody BaseParam baseParam){
		List<SysRole> listRole = null;
		//获取公司管理员所在公司
		SysUser sysUser = SessionManager.getLoginAccount();
		long organId = sysUser.getOrganId();
		PageHelper.startPage(baseParam.getPageNo(),baseParam.getPageSize());
		if(organId==1){
			//获取所有未删除角色
			listRole = sysRoleService.getAllRoles();
		}else{
			//获取本公司未删除角色
			listRole = sysRoleService.getAllSysRole(organId);
		}
		PageInfo<SysRole> pageInfo = new PageInfo<SysRole>(listRole);
		pageInfo.setList(null);
		HashMap<String, Object> modelMap = new HashMap<>();
		modelMap.put("listRole", listRole);
		modelMap.put("pager", pageInfo);
		return ResponseEntityBuilder.buildNormalResponse(modelMap);
	}

	/**
	 * @api {POST} /v2/sysRole/getAllValidRole 6.22. 根据公司获取所有启用角色
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Integer} organId 组织编号
	 * @apiSuccess {Integer} userId 角色编号
	 * @apiSuccess {Integer} organId 组织编号
	 * @apiSuccess {String} roleName 角色名称
	 * @apiSuccess {String} author 创建者
	 * @apiSuccess {Long} birth 创建时间
	 * @apiSuccess {Integer} status 状态：0禁用，1启用
	 * @apiParamExample {json} 请求示例：
	 * {"organId":46}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":[{"userId":70,"organId":46,"roleName":"管理员","roleCode":null,"roleDesc":null,"author":"超级管理员","birth":1498724751000,"status":1},{"userId":71,"organId":46,"roleName":"引擎管理员","roleCode":null,"roleDesc":null,"author":"超级管理员","birth":1498725103000,"status":1},{"userId":72,"organId":46,"roleName":"业务管理员","roleCode":null,"roleDesc":null,"author":"超级管理员","birth":1498725111000,"status":1}]}
	 */
    @RequestMapping(value = "getAllValidRole", method = RequestMethod.POST)
    public ResponseEntityDto getAllValidRole(@RequestBody Map<String, Object> param){
		String author = "";
		SysUser sysUser = SessionManager.getLoginAccount();
		Long orgId = sysUser.getOrganId();
		if(orgId==1){
			author = "超级管理员";
		}

		long organId = orgId;
		if(param.get("organId") != null){
			organId = Long.valueOf(param.get("organId").toString());
		}
    	List<SysRole> list = sysRoleService.getAllValidRole(organId,author);
    	return ResponseEntityBuilder.buildNormalResponse(list);
    }

	/**
	 * @api {POST} /v2/sysRole/save 6.23. 创建角色
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Integer} organId 组织编号
	 * @apiParam {String} roleName 角色名称
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"organId":1,"roleName":"浏览者"}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":1}
	 */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.SAVE_SYS_ROLE)
    public ResponseEntityDto save(@RequestBody SysRole sysRole) {
    	//确保每个公司只能创建一个公司管理员
    	SysUser sysUser = SessionManager.getLoginAccount();
    	Long or_id = sysUser.getOrganId();
    	if(or_id==1){
    		sysRole.setAuthor(sysUser.getNickName());
    		//查询公司管理员是否存在
    		List<SysRole> list_role = sysRoleService.getOrganRoleByAuthor(sysRole);
    		if(list_role!=null&&list_role.size()>0){
				throw new ApiException(ErrorCodeEnum.CREATE_ROLE_ADMIN_REPEAT.getCode(), ErrorCodeEnum.CREATE_ROLE_ADMIN_REPEAT.getMessage());
    		}
    	}else{
    		sysRole.setAuthor(sysUser.getNickName());
    	}
    	//验证名称唯一性
    	List<SysRole> list = sysRoleService.validateRoleOnly(sysRole);
    	if(list!=null&&list.size()>0){
			throw new ApiException(ErrorCodeEnum.CREATE_ROLE_NAME_REPEAT.getCode(), ErrorCodeEnum.CREATE_ROLE_NAME_REPEAT.getMessage());
    	}
        int num = sysRoleService.createSysRole(sysRole);
    	return ResponseEntityBuilder.buildNormalResponse(num);
    }

	/**
	 * @api {POST} /v2/sysRole/getRoleInfo/{userId} 6.24. 获取角色详情
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Long} userId 角色编号（url参数）
	 * @apiSuccess {Integer} userId 角色编号
	 * @apiSuccess {Integer} organId 组织编号
	 * @apiSuccess {String} roleName 角色名称
	 * @apiSuccess {String} author 创建者
	 * @apiSuccess {Long} birth 创建时间
	 * @apiSuccess {Integer} status 状态：0禁用，1启用
	 * @apiParamExample {json} 请求示例：
	 * {}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":{"userId":76,"organId":46,"roleName":"浏览者","roleCode":null,"roleDesc":null,"author":"管理员","birth":1563634443000,"status":1}}
	 */
	@RequestMapping(value = "/getRoleInfo/{id}", method = RequestMethod.POST)
	public ResponseEntityDto getRoleInfo(@PathVariable long id){
		SysRole sysrole = null;
		//获取管理员所在公司
		SysUser sysUser = SessionManager.getLoginAccount();
		long organId = sysUser.getOrganId();
		if(organId==1){
			sysrole = sysRoleService.findByAId(id);
		}else{
			sysrole = sysRoleService.findById(id,organId);
		}
		return ResponseEntityBuilder.buildNormalResponse(sysrole);
	}

	/**
	 * @api {POST} /v2/sysRole/update 6.25. 修改角色
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Integer} userId 角色编号
	 * @apiParam {Integer} organId 组织编号
	 * @apiParam {String} roleName 角色名称
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"userId":77,"organId":1,"roleName":"浏览者2"}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":1}
	 */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_SYS_ROLE)
    public ResponseEntityDto update(@RequestBody SysRole sysRole) {
    	List<SysRole> list = sysRoleService.validateRoleOnly(sysRole);
    	if(list!=null&&list.size()>0){
			throw new ApiException(ErrorCodeEnum.CREATE_ROLE_NAME_REPEAT.getCode(), ErrorCodeEnum.CREATE_ROLE_NAME_REPEAT.getMessage());
    	}
        int num = sysRoleService.updateSysRole(sysRole);
    	return ResponseEntityBuilder.buildNormalResponse(num);
    }

	/**
	 * @api {POST} /v2/sysRole/updateStatus 6.26. 角色停用、启用、删除
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Integer} status 状态：0停用，1启用, -1删除
	 * @apiParam {String} ids 用户Id，逗号分隔
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"status":0,"ids":"77"}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":1}
	 */
    @RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_SYS_ROLE_STATUS)
    public ResponseEntityDto updateStates(@RequestBody Map<String, Object> param){
		int status = (Integer) param.get("status");
		String ids = (String)param.get("ids");
    	int num = 0;
    	List<Integer> list = new ArrayList<Integer>();
    	
    	if(ids!=""){
    		String[] strs = ids.split(",");
            for(int i=0;i<strs.length;i++){
            	list.add(Integer.parseInt(strs[i]));
            }
    	}
    	if(list!=null && list.size()>0){
    		 num = sysRoleService.updateStatus(status,list);
    	}
    	return ResponseEntityBuilder.buildNormalResponse(num);
    }
}