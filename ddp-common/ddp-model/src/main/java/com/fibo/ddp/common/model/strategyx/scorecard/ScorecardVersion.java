package com.fibo.ddp.common.model.strategyx.scorecard;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`t_scorecard_version`")
public class ScorecardVersion implements Serializable {
    private static final long serialVersionUID = -1850194333747447612L;
    /**
     * 评分卡版本主键id
     */
    @TableId( type = IdType.AUTO)
    private Long id;
    /**
     * 评分卡id
     */
    private Long scorecardId;
    /**
     * 评分卡版本号
     */
    private String versionCode;
    /**
     * 描述信息
     */
    private String description;
    /**
     * 状态：-1 删除 0停用 1启用
     */
    private Integer status;
    /**
     * 计算方式1,求和，2，甲醛求和
     */
    private Integer scoreCalculateType;
    /**
     * 评分卡结果en
     */
    private String resultFieldEn;
    /**
     * 组织id
     */
    private Long organId;
    /**
     * 创建者id
     */
    private Long createUserId;
    /**
     * 修改者id
     */
    private Long updateUserId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 快照信息
     */
    private String snapshot;

}
