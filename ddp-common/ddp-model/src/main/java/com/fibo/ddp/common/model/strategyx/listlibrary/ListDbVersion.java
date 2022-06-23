package com.fibo.ddp.common.model.strategyx.listlibrary;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@TableName("t_list_db_version")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListDbVersion extends Model<ListDbVersion> {
    //名单库版本表id
    @TableId(type = IdType.AUTO)
    private Long id;
    //名单库id
    private Long listDbId;
    //版本号
    private String versionCode;
    //版本描述
    private String description;
    //固定输出的指标en
    private String resultFieldEn;
    //状态：-1删除 ，1启用，0停用
    private Integer status;
    //名单库表中列字段，字段id逗号分隔
    private String tableColumn;
    //检索匹配类型，精确匹配（1），模糊匹配（0）
    private Integer matchType;
    //查询字段间逻辑，and（1），or（0）
    private Integer queryType;
    //查询主键，字段编号逗号分割
    private String queryField;
    //所属组织id
    private Long organId;
    //创建者id
    private Long createUserId;
    //修改者id
    private Long updateUserId;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //名单库版本配置快照
    private Object snapshot;
    @TableField(exist = false)
    private List<StrategyOutput> strategyOutputList;
}
