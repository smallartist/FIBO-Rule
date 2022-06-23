package com.fibo.ddp.common.service.authx.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.system.SysRoleMapper;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.model.authx.system.SysRole;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.service.authx.system.SysRoleService;
import com.fibo.ddp.common.service.common.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper,SysRole> implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 获取本组织所有角色
     *
     * @return
     */
    @Override
    public List<SysRole> getAllSysRole(long organId) {
        return sysRoleMapper.getAllSysRole(organId);
    }

    /**
     * 获取所有角色
     */
    @Override
    public List<SysRole> getAllRoles() {
        return sysRoleMapper.getAllRoles();
    }

    /**
     * 获取本组织的单个角色
     */
    @Override
    public SysRole findById(long id, long organId) {
        return sysRoleMapper.findById(id, organId);
    }

    /**
     * 查询单个角色
     */
    @Override
    public SysRole findByAId(long id) {
        return sysRoleMapper.findByAId(id);
    }

    /**
     * 创建本公司的角色
     */
    @Override
    public int createSysRole(SysRole sysRole) {
        return sysRoleMapper.createSysRole(sysRole);
    }

    /**
     * 修改本公司角色
     */
    @Override
    public int updateSysRole(SysRole sysRole) {
        //获取管理员所在公司
        SysUser sysUser = SessionManager.getLoginAccount();
        long organId = sysUser.getOrganId();
        if (organId != 1) {
            //修改本公司角色
            sysRole.setOrganId(organId);
        }
        return sysRoleMapper.updateSysRole(sysRole);
    }

    /**
     * 修改角色状态(启用、停用、删除)
     */
    @Override
    public int updateStatus(int status, List<Integer> list) {
        //查询角色下的账号id
        List<Long> listu = sysUserMapper.getBatchUserIdsByRoleId(list);
        //批量删除角色关联账号
        if (listu != null && listu.size() > 0) {
            sysUserMapper.deleteUsersByIds(status, listu);
        }
        //批量删除角色账号关系
        sysUserMapper.deleteBatchUserRole(status, list);
        //批量删除角色
        int num = sysRoleMapper.updateStatus(status, list);
        return num;
    }

    /**
     * 根据角色查询公司
     */
    @Override
    public long getOrganByRoleId(long roleId) {
        return sysRoleMapper.getOrganByRoleId(roleId);
    }

    /**
     * 验证角色唯一性
     */
    @Override
    public List<SysRole> validateRoleOnly(SysRole sysRole) {
        return sysRoleMapper.validateRoleOnly(sysRole);
    }

    /**
     * 查询公司管理员角色id
     */
    @Override
    public List<SysRole> getOrganRoleByAuthor(SysRole sysRole) {
        return sysRoleMapper.getOrganRoleByAuthor(sysRole);
    }

    /**
     * 获取公司启用角色
     */
    @Override
    public List<SysRole> getAllValidRole(long organId, String author) {
        return sysRoleMapper.getAllValidRole(organId, author);
    }

}