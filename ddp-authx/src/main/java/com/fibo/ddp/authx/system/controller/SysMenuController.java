package com.fibo.ddp.authx.system.controller;

import com.fibo.ddp.common.model.authx.system.SysMenu;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.authx.system.request.MenuParam;
import com.fibo.ddp.common.model.authx.system.response.SysMenuVo;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.service.authx.system.SysMenuService;
import com.fibo.ddp.common.service.authx.system.SysUserService;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller("sysMenuControllerV2")
@RequestMapping("v2/sysMenu")
@ResponseBody
public class SysMenuController  {

	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private SysUserService sysUserService;

	/**
	 * @api {POST} /v2/sysMenu/getMenuList 6.31. 获取资源列表
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Integer} pageNo 页数
	 * @apiParam {Integer} pageSize 每页的条数
	 * @apiSuccess {JSON} pager 分页信息
	 * @apiSuccess {JSONArray} listMenu 资源列表
	 * @apiSuccess (listMenu) {Long} userId 资源编号
	 * @apiSuccess (listMenu) {Long} userId 分配者
	 * @apiSuccess (listMenu) {String} name 资源名称
	 * @apiSuccess (listMenu) {String} versionCode 资源代号
	 * @apiSuccess (listMenu) {String} url 资源路径
	 * @apiSuccess (listMenu) {Long} parentId 父节点
	 * @apiSuccess (listMenu) {String} des 资源描述
	 * @apiSuccess (listMenu) {Long} birth 创建时间
	 * @apiSuccess (listMenu) {String} icon 图标
	 * @apiSuccess (listMenu) {Integer} sort 顺序（值越小优先级越高）
	 * @apiSuccess (listMenu) {Integer} status 状态：0停用，1启用, -1删除
	 * @apiParamExample {json} 请求示例：
	 * {"pageNo":1,"pageSize":2}
	 * @apiSuccessExample {json} Success-Response:
	 * {"status":"1","error":"00000000","msg":null,"data":{"pager":{"pageNum":1,"pageSize":2,"size":2,"startRow":1,"endRow":2,"total":17,"pages":9,"list":null,"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2,3,4,5,6,7,8],"navigateFirstPage":1,"navigateLastPage":8,"lastPage":8,"firstPage":1},"listMenu":[{"userId":1,"userId":0,"name":"引擎管理","versionCode":"0001","url":"sysMenu/getChildMenu","parentId":0,"des":"引擎管理","birth":1498721562000,"icon":null,"sort":0,"status":1,"roleId":0,"checked":false,"chkDisabled":false,"hidden":false},{"userId":2,"userId":0,"name":"规则管理","versionCode":"0002","url":"sysMenu/getChildMenu","parentId":0,"des":"规则管理","birth":1498807962000,"icon":"bb","sort":5,"status":1,"roleId":0,"checked":false,"chkDisabled":false,"hidden":false}]}}
	 */
	@RequestMapping(value = "getMenuList", method = RequestMethod.POST)
	public ResponseEntityDto getMenuList(@RequestBody MenuParam menuParam) {
		PageHelper.startPage(menuParam.getPageNo(),menuParam.getPageSize());
		// 获取所有菜单
		List<SysMenu> listMenu = sysMenuService.getAllSysMenu(menuParam.getEntity());
		PageInfo<SysMenu> pageInfo = new PageInfo<SysMenu>(listMenu);
		pageInfo.setList(null);
		HashMap<String, Object> modelMap = new HashMap<>();
		modelMap.put("listMenu", listMenu);
		modelMap.put("pager", pageInfo);
		return ResponseEntityBuilder.buildNormalResponse(modelMap);
	}

