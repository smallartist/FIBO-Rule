package com.fibo.ddp.common.model.analyse;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("t_analyse_scorecard")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyseScorecard extends Model<AnalyseScorecard> {
    private static final long serialVersionUID = 5482147204283754208L;
    //主键id
    @TableId(type= IdType.AUTO)
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
    //评分卡id
    private Long scorecardId;
    //评分卡名
    private String scorecardName;
    //评分卡版本id
    private Long scorecardVersionId;
    //评分卡版本code
    private String scorecardVersionCode;
    //结果
    private String result;
    //次数
    private Long resultCount;
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
