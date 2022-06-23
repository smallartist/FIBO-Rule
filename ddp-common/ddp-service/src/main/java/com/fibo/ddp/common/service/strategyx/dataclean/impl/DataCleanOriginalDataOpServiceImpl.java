package com.fibo.ddp.common.service.strategyx.dataclean.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.strategyx.dataclean.DataCleanOriginalDataOpMapper;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanOriginalDataOp;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;
import com.fibo.ddp.common.service.strategyx.dataclean.DataCleanOriginalDataOpService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (DataCleanOriginalDataOp)表服务实现类
 *
 * @author makejava
 * @since 2022-03-21 11:15:09
 */
@Service("dataCleanOriginalDataOpService")
public class DataCleanOriginalDataOpServiceImpl extends ServiceImpl<DataCleanOriginalDataOpMapper, DataCleanOriginalDataOp> implements DataCleanOriginalDataOpService {

    @Override
    public DataCleanOriginalDataOp queryByDataCleanVersion(DataCleanVersion version) {
        Long versionId = version.getId();
        LambdaQueryWrapper<DataCleanOriginalDataOp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataCleanOriginalDataOp::getDataCleanVersionId, versionId);
        List<DataCleanOriginalDataOp> list = this.list(wrapper);
        List<DataCleanOriginalDataOp> collect = list.stream().filter(item -> {
            return item.getParentId() == 0;
        }).collect(Collectors.toList());
        DataCleanOriginalDataOp root = null;
        if (CollectionUtils.isNotEmpty(collect)) {
            root = collect.get(0);
            assemble(root, list);
        }
        return root;
    }

    @Override
    @Transactional
    public void addOriginalDataOp(DataCleanOriginalDataOp originalDataOp) {
        if (originalDataOp == null) {
            return;
        }
        this.save(originalDataOp);
        Long id = originalDataOp.getId();
        Long dataCleanVersionId = originalDataOp.getDataCleanVersionId();
        DataCleanOriginalDataOp child = originalDataOp.getChild();
        if (child != null) {
            child.setParentId(id);
            child.setDataCleanVersionId(dataCleanVersionId);
        }
    }

    @Override
    @Transactional
    public void updateDataOp(DataCleanVersion version, DataCleanOriginalDataOp originalDataOp) {
        Long versionId = version.getId();
        this.deleteByVersion(version);
        originalDataOp.setDataCleanVersionId(versionId);
        originalDataOp.setParentId(0L);
        this.addOriginalDataOp(originalDataOp);
    }
    @Transactional
    public void deleteByVersion(DataCleanVersion version) {
        Long versionId = version.getId();
        LambdaQueryWrapper<DataCleanOriginalDataOp> wrapper  = new LambdaQueryWrapper<>();
        wrapper.eq(DataCleanOriginalDataOp::getDataCleanVersionId,versionId);
        this.remove(wrapper);
    }


    private void assemble(DataCleanOriginalDataOp root, List<DataCleanOriginalDataOp> list) {
        for (DataCleanOriginalDataOp op : list) {
            if (root.getId().equals(op.getParentId())) {
                root.setChild(op);
                assemble(op, list);
                break;
            }
        }
    }

    ;
}

