package com.fibo.ddp.common.service.strategyx.collectionrule.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.strategyx.collectionrule.ListOperationFilterConditionMapper;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationFilterCondition;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationVersion;
import com.fibo.ddp.common.service.strategyx.collectionrule.ListOperationFilterConditionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("listOperationFilterConditionService")
public class ListOperationFilterConditionServiceImpl extends ServiceImpl<ListOperationFilterConditionMapper, ListOperationFilterCondition> implements ListOperationFilterConditionService {
    @Resource
    private ListOperationFilterConditionMapper listOperationFilterConditionMapper;

    @Override
    public List<ListOperationFilterCondition> queryByVersion(ListOperationVersion version) {
        LambdaQueryWrapper<ListOperationFilterCondition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ListOperationFilterCondition::getListOpVersionId, version.getId());
        wrapper.eq(ListOperationFilterCondition::getListOpId, version.getListOpId());
        List<ListOperationFilterCondition> result = null;
        List<ListOperationFilterCondition> list = this.list(wrapper);
        if (list != null && !list.isEmpty()) {
            result = assembleConditionTree(list, 0L);
        }
        return result;
    }

    @Override
    @Transactional
    public Long recursionSave(ListOperationFilterCondition condition,ListOperationVersion version,Long blockId) {
        condition.setListOpId(version.getListOpId());
        condition.setListOpVersionId(version.getId());
        condition.setListOpBlockId(blockId);
        boolean save = this.save(condition);
        Long id = condition.getId();
        List<ListOperationFilterCondition> children = condition.getChildren();
        if (save && children != null && !children.isEmpty()){
            List<ListOperationFilterCondition> batchCondition = new ArrayList<>();
            for (ListOperationFilterCondition child : children) {
                child.setParentId(id);
                if (child.getChildren()!=null){
                    recursionSave(child,version,blockId);
                }else {
                    batchCondition.add(child);
                }
            }
            if (!batchCondition.isEmpty()){
                this.saveBatch(batchCondition);
            }
        }
        return id;
    }

    @Override
    public boolean deleteByVersionId(Long versionId) {
        LambdaQueryWrapper<ListOperationFilterCondition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ListOperationFilterCondition::getListOpVersionId,versionId);
        return this.remove(wrapper);
    }


    public List<ListOperationFilterCondition> assembleConditionTree(List<ListOperationFilterCondition> list, long parentId) {
        List<ListOperationFilterCondition> result = new ArrayList<>();
        ListOperationFilterCondition operationCondition;
        for (ListOperationFilterCondition condition : list) {
            if (condition.getParentId()!=null && condition.getParentId() == parentId) {
                operationCondition = condition;
                operationCondition.setChildren(assembleConditionTree(list, condition.getId()));
                result.add(operationCondition);
            }
        }
        return result;
    }
}
