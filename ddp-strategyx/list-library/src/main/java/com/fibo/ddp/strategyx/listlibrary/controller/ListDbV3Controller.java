package com.fibo.ddp.strategyx.listlibrary.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.common.requestParam.UpdateFolderParam;
import com.fibo.ddp.common.model.common.requestParam.UpdateStatusParam;
import com.fibo.ddp.common.model.strategyx.listlibrary.ListDb;
import com.fibo.ddp.common.model.strategyx.listlibrary.request.ListDbDataParam;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.service.strategyx.listlibrary.ListDbV3Service;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.ResponseUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @apiDefine listDb 4.黑白名单库管理
 */
@RestController("listDbController")
@RequestMapping("v3/listDb")
public class ListDbV3Controller {
	@Autowired
	private ListDbV3Service listDbService;
	/**
	 * @api {POST} /v2/datamanage/listmanage/list 4.01. 获取黑、白名单列表
	 * @apiGroup listDb
	 * @apiVersion 2.0.0
	 * @apiParam {String} listType 名单库区分,b表示黑名单,w表示白名单
	 * @apiParam {Integer} pageNo 第几页
	 * @apiParam {Integer} pageSize 每页的条数
	 * @apiSuccess {JSONArray} listDbs 名单库列表
	 * @apiSuccess (listDbs) {Long} userId 名单库Id
	 * @apiSuccess (listDbs) {String} listType 名单库区分,用b表示黑名单,w表示白名单
	 * @apiSuccess (listDbs) {String} listName 名单库名称
	 * @apiSuccess (listDbs) {Integer} dataSource 数据来源：外部黑(白)名单（1）、内部黑(白)名单（2）、待选（0）
	 * @apiSuccess (listDbs) {String} listAttr 名单库属性,用户输入
	 * @apiSuccess (listDbs) {String} listDesc 名单库描述
	 * @apiSuccess (listDbs) {String} tableColumn 名单库表中列字段，字段id逗号分隔
	 * @apiSuccess (listDbs) {Integer} matchType 检索匹配类型，精确匹配（1），模糊匹配（0）
	 * @apiSuccess (listDbs) {Integer} queryType 查询字段间逻辑，and（1），or（0）
	 * @apiSuccess (listDbs) {String} queryField 查询主键，字段编号逗号分割
	 * @apiSuccess (listDbs) {Integer} status 名单库状态 启用（1），停用（0），删除（-1）
	 * @apiSuccess (listDbs) {String} nickName 创建人
	 * @apiSuccess (listDbs) {Long} created 创建时间
	 * @apiSuccess {JSON} pager 分页信息
	 * @apiParamExample {json} 请求示例：
	 * {"listType":"b","pageNo":1,"pageSize":2}
	 * @apiSuccessExample {json} Success-Response:
	 * {"status":"1","error":"00000000","msg":null,"data":{"pager":{"pageNum":1,"pageSize":2,"size":2,"startRow":1,"endRow":2,"total":5,"pages":3,"list":null,"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2,3],"navigateFirstPage":1,"navigateLastPage":3,"lastPage":3,"firstPage":1},"listDbs":[{"page":0,"rows":0,"total":null,"userId":116,"listType":"b","listName":"测试-黑名单3","dataSource":2,"listAttr":"666","listDesc":"测试-黑名单333","tableColumn":"587","matchType":1,"queryType":1,"queryField":"587","queryFieldCn":null,"organId":46,"status":0,"userId":135,"created":1615952361000,"nickName":"管理员"},{"page":0,"rows":0,"total":null,"userId":117,"listType":"b","listName":"测试-黑名单5","dataSource":1,"listAttr":"666","listDesc":"测试-黑名单555hh","tableColumn":"587,589","matchType":1,"queryType":1,"queryField":"589","queryFieldCn":null,"organId":46,"status":0,"userId":135,"created":1615952361000,"nickName":"管理员"}]}}
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ResponseEntityDto list(@RequestBody QueryListParam<ListDb> param) {
		PageInfo pageInfo = listDbService.queryByEntity(param);
		Map<String, Object> responseMap = ResponseUtil.getResponseMap(pageInfo);
		return  ResponseEntityBuilder.buildNormalResponse(responseMap);
	}
	/**
	 * @api {POST} /v2/datamanage/listmanage/save 4.02. 添加黑、白名单库
	 * @apiGroup listDb
	 * @apiVersion 2.0.0
	 * @apiParam {String} listType 名单库区分,用b表示黑名单,w表示白名单
	 * @apiParam {String} listName 名单库名称
	 * @apiParam {Integer} dataSource 数据来源：外部黑(白)名单（1）、内部黑(白)名单（2）、待选（0）
	 * @apiParam {String} listAttr 名单库属性,用户输入
	 * @apiParam {String} listDesc 名单库描述
	 * @apiParam {String} tableColumn 名单库表中列字段，字段id逗号分隔
	 * @apiParam {Integer} matchType 检索匹配类型，精确匹配（1），模糊匹配（0）
	 * @apiParam {Integer} queryType 查询字段间逻辑，and（1），or（0）
	 * @apiParam {String} queryField 查询主键，字段编号逗号分割
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"listType":"b","listName":"测试-黑名单5","dataSource":2,"listAttr":"666","listDesc":"测试-黑名单555","tableColumn":"587,589","matchType":1,"queryType":1,"queryField":"589"}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":true}
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.SAVE_BLACK_OR_WHITE_LIST_DB)
	public ResponseEntityDto<Object> save(@RequestBody ListDb listDb) {
		boolean result = listDbService.addListDb(listDb);
		return ResponseEntityBuilder.buildNormalResponse(result);
	}


	/**
	 * @api {POST} /v2/datamanage/listmanage/getListDbInfo/{userId} 4.03. 获取黑、白名单库详情
	 * @apiGroup listDb
	 * @apiVersion 2.0.0
	 * @apiParam {JSON} listDbInfo 名单库基础信息
	 * @apiParam (listDbInfo) {Long} userId 名单库Id
	 * @apiParam (listDbInfo) {String} listType 名单库区分,用b表示黑名单,w表示白名单
	 * @apiParam (listDbInfo) {String} listName 名单库名称
	 * @apiParam (listDbInfo) {Integer} dataSource 数据来源：外部黑(白)名单（1）、内部黑(白)名单（2）、待选（0）
	 * @apiParam (listDbInfo) {String} listAttr 名单库属性,用户输入
	 * @apiParam (listDbInfo) {String} listDesc 名单库描述
	 * @apiParam (listDbInfo) {String} tableColumn 名单库表中列字段，字段id逗号分隔
	 * @apiParam (listDbInfo) {Integer} matchType 检索匹配类型，精确匹配（1），模糊匹配（0）
	 * @apiParam (listDbInfo) {Integer} queryType 查询字段间逻辑，and（1），or（0）
	 * @apiParam (listDbInfo) {String} queryField 查询主键，字段编号逗号分割
	 * @apiParam {JSONArray} tableColumnList 名单库表中列字段集合
	 * @apiParam (tableColumnList) {Long} userId 指标Id
	 * @apiParam (tableColumnList) {Long} fieldEn 指标英文名
	 * @apiParam (tableColumnList) {Long} fieldCn 指标中文名
	 * @apiParam {JSONArray} queryFieldList 查询主键字段集合
	 * @apiParam (queryFieldList) {Long} userId 指标Id
	 * @apiParam (queryFieldList) {Long} fieldEn 指标英文名
	 * @apiParam (queryFieldList) {Long} fieldCn 指标中文名
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"listType":"b"}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":{"listDbInfo":{"page":0,"rows":0,"total":null,"userId":117,"listType":"b","listName":"测试-黑名单5","dataSource":1,"listAttr":"666","listDesc":"测试-黑名单555hh","tableColumn":"587,589","matchType":1,"queryType":1,"queryField":"589","queryFieldCn":null,"organId":46,"status":1,"userId":135,"created":1615897180000,"nickName":null},"queryFieldList":[{"page":0,"rows":0,"total":null,"userId":589,"fieldEn":"f_td_finalScore","fieldCn":"同盾_评分","fieldTypeId":271,"fieldType":"反欺诈1","valueType":1,"valueTypeName":null,"valueScope":"(0,999999]","isDerivative":0,"isDerivativeName":null,"isOutput":0,"isOutputName":null,"isCommon":1,"formula":"","formulaShow":"","usedFieldId":null,"origFieldId":null,"author":null,"nickName":null,"created":null,"engineId":null,"engineName":null,"status":null,"fieldCondList":[],"fieldRelId":null,"dataSourceId":null,"sqlStatement":null,"useSql":false}],"tableColumnList":[{"page":0,"rows":0,"total":null,"userId":587,"fieldEn":"f_hr_age","fieldCn":"年龄准入","fieldTypeId":270,"fieldType":"准入","valueType":1,"valueTypeName":null,"valueScope":"(-1,999999]","isDerivative":0,"isDerivativeName":null,"isOutput":0,"isOutputName":null,"isCommon":1,"formula":"","formulaShow":"","usedFieldId":null,"origFieldId":null,"author":null,"nickName":null,"created":null,"engineId":null,"engineName":null,"status":null,"fieldCondList":[],"fieldRelId":null,"dataSourceId":null,"sqlStatement":null,"useSql":false},{"page":0,"rows":0,"total":null,"userId":589,"fieldEn":"f_td_finalScore","fieldCn":"同盾_评分","fieldTypeId":271,"fieldType":"反欺诈1","valueType":1,"valueTypeName":null,"valueScope":"(0,999999]","isDerivative":0,"isDerivativeName":null,"isOutput":0,"isOutputName":null,"isCommon":1,"formula":"","formulaShow":"","usedFieldId":null,"origFieldId":null,"author":null,"nickName":null,"created":null,"engineId":null,"engineName":null,"status":null,"fieldCondList":[],"fieldRelId":null,"dataSourceId":null,"sqlStatement":null,"useSql":false}]}}
	 */
	@RequestMapping(value = "/getListDbInfo/{id}", method = RequestMethod.POST)
    public ResponseEntityDto<Object> getListDbInfo(@PathVariable Long id) {
		if (id==null){
			throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
		}
		ListDb listDb = listDbService.queryById(id);
		return ResponseEntityBuilder.buildNormalResponse(listDb);
    }
    
