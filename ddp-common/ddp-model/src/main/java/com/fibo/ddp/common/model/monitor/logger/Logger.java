package com.fibo.ddp.common.model.monitor.logger;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * ClassName:Logger <br/>
 * Description: 日志实体类. <br/>
 */
@Data
@TableName("t_logger")
public class Logger {
       
	  /**
	   * 主键
	   * */
	  @TableId(type = IdType.AUTO)
	  private Long id; 
	  
	  /**
	   * 操作类型
	   * */
	  private String opType;
	  
	  
	  /**
	   * 公司名称
	   * */
	  private String organName;
	  
	  /**
	   * 操作名称
	   * */
	  private String opName;
	  
	  /**
	   * 操作人员id
	   * */
	  private Long opUserId;
	  
	  /**
	   * 操作人员id
	   * */
	  private String opUserName;
	  
	  /**
	   * 组织id
	   * */
	  private Long organId;
	  
	  /**
	   * 方法名
	   * */
	  private String method;
	  
	  /**
	   * 请求地址
	   * */
	  private String requestPath;
	  
	  /**
	   * 请求参数
	   * */
	  private String requestParam;
	  
	  /**
	   * 响应参数
	   * */
	  private String responseParam;
	  
	  /**
	   * 请求ip
	   * */
	  private String ip;
	  
	  /**
	   * 开始时间
	   * */
	  private Date startTime;
	  /**
	   * 结束时间
	   * */
	  private Date endTime;
}
