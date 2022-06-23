package com.fibo.ddp.common.service.strategyx.collectionrule.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.strategyx.collectionrule.ListOperationBlockMapper;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationBlock;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationVersion;
import com.fibo.ddp.common.service.strategyx.collectionrule.ListOperationBlockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("listOperationBlockService")
public class ListOperationBlockServiceImpl extends ServiceImpl<ListOperationBlockMapper, ListOperationBlock> implements ListOperationBlockService {
    @Resource
    private ListOperationBlockMapper listOperationBlockMapper;

    @Override
    public List<ListOperationBlock> queryByVersion(ListOperationVersion version) {
        LambdaQueryWrapper<ListOperationBlock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ListOperationBlock::getListOpVersionId,version.getId());
        return listOperationBlockMapper.selectList(wrapper);
    }

    @Override
    public boolean deleteByVersionId(Long versionId) {
        if (versionId==null){
            return false;
        }
        LambdaQueryWrapper<ListOperationBlock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ListOperationBlock::getListOpVersionId,versionId);
        listOperationBlockMapper.delete(wrapper);
        return true;
    }
}
