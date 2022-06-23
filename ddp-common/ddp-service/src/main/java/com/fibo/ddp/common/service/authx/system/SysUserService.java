
package com.fibo.ddp.common.service.authx.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.authx.system.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SysUserService extends IService<SysUser> {
	/**
	 * 查询搜索用户
	 */
	public List<SysUser> getAllUsers(SysUser sysUser);
    /**
     * 查询本组织单个用户
     * @param
     * @return
     */
	public SysUser findById(SysUser sysUser);
    /**
     * 创建本公司用户
     * @param sysUser
     * @return
     */
    public long createSysUser(SysUser sysUser);
    /**
     * 修改本公司用户
     * @param user
     * @return
     */
    public int updateSysUser(SysUser user);
    /**
     * 修改用户状态(停用/启用)
     * @param
     * @return
     */
    public int updateStates(@Param("status") int status, @Param("list") List<Integer> list);
    /**
     * 通过用户id查询角色
     * @param userId
     * @return
     */
    public SysUser findRoleByUserId(long userId);
    /**
     * 修改密码
     */
    public int updatePassword(SysUser sysUser);

    /**
     * 本公司账号员工编号唯一性
     */
    public List<SysUser> validateUserOnly(SysUser sysUser);

    SysUser login(String account,String password);

}
