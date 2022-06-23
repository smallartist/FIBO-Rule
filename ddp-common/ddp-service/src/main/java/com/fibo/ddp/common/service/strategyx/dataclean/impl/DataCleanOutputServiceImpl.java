package com.fibo.ddp.common.service.strategyx.dataclean.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.strategyx.dataclean.DataCleanOutputMapper;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanOutput;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;
import com.fibo.ddp.common.service.strategyx.dataclean.DataCleanOutputService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("dataCleanOutputService")
public class DataCleanOutputServiceImpl extends ServiceImpl<DataCleanOutputMapper, DataCleanOutput> implements DataCleanOutputService {
    @Resource
    private DataCleanOutputMapper dtaCleanOutputMapper;

    @Override
    public List<DataCleanOutput> queryByVersion(DataCleanVersion version) {
        LambdaQueryWrapper<DataCleanOutput> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataCleanOutput::getDataCleanVersionId,version.getId());
        List<DataCleanOutput> result = this.list(wrapper);
        return result;
    }

    @Override
    public boolean deleteByVersionId(Long versionId) {
        LambdaQueryWrapper<DataCleanOutput> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataCleanOutput::getDataCleanVersionId,versionId);
        return this.remove(wrapper);
    }
}
