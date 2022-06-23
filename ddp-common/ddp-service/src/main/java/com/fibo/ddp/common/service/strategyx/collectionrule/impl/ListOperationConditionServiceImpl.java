package com.fibo.ddp.common.service.strategyx.collectionrule.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.strategyx.collectionrule.ListOperationConditionMapper;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationCondition;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationVersion;
import com.fibo.ddp.common.service.strategyx.collectionrule.ListOperationConditionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("listOperationConditionService")
public class ListOperationConditionServiceImpl extends ServiceImpl<ListOperationConditionMapper, ListOperationCondition> implements ListOperationConditionService {
    @Resource
    private ListOperationConditionMapper listOperationConditionMapper;

    @Override
    public List<ListOperationCondition> queryByVersion(ListOperationVersion version) {
        LambdaQueryWrapper<ListOperationCondition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ListOperationCondition::getListOpVersionId, version.getId());
        wrapper.eq(ListOperationCondition::getListOpId, version.getListOpId());
        List<ListOperationCondition> result = null;
        List<ListOperationCondition> list = this.list(wrapper);
        if (list != null && !list.isEmpty()) {
            result = assembleConditionTree(list, 0L);
        }
        return result;
    }

    @Override
    @Transactional
    public Long recursionSave(ListOperationCondition condition,ListOperationVersion version,Long blockId) {
        condition.setListOpId(version.getListOpId());
        condition.setListOpVersionId(version.getId());
        condition.setListOpBlockId(blockId);
        boolean save = this.save(condition);
        Long id = condition.getId();
        List<ListOperationCondition> children = condition.getChildren();
        if (save && children != null && !children.isEmpty()){
            List<ListOperationCondition> batchCondition = new ArrayList<>();
            for (ListOperationCondition child : children) {
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
        LambdaQueryWrapper<ListOperationCondition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ListOperationCondition::getListOpVersionId,versionId);
        return this.remove(wrapper);
    }


    public List<ListOperationCondition> assembleConditionTree(List<ListOperationCondition> list, long parentId) {
        List<ListOperationCondition> result = new ArrayList<>();
        ListOperationCondition operationCondition;
        for (ListOperationCondition condition : list) {
            if (condition.getParentId()!=null && condition.getParentId() == parentId) {
                operationCondition = condition;
                operationCondition.setChildren(assembleConditionTree(list, condition.getId()));
                result.add(operationCondition);
            }
        }
        return result;
    }
}
