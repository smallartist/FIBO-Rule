package com.fibo.ddp.common.service.datax.datamanage.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.datax.datamanage.FieldCallLogMapper;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.datax.datamanage.FieldCall;
import com.fibo.ddp.common.model.datax.datamanage.FieldCallLog;
import com.fibo.ddp.common.model.datax.datamanage.request.FieldCallParam;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.datax.datamanage.FieldCallLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (FieldCallLog)表服务实现类
 *
 * @author jgp
 * @since 2021-12-08 14:18:29
 */
@Service("fieldCallLogService")
public class FieldCallLogServiceImpl extends ServiceImpl<FieldCallLogMapper, FieldCallLog> implements FieldCallLogService {
    @Resource
    private FieldCallLogMapper fieldCallLogMapper;

    @Override
    public PageInfo queryFieldCallList(QueryListParam<FieldCallParam> param) {
        if (param.getPageNum() > 0 && param.getPageSize() > 0) {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
        }
        setOrganId(param.getEntity());
        List<FieldCall> fieldCallList = fieldCallLogMapper.findFieldCallList(param.getEntity());
        PageInfo<FieldCall> pageInfo = new PageInfo<>(fieldCallList);
        return pageInfo;
    }

    @Override
    public PageInfo queryFieldCallLogList(QueryListParam<FieldCallParam> param) {
        if (param.getPageNum() > 0 && param.getPageSize() > 0) {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
        }
        setOrganId(param.getEntity());
        List<FieldCallLog> fieldCallLogs = fieldCallLogMapper.findFieldCallLogList(param.getEntity());
        PageInfo<FieldCallLog> pageInfo = new PageInfo<>(fieldCallLogs);
        return pageInfo;
    }

    @Override
    public List<FieldCall> queryFieldCallCountList(FieldCallParam param) {
        setOrganId(param);
        List<FieldCall> fieldCallCountList = fieldCallLogMapper.findFieldCallCountList(param);
        return fieldCallCountList;
    }

    private void setOrganId(FieldCallParam param) {
        Long organId = SessionManager.getLoginAccount().getOrganId();
        param.setOrganId(organId);
    }
}