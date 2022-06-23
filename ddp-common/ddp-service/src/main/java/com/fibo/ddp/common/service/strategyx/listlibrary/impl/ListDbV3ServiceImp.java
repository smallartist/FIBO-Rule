package com.fibo.ddp.common.service.strategyx.listlibrary.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.dao.strategyx.listlibrary.ListDbMapper;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.datax.common.ExcelUtil;
import com.fibo.ddp.common.model.strategyx.listlibrary.ListDb;
import com.fibo.ddp.common.model.strategyx.listlibrary.ListDbVersion;
import com.fibo.ddp.common.model.strategyx.listlibrary.request.ListDbDataParam;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.datax.datamanage.FieldService;
import com.fibo.ddp.common.service.strategyx.listlibrary.ListDbV3Service;
import com.fibo.ddp.common.service.strategyx.listlibrary.ListDbVersionService;
import com.fibo.ddp.common.service.strategyx.strategyout.StrategyOutputService;
import com.fibo.ddp.common.utils.constant.strategyx.StrategyType;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class ListDbV3ServiceImp extends ServiceImpl<ListDbMapper, ListDb> implements ListDbV3Service {

	@Resource
	StrategyOutputService outputService;
	@Resource
	FieldService fieldService;
	@Resource
	public ListDbMapper listDbMapper;
	@Resource
	private ListDbVersionService versionService;
	@Resource
	public SysUserMapper sysUserMapper;

	@Override
	public boolean updateStatus(List<Long> ids,Integer status) {
		LambdaUpdateWrapper<ListDb> wrapper = new LambdaUpdateWrapper<>();
		wrapper.in(ListDb::getId, ids);
		ListDb listDb = new ListDb();
		listDb.setStatus(status);
		int updateNum = listDbMapper.update(listDb, wrapper);
		return updateNum > 0;
	}

	@Override
	@Transactional
	public boolean addListDb(ListDb listDb) {
		this.isExists(listDb);
		Long userId = SessionManager.getLoginAccount().getUserId();
		Long organId = SessionManager.getLoginAccount().getOrganId();
		listDb.setUserId(userId);
		listDb.setOrganId(organId);
		listDb.setStatus(1);
		listDbMapper.createListDb(listDb);
		versionService.addVersionList(listDb.getVersionList());
		return true;
	}

	@Override
	public ListDb findById(Map<String, Object> paramMap) {
		ListDb listDb = listDbMapper.findById(paramMap);
		listDb.setNickName(sysUserMapper.findNickNameById(listDb.getUserId()));
		List<StrategyOutput> strategyOutputs = outputService.queryByTactics(new StrategyOutput(listDb.getId(), StrategyType.LIST_DB));
		listDb.setStrategyOutputList(strategyOutputs);
		return listDb;
	}

	@Override
	@Transactional
	public boolean updateListDb(ListDb listDb) {
		this.isExists(listDb);
		Long organId = SessionManager.getLoginAccount().getOrganId();
		LambdaUpdateWrapper<ListDb> wrapper = new LambdaUpdateWrapper<>();
		wrapper.eq(ListDb::getId, listDb.getId()).eq(ListDb::getOrganId, organId);
		listDbMapper.update(listDb, wrapper);
		for (ListDbVersion listDbVersion : listDb.getVersionList()) {
			versionService.updateVersion(listDbVersion);
		}
		return true;
	}

	@Override
	public boolean isExists(ListDb listDb) {
    	if (listDb==null){
    		throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
		}
		LambdaQueryChainWrapper<ListDb> wrapper = new LambdaQueryChainWrapper<>(listDbMapper);
		if (listDb.getId()!=null){
			wrapper.ne(ListDb::getId, listDb.getId());
		}
		int count = wrapper.eq(ListDb::getListName, listDb.getListName())
				.eq(ListDb::getListCode,listDb.getListCode())
				.eq(ListDb::getOrganId, SessionManager.getLoginAccount().getOrganId()).count();
		if(count > 0){
			throw new ApiException(ErrorCodeEnum.LIST_DB_NAME_REPEAT.getCode(), "名单库名称、代码、类型不能完全相同");
		}
		return true;
	}

	@Override
	public String upload(HttpServletRequest request, Long id) throws Exception {
		String result = "";
		//将request变成多部分request
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		//获取multiRequest中所有的文件名
		Iterator iter = multiRequest.getFileNames();
		//遍历所有文件
		while (iter.hasNext()) {
			MultipartFile file = multiRequest.getFile(iter.next().toString());
			// 暂存文件到本地
			if (file != null) {
				String uploadDir = request.getSession().getServletContext().getRealPath("/") + "upload/listDbUpload/";
				if (!new File(uploadDir).exists()) {
					File dir = new File(uploadDir);
					dir.mkdirs();
				}
				String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
				String accessUrl = uploadDir + fileName;
				// 保存文件
				file.transferTo(new File(accessUrl));
				result = importExcel(accessUrl, id);
			}
		}
		return result;
	}

	private String importExcel(String accessUrl, Long id) {
		InputStream inputStream = null;
		Workbook workbook = null;
		Sheet sheet;
		try {
			inputStream = new FileInputStream(accessUrl);
			workbook = WorkbookFactory.create(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApiException(ErrorCodeEnum.FILE_UPLOAD_ERROR.getCode(), ErrorCodeEnum.FILE_UPLOAD_ERROR.getMessage());
		}

		int sucRows = 0; // 导入成功行数
		int failRows = 0; // 导入失败行数

		Long userId = SessionManager.getLoginAccount().getUserId();
		ListDb listDb = listDbMapper.selectById(id);
		String tableName = "organ" + "_" + listDb.getOrganId() + "_" + listDb.getListType() + "_" + id;
		// 插入多行数据 insertOne into  user_info (user_account,user_name,user_age,user_class) values ('00001', '张三 ','20','计算机系'), ('00002', '李四','19','计算机系');
		String sqlStr = "insertOne into " + tableName + "(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, user_id) values ";

		// 只取第一个Sheet页
		sheet = workbook.getSheetAt(0);
		if (sheet == null) {
			throw new ApiException(ErrorCodeEnum.FILE_UPLOAD_ERROR.getCode(), ErrorCodeEnum.FILE_UPLOAD_ERROR.getMessage());
		}

		List<String> multiValusArr = new ArrayList<>();
		// 循环行 Row
		for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
			try {
				Row Row = sheet.getRow(rowNum);
				if (Row == null) {
					continue;
				}

				List<String> valueArr = new ArrayList<>();
				// 循环单元格 Cell
				int lastCellNum = Row.getLastCellNum();
				for (int cellNum = 0; cellNum < 20; cellNum++) {
					Cell cell = Row.getCell(cellNum);
					String cellStr = ExcelUtil.getCellValue(cell).trim();
					valueArr.add("'" + cellStr + "'");
				}
				String valueStr = "(" + StringUtils.join(valueArr, ",") + "," + userId + ")";
				multiValusArr.add(valueStr);

				sucRows++;
			} catch (Exception e) {
				failRows++;
				e.printStackTrace();
			}
		}// end for Row

		String multiValusStr = StringUtils.join(multiValusArr, ",");

		sqlStr += multiValusStr;

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("sqlStr", sqlStr);
		listDbMapper.customInsert(paramMap);

		String result = "导入成功" + sucRows + "条，失败" + failRows + "条！";

		return result;
	}

	@Override
	public Map<String, Object> getListDbData(ListDbDataParam param) {
		Map<String, Object> result = new HashMap<>();
    	Long id = param.getId();
		ListDb listDb = listDbMapper.selectById(id);
		// 查询语句
		String tableName = "organ" + "_" + listDb.getOrganId() + "_" + listDb.getListType() + "_" + id;
		String sqlStr = "select * from " + tableName;

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("sqlStr", sqlStr);
		PageHelper.startPage(param.getPageNo(), param.getPageSize());
		List<LinkedHashMap<String, Object>> list = listDbMapper.customSelect(paramMap);
		PageInfo<LinkedHashMap<String, Object>> pageInfo = new PageInfo<>(list);
		pageInfo.setList(null);
		result.put("pageInfo", pageInfo);
		result.put("list", list);
    	return result;
	}

	@Override
	public boolean updateFolder(List<Long> ids, Long folderId) {
		LambdaUpdateWrapper<ListDb> wrapper = new LambdaUpdateWrapper<>();
		wrapper.in(ListDb::getId, ids);
		ListDb listDb = new ListDb();
		listDb.setFolderId(folderId);
		int updateNum = listDbMapper.update(listDb, wrapper);
		return updateNum > 0;
	}
	@Override
	public ListDb queryById(Long id) {
		if (id==null){
			return null;
		}
		ListDb listDb = this.getById(id);
		if (listDb.getOrganId()!=SessionManager.getLoginAccount().getOrganId()){
			return null;
		}
		List<ListDbVersion> listDbVersions = versionService.queryVersionListByListDbId(id);
		List<ListDbVersion> list  = new ArrayList<>();
		if (listDbVersions!=null&&!listDbVersions.isEmpty()){
			for (ListDbVersion listDbVersion : listDbVersions) {
				list.add(versionService.queryById(listDbVersion.getId()));
			}
		}
		listDb.setVersionList(list);
		return listDb;
	}
	@Override
	public PageInfo queryByEntity(QueryListParam<ListDb> listParam) {
		ListDb query = listParam.getEntity();
		Integer pageNum = listParam.getPageNum();
		Integer pageSize = listParam.getPageSize();
		if (pageNum > 0 && pageSize > 0) {
			PageHelper.startPage(pageNum, pageSize);
		}
		LambdaQueryWrapper<ListDb> wrapper = createWrapper(query);
		List<ListDb> ListDbs = listDbMapper.selectList(wrapper);
		PageInfo pageInfo = new PageInfo(ListDbs);

		//TODO 循环查用户表，待优化
		for (ListDb listDb : ListDbs) {
			if (listDb != null && listDb.getUserId() != null) {
				listDb.setNickName(sysUserMapper.findNickNameById(listDb.getUserId()));
				listDb.setVersionList(versionService.queryVersionListByListDbId(listDb.getId()));
			}
		}
		return pageInfo;
	}

	@Override
	public List<String> queryFieldEnsByListDbIds(Long id) {
		String idStr = listDbMapper.selectFieldIdsByListDbId(id);
		Set<String> fieldEns= new HashSet<>();
		if (idStr!=null&&!"".equals(idStr)){
			String[] split = idStr.split(",");
			for (String s : split) {
				fieldEns.add(fieldService.getFieldEnById(Long.valueOf(s)));
			}
		}
		return new ArrayList<>(fieldEns);
	}

	private LambdaQueryWrapper<ListDb> createWrapper(ListDb query) {
		LambdaQueryWrapper<ListDb> wrapper = new LambdaQueryWrapper<>();
		if (query!=null){
			if (StringUtils.isNotBlank(query.getListName())) {
				wrapper.like(ListDb::getListName, query.getListName());
			}
			if (query.getListType() != null) {
				wrapper.like(ListDb::getListType, query.getListType());
			}
			if (query.getListAttr()!=null){
				wrapper.eq(ListDb::getListAttr,query.getListAttr());
			}
			if (query.getDataSource()!=null){
				wrapper.eq(ListDb::getDataSource,query.getDataSource());
			}
			if (query.getFolderId()!=null&&query.getFolderId()!=0){
				wrapper.eq(ListDb::getFolderId,query.getFolderId());
			}
			if (StringUtils.isNotBlank(query.getListCode())){
				wrapper.like(ListDb::getListCode, query.getListCode());
			}
			if (StringUtils.isNotBlank(query.getNodeQuery())&&"1".equals(query.getNodeQuery())){
				wrapper.eq(ListDb::getStatus,1);
			}
		}
		wrapper.ne(ListDb::getStatus,-1);
		wrapper.eq(ListDb::getOrganId,SessionManager.getLoginAccount().getOrganId());
		wrapper.orderByDesc(ListDb::getCreated, ListDb::getId);
		return wrapper;
	}

}
