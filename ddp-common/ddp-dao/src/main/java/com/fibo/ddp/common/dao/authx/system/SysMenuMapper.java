package com.fibo.ddp.common.dao.authx.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.authx.system.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查询所有资源
     *
     * @return
     */
    public List<SysMenu> getAllSysMenu(SysMenu entity);

    /**
     * 查询单条资源
     *
     * @param id
     * @return
     */
    public SysMenu findById(long id);

    /**
     * 新增资源
     *
     * @param sysMenu
     * @return
     */
    public int createSysMenu(SysMenu sysMenu);

    /**
     * 修改资源
     *
     * @param sysMenu
     * @return
     */
    public int updateSysMenu(SysMenu sysMenu);

    /**
     * 修改资源状态
     *
     * @param id
     * @param idList
     * @return
     */
    public int updateStatus(@Param("status") int status, @Param("list") List<Integer> list);


    /**
     * 保存角色菜单关系
     */
    public int insertRoleMenu(@Param("roleId") long roleId, @Param("list") List<Integer> list);

    /**
     * 删除角色菜单关系(实现修改)
     */
    public int deleteRoleMenu(long roleId);

    /**
     * 分配资源树
     *
     * @param roleId
     * @return
     */
    public List<SysMenu> findTreeList(SysMenu entity);

    /**
     * 获取所有启用资源
     *
     * @return
     */
    public List<SysMenu> getAllValidMenu(SysMenu entity);

    /**
     * 验证唯一性
     */
    public List<SysMenu> validateMenuOnly(SysMenu sysMenu);

}