	/**
	 * @api {POST} /v2/sysMenu/save 6.32. 创建资源
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {String} name 资源名称
	 * @apiParam {String} versionCode 资源代号
	 * @apiParam {String} url 资源路径
	 * @apiParam {Long} parentId 父节点
	 * @apiParam {String} des 资源描述
	 * @apiParam {String} icon 图标
	 * @apiParam {Integer} sort 顺序（值越小优先级越高）
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"name":"测试资源","versionCode":"0066","url":"testMenu","parentId":0,"des":"测试资源描述","icon":"el-icon-eleme","sort":2}
	 * @apiSuccessExample {json} Success-Response:
	 * {"status":"1","error":"00000000","msg":null,"data":1}
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ArchivesLog(operationType = OpTypeConst.SAVE_SYS_MENU)
	public ResponseEntityDto save(@RequestBody SysMenu sysMenu) {
		List<SysMenu> list = sysMenuService.validateMenuOnly(sysMenu);
		if(list!=null&&list.size()>0){
			throw new ApiException(ErrorCodeEnum.CREATE_MENU_NAME_REPEAT.getCode(), ErrorCodeEnum.CREATE_MENU_NAME_REPEAT.getMessage());
		}
		int num = sysMenuService.createSysMenu(sysMenu);
		return ResponseEntityBuilder.buildNormalResponse(num);
	}

	/**
	 * @api {POST} /v2/sysMenu/getMenuInfo/{userId} 6.33. 获取资源详情
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Long} userId 资源编号（url参数）
	 * @apiSuccess {Long} userId 资源编号
	 * @apiSuccess {Long} userId 分配者
	 * @apiSuccess {String} name 资源名称
	 * @apiSuccess {String} versionCode 资源代号
	 * @apiSuccess {String} url 资源路径
	 * @apiSuccess {Long} parentId 父节点
	 * @apiSuccess {String} des 资源描述
	 * @apiSuccess {Long} birth 创建时间
	 * @apiSuccess {String} icon 图标
	 * @apiSuccess {Integer} sort 顺序（值越小优先级越高）
	 * @apiSuccess {Integer} status 状态：0停用，1启用, -1删除
	 * @apiParamExample {json} 请求示例：
	 * {}
	 * @apiSuccessExample {json} Success-Response:
	 * {"status":"1","error":"00000000","msg":null,"data":{"userId":32,"userId":135,"name":"测试资源","versionCode":"0066","url":"testMenu","parentId":0,"des":"测试资源描述","birth":1616760174000,"icon":"el-icon-eleme","sort":2,"status":1,"roleId":0,"checked":false,"chkDisabled":false,"hidden":false}}
	 */
	@RequestMapping(value = "/getMenuInfo/{id}", method = RequestMethod.POST)
	public ResponseEntityDto getMenuInfo(@PathVariable long id) {
		SysMenu sysMenu = sysMenuService.findById(id);
		return ResponseEntityBuilder.buildNormalResponse(sysMenu);
	}

	/**
	 * @api {POST} /v2/sysMenu/update 6.34. 修改资源
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Long} userId 资源编号
	 * @apiParam {String} name 资源名称
	 * @apiParam {String} versionCode 资源代号
	 * @apiParam {String} url 资源路径
	 * @apiParam {Long} parentId 父节点
	 * @apiParam {String} des 资源描述
	 * @apiParam {String} icon 图标
	 * @apiParam {Integer} sort 顺序（值越小优先级越高）
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"userId":32,"name":"测试资源1","versionCode":"0067","url":"testMenu","parentId":0,"des":"测试资源描述","icon":"el-icon-eleme","sort":5}
	 * @apiSuccessExample {json} Success-Response:
	 * {"status":"1","error":"00000000","msg":null,"data":1}
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ArchivesLog(operationType = OpTypeConst.UPDATE_SYS_MENU)
	public ResponseEntityDto update(@RequestBody SysMenu sysMenu) {
		List<SysMenu> list = sysMenuService.validateMenuOnly(sysMenu);
		if(list!=null&&list.size()>0){
			throw new ApiException(ErrorCodeEnum.CREATE_MENU_NAME_REPEAT.getCode(), ErrorCodeEnum.CREATE_MENU_NAME_REPEAT.getMessage());
		}
		int num = sysMenuService.updateSysMenu(sysMenu);
		return ResponseEntityBuilder.buildNormalResponse(num);
	}

	/**
	 * @api {POST} /v2/sysMenu/updateStatus 6.35. 资源删除
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Integer} status 状态：-1删除
	 * @apiParam {String} ids 资源编号，逗号分隔
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"status":-1,"ids":"26"}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":1}
	 */
	@RequestMapping(value = "updateStatus", method = RequestMethod.POST)
	@ArchivesLog(operationType = OpTypeConst.UPDATE_SYS_MENU_STATUS)
	public ResponseEntityDto updateStatus(@RequestBody Map<String, Object> param) {
		int status = (Integer) param.get("status");
		String ids = (String)param.get("ids");
		int num = 0;
		List<Integer> list = new ArrayList<Integer>();

		if (ids != "") {
			String[] strs = ids.split(",");
			for (int i = 0; i < strs.length; i++) {
				list.add(Integer.parseInt(strs[i]));
			}
		}

		if (list != null && list.size() > 0) {
			num = sysMenuService.updateStatus(status, list);
		}
		return ResponseEntityBuilder.buildNormalResponse(num);
	}

