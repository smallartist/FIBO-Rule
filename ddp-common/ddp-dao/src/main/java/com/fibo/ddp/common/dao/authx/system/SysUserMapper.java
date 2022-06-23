package com.fibo.ddp.common.dao.authx.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 查询搜索用户
     */
    public List<SysUser> getAllUsers(SysUser sysUser);

    /**
     * 查询本组织单个用户
     *
     * @param id
     * @return
     */
    public SysUser findById(SysUser sysUser);

    /**
     * 创建本公司用户
     *
     * @param sysUser
     * @return
     */
    public long createSysUser(SysUser sysUser);

    /**
     * 添加用户角色关系
     *
     * @param userId
     * @param roleId
     * @return
     */
    public int insertUserRole(@Param("userId") long userId,
                              @Param("roleId") long roleId,
                              @Param("organId") long organId);

    /**
     * 修改本公司用户
     *
     * @param sysUser
     * @return
     */
    public int updateSysUser(SysUser sysUser);

    /**
     * 修改用户角色关系
     *
     * @param sysUser
     * @return
     */
    public int updateUserRole(SysUser sysUser);

    /**
     * 修改用户状态(停用/启用/删除)
     *
     * @param states
     * @return
     */
    public int updateStates(@Param("status") int status, @Param("list") List<Integer> list);

    /**
     * 通过用户id查询角色
     *
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

    /**
     * 删除多个公司的用户角色关系
     */
    public int deleteUserRoleByOrgan(@Param("status") Integer status, @Param("list") List<Integer> list);


    /**
     * 删除角色关联的所有账号
     */
    public int deleteUsersByIds(@Param("status") Integer status, @Param("list") List<Long> list);

    /**
     * 批量删除角色账号关系
     */
    public int deleteBatchUserRole(@Param("status") Integer status, @Param("list") List<Integer> list);

    /**
     * 批量查询角色关联的账号
     */
    public List<Long> getBatchUserIdsByRoleId(List<Integer> list);

    String findNickNameById(Long userId);

    SysUser findUserById(Long userId);

    void deleteUsersByOrgans(@Param("status")Integer status, @Param("list") List<Integer> list);
}
