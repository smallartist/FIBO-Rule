package com.fibo.ddp.common.model.strategyx.collectionrule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_list_operation")
public class ListOperation implements Serializable {
    private static final long serialVersionUID = -56400943441634265L;
    /**
    * 自增主键
    */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
    * 集合操作名称
    */
    private String name;
    /**
    * 集合操作代码
    */
    private String code;
    /**
    * 集合操作描述
    */
    private String description;
    /**
    * 集合操作类型：1 集合规则，2 集合处理
    */
    private Integer opType;
    /**
    * 文件夹id
    */
    private Long folderId;
    /**
    * 状态：0 停用 1 启用 -1删除（默认启用）
    */
    private Integer status;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
    * 创建者id
    */
    private Long createUserId;
    /**
    * 修改者id
    */
    private Long updateUserId;
    /**
    * 组织id
    */
    private Long organId;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 修改时间
    */
    private Date updateTime;

    /**
     * 版本id
     */
    @TableField(exist = false)
    private List<ListOperationVersion> versionList;
    @TableField(exist = false)
    private String createUserName;
}
