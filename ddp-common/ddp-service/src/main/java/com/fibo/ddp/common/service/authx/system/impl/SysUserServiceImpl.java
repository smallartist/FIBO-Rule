package com.fibo.ddp.common.service.authx.system.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.service.authx.system.SysUserService;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.utils.common.MD5;
import com.fibo.ddp.common.utils.constant.StatusConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询搜索用户
     */
    @Override
    public List<SysUser> getAllUsers(SysUser sysUser) {
        return sysUserMapper.getAllUsers(sysUser);
    }

    /**
     * 查询本组织单个用户
     *
     * @return
     */
    @Override
    public SysUser findById(SysUser sysUser) {
        return sysUserMapper.findById(sysUser);
    }

    /**
     * 创建用户
     */
    @Override
    public long createSysUser(SysUser sysUser) {
        long num = 0;
        //密码加密
        String password = MD5.GetMD5Code("111111");
        sysUser.setPassword(password);
        //创建人
        SysUser user = SessionManager.getLoginAccount();
        String nickName = user.getNickName();
        sysUser.setAuthor(nickName);
        //创建用户并返回id
//        sysUserMapper.createSysUser(sysUser);
        sysUser.setStatus(StatusConst.STATUS_ENABLED);
        sysUser.setBirth(new Date());
        sysUserMapper.insert(sysUser);
        long uId = sysUser.getUserId();
        long roleId = sysUser.getSysRole().getRoleId();
        long orgaId = sysUser.getOrganId();
        if (uId != 0 && roleId != 0) {
            //添加表关系
            num = sysUserMapper.insertUserRole(uId, roleId, orgaId);
        }
        return num;
    }

    /**
     * 修改本公司用户
     */
    @Override
    public int updateSysUser(SysUser sysUser) {
        int num = 0;
        //修改用户
        int updateNum = sysUserMapper.updateSysUser(sysUser);
        //修改用户角色关系
        if (updateNum == 1) {
            num = sysUserMapper.updateUserRole(sysUser);
        }
        return num;
    }

    /**
     * 修改用户状态
     */
    @Override
    public int updateStates(int status, List<Integer> list) {
        return sysUserMapper.updateStates(status, list);
    }

    /**
     * 通过用户id查询角色
     */
    @Override
    public SysUser findRoleByUserId(long userId) {
        return sysUserMapper.findRoleByUserId(userId);
    }

    /**
     * 修改密码
     */
    @Override
    public int updatePassword(SysUser sysUser) {
        //获取登录人id
//		SysUser sysUser = SessionManager.getLoginAccount();
//    	long userId = sysUser.getUserId();
        //密码加密
        String password = MD5.GetMD5Code(sysUser.getPassword());
        sysUser.setPassword(password);
//		sysUser.setUserId(userId);
        return sysUserMapper.updatePassword(sysUser);
    }

    /**
     * 本公司账号员工编号唯一性
     */
    @Override
    public List<SysUser> validateUserOnly(SysUser sysUser) {
        return sysUserMapper.validateUserOnly(sysUser);
    }

    @Override
    public SysUser login(String account, String password) {
        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUser::getAccount, account)
                .eq(SysUser::getPassword, password);
        List<SysUser> list = this.list(wrapper);
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

}