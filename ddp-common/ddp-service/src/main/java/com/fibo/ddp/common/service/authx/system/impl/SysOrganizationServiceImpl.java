package com.fibo.ddp.common.service.authx.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.system.SysOrganizationMapper;
import com.fibo.ddp.common.dao.authx.system.SysRoleMapper;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.model.authx.system.SysOrganization;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.service.authx.system.SysOrganizationService;
import com.fibo.ddp.common.service.common.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SysOrganizationServiceImpl extends ServiceImpl<SysOrganizationMapper,SysOrganization> implements SysOrganizationService {
    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询公司
     */
    @Override
    public List<SysOrganization> getAllSysOrganization() {
        return sysOrganizationMapper.getAllSysOrganization();
    }

    /**
     * 查询单个公司
     */
    @Override
    public SysOrganization findById(long id) {
        return sysOrganizationMapper.findById(id);
    }

    /**
     * 创建公司
     */
    @Override
    public int createSysOrganization(SysOrganization SysOrganization) {
        //获取登录人信息（超级管理员）
        SysUser sysUser = SessionManager.getLoginAccount();
        String nickName = sysUser.getNickName();
        SysOrganization.setAuthor(nickName);
        //生成token唯一标识
        String uuid = UUID.randomUUID().toString();
        SysOrganization.setToken(uuid);
        return sysOrganizationMapper.createSysOrganization(SysOrganization);
    }

    /**
     * 修改公司
     */
    @Override
    public int updateSysOrganization(SysOrganization SysOrganization) {
        return sysOrganizationMapper.updateSysOrganization(SysOrganization);
    }

    /**
     * 批量修改公司状态
     */
    @Override
    public int updateStatus(int status, List<Integer> list) {
        //删除、停用、启用每个公司下的所有角色
        sysRoleMapper.deleteRolesByOrgans(status, list);
        //删除、停用、启用每个公司下的所有账号
        sysUserMapper.deleteUsersByOrgans(status, list);
        //删除、停用、启用每个公司下的所有用户角色关系
        sysUserMapper.deleteUserRoleByOrgan(status, list);
        //删除、停用、启用公司
        int num = sysOrganizationMapper.updateStatus(status, list);
        return num;
    }

    /**
     * 获取所有启用公司
     */
    @Override
    public List<SysOrganization> getAllValidOrgan() {
        return sysOrganizationMapper.getAllValidOrgan();
    }

    /**
     * 验证唯一性
     */
    @Override
    public List<SysOrganization> validateOrganOnly(
            SysOrganization SysOrganization) {
        return sysOrganizationMapper.validateOrganOnly(SysOrganization);
    }

}