	/**
	 * @api {POST} /v2/sysMenu/getTreeMenu 6.36. 新增/修改资源获取父节点树
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Integer} parentId 父节点Id
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"parentId":0}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":[{"userId":22,"userId":0,"name":"模型管理","versionCode":"0007","url":"sysMenu/getChildMenu","parentId":0,"des":"模型管理","birth":1498980762000,"icon":null,"status":1,"roleId":0,"checked":false,"chkDisabled":false,"isHidden":false},{"userId":23,"userId":0,"name":"数据源管理","versionCode":"0008","url":"sysMenu/getChildMenu","parentId":0,"des":"数据源管理","birth":1498984362000,"icon":null,"status":1,"roleId":0,"checked":false,"chkDisabled":false,"isHidden":false},{"userId":24,"userId":0,"name":"黑白名单库管理","versionCode":"0009","url":"sysMenu/getChildMenu","parentId":0,"des":"黑白名单库管理","birth":1498987962000,"icon":null,"status":1,"roleId":0,"checked":false,"chkDisabled":false,"isHidden":false},{"userId":25,"userId":0,"name":"评分卡管理","versionCode":"0010","url":"sysMenu/getChildMenu","parentId":0,"des":"评分卡管理","birth":1498897962000,"icon":null,"status":1,"roleId":0,"checked":false,"chkDisabled":false,"isHidden":false}]}
	 */
	@RequestMapping(value = "getTreeMenu", method = RequestMethod.POST)
	public ResponseEntityDto getTreeMenu(@RequestBody MenuParam menuParam){
		long parentId = menuParam.getParentId();
		List<SysMenu> listMenu = sysMenuService.getAllValidMenu(menuParam.getEntity());
		if(listMenu!=null&&listMenu.size()>0){
			for(int i=0;i<listMenu.size();i++){
				if(listMenu.get(i).getResourceId()==parentId){
					listMenu.get(i).setChecked(true);
				}
			}
		}
		return ResponseEntityBuilder.buildNormalResponse(listMenu);
	}
	