	/**
	 * @api {POST} /v2/datamanage/listmanage/update 4.04. 修改黑、白名单库
	 * @apiGroup listDb
	 * @apiVersion 2.0.0
	 * @apiParam {Long} userId 名单库Id
	 * @apiParam {String} listType 名单库区分,用b表示黑名单,w表示白名单
	 * @apiParam {String} listName 名单库名称
	 * @apiParam {Integer} dataSource 数据来源：外部黑(白)名单（1）、内部黑(白)名单（2）、待选（0）
	 * @apiParam {String} listAttr 名单库属性,用户输入
	 * @apiParam {String} listDesc 名单库描述
	 * @apiParam {String} tableColumn 名单库表中列字段，字段id逗号分隔
	 * @apiParam {Integer} matchType 检索匹配类型，精确匹配（1），模糊匹配（0）
	 * @apiParam {Integer} queryType 查询字段间逻辑，and（1），or（0）
	 * @apiParam {String} queryField 查询主键，字段编号逗号分割
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"userId":117,"listType":"b","listName":"测试-黑名单5","dataSource":1,"listAttr":"666","listDesc":"测试-黑名单555hh","tableColumn":"587,589","matchType":1,"queryType":1,"queryField":"589"}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":true}
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_BLACK_OR_WHITE_LIST_DB)
    public ResponseEntityDto<Object> update(@RequestBody ListDb listDb) {
		boolean result = listDbService.updateListDb(listDb);
        return ResponseEntityBuilder.buildNormalResponse(result);
    }

	/**
	 * @api {POST} /v2/datamanage/listmanage/updateStatus 4.05. 名单库停用、启用、删除
	 * @apiGroup listDb
	 * @apiVersion 2.0.0
	 * @apiParam {String} ids 名单库Id，多个用逗号分隔
	 * @apiParam {Integer} status 名单库状态 启用（1），停用（0），删除（-1）
	 * @apiParam {String} listType 名单库区分,用b表示黑名单,w表示白名单
	 * @apiSuccess {String} status 状态: 1成功, 0失败
	 * @apiParamExample {json} 请求示例：
	 * {"ids":"116,117","status":0,"listType":"b"}
	 * @apiSuccessExample {json} 成功返回数据示例：
	 * {"status":"1","error":"00000000","msg":null,"data":true}
	 */
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	@ArchivesLog(operationType = OpTypeConst.UPDATE_BLACK_OR_WHITE_LIST_DB)
	public ResponseEntityDto<Object> updateStatus(@RequestBody UpdateStatusParam param) {
		UpdateStatusParam.checkParam(param);
		listDbService.updateStatus(param.getList(),param.getStatus());
		return ResponseEntityBuilder.buildNormalResponse();
	}
	@ResponseBody
	@RequestMapping(value = "/updateFolder", method = RequestMethod.POST)
	@ArchivesLog(operationType = OpTypeConst.UPDATE_BLACK_OR_WHITE_LIST_DB_FOLDER)
	public ResponseEntityDto updateFolder(@RequestBody UpdateFolderParam param) {
		UpdateFolderParam.checkNotNull(param);
		boolean updateResult = listDbService.updateFolder(param.getIds(), param.getFolderId());
		if (updateResult) {
			return ResponseEntityBuilder.buildNormalResponse(updateResult);
		} else {
			return ResponseEntityBuilder.buildErrorResponse(ErrorCodeEnum.SERVER_ERROR);
		}
	}
	/**
	 * @api {POST} /v2/datamanage/listmanage/downTemplate 4.06. 名单库导入模板下载
	 * @apiGroup listDb
	 * @apiVersion 2.0.0
	 */
	@RequestMapping(value = "/downTemplate")
	public ResponseEntity<byte[]> downExcelTemplate() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("excleTemplate/listDb.xlsx");
		InputStream inputStream = classPathResource.getInputStream();
		HttpHeaders headers = new HttpHeaders();
		String fileName = new String("名单库导入模板.xlsx".getBytes("UTF-8"), "iso-8859-1");//为了解决中文名称乱码问题
		headers.setContentDispositionFormData("attachment", fileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(IOUtils.toByteArray(inputStream), headers, HttpStatus.CREATED);
	}

