package com.fibo.ddp.common.service.enginex.personas.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fibo.ddp.common.dao.enginex.personas.PersonasEngineResultDetailMapper;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.enginex.personas.PersonasEngineResultDetail;
import com.fibo.ddp.common.model.enginex.personas.vo.PersonasReport;
import com.fibo.ddp.common.model.enginex.personas.vo.PersonasReportParam;
import com.fibo.ddp.common.service.enginex.personas.PersonasEngineResultDetailService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (PersonasEngineResultDetail)表服务实现类
 *
 * @author jgp
 * @since 2022-01-06 14:24:57
 */
@Service("personasEngineResultDetailService")
public class PersonasEngineResultDetailServiceImpl extends ServiceImpl<PersonasEngineResultDetailMapper, PersonasEngineResultDetail> implements PersonasEngineResultDetailService {
    @Resource
    private PersonasEngineResultDetailMapper personasEngineResultDetailMapper;

    @Override
    public PersonasEngineResultDetail queryById(Long id) {
        if (id == null) {
            return null;
        }
        PersonasEngineResultDetail result = this.getById(id);
        return result;
    }

    @Override
    public PageInfo<PersonasEngineResultDetail> queryByEntity(QueryListParam<PersonasEngineResultDetail>  param) {
        if (QueryListParam.checkIsPage(param)) {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
        }
        LambdaQueryWrapper<PersonasEngineResultDetail> wrapper = createWrapper(param.getEntity());
        PageInfo<PersonasEngineResultDetail> resultPageInfo = new PageInfo<>(this.list(wrapper));
        return resultPageInfo;
    }

    @Override
    public Map queryReportList(QueryListParam<PersonasReportParam> param) {
        List<PersonasReport> list = personasEngineResultDetailMapper.selectReportList(param.getEntity());
        Map<String, List<PersonasReport>> collect = list.stream().collect(Collectors.groupingBy(item -> {
            return item.getTagId()+","+item.getTagVersionId()+","+item.getTagName();
        }));
        Map result = new HashMap();
        List<Map> reportList = new ArrayList<>();
        for (Map.Entry<String, List<PersonasReport>> entry : collect.entrySet()) {
            Map map = new HashMap();
            String[] split = entry.getKey().split(",");
            map.put("tagId",split[0]);
            map.put("yagVersionId",split[1]);
            map.put("tagName",split[2]);
            map.put("list",entry.getValue());
            reportList.add(map);
        }
        result.put("reportList",reportList);
        return result;
    }

    private LambdaQueryWrapper<PersonasEngineResultDetail> createWrapper(PersonasEngineResultDetail query) {
        LambdaQueryWrapper<PersonasEngineResultDetail> wrapper = new LambdaQueryWrapper<>();
        if (query != null) {
            if (query.getEngineId() != null) {
                wrapper.eq(PersonasEngineResultDetail::getEngineId, query.getEngineId());
            }
            if (query.getEngineVersionId() != null) {
                wrapper.eq(PersonasEngineResultDetail::getEngineVersionId, query.getEngineVersionId());
            }
            if (query.getBatchNo() != null) {
                wrapper.eq(PersonasEngineResultDetail::getBatchNo, query.getBatchNo());
            }
            if (query.getTagId() != null) {
                wrapper.eq(PersonasEngineResultDetail::getTagId,query.getTagId());
            }
            if (StringUtils.isNotBlank(query.getTagValue())){
                wrapper.eq(PersonasEngineResultDetail::getTagValue,query.getTagValue());
            }
            if (query.getQueryStartTime() != null) {
                wrapper.ge(PersonasEngineResultDetail::getCreateTime, query.getQueryStartTime());
            }
            if (query.getQueryEndTime() != null) {
                wrapper.le(PersonasEngineResultDetail::getCreateTime, query.getQueryEndTime());
            }
        }
        return wrapper;
    }
}
