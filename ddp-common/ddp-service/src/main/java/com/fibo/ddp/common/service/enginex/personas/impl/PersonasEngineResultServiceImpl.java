package com.fibo.ddp.common.service.enginex.personas.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fibo.ddp.common.dao.enginex.personas.PersonasEngineResultMapper;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.enginex.personas.PersonasEngineResult;
import com.fibo.ddp.common.service.enginex.personas.PersonasEngineResultService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;
import java.util.List;

/**
 * (PersonasEngineResult)表服务实现类
 *
 * @author jgp
 * @since 2022-01-06 14:23:10
 */
@Service("personasEngineResultService")
public class PersonasEngineResultServiceImpl extends ServiceImpl<PersonasEngineResultMapper, PersonasEngineResult> implements PersonasEngineResultService {
    @Resource
    private PersonasEngineResultMapper personasEngineResultMapper;


    @Override
    public PersonasEngineResult queryById(Long id) {
        if (id == null) {
            return null;
        }
        PersonasEngineResult result = this.getById(id);
        return result;
    }

    @Override
    public PageInfo<PersonasEngineResult> queryByEntity(QueryListParam<PersonasEngineResult> param) {
        if (QueryListParam.checkIsPage(param)) {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
        }
        LambdaQueryWrapper<PersonasEngineResult> wrapper = createWrapper(param.getEntity());
        PageInfo<PersonasEngineResult> resultPageInfo = new PageInfo<>(this.list(wrapper));
        return resultPageInfo;
    }

    private LambdaQueryWrapper<PersonasEngineResult> createWrapper(PersonasEngineResult query) {
        LambdaQueryWrapper<PersonasEngineResult> wrapper = new LambdaQueryWrapper<>();
        if (query != null) {
            if (query.getEngineId() != null) {
                wrapper.eq(PersonasEngineResult::getEngineId,query.getEngineId());
            }
            if (query.getEngineVersionId() != null) {
                wrapper.eq(PersonasEngineResult::getEngineVersionId,query.getEngineVersionId());
            }
            if (query.getBatchNo() != null) {
                wrapper.eq(PersonasEngineResult::getBatchNo, query.getBatchNo());
            }
            if (query.getQueryStartTime() != null) {
                wrapper.ge(PersonasEngineResult::getCreateTime, query.getQueryStartTime());
            }
            if (query.getQueryEndTime() != null) {
                wrapper.le(PersonasEngineResult::getCreateTime, query.getQueryEndTime());
            }
        }
        return wrapper;
    }
}