	/**
	 * @api {POST} /v2/sysMenu/findTreeList 6.37.1. 权限分配，获取资源树
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Integer} roleId 角色编号
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"roleId":76}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":[{"userId":22,"userId":0,"name":"模型管理","versionCode":"0007","url":"sysMenu/getChildMenu","parentId":0,"des":"模型管理","birth":1498980762000,"icon":null,"status":1,"roleId":0,"checked":false,"chkDisabled":false,"isHidden":false},{"userId":23,"userId":0,"name":"数据源管理","versionCode":"0008","url":"sysMenu/getChildMenu","parentId":0,"des":"数据源管理","birth":1498984362000,"icon":null,"status":1,"roleId":0,"checked":false,"chkDisabled":false,"isHidden":false},{"userId":24,"userId":0,"name":"黑白名单库管理","versionCode":"0009","url":"sysMenu/getChildMenu","parentId":0,"des":"黑白名单库管理","birth":1498987962000,"icon":null,"status":1,"roleId":0,"checked":false,"chkDisabled":false,"isHidden":false},{"userId":25,"userId":0,"name":"评分卡管理","versionCode":"0010","url":"sysMenu/getChildMenu","parentId":0,"des":"评分卡管理","birth":1498897962000,"icon":null,"status":1,"roleId":0,"checked":false,"chkDisabled":false,"isHidden":false}]}
	 */
	@RequestMapping(value = "findTreeList", method = RequestMethod.POST)
	public ResponseEntityDto findTreeList(@RequestBody MenuParam menuParam){
		Long roleId = menuParam.getRoleId();
		Long organRoleId = 0L;
		List<SysMenu> listAll = new ArrayList<SysMenu>();
		 //获取登录用户id
		SysUser user = SessionManager.getLoginAccount();
		long userId = user.getUserId();
		long orgId = user.getOrganId();
		//获取登录人角色id
		SysUser sysUser = sysUserService.findRoleByUserId(userId);
		if(sysUser!=null){
			organRoleId = sysUser.getSysRole().getRoleId();
		}
		//角色资源
		menuParam.getEntity().setRoleId(roleId);
		List<SysMenu> listRoleMenu = sysMenuService.findTreeList(menuParam.getEntity());
		if(orgId==1){
			//全部启用资源
			listAll = sysMenuService.getAllValidMenu(menuParam.getEntity());
			if(listAll!=null&&listAll.size()>0){
				for(int i=0;i<listAll.size();i++){
					//初始化禁用节点
					if(listAll.get(i).getResourceId()==14 || listAll.get(i).getResourceId()==13){
						listAll.get(i).setChkDisabled(true);
						listAll.get(i).setHidden(true);
					}
					//判断默认选中
					long id_i = listAll.get(i).getResourceId();
					if (listRoleMenu != null && listRoleMenu.size() > 0) {
						for (int j = 0; j < listRoleMenu.size(); j++) {
							long id_j = listRoleMenu.get(j).getResourceId();
							if (id_j == id_i) {
								listAll.get(i).setChecked(true);
							}
						}
					}
				}
			}
			
		}else{
			//公司资源
			menuParam.getEntity().setRoleId(organRoleId);
			listAll = sysMenuService.findTreeList(menuParam.getEntity());
			if(listAll!=null&&listAll.size()>0){
				for(int i=0;i<listAll.size();i++){
					//初始化禁用节点
					if(listAll.get(i).getResourceId()==4){
						listAll.get(i).setChkDisabled(true);
						listAll.get(i).setHidden(true);
					}
					//判断默认选中
					long id_i = listAll.get(i).getResourceId();
					if (listRoleMenu != null && listRoleMenu.size() > 0) {
						for (int j = 0; j < listRoleMenu.size(); j++) {
							long id_j = listRoleMenu.get(j).getResourceId();
							if (id_j == id_i) {
								listAll.get(i).setChecked(true);
							}
						}
					}
				}
			}
		}
		return ResponseEntityBuilder.buildNormalResponse(listAll);
	}

	/**
	 * @api {POST} /v2/sysMenu/insertRoleMenu 6.38.1. 权限分配保存
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiParam {Integer} roleId 角色编号
	 * @apiParam {String} ids 资源编号，逗号分隔
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"roleId":76,"ids":"1,18,2,15,3,16,17,4,11,12,19"}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":11}
	 */
	@RequestMapping(value = "insertRoleMenu", method = RequestMethod.POST)
	@ArchivesLog(operationType = OpTypeConst.SAVE_OR_UPDATE_MENU_ROLE)
	public ResponseEntityDto insertRoleMenu(@RequestBody Map<String, Object> param) {
		long roleId = Long.valueOf(param.get("roleId").toString());
		String ids = (String)param.get("ids");
		int num = 0;
		List<Integer> list = new ArrayList<Integer>();

		if (ids != "") {
			String[] strs = ids.split(",");
			for (int i = 0; i < strs.length; i++) {
				list.add(Integer.parseInt(strs[i]));
			}
			if (list != null && list.size() > 0) {
				num = sysMenuService.insertRoleMenu(roleId, list);
			}
		}else{
			num = sysMenuService.deleteRoleMenu(roleId);
		}
		return ResponseEntityBuilder.buildNormalResponse(num);
	}
	
