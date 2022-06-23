package com.fibo.ddp.common.service.authx.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.system.SysMenuMapper;
import com.fibo.ddp.common.model.authx.system.SysMenu;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.service.authx.system.SysMenuService;
import com.fibo.ddp.common.service.common.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;

    /**
     * 查询所有资源
     */
    @Override
    public List<SysMenu> getAllSysMenu(SysMenu entity) {
        return sysMenuMapper.getAllSysMenu(entity);
    }

    /**
     * 查询单条资源
     */
    @Override
    public SysMenu findById(long id) {
        return sysMenuMapper.findById(id);
    }

    /**
     * 新增资源
     */
    @Override
    public int createSysMenu(SysMenu sysMenu) {
        //获取登录人信息
        SysUser sysUser = SessionManager.getLoginAccount();
        //分配者
        long userId = sysUser.getUserId();
        sysMenu.setUserId(userId);
        return sysMenuMapper.createSysMenu(sysMenu);
    }

    /**
     * 修改资源
     */
    @Override
    public int updateSysMenu(SysMenu sysMenu) {
        return sysMenuMapper.updateSysMenu(sysMenu);
    }

    /**
     * 修改资源状态
     */
    @Override
    public int updateStatus(int status, List<Integer> list) {
        return sysMenuMapper.updateStatus(status, list);
    }

    /**
     * 获取所有启用资源
     */
    @Override
    public List<SysMenu> getAllValidMenu(SysMenu entity) {
        return sysMenuMapper.getAllValidMenu(entity);
    }

    /**
     * 分配资源树
     */
    @Override
    public List<SysMenu> findTreeList(SysMenu entity) {
        return sysMenuMapper.findTreeList(entity);
    }

    /**
     * 保存、修改资源树（实现修改）
     */
    @Override
    public int insertRoleMenu(long roleId, List<Integer> list) {
        //先删除原关系
        sysMenuMapper.deleteRoleMenu(roleId);
        //保存菜单角色关系
        int insertNum = sysMenuMapper.insertRoleMenu(roleId, list);
        return insertNum;
    }

    /**
     * 删除资源树
     */
    @Override
    public int deleteRoleMenu(long roleId) {
        return sysMenuMapper.deleteRoleMenu(roleId);
    }

    /**
     * 验证唯一性
     */
    @Override
    public List<SysMenu> validateMenuOnly(SysMenu sysMenu) {
        return sysMenuMapper.validateMenuOnly(sysMenu);
    }
}