package com.fibo.ddp.common.model.strategyx.scorecard;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVersionVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName:ScorecardVo <br/>
 * Description: 评分卡实体类. <br/>
 */
@TableName("t_scorecard")
@Data
public class Scorecard implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 代码
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    /**
     * 版本号
     */
    private String version;

    /**
     * 父节点id
     */
    private Long parentId;

    /**
     * 修改人id
     */
    private Long userId;

    /**
     * 创建人id
     */
    private Long author;

    /**
     * 创建人姓名
     */
    @TableField(exist = false)
    private String authorName;

    /**
     * 引擎id
     */
    private Long engineId;

    /**
     * 组织id
     */
    private Long OrganId;

    /**
     * 评分卡类型  0 : 系统的评分卡  1：组织的评分卡 2： 引擎的评分卡
     */
    private Integer type;

    /**
     * 状态   0 :停用 ，1 : 启用，-1：删除
     */
    private Integer status;

    /**
     * 创建日期
     */
    private Date created;

    /**
     * 修改日期
     */
    private Date updated;

    /**
     * 是否被选中
     */
    @TableField(exist = false)
    private boolean checked;

    /**
     * 得分
     */
    private String score;

    /**
     * pd
     */
    private String pd;

    /**
     * odds
     */
    private String odds;

    /**
     * engineName
     */
    @TableField(exist = false)
    private String engineName;

    /**
     * 得分计算方式
     */
    private Integer scoreCalculateType;

    /**
     * 接收评分卡结果的字段en
     */
    private String resultFieldEn;

    /**
     * 当前执行的版本信息
     */
    @TableField(exist = false)
    private ScorecardVersionVo executeVersion;
}