	/**
	 * @api {POST} /v2/sysMenu/getMenus 6.39. 获取菜单信息
	 * @apiGroup sysManager
	 * @apiVersion 2.0.0
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":[{"title":"系统首页","index":"sysMenu/getChildMenu","icon":null,"subs":[]},{"title":"引擎列表","index":"sysMenu/getChildMenu","icon":"xx","subs":[]},{"title":"引擎管理","index":"sysMenu/getChildMenu","icon":null,"subs":[]},{"title":"指标管理","index":"sysMenu/getChildMenu","icon":null,"subs":[]},{"title":"规则管理","index":"sysMenu/getChildMenu","icon":"bb","subs":[]},{"title":"评分卡管理","index":"sysMenu/getChildMenu","icon":null,"subs":[]},{"title":"模型管理","index":"sysMenu/getChildMenu","icon":null,"subs":[]},{"title":"数据源管理","index":"sysMenu/getChildMenu","icon":null,"subs":[]},{"title":"黑白名单库管理","index":"sysMenu/getChildMenu","icon":null,"subs":[]},{"title":"系统管理","index":"sysMenu/getChildMenu","icon":null,"subs":[{"title":"用户管理","index":"sysUser/view","icon":null,"subs":[]},{"title":"角色管理","index":"sysRole/view","icon":"aa","subs":[]},{"title":"日志管理","index":"log/index","icon":null,"subs":[]}]}]}
	 */
	@RequestMapping(value = "getMenus", method = RequestMethod.POST)
	public ResponseEntityDto getMenus(@RequestBody MenuParam menuParam){
		List<SysMenu> menuList = new ArrayList<>();
		SysUser user = SessionManager.getLoginAccount();
		long orgId = user.getOrganId();
		long userId = user.getUserId();
		if(orgId==1){
			menuList = sysMenuService.getAllValidMenu(menuParam.getEntity());
		}else{
			long roleId = 0;
			SysUser sysUser = sysUserService.findRoleByUserId(userId);
			if(sysUser!=null){
				roleId = sysUser.getSysRole().getRoleId();
			}
			menuParam.getEntity().setRoleId(roleId);
			menuList = sysMenuService.findTreeList(menuParam.getEntity());
		}

		long parentId = 0;
		List<SysMenuVo> result = recursionMenu(menuList, parentId);

		return ResponseEntityBuilder.buildNormalResponse(result);
	}

	/**
	 * 递归获取子菜单
	 * @param menuList
	 * @param parentId
	 * @return
	 */
	private List<SysMenuVo> recursionMenu(List<SysMenu> menuList, long parentId){
		List<SysMenuVo> sysMenuVoList = new ArrayList<>();
		for(SysMenu sysMenu : menuList) {
			if(sysMenu.getParentId() == parentId){
				SysMenuVo sysMenuVo = new SysMenuVo();
				sysMenuVo.setTitle(sysMenu.getName());
				sysMenuVo.setIndex(StringUtils.isBlank(sysMenu.getUrl()) ? UUID.randomUUID().toString() : sysMenu.getUrl());
				sysMenuVo.setIcon(sysMenu.getIcon());

				List<SysMenuVo> sysMenuVos = recursionMenu(menuList, sysMenu.getResourceId());
				if(!sysMenuVos.isEmpty()){
					sysMenuVo.setSubs(sysMenuVos);
				}

				sysMenuVoList.add(sysMenuVo);
			}
		}
		return sysMenuVoList;
	}

}