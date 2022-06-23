package com.fibo.ddp.common.service.strategyx.dataclean.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.strategyx.dataclean.DataCleanFilterConditionMapper;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanFilterCondition;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;
import com.fibo.ddp.common.model.strategyx.dataclean.FilterCondEnum;
import com.fibo.ddp.common.service.strategyx.dataclean.DataCleanFilterConditionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("dataCleanFilterConditionService")
public class DataCleanFilterConditionServiceImpl extends ServiceImpl<DataCleanFilterConditionMapper, DataCleanFilterCondition> implements DataCleanFilterConditionService {
    @Resource
    private DataCleanFilterConditionMapper dataCleanFilterConditionMapper;

    @Override
    public List<DataCleanFilterCondition> queryByVersion(DataCleanVersion version) {
        LambdaQueryWrapper<DataCleanFilterCondition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataCleanFilterCondition::getDataCleanVersionId, version.getId());
        wrapper.eq(DataCleanFilterCondition::getDataCleanId, version.getDataCleanId());
        List<DataCleanFilterCondition> result = null;
        List<DataCleanFilterCondition> list = this.list(wrapper);
        if (list != null && !list.isEmpty()) {
            result = assembleConditionTree(list, 0L);
        }
        return result;
    }

    @Override
    @Transactional
    public Long recursionSave(DataCleanFilterCondition condition, DataCleanVersion version, Long blockId, FilterCondEnum filterType) {
        condition.setDataCleanId(version.getDataCleanId());
        condition.setDataCleanVersionId(version.getId());
        condition.setDataCleanBlockId(blockId);
        condition.setFilterType(filterType.getCode());
        boolean save = this.save(condition);
        Long id = condition.getId();
        List<DataCleanFilterCondition> children = condition.getChildren();
        if (save && children != null && !children.isEmpty()){
            List<DataCleanFilterCondition> batchCondition = new ArrayList<>();
            for (DataCleanFilterCondition child : children) {
                child.setParentId(id);
                child.setFilterType(filterType.getCode());
                if (child.getChildren()!=null){
                    recursionSave(child,version,blockId, filterType);
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
        LambdaQueryWrapper<DataCleanFilterCondition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataCleanFilterCondition::getDataCleanVersionId,versionId);
        return this.remove(wrapper);
    }


    public List<DataCleanFilterCondition> assembleConditionTree(List<DataCleanFilterCondition> list, long parentId) {
        List<DataCleanFilterCondition> result = new ArrayList<>();
        DataCleanFilterCondition operationCondition;
        for (DataCleanFilterCondition condition : list) {
            if (condition.getParentId()!=null && condition.getParentId() == parentId) {
                operationCondition = condition;
                operationCondition.setChildren(assembleConditionTree(list, condition.getId()));
                result.add(operationCondition);
            }
        }
        return result;
    }
}
