package com.fibo.ddp.common.service.strategyx.dataclean.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.strategyx.dataclean.DataCleanConditionMapper;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanCondition;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;
import com.fibo.ddp.common.service.strategyx.dataclean.DataCleanConditionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("dataCleanConditionService")
public class DataCleanConditionServiceImpl extends ServiceImpl<DataCleanConditionMapper, DataCleanCondition> implements DataCleanConditionService {
    @Resource
    private DataCleanConditionMapper dataCleanConditionMapper;

    @Override
    public List<DataCleanCondition> queryByVersion(DataCleanVersion version) {
        LambdaQueryWrapper<DataCleanCondition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataCleanCondition::getDataCleanVersionId, version.getId());
        wrapper.eq(DataCleanCondition::getDataCleanId, version.getDataCleanId());
        List<DataCleanCondition> result = null;
        List<DataCleanCondition> list = this.list(wrapper);
        if (list != null && !list.isEmpty()) {
            result = assembleConditionTree(list, 0L);
        }
        return result;
    }

    @Override
    @Transactional
    public Long recursionSave(DataCleanCondition condition,DataCleanVersion version,Long blockId) {
        condition.setDataCleanId(version.getDataCleanId());
        condition.setDataCleanVersionId(version.getId());
        condition.setDataCleanBlockId(blockId);
        boolean save = this.save(condition);
        Long id = condition.getId();
        List<DataCleanCondition> children = condition.getChildren();
        if (save && children != null && !children.isEmpty()){
            List<DataCleanCondition> batchCondition = new ArrayList<>();
            for (DataCleanCondition child : children) {
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
        LambdaQueryWrapper<DataCleanCondition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataCleanCondition::getDataCleanVersionId,versionId);
        return this.remove(wrapper);
    }


    public List<DataCleanCondition> assembleConditionTree(List<DataCleanCondition> list, long parentId) {
        List<DataCleanCondition> result = new ArrayList<>();
        DataCleanCondition operationCondition;
        for (DataCleanCondition condition : list) {
            if (condition.getParentId()!=null && condition.getParentId() == parentId) {
                operationCondition = condition;
                operationCondition.setChildren(assembleConditionTree(list, condition.getId()));
                result.add(operationCondition);
            }
        }
        return result;
    }
}
