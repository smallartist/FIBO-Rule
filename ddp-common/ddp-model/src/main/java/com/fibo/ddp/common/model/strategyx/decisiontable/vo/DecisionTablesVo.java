package com.fibo.ddp.common.model.strategyx.decisiontable.vo;

import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTables;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors
public class DecisionTablesVo extends DecisionTables implements Serializable{

        private static final long serialVersionUID = 3661484767794532544L;
//        private Long userId;//决策表id
//
//        private String name;//决策表名称
//
//        private String versionCode;//决策表代码(英文)
//
//        private String description;//决策表描述
//
//        private String version;//决策表版本
//
//        private Integer status;//决策表状态：0 停用 1 启用 -1删除（默认启用）
//
//        private Long creator;//决策表创建者
//
//        private String createUserName;//创建者名称
//
//        private Long modifier;//决策表修改者
//
//        private Long organId;//所属组织id
//
//        private Long parentId;//文件id
//
//        private List<Long> parentIds;//文件id集合
//
//        private Date createTime;//创建时间
//
//        private Date updateTime;//修改时间
//        private String resultFieldEn;//存放执行结果的变量
//        private List<DecisionTablesDetailVo> leftDetailVo;//左侧决策表详情
//
//        private List<DecisionTablesDetailVo> topDetailVo;//顶部决策表详情
//
//        private DecisionTablesResultVo resultSet;//结果集（二维数组表）
//        private List<StrategyOutput> strategyOutputList;//输出字段

        private List<DecisionTablesVersionVo> decisionTablesVersionList;
}