	/**
	 * @api {POST} /v2/datamanage/listmanage/upload/{userId} 4.07. 名单库导入
	 * @apiGroup listDb
	 * @apiVersion 2.0.0
	 */
	@RequestMapping(value = "/upload/{id}", method = RequestMethod.POST)
	public ResponseEntityDto<Object> upload(HttpServletRequest request, @PathVariable Long id) throws Exception {
		//将当前上下文初始化给CommonsMutipartResolver（多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (!multipartResolver.isMultipart(request)) {
			throw new ApiException(ErrorCodeEnum.FILE_UPLOAD_ERROR.getCode(), ErrorCodeEnum.FILE_UPLOAD_ERROR.getMessage());
		}
		String result = listDbService.upload(request, id);
		return ResponseEntityBuilder.buildNormalResponse(result);
	}

	/**
	 * @api {POST} /v2/datamanage/listmanage/getListDbData 4.08. 查询名单库数据
	 * @apiGroup listDb
	 * @apiVersion 2.0.0
	 */
	@RequestMapping(value = "/getListDbData", method = RequestMethod.POST)
	public ResponseEntityDto<Object> getListDbData(@RequestBody ListDbDataParam param) {
		Map<String, Object> result = listDbService.getListDbData(param);
		return ResponseEntityBuilder.buildNormalResponse(result);
	}

}
