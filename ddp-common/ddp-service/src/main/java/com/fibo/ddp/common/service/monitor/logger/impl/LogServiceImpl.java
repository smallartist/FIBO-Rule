package com.fibo.ddp.common.service.monitor.logger.impl;

import com.fibo.ddp.common.dao.monitor.logger.LoggerMapper;
import com.fibo.ddp.common.model.monitor.logger.Logger;
import com.fibo.ddp.common.service.monitor.logger.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ClassName:LogService </br>
 * Description:日志接口实现
 * */
@Service
public class LogServiceImpl implements LogService {

	@Resource
	public LoggerMapper loggerMapper;

	@Override
	public List<Logger> getLogList(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return loggerMapper.getLogList(param);
	}

	@Override
	public Logger findById(Long id) {
		// TODO Auto-generated method stub
		return loggerMapper.selectById(id);
	}

	@Override
	public List<Date> getLastLoginInfo(Long userId) {
		return loggerMapper.getLastLoginInfo(userId);
	}
}
