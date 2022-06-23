package com.fibo.ddp.common.service.authx.system;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.authx.system.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SysRoleService extends IService<SysRole> {
		/**
		 * 获取本组织所有角色
		 * @param organId
		 * @return
		 */
		public List<SysRole> getAllSysRole(long organId);
		 /**
	     * 获取所有角色
	     */
	    public List<SysRole> getAllRoles();
		/**
		 * 获取本组织启用的角色
		 * @param organId
		 * @return
		 */
	    public List<SysRole> getAllValidRole(@Param("organId") long organId, @Param("author") String author);
	    /**
	     * 查询本组织的单个用户
	     * @param id
	     * @return
	     */
		public SysRole findById(@Param("userId") long id, @Param("organId") long organId);
	    /**
	     * 查询单个角色
	     * @param id
	     * @return
	     */
	    public SysRole findByAId(long id);
	    /**
	     * 创建本组织角色
	     * @param sysRole
	     * @return
	     */
	    public int createSysRole(SysRole sysRole);
	    /**
	     * 修改本公司角色
	     * @param sysRole
	     * @return
	     */
	    public int updateSysRole(SysRole sysRole);

	    /**
	     * 修改角色状态(启用、停用、删除)
	     * @param id
	     * @param idList
	     * @return
	     */
	    public int updateStatus(@Param("status") int status, @Param("list") List<Integer> list);
	    /**
	     * 根据角色查询角色所在公司
	     */
	    public long getOrganByRoleId(long roleId);
	    /**
	     * 验证角色唯一性
	     */
	    public List<SysRole> validateRoleOnly(SysRole sysRole);
	    /**
	     * 查询公司管理员角色id
	     */
	    public List<SysRole> getOrganRoleByAuthor(SysRole sysRole);

}
