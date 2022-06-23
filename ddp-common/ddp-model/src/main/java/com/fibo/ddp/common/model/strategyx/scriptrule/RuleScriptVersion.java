package com.fibo.ddp.common.model.strategyx.scriptrule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (RuleScriptVersion)实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`t_rule_script_version`")
public class RuleScriptVersion implements Serializable {
    private static final long serialVersionUID = -78864192587533951L;
    /**
     * 主键：规则版本id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 规则id
     */
    private Long ruleId;
    /**
     * 版本号
     */
    private String versionCode;
    /**
     * 版本描述
     */
    private String description;
    /**
     * 状态：-1删除 ，1启用，0停用
     */
    private Integer status;
    /**
     * 脚本类型：groovy，python，js
     */
    private String scriptType;
    /**
     * 脚本规则集内容json，包含脚本内容和脚本所用字段两个值
     */
    private String scriptContent;
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
     * 规则版本配置快照
     */
    private String snapshot;

}
