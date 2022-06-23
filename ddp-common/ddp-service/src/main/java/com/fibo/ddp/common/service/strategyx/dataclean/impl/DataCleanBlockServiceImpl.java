package com.fibo.ddp.common.service.strategyx.dataclean.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.strategyx.dataclean.DataCleanBlockMapper;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanBlock;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;
import com.fibo.ddp.common.service.strategyx.dataclean.DataCleanBlockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("dataCleanBlockService")
public class DataCleanBlockServiceImpl extends ServiceImpl<DataCleanBlockMapper, DataCleanBlock> implements DataCleanBlockService {
    @Resource
    private DataCleanBlockMapper dataCleanBlockMapper;

    @Override
    public List<DataCleanBlock> queryByVersion(DataCleanVersion version) {
        LambdaQueryWrapper<DataCleanBlock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataCleanBlock::getDataCleanVersionId,version.getId());
        return dataCleanBlockMapper.selectList(wrapper);
    }

    @Override
    public boolean deleteByVersionId(Long versionId) {
        if (versionId==null){
            return false;
        }
        LambdaQueryWrapper<DataCleanBlock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataCleanBlock::getDataCleanVersionId,versionId);
        dataCleanBlockMapper.delete(wrapper);
        return true;
    }
}
