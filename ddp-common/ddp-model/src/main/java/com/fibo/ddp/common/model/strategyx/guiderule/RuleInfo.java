package com.fibo.ddp.common.model.strategyx.guiderule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fibo.ddp.common.model.strategyx.guiderule.vo.RuleVersionVo;
import com.fibo.ddp.common.model.strategyx.scriptrule.RuleScriptVersion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors
@TableName("t_rule")
public class RuleInfo implements Serializable {
    private static final long serialVersionUID = -13354133324148507L;
    @TableId(type = IdType.AUTO)
    private Long id;//主键

    private String name;//规则名称

    private String code;//规则代码

    private String description;//规则描述

    private Integer priority;//规则优先级

    private Long parentId;//父节点id

    private Long author;//创建人id

    private Long userId;//修改人id

    private Long organId;//组织id

    private Integer engineId;

    private Integer status;//状态    0 :停用 ，1 : 启用，-1：删除

    private Integer type;//规则类型  0 : 系统的规则  1：组织的规则 2： 引擎的规则

    private Integer isNon;//逻辑关系“非”，0：否 ，1：是

    private String content;//规则具体内容

    private Date created;

    private Date updated;

    private Integer ruleType;//0硬性拒绝规则1加减分规则

    private Integer ruleAudit;

    private Integer score;//得分

    private String lastLogical;//逻辑关系符

    private Integer difficulty;//规则难度：1-简单规则，2复杂规则

    private String scriptType;//脚本类型python，js，groovy

    private String resultFieldEn;//存放是否命中的字段

    private String scoreFieldEn;//存放得分的字段en
    @TableField(exist = false)
    private String authorName;//创建人名称，需要去其他表查询
    @TableField(exist = false)
    private List<Long> parentIds;

    @TableField(exist = false)
    private Long versionId;//执行版本的id
    @TableField(exist = false)
    private RuleVersionVo version;
    @TableField(exist = false)
    private List<RuleScriptVersion> ruleScriptVersionList;
    @TableField(exist = false)
    private RuleScriptVersion scriptVersion;

    @TableField(exist = false)
    private List<RuleVersionVo>  ruleVersionList;

    public void setRuleScriptVersionList(List<RuleScriptVersion> ruleScriptVersionList) {
        this.ruleScriptVersionList = ruleScriptVersionList;
        List<RuleVersionVo>  ruleVersionList = new ArrayList<>();
        if (ruleScriptVersionList!=null&&!ruleScriptVersionList.isEmpty()){
            for (RuleScriptVersion ruleScriptVersion : ruleScriptVersionList) {
                RuleVersionVo ruleVersionVo = new RuleVersionVo();
                BeanUtils.copyProperties(ruleScriptVersion,ruleVersionVo);
                ruleVersionList.add(ruleVersionVo);
            }
        }
        this.ruleVersionList = ruleVersionList;
    }
}
