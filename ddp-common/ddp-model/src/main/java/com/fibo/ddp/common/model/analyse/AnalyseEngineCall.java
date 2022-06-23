package com.fibo.ddp.common.model.analyse;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("t_analyse_engine_call")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyseEngineCall extends Model<AnalyseEngineCall> {
    private static final long serialVersionUID = -3102832011497003819L;
    //引擎调用表主键id
    @TableId(type = IdType.AUTO)
    private Long id;
    //调用时间
    private Date callDate;
    //引擎id
    private Long engineId;
    //引擎名
    private String engineName;
    //引擎表述
    private String engineDescription;
    //引擎版本id
    private Long versionId;
    //引擎版本code
    private String versionCode;
    //调用次数
    private Long callCount;
    //组织id
    private Long organId;
    //创建者id
    private Long createUserId;
    //修改者id
    private Long updateUserId;
    //创建日期
    private Date createTime;
    //修改日期
    private Date updateTime;

}
