package com.fibo.ddp.common.dao.approval;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.approval.ApprovalConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (ApprovalConfig)表数据库访问层
 */
@Mapper
public interface ApprovalConfigMapper extends BaseMapper<ApprovalConfig> {
    int insertOrUpdate(ApprovalConfig entity);
    int insertOrUpdateBatch(@Param("entities") List<ApprovalConfig> entities);
}

