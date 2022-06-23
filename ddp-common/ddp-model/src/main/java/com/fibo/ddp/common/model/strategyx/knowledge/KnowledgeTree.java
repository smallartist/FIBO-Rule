package com.fibo.ddp.common.model.strategyx.knowledge;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName:KnowledgeTreeVo <br/>
 * Description: 知识库树形菜单实体类. <br/>
 */
@Data
@TableName("t_knowledge_tree")
public class KnowledgeTree  implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 * */
	@TableId(type = IdType.AUTO)
	private Long id;
	
	/**
	 * 目录名称
	 * */
	private String name;
	
	/**
	 * 父节点id
	 * */
	private Long parentId;
	
	/**
	 * 创建人id
	 * */
	private Long userId;
	
	/**
	 * 组织id
	 * */
	private Long organId;

	/**
	 * 引擎id
	 * */
	private Long engineId;

	/**
	 * 状态   0 :停用 ，1 : 启用，-1：删除
	 * */
	private Integer status;

	/**
	 * 目录类型  0 : 系统的目录  1：组织的目录 2： 引擎的目录
	 * */
	private Integer type;
	
	/**
	 * 树形分类：0：规则树  1：评分卡的树 2：回收站的树
	 * */
	private Integer treeType;

	/**
	 * 创建日期
	 * */
	private Date created;

	/**
	 * 修改日期
	 * */
	private Date updated;
	
	/**
	 * 子类集合
	 * */
	@TableField(exist = false)
	private KnowledgeTree[] children;
	
	/**
	 * 是否为父类
	 * */
	@TableField(exist = false)
	private String isParent = "true";
}
