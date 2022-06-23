package com.fibo.ddp.common.service.strategyx.listlibrary.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.dao.strategyx.listlibrary.ListDbMapper;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.datax.common.ExcelUtil;
import com.fibo.ddp.common.model.strategyx.listlibrary.ListDb;
import com.fibo.ddp.common.model.strategyx.listlibrary.ListDbVersion;
import com.fibo.ddp.common.model.strategyx.listlibrary.request.ListDbDataParam;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import com.fibo.ddp.common.service.common.runner.RunnerSessionManager;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.strategyx.listlibrary.ListDbService;
import com.fibo.ddp.common.service.strategyx.listlibrary.ListDbVersionService;
import com.fibo.ddp.common.service.strategyx.strategyout.StrategyOutputService;
import com.fibo.ddp.common.utils.constant.strategyx.StrategyType;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class ListDbServiceImp extends ServiceImpl<ListDbMapper, ListDb> implements ListDbService {

	@Resource
	StrategyOutputService outputService;
	@Resource
	public ListDbMapper listDbMapper;
	@Resource
	public SysUserMapper sysUserMapper;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Resource
	private ListDbVersionService versionService;

	@Override
	public List<ListDb> findByUser(Map<String, Object> paramMap) {
		return listDbMapper.findByUser(paramMap);
	}

	@Override
	public boolean updateStatus(Map<String, Object> paramMap) {
		return listDbMapper.updateStatus(paramMap);
	}

	@Override
	@Transactional
	public boolean createListDb(ListDb listDb, Map<String, Object> paramMap) {
		
		Long organId = SessionManager.getLoginAccount().getOrganId();
		listDbMapper.createListDb(listDb);
		Long id = listDb.getId();
		
		String listType = (String) paramMap.get("listType");
		String tableName = "organ"+"_"+organId+"_"+listType+"_"+id;
		String sqlStr = "CREATE TABLE "+  tableName + "(" +
				"  `id` int(11) NOT NULL AUTO_INCREMENT comment 'id'," ;
		//生成表字段
		String tableColumn = String.valueOf(paramMap.get("tableColumn"));
		String arrayTableColumn[] = tableColumn.split(",");
		int arrayTC[] = new int[arrayTableColumn.length];
		for(int i=0;i<arrayTableColumn.length;i++){  
			arrayTC[i]=Integer.parseInt(arrayTableColumn[i]);
		    sqlStr += "  `t" + i + "`" + " varchar(100) DEFAULT NULL comment '" + arrayTC[i]+ "'," ;
		}
		int start = arrayTableColumn.length;
		for(int j=start;j<20;j++){
			sqlStr += "  `t" + j + "`" + " varchar(100) DEFAULT NULL comment ''," ;
		}
		
	    sqlStr += "`user_id` int(11) NOT NULL COMMENT '创建人编号',"+
                  "`nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',"+
                  "`created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',"+
	    		  "PRIMARY KEY (`id`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		paramMap.put("sqlStr", sqlStr);
		
		listDbMapper.createTable(paramMap);
		
		Map<String, Object> indexMap = new HashMap<String, Object>();
		
		//循环生成索引列
		String queryField = String.valueOf(paramMap.get("queryField"));
		String arrayQueryField[] = queryField.split(",");
		int arrayQF[] = new int[arrayQueryField.length];
		
		int arrayTC2[] = new int[arrayTableColumn.length];
		String indexStr = "";
		for(int i=0;i<arrayQueryField.length;i++){
			//查询字段id
			arrayQF[i]=Integer.parseInt(arrayQueryField[i]);
			//循环维护字段id
			for(int j=0;j<arrayTableColumn.length;j++){
				arrayTC2[j]=Integer.parseInt(arrayTableColumn[j]);
				if(arrayQF[i]==arrayTC2[j]){
					if(indexStr.equals("")){
				    	indexStr = "  `t" + j + "`";
				    }else{
				    	indexStr += "," + "  `t" + j + "`";
				    }
				}
			}
		}
		
		String indexSql = "ALTER TABLE "+ tableName +" ADD INDEX `idx_top4` ("+indexStr+") ;";
		indexMap.put("indexSql", indexSql);
		
		listDbMapper.createIndex(indexMap);
		outputService.insertTacticsOutput(id,listDb.getStrategyOutputList());
		saveSnapshot(id);
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
	public boolean updateListDb(Map<String, Object> paramMap) {
		boolean b = listDbMapper.updateListDb(paramMap);
		ListDb listDb = new ListDb();
		try {
			BeanUtils.populate(listDb,paramMap);
		} catch (IllegalAccessException|InvocationTargetException e) {
			throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
		}
		List<StrategyOutput> strategyOutputList = JSONArray.parseArray(JSON.toJSONString(listDb.getStrategyOutputList()), StrategyOutput.class);
		outputService.updateTacticsOutput(listDb.getId(), strategyOutputList, StrategyType.LIST_DB);
		saveSnapshot(Long.valueOf(paramMap.get("id").toString()));
		return b;
	}

	/**
	 * 
	 * Description: 获取jdbc.properties里配置的数据库名
	 * @return 
	 * @see
	 */
    private String getDbName(){
		
		ResourceBundle resource = ResourceBundle.getBundle("conf/jdbc");
		String mysqlUrl = resource.getString("mysql.url");
		

		String aArray[]=mysqlUrl.split("/"); 
		String bArray[]=aArray[3].split("\\?");
    	String dbName = bArray[0];
        
		return dbName;
	}

	@Override
	public Integer isExists(Map<String, Object> paramMap) {
		return listDbMapper.isExists(paramMap);
	}

	@Override
	public String findFieldsByListDbIds(Map<String, Object> paramMap) {
		return listDbMapper.findFieldsByListDbIds(paramMap);
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

	private boolean saveSnapshot(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("id",id);
		map.put("userId",SessionManager.getLoginAccount().getUserId());
		threadPoolTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				LambdaUpdateWrapper<ListDb> wrapper = new LambdaUpdateWrapper<>();
				ListDb listDb =  findById(map);
				listDb.setSnapshot(null);
				wrapper.eq(ListDb::getId, id).set(ListDb::getSnapshot, JSON.toJSONString(listDb));
				listDbMapper.update(null, wrapper);
			}
		});
		return true;
	}

	@Override
	public ListDb queryByVersionId(Long versionId) {
		ListDbVersion version = versionService.queryById(versionId);
		Long listDbId = version.getListDbId();
		ListDb listDb = this.getById(listDbId);
		listDb.setExecuteVersion(version);
		return listDb;
	}

	@Override
	public ListDb queryById(Long id) {
		if (id==null){
			return null;
		}
		ListDb listDb = this.getById(id);
		if (listDb.getOrganId()!= RunnerSessionManager.getSession().getOrganId()){
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
	public List<Long> getNodeFieldIds(Long id) {
		ListDb listDb = queryById(id);
		String queryField = listDb.getQueryField();
		List<Long> list = new ArrayList<>();
		if (StringUtils.isNotBlank(queryField)){
			String[] split = queryField.split(",");
			for (String s : split) {
				list.add(Long.valueOf(s));
			}
		}
		return list;
	}
}
