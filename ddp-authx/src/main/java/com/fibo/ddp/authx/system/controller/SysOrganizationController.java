package com.fibo.ddp.authx.system.controller;

import com.fibo.ddp.common.model.authx.system.SysOrganization;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.BaseParam;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.service.authx.system.SysOrganizationService;
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

@Controller("sysOrganizationControllerV2")
@RequestMapping("v2/sysOrganization")
@ResponseBody
public class SysOrganizationController {

	@Autowired
	private SysOrganizationService sysOrganizationService;
	/**
	 * @api {POST} /v2/sysOrganization/getOrganList 6.41. 获取组织列表
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Integer} pageNo 页数
	 * @apiParam {Integer} pageSize 每页的条数
	 * @apiSuccess {JSON} pager 分页信息
	 * @apiSuccess {JSONArray} listOrgan 组织列表
	 * @apiSuccess (listOrgan) {Long} userId 组织编号
	 * @apiSuccess (listOrgan) {String} name 组织名称
	 * @apiSuccess (listOrgan) {String} versionCode 组织代号
	 * @apiSuccess (listOrgan) {Integer} status 状态：0禁用，1启用
	 * @apiSuccess (listOrgan) {String} author 创建者
	 * @apiSuccess (listOrgan) {Long} birth 创建时间
	 * @apiSuccess (listOrgan) {String} token 唯一标识
	 * @apiParamExample {json} 请求示例：
	 * {"pageNo":1,"pageSize":2}
	 * @apiSuccessExample {json} Success-Response:
	 * {"status":"1","error":"00000000","msg":null,"data":{"listOrgan":[{"userId":46,"name":"管理员","versionCode":"007","email":null,"telephone":null,"status":1,"author":"超级管理员","birth":1498722046000,"token":"4f15125c-93c0-43fb-9ed2-e0b92763fa3d"}],"pager":{"pageNum":1,"pageSize":2,"size":1,"startRow":1,"endRow":1,"total":1,"pages":1,"list":null,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1,"lastPage":1,"firstPage":1}}}
	 */
	@RequestMapping(value = "getOrganList", method = RequestMethod.POST)
	public ResponseEntityDto getOrganList(@RequestBody BaseParam baseParam){
		PageHelper.startPage(baseParam.getPageNo(), baseParam.getPageSize());
		List<SysOrganization> listOrgan = sysOrganizationService.getAllSysOrganization();
		PageInfo<SysOrganization> pageInfo = new PageInfo<SysOrganization>(listOrgan);
		pageInfo.setList(null);
		HashMap<String, Object> modelMap = new HashMap<>();
		modelMap.put("listOrgan", listOrgan);
		modelMap.put("pager", pageInfo);
		return ResponseEntityBuilder.buildNormalResponse(modelMap);
	}

	/**
	 * @api {POST} /v2/sysOrganization/getAllValidOrgan 6.42. 获取所有已启用组织
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiSuccess {Long} userId 组织编号
	 * @apiSuccess {String} name 组织名称
	 * @apiSuccess {String} versionCode 组织代号
	 * @apiSuccess {Integer} status 状态：0禁用，1启用
	 * @apiSuccess {String} author 创建者
	 * @apiSuccess {Long} birth 创建时间
	 * @apiSuccess {String} token 唯一标识
	 * @apiParamExample {json} 请求示例：
	 * {}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":[{"userId":46,"name":"管理员","versionCode":"007","email":null,"telephone":null,"status":1,"author":"超级管理员","birth":1498722046000,"token":"4f15125c-93c0-43fb-9ed2-e0b92763fa3d"},{"userId":1,"name":"rik","versionCode":"0001","email":"123.com ","telephone":"1234567489","status":1,"author":"超级管理员","birth":1498721562000,"token":"6a6ea35e-aabe-4e64-bd98-dae304b10a21"}]}
	 */
	@RequestMapping(value = "getAllValidOrgan", method = RequestMethod.POST)
	public ResponseEntityDto getAllValidOrgan(){
		List<SysOrganization> list = new ArrayList<>();
		SysUser sysUser = SessionManager.getLoginAccount();
		Long organId = sysUser.getOrganId();
		if(organId.longValue() == 1){
			list = sysOrganizationService.getAllValidOrgan();
		} else {
			SysOrganization sysOrganization = sysOrganizationService.findById(organId);
			list.add(sysOrganization);
		}
		return ResponseEntityBuilder.buildNormalResponse(list);
	}

