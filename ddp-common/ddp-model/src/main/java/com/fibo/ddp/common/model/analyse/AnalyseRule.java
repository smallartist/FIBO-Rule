package com.fibo.ddp.common.model.analyse;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("t_analyse_rule")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyseRule extends Model<AnalyseRule> {
    private static final long serialVersionUID = -3167163273701099335L;
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
    //规则id
    private Long ruleId;
    //规则名
    private String ruleName;
    //规则版本id
    private Long ruleVersionId;
    //规则版本code
    private String ruleVersionCode;
    //命中次数
    private Long hitCount;
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
