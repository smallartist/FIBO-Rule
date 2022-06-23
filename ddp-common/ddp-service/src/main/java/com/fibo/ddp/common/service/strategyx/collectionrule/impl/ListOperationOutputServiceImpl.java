package com.fibo.ddp.common.service.strategyx.collectionrule.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.strategyx.collectionrule.ListOperationOutputMapper;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationOutput;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationVersion;
import com.fibo.ddp.common.service.strategyx.collectionrule.ListOperationOutputService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("listOperationOutputService")
public class ListOperationOutputServiceImpl extends ServiceImpl<ListOperationOutputMapper, ListOperationOutput> implements ListOperationOutputService {
    @Resource
    private ListOperationOutputMapper listOperationOutputMapper;

    @Override
    public List<ListOperationOutput> queryByVersion(ListOperationVersion version) {
        LambdaQueryWrapper<ListOperationOutput> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ListOperationOutput::getListOpVersionId,version.getId());
        List<ListOperationOutput> result = this.list(wrapper);
        return result;
    }

    @Override
    public boolean deleteByVersionId(Long versionId) {
        LambdaQueryWrapper<ListOperationOutput> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ListOperationOutput::getListOpVersionId,versionId);
        return this.remove(wrapper);
    }
}