	/**
	 * @api {POST} /v2/sysOrganization/save 6.43. 创建组织
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {String} name 组织名称
	 * @apiParam {String} versionCode 组织代号
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"name":"测试公司","versionCode":"666"}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":1}
	 */
    @RequestMapping(value = "save", method = RequestMethod.POST)
	@ArchivesLog(operationType = OpTypeConst.SAVE_ORGAN)
    public ResponseEntityDto save(@RequestBody SysOrganization sysOrganization) {
    	//验证唯一性
    	List<SysOrganization> list = sysOrganizationService.validateOrganOnly(sysOrganization);
    	if(list!=null&&list.size()>0){
			throw new ApiException(ErrorCodeEnum.CREATE_ORGAN_NAME_REPEAT.getCode(), ErrorCodeEnum.CREATE_ORGAN_NAME_REPEAT.getMessage());
    	}
    	int num = sysOrganizationService.createSysOrganization(sysOrganization);
		return ResponseEntityBuilder.buildNormalResponse(num);
    }

	/**
	 * @api {POST} /v2/sysOrganization/getOrganInfo/{userId} 6.44. 获取组织详情
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Long} userId 组织编号（url参数）
	 * @apiSuccess {Long} userId 组织编号
	 * @apiSuccess {String} name 组织名称
	 * @apiSuccess {String} versionCode 组织代号
	 * @apiSuccess {Integer} status 状态：0禁用，1启用
	 * @apiSuccess {String} author 创建者
	 * @apiSuccess {Long} birth 创建时间
	 * @apiSuccess {String} token 唯一标识
	 * @apiParamExample {json} 请求示例：
	 * {}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":{"userId":46,"name":"管理员","versionCode":"007","email":null,"telephone":null,"status":1,"author":"超级管理员","birth":1498722046000,"token":"4f15125c-93c0-43fb-9ed2-e0b92763fa3d"}}
	 */
	@RequestMapping(value = "/getOrganInfo/{id}", method = RequestMethod.POST)
	public ResponseEntityDto getOrganInfo(@PathVariable long id){
		SysOrganization sysOrganization = sysOrganizationService.findById(id);
		return ResponseEntityBuilder.buildNormalResponse(sysOrganization);
	}

	/**
	 * @api {POST} /v2/sysOrganization/update 6.45. 修改组织
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Long} userId 组织编号
	 * @apiParam {String} name 组织名称
	 * @apiParam {String} versionCode 组织代号
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"userId":47,"name":"测试公司2","versionCode":"666"}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":1}
	 */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_ORGAN)
    public ResponseEntityDto update(@RequestBody SysOrganization sysOrganization) {
    	//验证唯一性
    	List<SysOrganization> list = sysOrganizationService.validateOrganOnly(sysOrganization);
    	if(list!=null&&list.size()>0){
			throw new ApiException(ErrorCodeEnum.CREATE_ORGAN_NAME_REPEAT.getCode(), ErrorCodeEnum.CREATE_ORGAN_NAME_REPEAT.getMessage());
    	}
    	int num = sysOrganizationService.updateSysOrganization(sysOrganization);
		return ResponseEntityBuilder.buildNormalResponse(num);
    }

	/**
	 * @api {POST} /v2/sysOrganization/updateStatus 6.46. 组织停用、启用、删除
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Integer} status 状态：0停用，1启用, -1删除
	 * @apiParam {String} ids 用户Id，逗号分隔
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"status":0,"ids":"46"}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":1}
	 */
    @RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_ORGAN_STATUS)
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
    		 num = sysOrganizationService.updateStatus(status,list);
    	}
		return ResponseEntityBuilder.buildNormalResponse(num);
    }
}